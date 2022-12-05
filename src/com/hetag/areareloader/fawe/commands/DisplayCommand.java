package com.hetag.areareloader.fawe.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.hetag.areareloader.fawe.AreaMethods;
import com.hetag.areareloader.fawe.AreaReloader;
import com.hetag.areareloader.fawe.configuration.Manager;
import com.hetag.areareloader.fawe.effects.ParticleEffect;

import net.md_5.bungee.api.ChatColor;

public class DisplayCommand extends ARCommand {

	public long pDelay;
	public BukkitRunnable br;
	public static HashMap<String, HashMap<Location, Block>> blocks = new HashMap<String, HashMap<Location, Block>>();
	public static HashMap<String, Integer> entries = new HashMap<String, Integer>();
	public String ef;
	public BukkitRunnable runnable;

	public DisplayCommand() {
		super("display", "/ar display <area>", ChatColor.translateAlternateColorCodes('&', Manager.getConfig().getString("Commands.Display.Description")), new String[] { "display", "d" });
		pDelay = Manager.getConfig().getLong("Commands.Display.ParticleDelay");
		ef = Manager.getConfig().getString("Commands.Display.ParticleEffect");
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!hasPermission(sender) || !correctLength(sender, args.size(), 1, 1) && !isPlayer(sender)) {
			return;
		}

		String area = args.get(0);
		if (Manager.areas.getConfig().contains("Areas." + area)) {
			if (!entries.containsKey(area)) {
				display(area, sender);
				sendMessage(sender, displayArea().replace("%area%", area), true);
			} else {
				remove(area, sender);
				sendMessage(sender, removeDisplay().replace("%area%", area), true);
			}
		} else {
			sendMessage(sender, LoadCommand.invalidArea().replaceAll("%area%", area), true);
		}
	}
	
	public void display(String area, CommandSender sender) {
		br = new BukkitRunnable() {
			public void run() {
				if (!entries.containsKey(area)) {
				entries.put(area, br.getTaskId());
				}
				if (blocks.containsKey(area)) {
					blocks.remove(area);
				}
				Location corner1 = new Location(Bukkit.getWorld(AreaMethods.getAreaInWorld(area)), AreaMethods.getAreaX(area), AreaMethods.getAreaY(area), AreaMethods.getAreaZ(area));
				Location corner2 = new Location(Bukkit.getWorld(AreaMethods.getAreaInWorld(area)), AreaMethods.getAreaMaxX(area), AreaMethods.getAreaMaxY(area), AreaMethods.getAreaMaxZ(area));
				for (Location finalLoc : getHollowCube(corner1, corner2, 0.25)) {
					if (useParticles()) {
						ParticleEffect effect = null;
						if (ef.equalsIgnoreCase("BLOCK_CRACK")) {
							effect = ParticleEffect.BLOCK_CRACK;
							effect.display(finalLoc, 1, 0.03F, 0.03F, 0.03F, 0.03F, Material.valueOf(match()).createBlockData());
							effect.display(finalLoc, 1, 0, 0, 0, 0, Material.valueOf(match()).createBlockData());
						} else if (ef.equalsIgnoreCase("BLOCK_DUST")) {
							effect = ParticleEffect.BLOCK_DUST;
							effect.display(finalLoc, 1, 0.03F, 0.03F, 0.03F, 0.03F, Material.valueOf(match()).createBlockData());
							effect.display(finalLoc, 1, 0, 0, 0, 0, Material.valueOf(match()).createBlockData());
						} else {
							effect = ParticleEffect.valueOf(ef);
							effect.display(finalLoc, 1);
						}
					} else {
						Player player = null;
						if (restrictVision()) {
							player = (Player) sender;
						} else {
							for (Player online : Bukkit.getServer().getOnlinePlayers()) {
								player = online;
							}
						}
						if (!blocks.containsKey(area)) {
							blocks.put(area, new HashMap<>());
						} else {
						blocks.get(area).put(finalLoc, finalLoc.getBlock());
						blockChange(player, finalLoc.getBlock());
					}
					}
				}

			}
		};
		br.runTaskTimerAsynchronously(AreaReloader.plugin, 0, pDelay * 20 / 1000);
	}

	public static void remove(String area, CommandSender sender) {
		AreaReloader.getInstance().getServer().getScheduler().cancelTask(entries.get(area));
		entries.remove(area);

		for (Block block : blocks.get(area).values()) {
			Player player = null;
			if (restrictVision()) {
				if (sender != null)
				player = (Player) sender;
			} else {
				for (Player online : Bukkit.getServer().getOnlinePlayers()) {
					player = online;
				}
			}
			player.sendBlockChange(block.getLocation(), block.getBlockData());
		}

		blocks.remove(area);
	}

	public static void removeAllDisplays() {
		for (Entry<String, Integer> active_tasks : entries.entrySet()) {
			AreaReloader.getInstance().getServer().getScheduler().cancelTask(active_tasks.getValue());
			entries.remove(active_tasks.getKey());
		}
		for (String area : blocks.keySet()) {
			for (Block block : blocks.get(area).values()) {
				Player player = null;
				for (Player online : Bukkit.getServer().getOnlinePlayers()) {
					player = online;
				}
				player.sendBlockChange(block.getLocation(), block.getBlockData());
			}
			blocks.remove(area);
		}
	}
	
	public static boolean isDisplaying(final String area) {
		if (entries.containsKey(area)) {
			return true;
		}
		return false;
	}
	
	private void blockChange(Player player, Block block) {
		player.sendBlockChange(block.getLocation(), Material.matchMaterial(match()).createBlockData());
	}
	
	public static String displayArea() {
		return formatColors(Manager.getConfig().getString("Commands.Display.Display"));
	}
	
	public static String removeDisplay() {
		return formatColors(Manager.getConfig().getString("Commands.Display.RemoveDisplay"));
	}
	
	public boolean useParticles() {
		return Manager.getConfig().getBoolean("Commands.Display.UseParticles");
	}
	
	public static boolean restrictVision() {
		return Manager.getConfig().getBoolean("Commands.Display.Block.RestrictVision");
	}
	
	public String match() {
		return Manager.getConfig().getString("Commands.Display.Block.Material");
	}
	
	public static Set<String> getDisplayedAreas() {
		return entries.keySet();
	}

	public List<Location> getHollowCube(Location corner1, Location corner2, double point) {
		List<Location> result = new ArrayList<Location>();
		World world = corner1.getWorld();
		double minX = Math.min(corner1.getX(), corner2.getX());
		double minY = Math.min(corner1.getY(), corner2.getY());
		double minZ = Math.min(corner1.getZ(), corner2.getZ());
		double maxX = Math.max(corner1.getX(), corner2.getX());
		double maxY = Math.max(corner1.getY(), corner2.getY());
		double maxZ = Math.max(corner1.getZ(), corner2.getZ());

		for (double x = minX; x <= maxX; x += point) {
			for (double y = minY; y <= maxY; y += point) {
				for (double z = minZ; z <= maxZ; z += point) {
					int components = 0;
					if (x == minX || x == maxX)
						components++;
					if (y == minY || y == maxY)
						components++;
					if (z == minZ || z == maxZ)
						components++;
					if (components >= 2) {
						result.add(new Location(world, x, y, z));
					}
				}
			}
		}
		return result;
	}
	
	@Override
	protected List<String> getTabCompletion(final CommandSender sender, final List<String> args) {
		List<String> list = new ArrayList<String>();
		if (!sender.hasPermission("areareloader.command.display") || args.size() >= 1) {
			return new ArrayList<String>();
		}
		for (final String map : AreaMethods.getAreas()) {
			list.add(map);
		}
		return list;
	}
}