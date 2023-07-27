package com.hedario.areareloader.fawe.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hedario.areareloader.fawe.AreaMethods;
import com.hedario.areareloader.fawe.AreaReloader;
import com.hedario.areareloader.fawe.configuration.Manager;
import com.sk89q.worldedit.WorldEditException;

public class CreateCommand extends ARCommand {
	private boolean skipE, skipB;
	public CreateCommand() {
		super("create", "/ar create <name> <copyEntities: true|false> <copyBiomes: true|false>", formatColors(Manager.getConfig().getString("Commands.Create.Description")), new String[] { "create" });
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!hasPermission(sender) || !isPlayer(sender) || !this.correctLength(sender, args.size(), 3, 3)) {
			return;
		}
			String area = args.get(0);
			//boolean skipEntities = false;
			//boolean copyBiomes = false;
			if (area.equalsIgnoreCase("all")) {
				sendMessage(sender, invalidName().replaceAll("%area%", area), true);
				return;
			}
			if (Manager.areas.getConfig().contains("Areas." + args.get(0))) {
				sendMessage(sender, exists().replaceAll("%area%", area), true);
				return;
			}
			final String skipEnts = args.get(1);
			if (skipEnts.equalsIgnoreCase("true")) {
				skipE= true;
				//skipEntities = true;
			} else if (skipEnts.equalsIgnoreCase("false")) {
				//skipEntities = false;
				skipE= false;
			} else {
				sendMessage(sender, invalidValue(), true);
				return;
			}
			final String biomes = args.get(2);
			if (biomes.equalsIgnoreCase("true")) {
				//copyBiomes = true;
				skipB = true;
			} else {
				skipB = false;
				//copyBiomes = false;
			}
			try {
				sendMessage(sender, preparing().replaceAll("%area%", area), true);
				Bukkit.getScheduler().runTaskAsynchronously(AreaReloader.plugin, () -> {
					if (AreaMethods.createNewArea((Player) sender, args.get(0), 16, skipE, skipB)) {
						sendMessage(sender, success().replaceAll("%area%", area), true);
					} else {
						sendMessage(sender, fail().replaceAll("%area%", area), true);
					}
				});
			} catch (WorldEditException e) {
				Manager.printDebug(this.getName(), e, sender);
			}
	}

	private String preparing() {
		return Manager.getConfig().getString("Commands.Create.Preparing");
	}
	
	private String exists() {
		return Manager.getConfig().getString("Commands.Create.AlreadyExists");
	}

	private String success() {
		return Manager.getConfig().getString("Commands.Create.Success");
	}

	private String fail() {
		return Manager.getConfig().getString("Commands.Create.Failure");
	}
	
	private String invalidName() {
		return Manager.getConfig().getString("Commands.Create.InvalidName");
	}
	
	private String invalidValue() {
		return Manager.getConfig().getString("Commands.Create.InvalidValue");
	}
	
	@Override
	protected List<String> getTabCompletion(final CommandSender sender, final List<String> args) {
		List<String> list = new ArrayList<String>();
		if (!sender.hasPermission("areareloader.command.create") || args.size() >= 3) {
			return new ArrayList<String>();
		}
		if (args.size() == 1) {
			list.add("true");
			list.add("false");
		}
		if (args.size() == 2) {
			list.add("true");
			list.add("false");
		}
		return list;
	}

}
