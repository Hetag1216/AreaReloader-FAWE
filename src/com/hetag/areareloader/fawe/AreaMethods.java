package com.hetag.areareloader.fawe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fastasyncworldedit.core.FaweAPI;
import com.hetag.areareloader.fawe.commands.DisplayCommand;
import com.hetag.areareloader.fawe.configuration.Manager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;

public class AreaMethods {
	static AreaReloader plugin;
	public static boolean fastMode = Manager.getConfig().getBoolean("Settings.AreaLoading.FastMode");
	
	public static void performSetup() {
		File areas = new File(AreaReloader.plugin.getDataFolder() + File.separator + "Areas");
		if (!areas.exists()) {
			try {
				areas.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void deleteArea(String area) {
		kill(area);
		Manager.areas.getConfig().set("Areas." + area, null);
		Manager.areas.saveConfig();
		File dir = new File(AreaReloader.plugin.getDataFolder() + File.separator + "Areas" + File.separator + area);
		if (dir.exists()) {
			File[] files = dir.listFiles();
			if ((files != null) && (files.length != 0)) {
				File[] arrayOfFile1;
				int j = (arrayOfFile1 = files).length;
				for (int i = 0; i < j; i++) {
					File file = arrayOfFile1[i];
					file.delete();
				}
			}
			dir.delete();
		}
	}

	public static boolean isInteger(String s) {
		return isInteger(s, 10);
	}
	
	public static String formatTime(long time) {
		time = Math.abs(time);
		final long days = time / TimeUnit.DAYS.toMillis(1);
		final long hours = time % TimeUnit.DAYS.toMillis(1) / TimeUnit.HOURS.toMillis(1);
		final long minutes = time % TimeUnit.HOURS.toMillis(1) / TimeUnit.MINUTES.toMillis(1);
		final long seconds = time % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);
		final long millis = time / TimeUnit.MILLISECONDS.toMillis(1);
		String format = "";
		String t = "";
		if (days > 0) {
			format += String.valueOf(days) + ":";
			t = "days";
		}
		if (hours > 0) {
			format += String.valueOf(hours) + ":";
			t = "hours";
		}
		if (minutes > 0) {
			format += String.valueOf(minutes) + ":";
			t = "minutes";
		}
		if (seconds >= 0) {
			t = "seconds";
			format += String.valueOf(seconds) + "." + String.valueOf(Math.round(millis)).substring(0, 2);
		}
		
		return format + " " + t;
	}

	public static boolean isInteger(String s, int radix) {
		if (s.isEmpty()) {
			return false;
		}
		for (int i = 0; i < s.length(); i++) {
			if ((i == 0) && (s.charAt(i) == '-')) {
				if (s.length() == 1) {
					return false;
				}
			} else if (Character.digit(s.charAt(i), radix) < 0) {
				return false;
			}
		}
		return true;
	}

	public static Integer getMaxInt(int min, int max, int size) {
		if (max - min < size) {
			return Integer.valueOf(max - min);
		}
		return Integer.valueOf(size);
	}
	

	public static boolean loadSchematicArea(CommandSender p, String area, String schemFile, World world, Location location) throws WorldEditException, FileNotFoundException, IOException {
		File file = new File(AreaReloader.plugin.getDataFolder() + File.separator + "Areas" + File.separator + area + File.separator + schemFile + ".schem");
		Manager.printDebug("-=-=-=-=-=-=-=-=-=-=- Area Building -=-=-=-=-=-=-=-=-=-=-");
		Manager.printDebug("Area: " + area);
		
		if (!file.exists()) {
			Manager.printDebug("Schematic File: Missing.");
			return false;
		}
		Manager.printDebug("Schematic File: Found.");
		
        try {
            FaweAPI.load(file)
            .paste(FaweAPI.getWorld(world.getName()), BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ()));       	
            Manager.printDebug("Section has been built.");
        } catch (Exception e) {
			Manager.printDebug("An error has occurred when building area: " + file.getName());
			Manager.printDebug(e.getMessage());
            Manager.printDebug("Printing stack trace to console...");
        }

		Manager.printDebug("-=-=-=-=-=-=-=-=-=-=- -=- -=-=-=-=-=-=-=-=-=-=-");
		Manager.printDebug("");
		return true;
	}


	public static boolean createNewArea(final Player player, final String area, final int size, final boolean copyEntities, final boolean copyBiomes) throws WorldEditException {
		File dir = new File(AreaReloader.plugin.getDataFolder() + File.separator + "Areas" + File.separator + area);
		if (dir.exists()) {
			File[] files = dir.listFiles();
			if ((files != null) && (files.length != 0)) {
				File[] arrayOfFile1;
				int j = (arrayOfFile1 = files).length;
				for (int i = 0; i < j; i++) {
					File file = arrayOfFile1[i];
					file.delete();
				}
			}
			dir.delete();
		}
		BukkitPlayer lp = BukkitAdapter.adapt(player);
		LocalSession ls = WorldEdit.getInstance().getSessionManager().get(lp);
		Region sel = ls.getSelection(BukkitAdapter.adapt(player.getWorld()));
		int maxX = 0;
		int maxZ = 0;
		if ((sel instanceof CuboidRegion)) {
			int curX = 0;
			BlockVector3 min = sel.getMinimumPoint();
			BlockVector3 max = sel.getMaximumPoint();

			Manager.areas.getConfig().set("Areas." + area + ".World", sel.getWorld().getName());
			Manager.areas.getConfig().set("Areas." + area + ".HasCopiedEntities", copyEntities);
			Manager.areas.getConfig().set("Areas." + area + ".HasCopiedBiomes", copyBiomes);
			Manager.areas.getConfig().set("Areas." + area + ".X", min.getBlockX());
			Manager.areas.getConfig().set("Areas." + area + ".Y", min.getBlockY());
			Manager.areas.getConfig().set("Areas." + area + ".Z", min.getBlockZ());
			Manager.areas.getConfig().set("Areas." + area + ".Maximum.Z", max.getBlockZ());
			Manager.areas.getConfig().set("Areas." + area + ".Maximum.Y", max.getBlockY());
			Manager.areas.getConfig().set("Areas." + area + ".Maximum.X", max.getBlockX());
			Manager.areas.saveConfig();
			for (int x = min.getBlockX(); x <= max.getBlockX(); x += size) {
				int curZ = 0;
				for (int z = min.getBlockZ(); z <= max.getBlockZ(); z += size) {
					EditSession extent = WorldEdit.getInstance().newEditSessionBuilder().world(sel.getWorld())
							.fastMode(fastMode)
						    .combineStages(true)
						    .changeSetNull()
						    .checkMemory(false)
						    .allowedRegionsEverywhere()
						    .limitUnlimited()
						    .build();
					int maxY = max.getBlockY();
					Location pt1 = new Location(player.getWorld(), x, min.getBlockY(), z);
					Location pt2 = new Location(player.getWorld(), x + getMaxInt(x, max.getBlockX(), size).intValue(), maxY, z + getMaxInt(z, max.getBlockZ(), size).intValue());

					BlockVector3 bvmin = BukkitAdapter.asBlockVector(pt1);
					BlockVector3 bvmax = BukkitAdapter.asBlockVector(pt2);
					CuboidRegion region = new CuboidRegion(sel.getWorld(), bvmin, bvmax);

					BlockArrayClipboard cc = new BlockArrayClipboard(region);
					ForwardExtentCopy clipCopy = new ForwardExtentCopy(extent, region, cc, region.getMinimumPoint());
					clipCopy.setCopyingEntities(copyEntities);
					clipCopy.setCopyingBiomes(copyBiomes);
					Manager.printDebug("-=-=-=-=-=-=-=-=-=-=- Area Creation -=-=-=-=-=-=-=-=-=-=-");	
					Manager.printDebug("Area: " + area);
					try {
						Operations.completeLegacy(clipCopy);
						Manager.printDebug("Succesfully copied the selected clipboard to system.");
					} catch (Exception e) {
						e.printStackTrace();
						Manager.printDebug("An error has occurred when coping the selected clipboard!");
						Manager.printDebug(e.getMessage());
					}
					File file = new File(AreaReloader.plugin.getDataFolder() + File.separator + "Areas" + File.separator + area + File.separator + getFileName(area, curX, curZ) + ".schem");
					if (file.exists()) {
						file.delete();
					}
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
						Manager.printDebug("Creating Areas' files directory.");
					}
					if (!file.exists()) {
						try {
							file.createNewFile();
							Manager.printDebug("Saving to file the selected clipboard.");
						} catch (IOException e) {
							e.printStackTrace();
							Manager.printDebug("An error has occurred when saving to clipboard area: " + file.getName());
							Manager.printDebug(e.getMessage());
						}
					}
					try (ClipboardWriter writer = BuiltInClipboardFormat.FAST.getWriter(new FileOutputStream(file))) {
						writer.write(cc);
						Manager.printDebug("The clipboard was succesfully saved to file.");
					} catch (FileNotFoundException e) {
						Manager.printDebug("FileNotFoundException: Something went wrong while writing the schematic file:");
						Manager.printDebug(e.getMessage());
						e.printStackTrace();
					} catch (IOException e) {
						Manager.printDebug("IOException: Something went wrong while writing the schematic file:");
						Manager.printDebug(e.getMessage());
						e.printStackTrace();
					}

					Manager.printDebug("-=-=-=-=-=-=-=-=-=-=- -=- -=-=-=-=-=-=-=-=-=-=-");
					Manager.printDebug("");
					curZ++;
					maxZ = curZ;
				}
				curX++;
				maxX = curX;
			}
			maxX--;
			maxZ--;
			
			Manager.areas.getConfig().set("Areas." + area + ".Size.X", maxX);
			Manager.areas.getConfig().set("Areas." + area + ".Size.Z", maxZ);
			Manager.areas.getConfig().set("Areas." + area + ".Size.Chunk", size);
			Manager.areas.getConfig().set("Areas." + area + ".AutoReload.Enabled", false);
			Manager.areas.getConfig().set("Areas." + area + ".AutoReload.Time", 200000);
			Manager.areas.saveConfig();
			return true;
		}
		return false;
	}
	
	public static void kill(String area) {
		Manager.printDebug("-=-=-=-=-=-=-=-=-=-=- Area Killing -=-=-=-=-=-=-=-=-=-=-");
		Manager.printDebug("Area: " + area);
		if (AreaReloader.getInstance().getQueue().isQueued(area)) {
			AreaReloader.getInstance().getQueue().remove(area, AreaReloader.getInstance().getQueue().getTaskByName(area));
			Manager.printDebug("Killed area's execution.");
		}
		if (DisplayCommand.isDisplaying(area)) {
			DisplayCommand.remove(area, null);
		}
		AreaLoader.reset(area);
		
		Manager.printDebug("Removed from the loading instances.");
		Manager.printDebug("Removed from the automatic loading instances.");
		
		Manager.printDebug("-=-=-=-=-=-=-=-=-=-=- -=- -=-=-=-=-=-=-=-=-=-=-");
		Manager.printDebug("");
	}
	
	public static List<String> getAreas() {
		List<String> areas = new ArrayList<String>();
		if (Manager.areas.getConfig().contains("Areas")) {
			for (String keys : Manager.areas.getConfig().getConfigurationSection("Areas").getKeys(false)) {
				areas.add(keys);
			}
		}
		return areas;
	}
	
	public static String getXCoord(String area) {
		return Manager.areas.getConfig().getString("Areas." + area + ".X");
	}
	
	public static String getZCoord(String area) {
		return Manager.areas.getConfig().getString("Areas." + area + ".Z");
	}
	
	public static String getAreaInWorld(String area) {
		return Manager.areas.getConfig().getString("Areas." + area + ".World");
	}
	
	public static String getFileName(String file, int x, int z) {
		return file + "_" + x + "_" + z;
	}

	public static Integer getAreaSizeX(String area) {
		return Manager.areas.getConfig().getInt("Areas." + area + ".Size.X");
	}

	public static Integer getAreaSizeZ(String area) {
		return Manager.areas.getConfig().getInt("Areas." + area + ".Size.Z");
	}
	
	public static Integer getAreaMaxX(String area) {
		return Manager.areas.getConfig().getInt("Areas." + area + ".Maximum.X");
	}
	
	public static Integer getAreaMaxY(String area) {
		return Manager.areas.getConfig().getInt("Areas." + area + ".Maximum.Y");
	}
	
	public static Integer getAreaMaxZ(String area) {
		return Manager.areas.getConfig().getInt("Areas." + area + ".Maximum.Z");
	}
	
	public static Integer getAreaX(String area) {
		return Manager.areas.getConfig().getInt("Areas." + area + ".X");
	}
	
	public static Integer getAreaY(String area) {
		return Manager.areas.getConfig().getInt("Areas." + area + ".Y");
	}
	
	public static Integer getAreaZ(String area) {
		return Manager.areas.getConfig().getInt("Areas." + area + ".Z");
	}

	public static Integer getAreaChunk(String area) {
		return Manager.areas.getConfig().getInt("Areas." + area + ".Size.Chunk");
	}

	public static void reloadConfig() {
		AreaReloader.plugin.reloadConfig();
	}
	
	public static void sendDebugMessage(Player player, String string) {
		player.sendMessage(debugPrefix() + string);
	}

	public static void sendDebugMessage(CommandSender sender, String string) {
		sender.sendMessage(debugPrefix() + string);
	}

	public static String debugPrefix() {
		return ChatColor.translateAlternateColorCodes('&', AreaReloader.plugin.getConfig().getString("Settings.Debug.Prefix"));
	}
}
