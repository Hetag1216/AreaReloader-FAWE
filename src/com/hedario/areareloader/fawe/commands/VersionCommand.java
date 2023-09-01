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
		if (!hasPermission(sender) || !correctLength(sender, args.size(), 0, 1)) {
			return;
		}
		sendMessage(sender, "&8&m-----&r "+ this.prefix + "&8&m-----", false);
		sendMessage(sender, "&6Version &7» &e" + AreaReloader.plugin.getDescription().getVersion(), false);
		sendMessage(sender, "&6API version &7» &e" + AreaReloader.plugin.getDescription().getAPIVersion(), false);
		sendMessage(sender, "&6Author &7» &e" + AreaReloader.plugin.getDescription().getAuthors().toString().replace("[", "").toString().replace("]", ""), false);
		sendMessage(sender, "&6Compatible Minecraft Version(s) &7» &e1.17.1, 1.18.2, 1.19.4, 1.20, 1.20.1", false);
		sendMessage(sender, "&6AreaReloader-FAWE's dependency &7» &e" + AreaReloader.plugin.getDescription().getDepend().toString().replace("[", "").toString().replace("]", ""), false);
		sendMessage(sender, "&6AreaReloader-FAWE's Java requirements &7» &e Java 16+", false);
		sendMessage(sender, "&6System Java version &7» &e" + System.getProperty("java.version"), false);
		sendMessage(sender, "", false);
		sendMessage(sender, "&6Page &7» &ewww.spigotmc.org/resources/areareloader.70655/", false);
		sendMessage(sender, "&6Github &7» &egithub.com/Hetag1216/AreaReloader", false);
		sendMessage(sender, "&6Discord &7» &ediscord.gg/yqs9UJs", false);
		sendMessage(sender, "&6My plugins &7» &ewww.spigotmc.org/members/_hetag1216_.243334/", false);
		sendMessage(sender, "&6Donation &7» &ewww.paypal.me/Hetag1216", false);
		sendMessage(sender, "&6Discord &7» &ediscord.gg/yqs9UJs", false);
	}
}
