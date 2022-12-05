package com.hetag.areareloader.fawe.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import com.hetag.areareloader.fawe.AreaReloader;
import com.hetag.areareloader.fawe.configuration.Manager;

import net.md_5.bungee.api.ChatColor;

public class ListCommand extends ARCommand {
	public ListCommand() {
		super("list", "/ar list", ChatColor.translateAlternateColorCodes('&', Manager.getConfig().getString("Commands.List.Description")), new String[] { "list" });
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!hasPermission(sender) || !correctLength(sender, args.size(), 0, 1)) {
			return;
		}
		if (args.size() == 0) {
			List<String> strings = new ArrayList<>();
			ConfigurationSection cs = Manager.areas.getConfig().getConfigurationSection("Areas");
			if (cs != null && cs.getKeys(false).size() > 0) {
				for (String area : cs.getKeys(false)) {
					strings.add(area);
				}
			} else {
				sendMessage(sender, notFound(), true);
				return;
			}
			Collections.sort(strings);
			Collections.reverse(strings);
			for (String formatted : getPage(strings, "", 1, true)) {
				if (AreaReloader.getInstance().getQueue().isQueued(formatted)) {
					formatted = formatted + " (Being loaded)";
				}
				sendMessage(sender, "&e " + formatted, false);
				
			}
			return;
		} else if (args.size() == 1) {
			String arg = args.get(0).toLowerCase();
			if (isNumeric(arg)) {
				List<String> strings = new ArrayList<>();
				ConfigurationSection cs = Manager.areas.getConfig().getConfigurationSection("Areas");
				if (cs != null && cs.getKeys(false).size() > 0) {
					for (String area : cs.getKeys(false)) {
						strings.add(area);
					}
				} else {
					sendMessage(sender, notFound(), true);
					return;
				}
				for (String formatted : getPage(strings, "", Integer.valueOf(arg), true)) {
					if (AreaReloader.getInstance().getQueue().isQueued(formatted)) {
						formatted = formatted + " (Being loaded)";
					}
					sendMessage(sender, "&e" + formatted, false);
				}
			} else {
				sendMessage(sender, ChatColor.YELLOW + arg + ChatColor.GOLD + "is not a number!", true);
			}
		}
	}

	private String notFound() {
		return Manager.getConfig().getString("Commands.List.NoAreasFound");
	}
}
