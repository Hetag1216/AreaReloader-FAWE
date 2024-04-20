package com.hedario.areareloader.fawe.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.hedario.areareloader.fawe.AreaReloader;
import com.hedario.areareloader.fawe.configuration.Manager;

public class HookCommand extends ARCommand {
	public HookCommand() {
		super("hook", "/ar hook", Manager.getConfig().getString("Commands.Hook.Description"), new String[] { "hook", "hooks" });
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!hasPermission(sender) || !correctLength(sender, args.size(), 0, 1)) {
			return;
		}
		sendMessage(sender, "&6-=-=-=-= " + this.getPrefix() + "&6=-=-=-=-", false);
		sendMessage(sender, "&7- &eFastAsyncWorldEdit &7(&eFAWE&7)", false);
		sendMessage(sender, AreaReloader.plugin.getStatus(), false);
		if (AreaReloader.getWEInstance() != null) {
			sendMessage(sender, "&6Version &7» &e" + AreaReloader.getWEInstance().getDescription().getVersion(), false);
		}
		sendMessage(sender, "&6-=-=-=-= -=- =-=-=-=-", false);
	}
}
