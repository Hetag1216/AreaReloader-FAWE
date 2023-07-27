package com.hedario.areareloader.fawe.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.hedario.areareloader.fawe.AreaMethods;
import com.hedario.areareloader.fawe.AreaReloader;
import com.hedario.areareloader.fawe.AreaScheduler;
import com.hedario.areareloader.fawe.configuration.Manager;

public class ConfigReloadCommand extends ARCommand {
	public ConfigReloadCommand() {
		super("reload", "/ar reload", formatColors(Manager.getConfig().getString("Commands.Reload.Description")), new String[] { "reload" });
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!hasPermission(sender) || !correctLength(sender, args.size(), 0, 1)) {
			return;
		}
		try {
			Manager.resetDebug();
			DisplayCommand.removeAllDisplays();
			AreaMethods.performSetup();
			Manager.reloadAllInstances();
			AreaScheduler.init();
			if (AreaReloader.checker) {
				AreaScheduler.checkForAreas();
				AreaScheduler.manageTimings();
				if (AreaScheduler.getAreas() != null) {
					AreaScheduler.updateDelay(AreaScheduler.getAreas(), AreaScheduler.getAreasResetTime());
				}
			}
			sendMessage(sender, success(), true);
		} catch (Exception e) {
			sendMessage(sender, fail(), true);
			Manager.printDebug(this.getName(), e, sender);
		}

	}

	public String success() {
		return Manager.getConfig().getString("Commands.Reload.Success");
	}

	public String fail() {
		return Manager.getConfig().getString("Commands.Reload.Fail");
	}
}
