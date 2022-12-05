package com.hetag.areareloader.fawe.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.hetag.areareloader.fawe.AreaMethods;
import com.hetag.areareloader.fawe.configuration.Manager;

public class DeleteCommand extends ARCommand {
	public DeleteCommand() {
		super("delete", "/ar delete <name>", formatColors(Manager.getConfig().getString("Commands.Delete.Description")), new String[] { "delete" });
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!hasPermission(sender) || !correctLength(sender, args.size(), 1, 1)) {
			return;
		}
		if (args.size() == 0) {
			sendMessage(sender, getProperUsage(), false);
			return;
		}
		String area = args.get(0);
		if (Manager.areas.getConfig().contains("Areas." + area)) {
			sendMessage(sender, success().replaceAll("%area%", area), true);
			if (DisplayCommand.getDisplayedAreas().contains(area)) {
				DisplayCommand.getDisplayedAreas().remove(area);
			}
			AreaMethods.deleteArea(area);
			return;
		}
		sendMessage(sender, invalidArea().replaceAll("%area%", area), true);

	}

	private String success() {
		return formatColors(Manager.getConfig().getString("Commands.Delete.Success"));
	}

	private String invalidArea() {
		return formatColors(Manager.getConfig().getString("Commands.Delete.InvalidArea"));
	}
	@Override
	protected List<String> getTabCompletion(final CommandSender sender, final List<String> args) {
		List<String> list = new ArrayList<String>();
		if (!sender.hasPermission("areareloader.command.delete") || args.size() >= 1) {
			return new ArrayList<String>();
		}
		for (final String map : AreaMethods.getAreas()) {
			list.add(map);
		}
		return list;
	}
}
