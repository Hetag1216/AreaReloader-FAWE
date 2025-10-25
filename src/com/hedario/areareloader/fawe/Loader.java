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
	@SuppressWarnings("unused")
	private final int maxX, maxZ, length, maxChunks;
	private int x, z, chunks;
	private CommandSender sender;
	private long startTime, tick;
	private boolean completed;
	private BukkitTask task;
	private int percentage;
	
	public Loader(String area, Location location, int x, int z, CommandSender sender) {
		AreaMethods.sendMessage(sender, "called", true);
		this.name = area;
		this.location = location;
		this.maxX = x;
		this.maxZ = z;
		x++;
		z++;
		this.maxChunks = AreaMethods.getAreaChunk(area);
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
		this.tick = startTime;
		AreaMethods.sendMessage(sender, "pre put", true);
		this.percentage = Manager.getConfig().getInt("Settings.AreaLoading.Percentage");
		AreaMethods.sendMessage(sender, "put", true);
		INSTANCES.put(area, this);
		start();
		AreaMethods.sendMessage(sender, "started", true);
	}
	
	private void progress() throws FileNotFoundException, WorldEditException, IOException {
		if (System.currentTimeMillis() >= tick + AreaMethods.getInterval(this.name) && !completed) {
			if (!AreaMethods.loadSchematicArea(sender, this.name, AreaMethods.getFileName(this.name, x, z), location.getWorld(), location.clone().add(x * length, 0.0D, z * length))) {
				if (sender instanceof Player) {
					((Player) sender).getWorld().playSound(((Player) sender).getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 0.5F);
					AreaMethods.sendMessage(sender, "couldn't load", true);
				}
				return;
			} else {
				chunks += 1;
				z += 1;
				tick = System.currentTimeMillis();
				if (z > this.maxZ) {
					z = 0;
					x += 1;
				}
				int perc = (chunks * 100) / maxChunks;
				if (perc % percentage == 0) {
					AreaMethods.sendMessage(sender, process().replace("%area%", name).replace("%perc%", String.valueOf(perc)), true);
				}
				AreaMethods.sendMessage(sender, "progressed", true);
			}
			if (chunks == this.maxChunks) {
				z -= 1;
				this.completed = true;
				AreaMethods.sendMessage(sender, "completed", true);
			}
		}
		if (completed) {
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
			task.cancel();
			INSTANCES.remove(name);
			AreaMethods.sendMessage(sender, "finished" + INSTANCES.size(), true);
		}
	}
	
	private void start() {
		this.task = new BukkitRunnable() {
			@Override
			public void run() {
				try {
					progress();
				} catch (FileNotFoundException e) {
					handleError(e, "An error happened while loading the area; file couldn't be found?");
				} catch (WorldEditException | IOException e) {
					handleError(e, "An error happened while loading the area.");
				} catch (Throwable e) {
					handleError(e, "An unexpected error happened while loading the area.");
				}
			}
		}.runTaskTimer(AreaReloader.plugin, 0, 0);
	}
	
	public static Loader get(final String area) {
		return INSTANCES.get(area);
	}
	
	private void handleError(Throwable exception, String message) {
	    AreaMethods.sendMessage(sender, AreaMethods.getPrimaryColor() + message, true);
	    AreaMethods.sendMessage(sender, AreaMethods.getPrimaryColor() + "Logged to console and debug.", true);

	    Manager.printDebug("- AREA LOADING");
	    Manager.printDebug("Area: " + name);
	    Manager.printDebug("X, Z: " + x + " " + z);
	    Manager.printDebug("File:" + AreaMethods.getFileName(this.name, x, z));
	    Manager.printDebug("Task ID:" + task.getTaskId());
	    Manager.printDebug("Chunks: " + chunks + "/" + maxChunks);
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
