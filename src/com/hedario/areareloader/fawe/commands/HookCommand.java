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
		if (!hasPermission(sender) || !correctLength(sender, args.size(), 0, 0)) {
			return;
		}
		sendMessage(sender, this.getNeutral() + "-=-=-=-= " + this.getPrefix() + this.getNeutral() + "=-=-=-=-", false);
		sendMessage(sender, this.getNeutral() + "- " + this.getSecondary() + "FastAsyncWorldEdit " + this.getNeutral() + "(" + this.getSecondary() + "FAWE" + this.getNeutral() + ")", false);
		sendMessage(sender, AreaReloader.plugin.getStatus(this.getPrimary(), this.getNeutral()), false);
		if (AreaReloader.getWEInstance() != null) {
			sendMessage(sender, this.getPrimary() + "Version " + this.getNeutral() + "» " + this.getSecondary() + "" + AreaReloader.getWEInstance().getDescription().getVersion(), false);
		}
		sendMessage(sender, this.getPrimary() + "-=-=-=-= -=- =-=-=-=-", false);
	}
}
