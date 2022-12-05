package com.hetag.areareloader.fawe.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.hetag.areareloader.fawe.AreaReloader;
import com.hetag.areareloader.fawe.configuration.Manager;

import net.md_5.bungee.api.ChatColor;

public class HookCommand extends ARCommand {
	public HookCommand() {
		super("hook", "/ar hook", formatColors(Manager.getConfig().getString("Commands.Hook.Description")), new String[] { "hook", "hooks" });
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!hasPermission(sender) || !correctLength(sender, args.size(), 0, 1)) {
			return;
		}
		sender.sendMessage(ChatColor.GOLD + "-=-=-=-= Hooks =-=-=-=-");
		sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.YELLOW + "FastAsyncWorldEdit");
		sender.sendMessage(AreaReloader.plugin.getStatus());
		if (AreaReloader.getWEInstance() != null) {
			sender.sendMessage(ChatColor.GOLD + "Version: " + ChatColor.YELLOW + AreaReloader.getWEInstance().getDescription().getVersion());
		}
		sender.sendMessage(ChatColor.GOLD + "-=-=-=-= -=- =-=-=-=-");
	}
}
