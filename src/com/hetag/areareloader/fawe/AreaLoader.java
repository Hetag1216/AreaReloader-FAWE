package com.hetag.areareloader.fawe;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.hetag.areareloader.fawe.configuration.Manager;
import com.sk89q.worldedit.WorldEditException;
public class AreaLoader {
	public static List<AreaLoader> areas = new ArrayList<AreaLoader>();
	private String area;
	private int x, maxX, z, maxZ;
	private int size;
	private int chunks, maxChunks;
	private Location location;
	private boolean completed = false;
	private CommandSender sender;
	private static long fakeTime;
	private static long interval;
	public static double percentage;
	public long time;
	public static BukkitRunnable executer;
	private boolean sent = false;
	private float curr_perc;

	public AreaLoader(String area, int x, int z, int size, Location location, CommandSender sender) {
		if (AreaReloader.getInstance().getQueue().isQueued(area) || areas.contains(this)) {
			if (AreaReloader.debug) {
				Manager.printDebug("-=-=-=-=-=-=-=-=-=-=- Area Loading -=-=-=-=-=-=-=-=-=-=-");
				Manager.printDebug(area + " is already in the queue, it may be currently loading.");
				Manager.printDebug("-=-=-=-=-=-=-=-=-=-=- -=- -=-=-=-=-=-=-=-=-=-=-");
			}
			if (getSender() != null)
			getSender().sendMessage(prefix() + "Area '" + ChatColor.YELLOW + area + ChatColor.GOLD + "' is already being loaded.");
			return;
		}
		if (sender != null) {
			this.sender = sender;
		}
		this.setArea(area);
		this.maxX = x;
		this.maxZ = z;
		this.size = size;
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
		interval = Manager.getConfig().getLong("Settings.AreaLoading.Interval");
		percentage = Manager.getConfig().getDouble("Settings.AreaLoading.Percentage");
	}

	private void progress() throws FileNotFoundException, WorldEditException, IOException {
		if (!AreaMethods.loadSchematicArea(sender, getArea(), AreaMethods.getFileName(getArea(), x, z), location.getWorld(), location.clone().add(x * size, 0.0D, z * size))) {
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
			if (AreaMethods.fastMode) {
				chunks += 1;
				z += 1;
			} else {
				if (System.currentTimeMillis() >= time + interval) {
					chunks += 1;
					z += 1;
					time = System.currentTimeMillis();
				}
			}
		}
		if (z > this.maxZ) {
			z = 0;
			x += 1;
		}
		if (chunks == this.maxChunks) {
			z -= 1;
			complete();
			return;
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
		List<Integer> completed = new ArrayList<Integer>();
		for (AreaLoader al : areas) {
			if (al.completed) {
				if ((al.sender != null)) {
					final long time = System.currentTimeMillis() - fakeTime;
					al.sender.sendMessage(prefix() + success().replace("%area%", al.getArea()).replace("%time%", AreaMethods.formatTime(time)));
				}
				completed.add(areas.indexOf(al));
				// remove the area from the queue and cancel its running task.
				if (AreaReloader.getInstance().getQueue().isQueued(al.getArea())) {
					AreaReloader.getInstance().getQueue().remove(al.getArea(), AreaReloader.getInstance().getQueue().getTaskByName(al.getArea()));
					Manager.printDebug("-=-=-=-=-=-=-=-=-=-=- Area Loading -=-=-=-=-=-=-=-=-=-=-");
					Manager.printDebug("Area: " + al.getArea() + " with task id " + AreaReloader.getInstance().getQueue().getTaskByName(al.getArea()) + " has been removed from the queue list.");
					Manager.printDebug(al.getArea() + " has succesfully been removed from the active sessions.");
					Manager.printDebug("-=-=-=-=-=-=-=-=-=-=- -=- -=-=-=-=-=-=-=-=-=-=-");
				}
			} else {
				try {
					al.progress();
				} catch (WorldEditException | IOException e) {
					if (al.sender != null) {
						al.sender.sendMessage(prefix() + fail().replace("%area%", al.getArea()));
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
							al.sender.sendMessage(prefix() + "Loading area '" + ChatColor.YELLOW + al.getArea() + ChatColor.GOLD + "' " + ChatColor.YELLOW + perc + "%" + ChatColor.GOLD + ".");
							al.sent = true;
						}
					}
					if (al.sent && Math.round(perc) % percentage != 0L) {
						al.sent = false;
					}
				}
			}
		for (Iterator<Integer> iterator = completed.iterator(); iterator.hasNext();) {
			int id = ((Integer) iterator.next()).intValue();
			areas.remove(id);
		}
	}
	
	private void manage() {
		executer = new BukkitRunnable() {
			public void run() {
				if (!AreaReloader.getInstance().getQueue().isQueued(getArea())) {
					AreaReloader.getInstance().getQueue().add(getArea(), this.getTaskId());
				}
				progressAll();
			}
		};
		executer.runTaskTimer(AreaReloader.getInstance(), 0, interval / 1000 * 20);
	}

	public static String prefix() {
		return ChatColor.translateAlternateColorCodes('&', AreaReloader.plugin.getConfig().getString("Settings.Language.ChatPrefix"));
	}

	private static String success() {
		return ChatColor.translateAlternateColorCodes('&', AreaReloader.plugin.getConfig().getString("Commands.Load.Success"));
	}
	
	private static String fail() {
		return ChatColor.translateAlternateColorCodes('&', AreaReloader.plugin.getConfig().getString("Commands.Load.Fail"));
	}
	
	public static void reset(String area) {
		for (AreaLoader al : areas) {
			if (al.getArea().contains(area)) {
				areas.remove(al);
				break;
			}
		}
	}
	
	public static boolean isInstance(String area) {
		for (AreaLoader al : areas) {
			if (al.getArea().contains(area)) {
				return true;
			}
		}
		return false;
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