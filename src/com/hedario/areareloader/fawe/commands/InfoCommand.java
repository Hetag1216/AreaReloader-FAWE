package com.hedario.areareloader.fawe.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import com.hedario.areareloader.fawe.AreaLoader;
import com.hedario.areareloader.fawe.AreaMethods;
import com.hedario.areareloader.fawe.AreaReloader;
import com.hedario.areareloader.fawe.AreaScheduler;
import com.hedario.areareloader.fawe.configuration.Manager;

public class InfoCommand extends ARCommand {
	public InfoCommand() {
		super("info", "/ar info <area>", Manager.getConfig().getString("Commands.Info.Description"), new String[] { "info" });
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!hasPermission(sender) || !correctLength(sender, args.size(), 1, 1)) {
			return;
		}
		String area = args.get(0);
		String display = null;
		if (DisplayCommand.getDisplayedAreas().contains(area)) {
			display = "true";
		} else {
			display = "false";
		}
		if (!Manager.getAreasConfig().contains("Areas." + area)) {
			sendMessage(sender, LoadCommand.invalidArea().replaceAll("%area%", area), true);
		}
		sendMessage(sender, "&7-=-=-=-=-=-=-=-=-=-=- « &6" + area + " &7» -=-=-=-=-=-=-=-=-=-=-", false);
		sendMessage(sender, "&6World &7» &e" + AreaMethods.getAreaInWorld(area), false);
		sendMessage(sender, "&6First corner &7» &e" + AreaMethods.getAreaX(area) + "&7, &e" + AreaMethods.getAreaY(area) + "&7, &e" + AreaMethods.getAreaZ(area), false);
		sendMessage(sender, "&6Second corner &7» &e" + AreaMethods.getAreaMaxX(area) + "&7, &e" + AreaMethods.getAreaMaxY(area) + "&7, &e" + AreaMethods.getAreaMaxZ(area), false);
		sendMessage(sender, "&6Chunk size &7» &e" + AreaMethods.getAreaChunk(area), false);
		sendMessage(sender, "&6Block length &7» &e" + ((AreaMethods.getAreaLength(area) != null && AreaMethods.getAreaLength(area) > 0) ? AreaMethods.getAreaLength(area) : 16), false);
		sendMessage(sender, "&6Loading Interval &7» &6" + (AreaMethods.isGlobalInterval(area) ? AreaMethods.formatTime(AreaMethods.getInterval(area)) + " &7(&eGLOBAL&7)" : AreaMethods.formatTime(AreaMethods.getInterval(area))), false);
		
		if (Manager.getAreasConfig().getBoolean("Areas." + area + ".SafeLocation.Enabled")) {
			World world = Bukkit.getWorld(Manager.getAreasConfig().getString("Areas." + area + ".SafeLocation.World"));
			double x = Manager.getAreasConfig().getDouble("Areas." + area + ".SafeLocation.X");
			double y = Manager.getAreasConfig().getDouble("Areas." + area + ".SafeLocation.Y");
			double z = Manager.getAreasConfig().getDouble("Areas." + area + ".SafeLocation.Z");
			sendMessage(sender, "&6Safe location &7» &e" + world.getName() + "&7, &e" + x + "&7, &e" + y + "&7, &e" + z, false);
			sendMessage(sender, "&6Safe location speed &7» &e" + Manager.getAreasConfig().getInt("Areas." + area + ".SafeLocation.Settings.Speed"), false);
			sendMessage(sender, "&6Safe location interval &7» &e" + Manager.getAreasConfig().getInt("Areas." + area + ".SafeLocation.Settings.Interval"), false);
		}
		sendMessage(sender, "&6Is being displayed &7» &e" + display, false);
		sendMessage(sender, "&6Has copied entities &7» &e" + Manager.getAreasConfig().getBoolean("Areas." + area + ".HasCopiedEntities"), false);
		sendMessage(sender, "&6Has copied biomes &7» &e" + Manager.getAreasConfig().getBoolean("Areas." + area + ".HasCopiedBiomes"), false);
		sendMessage(sender, "&6Is using fast mode &7» &e" + AreaMethods.fastMode, false);

		if (AreaReloader.getQueue().isQueued(area)) {
			if (AreaLoader.isInstance(area)) {
				sendMessage(sender,"&6Currently loaded percentage &7» &e" + String.format("%.2f", AreaLoader.get(area).getPerc()) + "&6%", false);
			}
		}
		sendMessage(sender, "&6Is automatically reloading &7» &e" + Manager.getAreasConfig().getBoolean("Areas." + area + ".AutoReload.Enabled"), false);
		if (Manager.getAreasConfig().getBoolean("Areas." + area + ".AutoReload.Enabled") == true) {
			sendMessage(sender, "&6Auto reloading time &7» &e" 	+ AreaMethods.formatTime(Manager.getAreasConfig().getLong("Areas." + area + ".AutoReload.Time")), false);
			sendMessage(sender, "&6Next auto reload in &7» &e" + AreaMethods.formatTime(AreaScheduler.getRemainingTime(area)), false);
		}
		return;
	}

	@Override
	protected List<String> getTabCompletion(final CommandSender sender, final List<String> args) {
		List<String> list = new ArrayList<String>();
		if (!sender.hasPermission("areareloader.command.info") || args.size() >= 1) {
			return new ArrayList<String>();
		}
		for (final String map : AreaMethods.getAreas()) {
			list.add(map);
		}
		return list;
	}
}
