package com.hedario.areareloader.fawe.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.hedario.areareloader.fawe.configuration.Manager;

public class HelpCommand extends ARCommand {
	private String invalidTopic;

	public HelpCommand() {
		super("help", "/ar help <Page/Topic>", Manager.getConfig().getString("Commands.Help.Description"), new String[] { "help", "h" });
		this.invalidTopic = Manager.getConfig().getString("Commands.Help.InvalidTopic");
	}

	public void execute(CommandSender sender, List<String> args) {
		if ((!hasPermission(sender)) || (!correctLength(sender, args.size(), 0, 1))) {
			return;
		}
		if (args.size() == 0) {
			List<String> strings = new ArrayList<>();
			for (ARCommand command : instances.values()) {
				if ((!command.getName().equalsIgnoreCase("help"))
						&& (sender.hasPermission("areareloader.command." + command.getName()))) {
					strings.add(command.getProperUse());
				}
			}
			Collections.sort(strings);
			Collections.reverse(strings);
			Collections.reverse(strings);
			for (String s : getPage(strings, 1, false)) {
				if (!s.equalsIgnoreCase(getPage(strings, 1, false).get(0))) {
					String start = s.substring(0, 3);
					this.sendMessage(sender, "&6" + start + " &e" + s.substring(4, s.length()), false);
				} else {
					this.sendMessage(sender, getPage(strings, 1, false).get(0), false);
				}
			}
			return;
		}
		String arg = ((String) args.get(0)).toLowerCase();
		if (isNumeric(arg)) {
			List<String> strings = new ArrayList<>();
			for (ARCommand command : instances.values()) {
				strings.add(command.getProperUse());
			}
			for (String s : getPage(strings, Integer.valueOf(arg).intValue(), true)) {
				if (!s.equalsIgnoreCase(getPage(strings, 1, false).get(0))) {
					String start = s.substring(0, 3);
					this.sendMessage(sender, "&6" + start + " &e" + s.substring(4, s.length()), false);
				} else {
					this.sendMessage(sender, getPage(strings, 1, false).get(0), false);
				}
			}
		} else if (instances.keySet().contains(arg)) {
			((ARCommand) instances.get(arg)).help(sender, true);
		} else {
			this.sendMessage(sender, invalidTopic, false);
		}
	}

	@Override
	protected List<String> getTabCompletion(final CommandSender sender, final List<String> args) {
		List<String> list = new ArrayList<String>();
		if (!sender.hasPermission("areareloader.command.help")) {
			return new ArrayList<String>();
		}
		for (String commands : instances.keySet()) {
			list.add(commands);
		}
		return list;
	}
}
