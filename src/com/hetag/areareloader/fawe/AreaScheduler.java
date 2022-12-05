package com.hetag.areareloader.fawe;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.hetag.areareloader.fawe.configuration.Manager;

public class AreaScheduler {
	public static List<AreaScheduler> areas = new ArrayList<>();
	public static FileConfiguration config = Manager.areas.getConfig();

	public String area;
	public static boolean notifyOnReload, notifyConsoleOnReload, checker;
	private long reset;
	private long delay;

	public AreaScheduler(String area, long delay) {
		if (AreaReloader.getInstance().getQueue().isQueued(area) || areas.contains(this)) {
			updateDelay(area, delay);
			return;
		}
		this.area = area;
		this.delay = delay;
		this.reset = System.currentTimeMillis();
		areas.add(this);
	}
	
	public static void init() {
		checker = Manager.getConfig().getBoolean("Settings.AutoReload.Checker");
		notifyOnReload = Manager.getConfig().getBoolean("Settings.AutoReload.Notify.Admins");
		notifyConsoleOnReload = Manager.getConfig().getBoolean("Settings.AutoReload.Notify.Console");
		
		if (checker) {
			AreaReloader.log.info("Checker for areas to auto reload is enabled!");
			checkForAreas();
			manageTimings();
			AreaReloader.log.info("Found " + areas.size() + " areas to automatically reload!");
		} else {
			AreaReloader.log.info("Checker for areas to auto reload is disabled!");
		}
	}
	
	public static void checkForAreas() {
		if (config.contains("Areas")) {
			for (String keys : config.getConfigurationSection("Areas").getKeys(false)) {
				if (config.contains("Areas." + keys + ".AutoReload.Enabled") && config.getBoolean("Areas." + keys + ".AutoReload.Enabled") == true) {
					long resetTime = config.getLong("Areas." + keys + ".AutoReload.Time");
					new AreaScheduler(keys, resetTime);
				}
			}
		}
	}



	public static void updateDelay(String area, long delay) {
		for (AreaScheduler s : areas) {
			if (s.getArea().equalsIgnoreCase(area)) {
				s.setDelay(delay);
				s.setLastReset(System.currentTimeMillis());
				return;
			}
		}
		new AreaScheduler(area, delay);
	}

	public static long getRemainingTime(String area) {
		for (AreaScheduler scheduler : areas) {
			if (scheduler.getArea().equalsIgnoreCase(area)) {
				return scheduler.getLastReset() + scheduler.getDelay() - System.currentTimeMillis();
			}
		}
		return 0L;
	}
	
	public static String getAreas() {
		if (config.contains("Areas")) {
			for (String keys : config.getConfigurationSection("Areas").getKeys(false)) {
				if (config.contains("Areas." + keys + ".AutoReload.Enabled") && config.getBoolean("Areas." + keys + ".AutoReload.Enabled") == true) {
					return keys;
				}
			}
		}
		return null;
	}
	
	public static long getAreasResetTime() {
		if (config.contains("Areas")) {
			for (String keys : config.getConfigurationSection("Areas").getKeys(false)) {
				if (config.contains("Areas." + keys + ".AutoReload.Enabled") && config.getBoolean("Areas." + keys + ".AutoReload.Enabled") == true) {
					long resetTime = config.getLong("Areas." + keys + ".AutoReload.Time");
					return resetTime;
				}
			}
		}
		return 0;
	}
	
	public static boolean isInstance(String area) {
		for (AreaScheduler as : areas) {
			if (as.area == area) {
				return true;
			}
		}
		return false;
	}
	
	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public long getDelay() {
		return this.delay;
	}

	public void setDelay(long delay) {
		this.delay = Long.valueOf(delay);
	}

	public long getLastReset() {
		return this.reset;
	}

	public void setLastReset(long reset) {
		this.reset = reset;
	}

	public static void progress() {
		for (AreaScheduler scheduler : areas) {
			if (System.currentTimeMillis() >= scheduler.getDelay() + scheduler.getLastReset()) {
				World world = Bukkit.getServer().getWorld(config.getString("Areas." + scheduler.getArea() + ".World"));
				int x = AreaMethods.getAreaX(scheduler.getArea());
				int z = AreaMethods.getAreaZ(scheduler.getArea());
				int y = AreaMethods.getAreaY(scheduler.getArea());
				int size = AreaMethods.getAreaChunk(scheduler.getArea());
				int maxX = AreaMethods.getAreaSizeX(scheduler.getArea());
				int maxZ = AreaMethods.getAreaSizeZ(scheduler.getArea());
				Location location = new Location(world, x, y, z);
				new AreaLoader(scheduler.getArea(), maxX, maxZ, size, location, null);
				if (notifyConsoleOnReload) {
					AreaReloader.log.info("Automatically reloading area: " + scheduler.getArea());
				}
				if (notifyOnReload) {
					for (Player ops : Bukkit.getServer().getOnlinePlayers()) {
						if (ops.isOp() || ops.hasPermission("areareloader.command.admin")) {
							ops.sendMessage(AreaLoader.prefix() + "Automatically reloading area: " + ChatColor.YELLOW + scheduler.getArea() + ChatColor.GOLD + ".");
							ops.getWorld().playSound(ops.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1F, 0.3F);
						}
					}
				}
				scheduler.setLastReset(System.currentTimeMillis());
			}
		}
	}

	public static void manageTimings() {
		Runnable br = new Runnable() {
			public void run() {
				progress();
			}
		};
		AreaReloader.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(AreaReloader.plugin, br, 0, 10000 / 1000 * 20);
	}

}
