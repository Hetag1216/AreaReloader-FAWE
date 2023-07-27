package com.hedario.areareloader.fawe.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitTask;

import com.hedario.areareloader.fawe.AreaLoader;
import com.hedario.areareloader.fawe.AreaMethods;
import com.hedario.areareloader.fawe.AreaReloader;
import com.hedario.areareloader.fawe.AreaScheduler;
import com.hedario.areareloader.fawe.configuration.Manager;

public class CancelCommand extends ARCommand {
	public CancelCommand() {
		super("cancel", "/ar cancel <area, ALL>", formatColors(Manager.getConfig().getString("Commands.Cancel.Description")), new String[] { "cancel", "c" });

	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!this.hasPermission(sender) || !this.correctLength(sender, args.size(), 1, 1)) {
			return;
		}
		String input = args.get(0);
		try {
			if (input.equalsIgnoreCase("all")) {
				if (!AreaReloader.getInstance().getQueue().get().isEmpty()) {
					AreaReloader.getInstance().getServer().getScheduler().cancelTasks(AreaReloader.getInstance());
					for (BukkitTask tasks : AreaReloader.getInstance().getServer().getScheduler().getPendingTasks()) {
						tasks.cancel();
					}
					AreaLoader.areas.clear();
					AreaReloader.getInstance().getQueue().get().clear();
					if (!DisplayCommand.entries.isEmpty()) {
						DisplayCommand.removeAllDisplays();
					}
					AreaScheduler.init();
					if (AreaReloader.checker) {
						AreaScheduler.checkForAreas();
						AreaScheduler.manageTimings();
						if (AreaScheduler.getAreas() != null) {
							AreaScheduler.updateDelay(AreaScheduler.getAreas(), AreaScheduler.getAreasResetTime());
						}
					}
				} else {
					this.sendMessage(sender, noAreas(), true);
					return;
				}
				this.sendMessage(sender, cancelAll(), true);
				return;
			} else {
				if (AreaReloader.getInstance().getQueue().isQueued(input)) {
					this.sendMessage(sender, success().replace("%area%", input).replace("%id%", String.valueOf(AreaReloader.getInstance().getQueue().getTaskByName(input))), true);
					AreaMethods.kill(input);
					return;
				} else {
					this.sendMessage(sender, fail().replace("%area%", input), true);
					return;
				}
			}
		} catch (Exception e) {
			Manager.printDebug(this.getName(), e, sender);
		}
	}
	
	private String noAreas() {
		return formatColors(Manager.getConfig().getString("Commands.Cancel.NoAreasLoading"));
	}

	private String success() {
		return formatColors(Manager.getConfig().getString("Commands.Cancel.Success"));
	}

	private String fail() {
		return formatColors(Manager.getConfig().getString("Commands.Cancel.Fail"));
	}

	private String cancelAll() {
		return formatColors(Manager.getConfig().getString("Commands.Cancel.CancelAll"));
	}
	
	@Override
	protected List<String> getTabCompletion(final CommandSender sender, final List<String> args) {
		List<String> list = new ArrayList<String>();
		if (!sender.hasPermission("areareloader.command.cancel") || args.size() >= 1) {
			return new ArrayList<String>();
		}
		list.add("all");
		for (final String map : AreaMethods.getAreas()) {
			list.add(map);
		}
		return list;
	}
}
