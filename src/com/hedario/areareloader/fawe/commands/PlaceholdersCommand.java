package com.hedario.areareloader.fawe.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.hedario.areareloader.fawe.configuration.Manager;

public class PlaceholdersCommand extends ARCommand {

	public PlaceholdersCommand() {
		super("placeholders", "/ar placeholders", Manager.getConfig().getString("Commands.Placeholders.Description"), new String[] {"placeholders"});
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!this.hasPermission(sender) || !this.correctLength(sender, args.size(), 0, 0)) {
			return;
		}
		sendMessage(sender, this.getNeutral() + "-=-=-=-= " + this.getPrefix() + this.getNeutral() + "=-=-=-=-", false);
		sendMessage(sender, this.getPrimary() + "%AreaReloader_remaining_time_<area>% " + this.getNeutral() + "» " + this.getSecondary() + "Shows the remaining time of an automatic load.", false);
		sendMessage(sender, this.getPrimary() + "%AreaReloader_world_<area>% " + this.getNeutral() + "» " + this.getSecondary() + "Shows the world name of the area.", false);
		sendMessage(sender, this.getPrimary() + "%AreaReloader_corner1_<area>% " + this.getNeutral() + "» " + this.getSecondary() + "Shows the first corner's coordinates of the area.", false);
		sendMessage(sender, this.getPrimary() + "%AreaReloader_corner2_<area>% " + this.getNeutral() + "» " + this.getSecondary() + "Shows the second corner's coordinates of the area.", false);
		sendMessage(sender, this.getPrimary() + "%AreaReloader_chunks_<area>% " + this.getNeutral() + "» " + this.getSecondary() + "Shows the chunk size of the area.", false);
		sendMessage(sender, this.getPrimary() + "%AreaReloader_global_loading_interval% " + this.getNeutral() + "» " + this.getSecondary() + "Shows the global loading interval.", false);
		sendMessage(sender, this.getPrimary() + "%AreaReloader_loading_interval_<area>% " + this.getNeutral() + "» " + this.getSecondary() + "Shows the loading interval of the area.", false);
	}
}