package com.hedario.areareloader.fawe;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.hedario.areareloader.fawe.AreaMethods.Phase;
import com.hedario.areareloader.fawe.configuration.Manager;
import com.hedario.areareloader.fawe.events.AreaCompleteEvent;
import com.hedario.areareloader.fawe.events.AreaLoadEvent;
import com.sk89q.worldedit.WorldEditException;

/**
 * A class that reads the schematic files and allows area loading.<br>
 * Each area is split into section that can be restored after a set customisable
 * time interval.
 */
public class Loader {
	private static final Map<String, Loader> INSTANCES = new HashMap<String, Loader>();
	private final String name;
	private final Location location;
	private final int maxX, maxZ, length;
	private int x, z, percentage;
	public int perc, lastperc;
	private CommandSender sender;
	private long startTime;
	private boolean completed;
	public BukkitTask task;
	
	public Loader(String area, Location location, int x, int z, CommandSender sender) {
		this.name = area;
		this.location = location;
		this.maxX = x;
		this.maxZ = z;
		this.length = (AreaMethods.getAreaLength(area) != null && AreaMethods.getAreaLength(area) > 0) ? AreaMethods.getAreaLength(area) : 16;
		this.x = 0;
		this.z = 0;
		this.completed = false;

		if (INSTANCES.containsKey(area)) {
			AreaMethods.sendMessage(sender, "area is already loading", true);
			return;
		}

		if (sender != null) {
			this.sender = sender;
			Bukkit.getServer().getPluginManager().callEvent(new AreaLoadEvent(sender, area));
		} else {
			Bukkit.getServer().getPluginManager().callEvent(new AreaLoadEvent(area));
		}
		this.startTime = System.currentTimeMillis();
		this.percentage = Manager.getConfig().getInt("Settings.AreaLoading.Percentage");
		INSTANCES.put(area, this);
		start();
	}
	
	private void progress() throws FileNotFoundException, WorldEditException, IOException {
		if (!completed) {
			if (!AreaMethods.loadSchematicArea(sender, this.name, AreaMethods.getFileName(this.name, x, z), location.getWorld(), location.clone().add(x * length, 0.0D, z * length))) {
				if (sender instanceof Player) {
					((Player) sender).getWorld().playSound(((Player) sender).getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 0.5F);
				}
				return;
			} else {
				if (x == this.maxX && z == this.maxZ) {
					this.completed = true;
					return;
				}
				z++;
				if (z > this.maxZ) {
					z = 0;
					x++;
				}
				if (x > this.maxX) {
					x = this.maxX;
				}
				int total = (this.maxX + 1) * (this.maxZ + 1);
				int processed = x * (this.maxZ + 1) + z;
				perc = (int) ((processed * 100L) / total);
				if (perc % percentage == 0 && lastperc != perc) {
					AreaMethods.sendMessage(sender, process().replace("%area%", name).replace("%perc%", String.valueOf(perc)), true);
				}
				lastperc = perc;
			}
		} else {
			if (sender != null) {
				final long time = System.currentTimeMillis() - startTime;
				AreaMethods.sendMessage(sender, success().replace("%area%", name).replace("%time%", AreaMethods.formatTime(time)), true);
				Bukkit.getServer().getPluginManager().callEvent(new AreaCompleteEvent(sender, name));
				if (sender instanceof Player) {
					((Player) sender).getWorld().playSound(((Player) sender).getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, 0.3F);
				}
			} else {
				Bukkit.getServer().getPluginManager().callEvent(new AreaCompleteEvent(name));
			}
			remove();
		}
	}
	
	private void start() {
		this.task = new BukkitRunnable() {
			@Override
			public void run() {
				try {
					progress();
				} catch (FileNotFoundException e) {
					handleError(Phase.LOADING, e, "An error happened while loading the area; file couldn't be found?");
				} catch (WorldEditException | IOException e) {
					handleError(Phase.LOADING, e, "An error happened while loading the area.");
				} catch (Throwable e) {
					handleError(Phase.LOADING, e, "An unexpected error happened while loading the area.");
				}
			}
		}.runTaskTimer(AreaReloader.plugin, 0, (AreaMethods.getInterval(name) * 20) / 1000);
	}
	
	public static Loader get(final String area) {
		return INSTANCES.get(area);
	}
	
	public static Map<String, Loader> getInstances() {
		return INSTANCES;
	}

	public void remove() {
		if (task != null) {
			if (!task.isCancelled()) {
				task.cancel();
			}
		}
		INSTANCES.remove(name);
	}
	
	private void handleError(final Phase phase, Throwable exception, String message) {
		if (sender != null) {
			AreaMethods.sendMessage(sender, AreaMethods.getPrimaryColor() + message, true);
			AreaMethods.sendMessage(sender, AreaMethods.getPrimaryColor() + "Logged to console and debug.", true);
		}
	    Manager.printDebug("- PHASE: " + phase.name());
	    Manager.printDebug("Area: " + name);
	    Manager.printDebug("Section " + x + "_" + z);
	    Manager.printDebug("File:" + AreaMethods.getFileName(this.name, x, z));
	    Manager.printDebug("Task ID:" + task.getTaskId());
	    Manager.printDebug("Section size: " + length);
	    Manager.printDebug("Exception type: " + exception.getClass().getSimpleName());
	    Manager.printDebug("Stack trace: " + exception.getMessage());
	    AreaReloader.log.log(Level.WARNING, message);
	}
	
	public String process() {
		return Manager.getConfig().getString("Commands.Load.Process");
	}

	private String success() {
		return Manager.getConfig().getString("Commands.Load.Success");
	}
}
