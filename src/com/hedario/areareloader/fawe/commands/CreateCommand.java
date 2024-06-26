package com.hedario.areareloader.fawe.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.hedario.areareloader.fawe.AreaMethods;
import com.hedario.areareloader.fawe.AreaReloader;
import com.hedario.areareloader.fawe.Queue;
import com.hedario.areareloader.fawe.configuration.Manager;
import com.sk89q.worldedit.WorldEditException;

public class CreateCommand extends ARCommand {
	private boolean skipE, skipB, isAsync = false;
	private int length = 16;
	public CreateCommand() {
		super("create", "/ar create <name> <copyEntities: true|false> <copyBiomes: true|false> [length] [async]", Manager.getConfig().getString("Commands.Create.Description"), new String[] { "create" });
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!hasPermission(sender) || !isPlayer(sender) || !this.correctLength(sender, args.size(), 3, 5)) {
			return;
		}
		String area = args.get(0);
		if (area.equalsIgnoreCase("all")) {
			sendMessage(sender, invalidName().replaceAll("%area%", area), true);
			return;
		}
		if (Manager.areas.getConfig().contains("Areas." + args.get(0))) {
			sendMessage(sender, exists().replaceAll("%area%", area), true);
			return;
		}
		
		final String skipEnts = args.get(1);
		if (skipEnts.contains("true")) {
			skipE = true;
		} else if (skipEnts.contains("false")) {
			skipE = false;
		} else {
			sendMessage(sender, invalidValue(), true);
			return;
		}
		
		final String biomes = args.get(2);
		if (biomes.contains("true")) {
			skipB = true;
		} else if (biomes.contains("false")) {
			skipB = false;
		} else {
			sendMessage(sender, invalidValue(), true);
			return;
		}

		if (this.isNumeric(args.get(3))) {
			length = Integer.valueOf(args.get(3));
			if (length < 0) {
				sendMessage(sender, invalidLength(), true);
				return;
			}
		} else {
			sendMessage(sender, invalidLength(), true);
			return;
		}
		
		final String async = args.get(4);
		if (async.contains("true")) {
			isAsync = true;
		} else if (async.contains("false")) {
			isAsync = false;
		} else {
			sendMessage(sender, invalidValue(), true);
			return;
		}
		
		if (skipE) {
			isAsync = false;
		}
		
		try {
			BukkitRunnable br = new BukkitRunnable() {
				@Override
				public void run() {
					sendMessage(sender, preparing().replaceAll("%area%", area), true);
					Player player = (Player) sender;
					if (AreaMethods.createNewArea((Player) sender, args.get(0), length, skipE, skipB)) {
						sendMessage(sender, success().replaceAll("%area%", area), true);
						player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, 0.3F);
						Queue.get().remove(area);
					} else {
						sendMessage(sender, fail().replaceAll("%area%", area), true);
						player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 0.5F);
					}
				}
			};

			if (isAsync) {
				br.runTaskAsynchronously(AreaReloader.getInstance());
			} else {
				br.runTask(AreaReloader.getInstance());
			}
			Queue.get().put(area, -1);

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
	
	private String invalidLength() {
		return Manager.getConfig().getString("Commands.Create.InvalidLength");
	}

	@Override
	protected List<String> getTabCompletion(final CommandSender sender, final List<String> args) {
		List<String> list = new ArrayList<String>();
		if (!sender.hasPermission("areareloader.command.create") || args.size() >= 5) {
			return new ArrayList<String>();
		}
		if (args.size() == 0) {
			list.add("Choose the name of the area");
		} else if (args.size() == 1) {
			list.add("copyEntities:true");
			list.add("copyEntities:false");
			list.add("true");
			list.add("false");
		} else if (args.size() == 2) {
			list.add("copyBiomes:true");
			list.add("copyBiomes:false");
			list.add("true");
			list.add("false");
		} else if (args.size() == 3) {
			list.add("length");
			list.add("16");
			list.add("32");
		} else if (args.size() == 4) {
			list.add("async:true");
			list.add("async:false");
			list.add("true");
			list.add("false");
		} else {
			return new ArrayList<String>();
		}
		return list;
	}
}