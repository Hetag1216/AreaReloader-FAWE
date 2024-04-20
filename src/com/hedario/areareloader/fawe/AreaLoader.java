package com.hedario.areareloader.fawe;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.hedario.areareloader.fawe.configuration.Manager;
import com.hedario.areareloader.fawe.events.AreaCompleteEvent;
import com.hedario.areareloader.fawe.events.AreaLoadEvent;
import com.sk89q.worldedit.WorldEditException;
public class AreaLoader {
	public static List<AreaLoader> areas = new ArrayList<AreaLoader>();
	private String area;
	private int x, maxX, z, maxZ;
	private int length;
	private int chunks, maxChunks;
	private Location location;
	private boolean completed = false;
	private CommandSender sender;
	private static long fakeTime;
	public static double percentage;
	public long time;
	private boolean sent = false;
	private float curr_perc;

	public AreaLoader(String area, int x, int z, int length, Location location, CommandSender sender) {
		if (sender != null) {
			this.sender = sender;
		}
		if (AreaReloader.getQueue().isQueued(area) || areas.contains(this)) {
			if (AreaReloader.debug) {
				Manager.printDebug("-=-=-=-=-=-=-=-=-=-=- Area Loading -=-=-=-=-=-=-=-=-=-=-");
				Manager.printDebug(area + " is already in the queue, it may be currently loading.");
				Manager.printDebug("-=-=-=-=-=-=-=-=-=-=- -=- -=-=-=-=-=-=-=-=-=-=-");
			}
			if (getSender() != null)
				AreaMethods.sendMessage(getSender(), alreadyLoading().replace("%area%", area), true);
			return;
		}
		if (AreaMethods.isAsyncCreation && AreaMethods.creations.contains(area)) {
			AreaMethods.sendMessage(getSender(), stillCreating().replace("%area%", area), true);
			return;
		}
		if (getSender() != null) {
			Bukkit.getServer().getPluginManager().callEvent(new AreaLoadEvent(getSender(), area));
		} else {
			Bukkit.getServer().getPluginManager().callEvent(new AreaLoadEvent(area));
		}
		
		this.area = area;
		this.maxX = x;
		this.maxZ = z;
		this.length = (AreaMethods.getAreaLength(area) != null && AreaMethods.getAreaLength(area) > 0) ? AreaMethods.getAreaLength(area) : 16;
		chunks = 0;
		x++;
		z++;
		this.maxChunks = (x * z);
		this.location = location;
		fakeTime = System.currentTimeMillis();
		time = System.currentTimeMillis();
		areas.add(this);
		manage();
	}
	
	public static void init() {
		percentage = Manager.getConfig().getDouble("Settings.AreaLoading.Percentage");
	}

	private void progress() throws FileNotFoundException, WorldEditException, IOException {
		if (!AreaMethods.loadSchematicArea(sender, getArea(), AreaMethods.getFileName(getArea(), x, z), location.getWorld(), location.clone().add(x * length, 0.0D, z * length))) {
			AreaReloader.log.warning("Failed to reset section '" + AreaMethods.getFileName(getArea(), x, z) + "'!");
			Manager.printDebug("-=-=-=-=-=-=-=-=-=-=- Area Loading -=-=-=-=-=-=-=-=-=-=-");
			Manager.printDebug("Failed to reset section '" + AreaMethods.getFileName(getArea(), x, z) + "'!");
			Manager.printDebug("-=-=-=-=-=-=-=-=-=-=- -=- -=-=-=-=-=-=-=-=-=-=-");
			if (sender instanceof Player) {
				Player player = (Player) sender;
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 0.5F);
			}
			return;
		} else {
			if (System.currentTimeMillis() >= time + AreaMethods.getInterval(getArea())) {
				chunks += 1;
				z += 1;
				time = System.currentTimeMillis();
				if (z > this.maxZ) {
					z = 0;
					x += 1;
				}
			}
			if (chunks == this.maxChunks) {
				z -= 1;
				complete();
				return;
			}
		}
	}

	private void complete() {
		this.completed = true;
		if (sender instanceof Player) {
			Player player = (Player) sender;
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, 0.3F);
		}
	}
	
	private void progressAll() {
		List<AreaLoader> completed = new ArrayList<AreaLoader>();
		for (AreaLoader al : areas) {
			if (al.completed) {
				if ((al.sender != null)) {
					final long time = System.currentTimeMillis() - fakeTime;
					AreaMethods.sendMessage(al.sender, success().replace("%area%", al.getArea()).replace("%time%", AreaMethods.formatTime(time)), true);
					Bukkit.getServer().getPluginManager().callEvent(new AreaCompleteEvent(al.getSender(), al.getArea()));
				} else {
					Bukkit.getServer().getPluginManager().callEvent(new AreaCompleteEvent(al.getArea()));
				}
				completed.add(al);
				// remove the area from the queue and cancel its running task.
				if (AreaReloader.getQueue().isQueued(al.getArea())) {
					AreaReloader.getQueue().remove(al.getArea(), AreaReloader.getQueue().getTaskByName(al.getArea()));
					Manager.printDebug("-=-=-=-=-=-=-=-=-=-=- Area Loading -=-=-=-=-=-=-=-=-=-=-");
					Manager.printDebug("Area: " + al.getArea() + " with task id " + AreaReloader.getQueue().getTaskByName(al.getArea()) + " has been removed from the queue list.");
					Manager.printDebug(al.getArea() + " has succesfully been removed from the active sessions.");
					Manager.printDebug("-=-=-=-=-=-=-=-=-=-=- -=- -=-=-=-=-=-=-=-=-=-=-");
				}
			} else {
				try {
					al.progress();
				} catch (WorldEditException | IOException e) {
					if (al.sender != null) {
						AreaMethods.sendMessage(al.sender, fail().replace("%area%", al.getArea()), true);
					}
					Manager.printDebug("-=-=-=-=-=-=-=-=-=-=- Area Loading -=-=-=-=-=-=-=-=-=-=-");
					Manager.printDebug("An error has occurred while loading area: " + al.getArea());
					Manager.printDebug("");
					Manager.printDebug(e.getMessage());
					Manager.printDebug("-=-=-=-=-=-=-=-=-=-=- -=- -=-=-=-=-=-=-=-=-=-=-");
					e.printStackTrace();
				}
				int perc = (int) (al.chunks * 100.0D / al.maxChunks);
				al.curr_perc = (float) (al.chunks * 100.0D / al.maxChunks);
					if ((Math.round(perc) % percentage == 0L) && (Math.round(perc) % 100L != 0L) && (al.sender != null)) {
						if (!al.sent && Math.round(perc) % percentage == 0L) {
							AreaMethods.sendMessage(al.sender, process().replace("%area%", al.getArea()).replace("%perc%", String.valueOf(perc)), true);
							al.sent = true;
						}
					}
					if (al.sent && Math.round(perc) % percentage != 0L) {
						al.sent = false;
					}
				}
			}
		for (AreaLoader l : completed) {
			areas.remove(l);
		}
	}

	private void manage() {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (!AreaReloader.getQueue().isQueued(getArea())) {
					AreaReloader.getQueue().add(getArea(), this.getTaskId());
				}
				progressAll();
				if (areas.isEmpty()) {
					this.cancel();
				}
			}
		}.runTaskTimer(AreaReloader.getInstance(), 0, 0);
	}
	
	public static String alreadyLoading() {
		return Manager.getConfig().getString("Commands.Load.AlreadyLoading");
	}
	
	public static String process() {
		return Manager.getConfig().getString("Commands.Load.Process");
	}

	private static String success() {
		return Manager.getConfig().getString("Commands.Load.Success");
	}
	
	private static String fail() {
		return Manager.getConfig().getString("Commands.Load.Fail");
	}
	
	public static String stillCreating() {
		return Manager.getConfig().getString("Commands.Load.StillCreating");
	}

	public static void reset(String area) {
		if (isInstance(area))
			areas.remove(get(area));
	}

	public static boolean isInstance(String area) {
		if (get(area).getArea().contains(area))
			return true;

		return false;
	}

	public static AreaLoader get(String area) {
		for (AreaLoader al : areas) {
			if (al.getArea().contains(area))
				return al;
		}
		return null;
	}

	/**
	 * @return maxX
	 */
	public int getMaxX() {
		return maxX;
	}
	
	/**
	 * 
	 * @return maxZ
	 */
	public int getMaxZ() {
		return maxZ;
	}
	
	/**
	 * The advancement point of x.
	 * @return z
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * The advancement point of Z.
	 * @return z
	 */
	public int getZ() {
		return z;
	}

	/**
	 * The maximum X point to set.
	 * @param maxX
	 */
	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}
	
	/**
	 * The maximum Z point to set.
	 * @param maxZ
	 */
	public void setMaxZ(int maxZ) {
		this.maxZ = maxZ;
	}
	
	/**
	 * The advancement of point X to set.
	 * @param maxX
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * The advancement of point Z to set.
	 * @param maxZ
	 */
	public void setZ(int z) {
		this.z = z;
	}
	
	public CommandSender getSender() {
		return sender != null ? sender : null;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	public float getPerc() {
		return curr_perc;
	}
}