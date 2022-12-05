package com.hetag.areareloader.fawe.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.hetag.areareloader.fawe.configuration.Manager;

public class HelpCommand extends ARCommand {
	private String invalidTopic;

	public HelpCommand() {
		super("help", "/ar help <Page/Topic>", formatColors("Commands.Help.Description"), new String[] { "help", "h" });
		this.invalidTopic = formatColors(Manager.getConfig().getString("Commands.Help.InvalidTopic"));
	}

	public void execute(CommandSender sender, List<String> args) {
		if ((!hasPermission(sender)) || (!correctLength(sender, args.size(), 0, 1))) {
			return;
		}
		if (args.size() == 0) {
			List<String> strings = new ArrayList<>();
			for (ARCommand command : instances.values()) {
				if ((!command.getName().equalsIgnoreCase("help")) && (sender.hasPermission("areareloader.command." + command.getName()))) {
					strings.add(command.getProperUse());
				}
			}
			Collections.sort(strings);
			Collections.reverse(strings);
			Collections.reverse(strings);
			for (String s : getPage(strings, ChatColor.GRAY + "", 1, false)) {
				String start = s.substring(0, 2);
				this.sendMessage(sender, "&6" + start + " &e" + s.substring(3, s.length()), false);
			}
			return;
		}
		String arg = ((String) args.get(0)).toLowerCase();
		if (isNumeric(arg)) {
			List<String> strings = new ArrayList<>();
			for (ARCommand command : instances.values()) {
				strings.add(command.getProperUse());
			}
			for (String s : getPage(strings, "", Integer.valueOf(arg).intValue(), true)) {
				String start = s.substring(0, 2);
				this.sendMessage(sender, "&6" + start + " &e" + s.substring(3, s.length()), false);
			}
		} else if (instances.keySet().contains(arg)) {
			((ARCommand) instances.get(arg)).help(sender, true);
		} else {
			this.sendMessage(sender, invalidTopic, false);
		}
	}
}
