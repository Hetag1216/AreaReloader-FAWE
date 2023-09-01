package com.hedario.areareloader.fawe.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hedario.areareloader.fawe.AreaMethods;
import com.hedario.areareloader.fawe.TPManager;
import com.hedario.areareloader.fawe.configuration.Manager;

public class LocationCommand extends ARCommand {
	public LocationCommand() {
		super("location", "/ar location <set|teleport>", Manager.getConfig().getString("Commands.Location.Description"), new String[] {"location", "loc"});
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!hasPermission(sender) || !this.correctLength(sender, args.size(), 2, 2) || !isPlayer(sender)) {
			return;
		}
		
		final String area = args.get(0);
		if (!AreaMethods.areaExist(area)) {
			this.sendMessage(sender, invalidArea(), true);
			return;
		}
		
		if (args.get(1).equalsIgnoreCase("set")) {
			final Location location = ((Player) sender).getLocation();
			Manager.areas.getConfig().set("Areas." + area + ".SafeLocation.Enabled", true);
			Manager.areas.getConfig().set("Areas." + area + ".SafeLocation.World", location.getWorld().getName());
			Manager.areas.getConfig().set("Areas." + area + ".SafeLocation.X", (int) location.getX());
			Manager.areas.getConfig().set("Areas." + area + ".SafeLocation.Y", (int) location.getY());
			Manager.areas.getConfig().set("Areas." + area + ".SafeLocation.Z", (int) location.getZ());
			Manager.areas.getConfig().set("Areas." + area + ".SafeLocation.Settings.Speed", (AreaMethods.getAreaChunk(area) > 7 ? 7 : AreaMethods.getAreaChunk(area)));
			Manager.areas.getConfig().set("Areas." + area + ".SafeLocation.Settings.Interval", 500);
			Manager.areas.saveConfig();
			this.sendMessage(sender, onSet().replace("%area%", area), true);
		} else if (args.get(1).equalsIgnoreCase("teleport")) {
			if (sender instanceof Player) {
				((Player) sender).teleport(TPManager.getSafeLocation(area));
			}
			this.sendMessage(sender, onTeleport().replace("%area%", area), true);
		} else {
			return;
		}
	}
	
	private String onSet() {
		return Manager.getConfig().getString("Commands.Location.Set");
	}
	
	private String onTeleport() {
		return Manager.getConfig().getString("Commands.Location.Teleport");
	}
	
	private String invalidArea() {
		return Manager.getConfig().getString("Commands.Location.InvalidArea");
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
