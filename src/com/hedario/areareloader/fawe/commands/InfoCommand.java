package com.hedario.areareloader.fawe.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import com.hedario.areareloader.fawe.AreaMethods;
import com.hedario.areareloader.fawe.AreaScheduler;
import com.hedario.areareloader.fawe.Loader;
import com.hedario.areareloader.fawe.configuration.Manager;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class InfoCommand extends ARCommand {
	public InfoCommand() {
		super("info", "/ar info <area>", Manager.getConfig().getString("Commands.Info.Description"), new String[] { "info" });
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!hasPermission(sender) || !correctLength(sender, args.size(), 1, 1)) {
			return;
		}
		String area = args.get(0);
		if (!Manager.areas.getConfig().contains("Areas." + area)) {
			sendMessage(sender, LoadCommand.invalidArea().replace("%area%", area), true);
			return;
		}
		String display = null;
		if (DisplayCommand.getDisplayedAreas().contains(area)) {
			display = "true";
		} else {
			display = "false";
		}
		
		
		sendMessage(sender, this.getNeutral() + "-=-=-=-=-=-=-=-=-=-=- « " + this.getPrimary() + area + this.getNeutral() + " » -=-=-=-=-=-=-=-=-=-=-", false);
		sendMessage(sender, this.getPrimary() + "World " + this.getNeutral() + "» " + this.getSecondary() + AreaMethods.getAreaInWorld(area), false);
		TextComponent pos = new TextComponent(this.getPrimary() + "First corner " + this.getNeutral() + "» " + this.getSecondary() + AreaMethods.getAreaX(area) + this.getNeutral() + ", " + this.getSecondary() + AreaMethods.getAreaY(area)+ this.getNeutral() + ", " + this.getSecondary() + AreaMethods.getAreaZ(area));
		pos.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.getNeutral() + "Click to select pos1").create()));
		pos.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "//pos1 " + AreaMethods.getAreaX(area) + "," + AreaMethods.getAreaY(area) + "," + AreaMethods.getAreaZ(area)));
		sender.spigot().sendMessage(pos);
		pos = new TextComponent(this.getPrimary() + "Second corner " + this.getNeutral() + "» " + this.getSecondary() + AreaMethods.getAreaX(area) + this.getNeutral() + ", " + this.getSecondary() + AreaMethods.getAreaY(area)+ this.getNeutral() + ", " + this.getSecondary() + AreaMethods.getAreaZ(area));
		pos.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.getNeutral() + "Click to select pos2").create()));
		pos.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "//pos2 " + AreaMethods.getAreaMaxX(area) + "," + AreaMethods.getAreaMaxY(area) + "," + AreaMethods.getAreaMaxZ(area)));
		sender.spigot().sendMessage(pos);
		sendMessage(sender, this.getPrimary() + "Chunk size " + this.getNeutral() + "» " + this.getSecondary() + AreaMethods.getAreaChunk(area), false);
		sendMessage(sender, this.getPrimary() + "Block length " + this.getNeutral() + "» " + this.getSecondary() + ((AreaMethods.getAreaLength(area) != null && AreaMethods.getAreaLength(area) > 0) ? AreaMethods.getAreaLength(area) : 16), false);
		sendMessage(sender, this.getPrimary() + "Loading Interval " + this.getNeutral() + "» " + this.getSecondary() + (AreaMethods.isGlobalInterval(area) ? AreaMethods.formatTime(AreaMethods.getInterval(area)) + " " + this.getNeutral() + "(" + this.getSecondary() + "GLOBAL" + this.getNeutral() + ")" : AreaMethods.formatTime(AreaMethods.getInterval(area))), false);
		
		if (Manager.getAreasConfig().getBoolean("Areas." + area + ".SafeLocation.Enabled")) {
			World world = Bukkit.getWorld(Manager.getAreasConfig().getString("Areas." + area + ".SafeLocation.World"));
			double x = Manager.getAreasConfig().getDouble("Areas." + area + ".SafeLocation.X");
			double y = Manager.getAreasConfig().getDouble("Areas." + area + ".SafeLocation.Y");
			double z = Manager.getAreasConfig().getDouble("Areas." + area + ".SafeLocation.Z");
			sendMessage(sender, this.getPrimary() + "Safe location " + this.getNeutral() + "» " + this.getSecondary() + world.getName() + this.getNeutral() + ", " + this.getSecondary() + x + this.getNeutral() + ", " + this.getSecondary() + y + this.getNeutral() + ", " + this.getSecondary() + z, false);
			sendMessage(sender, this.getPrimary() + "Safe location speed " + this.getNeutral() + "» " + this.getSecondary() + Manager.getAreasConfig().getInt("Areas." + area + ".SafeLocation.Settings.Speed"), false);
			sendMessage(sender, this.getPrimary() + "Safe location interval " + this.getNeutral() + "» " + this.getSecondary() + Manager.getAreasConfig().getInt("Areas." + area + ".SafeLocation.Settings.Interval"), false);
		}
		sendMessage(sender, this.getPrimary() + "Is being displayed " + this.getNeutral() + "» " + this.getSecondary() + display, false);
		sendMessage(sender, this.getPrimary() + "Has copied entities " + this.getNeutral() + "» " + this.getSecondary() + Manager.getAreasConfig().getBoolean("Areas." + area + ".HasCopiedEntities"), false);
		sendMessage(sender, this.getPrimary() + "Has copied biomes " + this.getNeutral() + "» " + this.getSecondary() + Manager.getAreasConfig().getBoolean("Areas." + area + ".HasCopiedBiomes"), false);

		if (Loader.getInstances().containsKey(area)) {
			sendMessage(sender, this.getPrimary() + "Currently loaded percentage " + this.getNeutral() + "» " + this.getSecondary() + Loader.get(area).perc + "%", false);
		}
		sendMessage(sender, this.getPrimary() + "Is automatically reloading " + this.getNeutral() + "» " + this.getSecondary() + Manager.getAreasConfig().getBoolean("Areas." + area + ".AutoReload.Enabled"), false);
		if (Manager.getAreasConfig().getBoolean("Areas." + area + ".AutoReload.Enabled") == true) {
			sendMessage(sender, this.getPrimary() + "Auto reloading time " + this.getNeutral() + "» " + this.getSecondary() + AreaMethods.formatTime(Manager.getAreasConfig().getLong("Areas." + area + ".AutoReload.Time")), false);
			sendMessage(sender, this.getPrimary() + "Next auto reload in " + this.getNeutral() + "» " + this.getSecondary() + AreaMethods.formatTime(AreaScheduler.getRemainingTime(area)), false);
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
