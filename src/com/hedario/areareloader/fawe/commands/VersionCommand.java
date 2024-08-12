package com.hedario.areareloader.fawe.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.hedario.areareloader.fawe.AreaReloader;
import com.hedario.areareloader.fawe.configuration.Manager;

public class VersionCommand extends ARCommand {
	public VersionCommand() {
		super("version", "/ar version", Manager.getConfig().getString("Commands.Version.Description"), new String[] { "version", "v" });
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!hasPermission(sender)) {
			return;
		}
		sendMessage(sender, this.getNeutral() + "&m-----&r "+ this.getPrefix() + this.getNeutral() + "&m-----", false);
		sendMessage(sender, this.getPrimary() + "Version " + this.getNeutral() + "» " + this.getSecondary() + "" + AreaReloader.plugin.getDescription().getVersion(), false);
		sendMessage(sender, this.getPrimary() + "API version " + this.getNeutral() + "» " + this.getSecondary() + "" + AreaReloader.plugin.getDescription().getAPIVersion(), false);
		sendMessage(sender, this.getPrimary() + "Author " + this.getNeutral() + "» " + this.getSecondary() + "" + AreaReloader.plugin.getDescription().getAuthors().toString().replace("[", "").toString().replace("]", ""), false);
		sendMessage(sender, this.getPrimary() + "Compatible Minecraft Version(s) " + this.getNeutral() + "» " + this.getSecondary() + "1.17.1, 1.18.2, 1.19.4, 1.20, 1.20.1", false);
		sendMessage(sender, this.getPrimary() + "AreaReloader-FAWE's dependency " + this.getNeutral() + "» " + this.getSecondary() + "" + AreaReloader.plugin.getDescription().getDepend().toString().replace("[", "").toString().replace("]", ""), false);
		sendMessage(sender, this.getPrimary() + "AreaReloader-FAWE's Java requirements " + this.getNeutral() + "» " + this.getSecondary() + " Java 16+", false);
		sendMessage(sender, this.getPrimary() + "System Java version " + this.getNeutral() + "» " + this.getSecondary() + "" + System.getProperty("java.version"), false);
		sendMessage(sender, "", false);
		sendMessage(sender, this.getPrimary() + "Page " + this.getNeutral() + "» " + this.getSecondary() + "www.spigotmc.org/resources/areareloader-fawe.106585/", false);
		sendMessage(sender, this.getPrimary() + "Github " + this.getNeutral() + "» " + this.getSecondary() + "github.com/Hetag1216/AreaReloader-FAWE", false);
		sendMessage(sender, this.getPrimary() + "Discord " + this.getNeutral() + "» " + this.getSecondary() + "discord.gg/yqs9UJs", false);
		sendMessage(sender, this.getPrimary() + "My plugins " + this.getNeutral() + "» " + this.getSecondary() + "www.spigotmc.org/members/_hetag1216_.243334/", false);
		sendMessage(sender, this.getPrimary() + "Donation " + this.getNeutral() + "» " + this.getSecondary() + "www.paypal.me/Hetag1216", false);
	}
}
