package com.hedario.areareloader.fawe;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.hedario.areareloader.fawe.configuration.Manager;

import net.md_5.bungee.api.ChatColor;

public class AreaScheduler {
	public static List<AreaScheduler> areas = new ArrayList<>();

	public String area;
	public static boolean notifyOnReload, notifyConsoleOnReload, checker;
	private long reset;
	private long delay;

	public AreaScheduler(String area, long delay) {
		if (Loader.getInstances().containsKey(area) && areas.contains(this)) {
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
		if (!checker) {
			AreaReloader.log.info("Checker for areas to auto reload is disabled!");
			return;
		}
		if (!areas.isEmpty()) {
			areas.clear();
		}
		notifyOnReload = Manager.getConfig().getBoolean("Settings.AutoReload.Notify.Admins");
		notifyConsoleOnReload = Manager.getConfig().getBoolean("Settings.AutoReload.Notify.Console");
		AreaReloader.log.info("Checker for areas to auto reload is enabled!");
		checkForAreas();
		manageTimings();
		AreaReloader.log.info("Found " + areas.size() + " areas to automatically reload!");
	}

	public static void checkForAreas() {
		if (Manager.getAreasConfig().contains("Areas")) {
			for (String keys : Manager.getAreasConfig().getConfigurationSection("Areas").getKeys(false)) {
				if (Manager.getAreasConfig().contains("Areas." + keys + ".AutoReload.Enabled") && Manager.getAreasConfig().getBoolean("Areas." + keys + ".AutoReload.Enabled") == true) {
					long resetTime = Manager.getAreasConfig().getLong("Areas." + keys + ".AutoReload.Time");
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
		if (Manager.getAreasConfig().contains("Areas")) {
			for (String keys : Manager.getAreasConfig().getConfigurationSection("Areas").getKeys(false)) {
				if (Manager.getAreasConfig().contains("Areas." + keys + ".AutoReload.Enabled") && Manager.getAreasConfig().getBoolean("Areas." + keys + ".AutoReload.Enabled") == true) {
					return keys;
				}
			}
		}
		return null;
	}
	
	public static long getAreasResetTime() {
		if (Manager.getAreasConfig().contains("Areas")) {
			for (String keys : Manager.getAreasConfig().getConfigurationSection("Areas").getKeys(false)) {
				if (Manager.getAreasConfig().contains("Areas." + keys + ".AutoReload.Enabled") && Manager.getAreasConfig().getBoolean("Areas." + keys + ".AutoReload.Enabled") == true) {
					long resetTime = Manager.getAreasConfig().getLong("Areas." + keys + ".AutoReload.Time");
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
				if (Loader.getInstances().containsKey(scheduler.getArea())) {
					scheduler.setLastReset(System.currentTimeMillis());
					continue;
				}
				World world = Bukkit.getServer().getWorld(Manager.getAreasConfig().getString("Areas." + scheduler.getArea() + ".World"));
				int x = AreaMethods.getAreaX(scheduler.getArea());
				int z = AreaMethods.getAreaZ(scheduler.getArea());
				int y = AreaMethods.getAreaY(scheduler.getArea());
				int maxX = AreaMethods.getAreaSizeX(scheduler.getArea());
				int maxZ = AreaMethods.getAreaSizeZ(scheduler.getArea());
				Location location = new Location(world, x, y, z);
				new Loader(scheduler.getArea(), location, maxX, maxZ, null);
				if (notifyConsoleOnReload) {
					AreaReloader.log.info("Automatically reloading area: " + scheduler.getArea());
				}
				if (notifyOnReload) {
					for (Player ops : Bukkit.getServer().getOnlinePlayers()) {
						if (ops.isOp() || ops.hasPermission("areareloader.command.admin")) {
							ops.sendMessage(ChatColor.translateAlternateColorCodes('&', AreaMethods.getPrefix()) + AreaMethods.getPrimaryColor() + "Automatically reloading " + AreaMethods.getSecondaryColor() + scheduler.getArea() + AreaMethods.getPrimaryColor() + ".");
						}
					}
				}
				scheduler.setLastReset(System.currentTimeMillis());
			}
		}
	}

	public static void manageTimings() {
		Bukkit.getScheduler().runTaskTimerAsynchronously(AreaReloader.plugin, () -> {
			progress();
		}, 600, 200);
	}
}
