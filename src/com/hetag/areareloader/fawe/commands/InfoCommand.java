package com.hetag.areareloader.fawe.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.hetag.areareloader.fawe.AreaLoader;
import com.hetag.areareloader.fawe.AreaMethods;
import com.hetag.areareloader.fawe.AreaReloader;
import com.hetag.areareloader.fawe.AreaScheduler;
import com.hetag.areareloader.fawe.configuration.Manager;

public class InfoCommand extends ARCommand {
	public InfoCommand() {
		super("info", "/ar info <area>",  formatColors(Manager.getConfig().getString("Commands.Info.Description")), new String[] {"info"});
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!hasPermission(sender) || !correctLength(sender, args.size(), 0, 1)) {
			return;
		}
		if (args.size() == 0) {
			sendMessage(sender, getProperUsage(), false);
			return;
		}
		String area = args.get(0);
		String display = null;
		if (DisplayCommand.getDisplayedAreas().contains(area)) {
			display = "true";
		} else {
			display = "false";
		}
		if (Manager.areas.getConfig().contains("Areas." + area)) {
			sender.sendMessage(prefix);
			sendMessage(sender, "&7-=-=-=-=-=-=-=-=-=-=- « &6" + area + " &7» -=-=-=-=-=-=-=-=-=-=-", false);
			sendMessage(sender, "&6World &7» &e" + AreaMethods.getAreaInWorld(area) , false);
			sendMessage(sender, "&6First corner &7» &e" + AreaMethods.getAreaX(area) + "&7, &e" + AreaMethods.getAreaY(area) + "&7, &e" + AreaMethods.getAreaZ(area), false);
			sendMessage(sender, "&6Second corner &7» &e" + AreaMethods.getAreaMaxX(area) + "&7, &e" + AreaMethods.getAreaMaxY(area) + "&7, &e" + AreaMethods.getAreaMaxZ(area), false);
			sendMessage(sender, "&6Is being displayed &7» &e" + display, false);
			sendMessage(sender, "&6Has copied entities &7» &e" + Manager.areas.getConfig().getBoolean("Areas." + area + ".HasCopiedEntities"), false);
			sendMessage(sender, "&6Has copied biomes &7» &e" + Manager.areas.getConfig().getBoolean("Areas." + area + ".HasCopiedBiomes"), false);
			sendMessage(sender, "&6Is using fast mode &7» &e" + AreaMethods.fastMode, false);
			if (AreaReloader.getInstance().getQueue().isQueued(area)) {
				for (AreaLoader al : AreaLoader.areas) {
					if (al.getArea().contains(area))
					sendMessage(sender, "&6Currently loaded percentage &7» &e" + String.format("%.2f", al.getPerc()), false);
				}
			}
			sendMessage(sender, "&6Is automatically reloading &7» &e" + Manager.areas.getConfig().getBoolean("Areas." + area + ".AutoReload.Enabled"), false);
			if (Manager.areas.getConfig().getBoolean("Areas." + area + ".AutoReload.Enabled") == true) {
				sendMessage(sender, "&6Auto reloading time &7» &e" + Manager.areas.getConfig().getLong("Areas." + area + ".AutoReload.Time"), false);
				sendMessage(sender, "&6Next auto reload in &7» &e" + AreaScheduler.getRemainingTime(area), false);
			}
			return;
		} else {
			sendMessage(sender, LoadCommand.invalidArea().replaceAll("%area%", area), true);
		}
	}
	
	@Override
	protected List<String> getTabCompletion(final CommandSender sender, final List<String> args) {
		List<String> list = new ArrayList<String>();
		if (!sender.hasPermission("areareloader.command.info") || args.size() >= 1) {
			return new ArrayList<String>();
		}
		for (final String map : AreaMethods.getAreas()) {
			list.add(map);
		}
		return list;
	}
}
