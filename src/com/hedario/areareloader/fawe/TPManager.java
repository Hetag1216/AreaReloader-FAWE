package com.hedario.areareloader.fawe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.hedario.areareloader.fawe.configuration.Manager;

public class TPManager {
	private String area;
	private int x, y, z, maxIts;
	private int maxX, maxY, maxZ;
	private boolean process;
	public static final Map<String, TPManager> handler = new ConcurrentHashMap<String, TPManager>();

	public TPManager(String area) {
		this.area = area;
		final int ix = AreaMethods.getAreaX(area);
		final int iy = AreaMethods.getAreaY(area);
		final int iz = AreaMethods.getAreaZ(area);
		final int jx = AreaMethods.getAreaMaxX(area);
		final int jy = AreaMethods.getAreaMaxY(area);
		final int jz = AreaMethods.getAreaMaxZ(area);

		this.x = Math.min(ix, jx);
		this.y = Math.min(iy, jy);
		this.z = Math.min(iz, jz);
		this.maxX = Math.max(ix, jx);
		this.maxY = Math.max(iy, jy);
		this.maxZ = Math.max(iz, jz);

		maxIts = getSpeed(area);
		process = true;
		handler.put(area, this);
		manage(area);
	}

	public void manage(String area) {
		for (TPManager instance : handler.values()) {
			new BukkitRunnable() {
				@Override
				public void run() {
					int it = 0;
					final String areaName = instance.getArea();
					instance.setProcess(true);
					for (int x = instance.getX(); x <= instance.getMaxX() && instance.isProcess(); x++) {
						for (int z = instance.getZ(); z <= instance.getMaxZ() && instance.isProcess(); z++) {
							for (int y = instance.getY(); y <= instance.getMaxY() && instance.isProcess(); y++) {
								final World world = Bukkit.getWorld(AreaMethods.getAreaInWorld(areaName));
								final Location loc = new Location(world, x, y, z);
								for (Entity entity : world.getNearbyEntities(loc, 1, 1, 1, target -> target instanceof Player)) {
									Location safeLocation = getSafeLocation(areaName);
									safeLocation.setYaw(getYaw(area));
									safeLocation.setPitch(getPitch(area));
									entity.teleport(safeLocation);
								}
							}
						}
						it++;
						if (it >= instance.getMaxIts()) {
							instance.setX(++x);
							instance.setProcess(false);
							this.cancel();
							schedule(areaName);
							return;
						}
					}
					handler.remove(areaName);
					this.cancel();
				}
			}.runTask(AreaReloader.getInstance());

			if (!instance.isProcess()) {
				continue;
			}
		}
	}

	public void schedule(final String area) {
		new BukkitRunnable() {
			@Override
			public void run() {
				manage(area);
			}
		}.runTaskLater(AreaReloader.getInstance(), getInterval(area) / 1000 * 20);
	}

	public int getSpeed(final String area) {
		if (Manager.areas.getConfig().contains("Areas." + area + ".SafeLocation.Settings.Speed")) {
			return Manager.areas.getConfig().getInt("Areas." + area + ".SafeLocation.Settings.Speed");
		}
		return 0;
	}

	public int getInterval(final String area) {
		if (Manager.areas.getConfig().contains("Areas." + area + ".SafeLocation.Settings.Interval")) {
			return Manager.areas.getConfig().getInt("Areas." + area + ".SafeLocation.Settings.Interval");
		}
		return 0;
	}

	public static boolean isEnabled(final String area) {
		if (Manager.areas.getConfig().contains("Areas." + area + ".SafeLocation.Enabled")) {
			return Manager.areas.getConfig().getBoolean("Areas." + area + ".SafeLocation.Enabled");
		}
		return false;
	}

	public static boolean isInstanceOf(final String area) {
		if (handler.containsKey(area))
			return true;
		return false;
	}

	public static Location getSafeLocation(final String area) {
		if (area == null)
			return null;
		World world = Bukkit.getWorld(Manager.areas.getConfig().getString("Areas." + area + ".SafeLocation.World"));
		double sx = Manager.areas.getConfig().getDouble("Areas." + area + ".SafeLocation.X");
		double sy = Manager.areas.getConfig().getDouble("Areas." + area + ".SafeLocation.Y");
		double sz = Manager.areas.getConfig().getDouble("Areas." + area + ".SafeLocation.Z");
		return new Location(world, sx, sy, sz);
	}
	
	public static float getYaw(final String area) {
		return (float) Manager.areas.getConfig().getDouble("Areas." + area + ".SafeLocation.Yaw");
	}
	
	public static float getPitch(final String area) {
		return (float) Manager.areas.getConfig().getDouble("Areas." + area + ".SafeLocation.Pitch");
	}

	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the z
	 */
	public int getZ() {
		return z;
	}

	/**
	 * @param z the z to set
	 */
	public void setZ(int z) {
		this.z = z;
	}

	/**
	 * @return the count
	 */
	public int getMaxIts() {
		return maxIts;
	}

	/**
	 * @param count the count to set
	 */
	public void setMaxIts(int count) {
		this.maxIts = count;
	}

	/**
	 * @return the process
	 */
	public boolean isProcess() {
		return process;
	}

	/**
	 * @param process the process to set
	 */
	public void setProcess(boolean process) {
		this.process = process;
	}

	/**
	 * @return the maxX
	 */
	public int getMaxX() {
		return maxX;
	}

	/**
	 * @param maxX the maxX to set
	 */
	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	/**
	 * @return the maxZ
	 */
	public int getMaxZ() {
		return maxZ;
	}

	/**
	 * @param maxZ the maxZ to set
	 */
	public void setMaxZ(int maxZ) {
		this.maxZ = maxZ;
	}

	/**
	 * @return the maxY
	 */
	public int getMaxY() {
		return maxY;
	}

	/**
	 * @param maxY the maxU to set
	 */
	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}
}
