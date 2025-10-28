package com.hedario.areareloader.fawe.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.hedario.areareloader.fawe.AreaMethods;
import com.hedario.areareloader.fawe.AreaMethods.Phase;
import com.hedario.areareloader.fawe.AreaReloader;
import com.hedario.areareloader.fawe.AreaScheduler;
import com.hedario.areareloader.fawe.Loader;
import com.hedario.areareloader.fawe.configuration.Manager;

public class CancelCommand extends ARCommand {
	public CancelCommand() {
		super("cancel", "/ar cancel <area, ALL>", Manager.getConfig().getString("Commands.Cancel.Description"), new String[] { "cancel", "c" });
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!this.hasPermission(sender) || !this.correctLength(sender, args.size(), 1, 1)) {
			return;
		}
		String input = args.get(0);
		try {
			if (input.equalsIgnoreCase("all")) {
				if (Loader.getInstances().isEmpty()) {
					this.sendMessage(sender, noAreas(), true);
					return;
				}
				for (Loader areas : Loader.getInstances().values()) {
					areas.remove();
				}
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

				this.sendMessage(sender, cancelAll(), true);
				return;
			} else {
				final Loader loader = Loader.get(input);
				if (loader == null) {
					this.sendMessage(sender, fail().replace("%area%", input), true);
					return;
				} else {
					AreaMethods.kill(Phase.CANCEL, input);
					this.sendMessage(sender, success().replace("%area%", input).replace("%id%", String.valueOf(loader.task.getTaskId())), true);
				}
			}
		} catch (Exception e) {
			Manager.printDebug(this.getName(), e, sender);
		}
	}

	private String noAreas() {
		return Manager.getConfig().getString("Commands.Cancel.NoAreasLoading");
	}

	private String success() {
		return Manager.getConfig().getString("Commands.Cancel.Success");
	}

	private String fail() {
		return Manager.getConfig().getString("Commands.Cancel.Fail");
	}

	private String cancelAll() {
		return Manager.getConfig().getString("Commands.Cancel.CancelAll");
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
