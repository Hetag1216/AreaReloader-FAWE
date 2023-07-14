package com.hedario.areareloader.fawe.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.hedario.areareloader.fawe.AreaMethods;
import com.hedario.areareloader.fawe.configuration.Manager;
import com.hedario.areareloader.fawe.effects.ParticleEffect;

public class LocationCommand extends ARCommand {

	public LocationCommand() {
		super("location", "/ar location <set|teleport", "Sets a safe location where players will be tped on load", new String[] {"location", "loc"});
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!hasPermission(sender)) {
			return;
		}
		final String area = args.get(0);
		if (!AreaMethods.areaExist(area)) {
			this.sendMessage(sender, "area doesn't exist", true);
			return;
		}
		if (args.get(1).equalsIgnoreCase("set")) {
			// sets the "safe location" of the area to the config and registers it
			final Location location = ((Player) sender).getLocation();
			Manager.areas.getConfig().set("Areas." + area + ".SafeLocation.Enabled", true);
			Manager.areas.getConfig().set("Areas." + area + ".SafeLocation.World", location.getWorld().getName());
			Manager.areas.getConfig().set("Areas." + area + ".SafeLocation.X", location.getX());
			Manager.areas.getConfig().set("Areas." + area + ".SafeLocation.Y", location.getY());
			Manager.areas.getConfig().set("Areas." + area + ".SafeLocation.Z", location.getZ());
			Manager.areas.saveConfig();
		} else if (args.get(1).equalsIgnoreCase("teleport")) {
			// teleports the player to the saved location
			if (sender instanceof Player) {
				((Player) sender).teleport(getSafeLocation(area));
			}
		} else {
			return;
		}
	}
	
	public static void teleportPlayers(final String area) {
		final int ix = AreaMethods.getAreaX(area);
		final int iy = AreaMethods.getAreaY(area);
		final int iz = AreaMethods.getAreaZ(area);
		final int jx = AreaMethods.getAreaMaxX(area);
		final int jy = AreaMethods.getAreaMaxY(area);
		final int jz = AreaMethods.getAreaMaxZ(area);

		for (double x = ix; x <= jx; x++) {
			for (double y = iy; y <= jy; y++) {
				for (double z = iz; z <= jz; z++) {
					final Location loc = new Location(getSafeLocation(area).getWorld(), x, y, z);
					ParticleEffect.CLOUD.display(loc.clone().add(0, 0.5F, 0));
					for (Entity entities : getSafeLocation(area).getWorld().getNearbyEntities(loc, 1, 1, 1, target -> target instanceof Player)) {
						entities.teleport(getSafeLocation(area));
					}
				}
			}
		}
	}
	
	public static Location getSafeLocation(final String area) {
		World world = Bukkit.getWorld(Manager.areas.getConfig().getString("Areas." + area + ".SafeLocation.World"));
		double sx = Manager.areas.getConfig().getDouble("Areas." + area + ".SafeLocation.X");
		double sy = Manager.areas.getConfig().getDouble("Areas." + area + ".SafeLocation.Y");
		double sz = Manager.areas.getConfig().getDouble("Areas." + area + ".SafeLocation.Z");
		return new Location(world, sx, sy, sz);
	}
	
	@Override
	protected List<String> getTabCompletion(final CommandSender sender, final List<String> args) {
		List<String> list = new ArrayList<String>();
		if (!sender.hasPermission("areareloader.command.location")) {
			return new ArrayList<String>();
		}
		if (args.size() == 0) {
			for (final String map : AreaMethods.getAreas()) {
				list.add(map);
			}
		} else if (args.size() == 1) {
			list.add("set"); list.add("teleport");
		}
		return list;
	}

}
