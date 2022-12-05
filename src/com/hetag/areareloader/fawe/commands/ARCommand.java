package com.hetag.areareloader.fawe.commands;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hetag.areareloader.fawe.configuration.Manager;

public abstract class ARCommand implements SubCommand {
	protected String noPermissionMessage;
	protected String mustBePlayerMessage;
	private final String name;
	protected final String prefix;
	private final String properUse;
	private final String description;
	private final String[] aliases;
	public static Map<String, ARCommand> instances = new HashMap<>();

	public ARCommand(String name, String properUse, String description, String[] aliases) {
		this.name = name;
		this.properUse = properUse;
		this.description = description;
		this.aliases = aliases;
		this.noPermissionMessage = ChatColor.translateAlternateColorCodes('&', Manager.getConfig().getString("Settings.Language.NoPermission"));
		this.mustBePlayerMessage = ChatColor.translateAlternateColorCodes('&', Manager.getConfig().getString("Settings.Language.MustBePlayer"));
		this.prefix = ChatColor.translateAlternateColorCodes('&', Manager.getConfig().getString("Settings.Language.ChatPrefix"));
		instances.put(name, this);
	}

	public String getName() {
		return ChatColor.GREEN + this.name;
	}

	public String getProperUse() {
		return this.properUse;
	}

	public String getDescription() {
		return this.description;
	}

	public String[] getAliases() {
		return this.aliases;
	}

	public void help(CommandSender sender, boolean description) {
		sender.sendMessage(getProperUsage());
		if (description) {
			sender.sendMessage(ChatColor.GRAY + this.description);
		}
	}
	protected static String formatColors(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	
	protected void sendMessage(CommandSender sender, String message, boolean prefix) {
		if (prefix) {
			sender.sendMessage(formatColors(this.prefix + message));
		} else {
			sender.sendMessage(formatColors(message));
		}
		return;
	}

	protected boolean hasPermission(CommandSender sender) {
		if (sender.hasPermission("areareloader.command." + this.name)) {
			return true;
		}
		sender.sendMessage(prefix + this.noPermissionMessage);
		return false;
	}

	protected boolean hasPermission(CommandSender sender, String extra) {
		if (sender.hasPermission("areareloader.command." + this.name + "." + extra)) {
			return true;
		}
		sender.sendMessage(prefix + this.noPermissionMessage);
		return false;
	}

	protected boolean correctLength(CommandSender sender, int size, int min, int max) {
		if ((size < min) || (size > max)) {
			help(sender, false);
			return false;
		}
		return true;
	}

	protected boolean isPlayer(CommandSender sender) {
		if ((sender instanceof Player)) {
			return true;
		}
		sender.sendMessage(prefix + this.mustBePlayerMessage);
		return false;
	}

	protected boolean isNumeric(String id) {
		NumberFormat formatter = NumberFormat.getInstance();
		ParsePosition pos = new ParsePosition(0);
		formatter.parse(id, pos);
		return id.length() == pos.getIndex();
	}

	/**
	 * Gets the command name of the specified command.
	 * 
	 * @param cmd
	 * @return
	 */
	protected String getCommand(String cmd) {
		return ChatColor.DARK_GRAY + "\n/" + ChatColor.DARK_AQUA + "ar " + ChatColor.GREEN + cmd;
	}

	/**
	 * Gets the permission of the specified command.
	 * 
	 * @param pex
	 * @return
	 */
	protected String getPermission(String pex) {
		String permission = ChatColor.DARK_AQUA + "areareloader.command.";
		String perm = "- " + permission + ChatColor.AQUA + pex;
		return perm;
	}

	/**
	 * Gets the proper usage of the command. Recommended for formatting.
	 * 
	 * @return the proper usage of the command
	 */
	protected String getProperUsage() {
		return prefix + ChatColor.translateAlternateColorCodes('&', Manager.getConfig().getString("Commands.Help.ProperUsage")) + getProperUse();
	}

	protected List<String> getPage(List<String> entries, String title, int page, boolean sort) {
		List<String> strings = new ArrayList<>();
		if (sort) {
			Collections.sort(entries);
		}
		if (page < 1) {
			page = 1;
		}
		if (page * 8 - 8 >= entries.size()) {
			page = Math.round(entries.size() / 8) + 1;
			if (page < 1) {
				page = 1;
			}
		}
		strings.add(formatColors("&8&m-----&r " + this.prefix + " &7 -&8[&7" + page + "&8/" + "&7" + (int) Math.ceil((entries.size() + 0.0D) / 8.0D) + "&8] &m-----&r "));
		if (title != null || title != "") {
			strings.add(title);
		}
		if (entries.size() > page * 8 - 8) {
			for (int i = page * 8 - 8; i < entries.size(); i++) {
				if (entries.get(i) != null) {
					strings.add(entries.get(i));
				}
				if (i >= page * 8 - 1) {
					break;
				}
			}
		}
		return strings;
	}
	
	protected List<String> getTabCompletion(final CommandSender sender, final List<String> args) {
		return new ArrayList<String>();
	}
}
