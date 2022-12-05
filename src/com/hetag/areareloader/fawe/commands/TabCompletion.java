package com.hetag.areareloader.fawe.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabCompletion implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0 || args.length == 1) {
			if (Arrays.asList(Executor.commandaliases).contains(command.getName())) {
			return getPossibleCompletions(args, getCommandsForUser(sender));
			} else {
				return getPossibleCompletions(args, getCommandsForUser(sender));
			}
		} else if (args.length > 1) {
			for (final ARCommand cmd : ARCommand.instances.values()) {
				if (Arrays.asList(cmd.getAliases()).contains(args[0].toLowerCase()) && sender.hasPermission("areareloader.command." + cmd.getName())) {
					final List<String> newargs = new ArrayList<String>();
					for (int i = 1; i < args.length - 1; i++) {
						if (!(args[i].equals("") || args[i].equals(" ")) && args.length >= 1) {
							newargs.add(args[i]);
						}
					}
					return getPossibleCompletions(args, cmd.getTabCompletion(sender, newargs));
				}
			}
		}
		return null;
	}
	
	public static List<String> getPossibleCompletions(final String[] args, final List<String> possibilitiesOfCompletion) {
		final String argumentToFindCompletionFor = args[args.length - 1];

		final List<String> listOfPossibleCompletions = new ArrayList<String>();

		for (final String foundString : possibilitiesOfCompletion) {
			if (foundString.regionMatches(true, 0, argumentToFindCompletionFor, 0, argumentToFindCompletionFor.length())) {
				listOfPossibleCompletions.add(foundString);
			}
		}
		return listOfPossibleCompletions;
	}
	
	public static List<String> getCommandsForUser(CommandSender sender) {
		List<String> commands = new ArrayList<String>();
		for (final String command : ARCommand.instances.keySet()) {
			if (sender.hasPermission("areareloader.command." + command.toLowerCase())) {
				commands.add(command.toLowerCase());
			}
		}
		return commands;
	}

}
