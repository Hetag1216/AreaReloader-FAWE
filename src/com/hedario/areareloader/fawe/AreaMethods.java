package com.hedario.areareloader.fawe;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fastasyncworldedit.core.FaweAPI;
import com.hedario.areareloader.fawe.commands.ARCommand;
import com.hedario.areareloader.fawe.commands.DisplayCommand;
import com.hedario.areareloader.fawe.configuration.Manager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
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

import net.md_5.bungee.api.ChatColor;

public class AreaMethods {
	public static enum Phase {
		CREATION,
		DELETION,
		LOADING,
		CANCEL;
	}
	private static final List<String> PENDING = new ArrayList<String>();
	
	public static void performSetup() {
		final Path dir = AreaReloader.plugin.getDataFolder().toPath().resolve("Areas");
		try {
			Files.createDirectories(dir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void deleteArea(String area) {
		final Logger logger = AreaReloader.plugin.getLogger();
		kill(Phase.DELETION, area);
		final Path areasBase = AreaReloader.plugin.getDataFolder().toPath().resolve("Areas");
		final Path areaDir = areasBase.resolve(area);
		if (Files.exists(areaDir)) {
			try {
				try (Stream<Path> walk = Files.walk(areaDir)) {
					walk.sorted(Comparator.reverseOrder()).forEach(p -> {
						try {
							Files.deleteIfExists(p);
						} catch (IOException e) {
							logger.log(Level.WARNING, "Failed to delete " + p + " while cleaning existing area " + area, e);
						}
					});
				}
			} catch (IOException e) {
				logger.log(Level.WARNING, "Failed to fully delete existing area directory for " + area, e);
			}
		}
		Manager.getAreasConfig().set("Areas." + area, null);
		Manager.areas.saveConfig();
	}

	public static boolean isInteger(String s) {
		return isInteger(s, 10);
	}
	
	public static String formatTime(final long time) {
		String result = new String();
		if (time < 0) {
			result = "-";
		}
		final long days = TimeUnit.MILLISECONDS.toDays(time);
		final long hours = TimeUnit.MILLISECONDS.toHours(time) % 24;
		final long minutes = TimeUnit.MILLISECONDS.toMinutes(time) % 60;
		final long seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60;
		final long milliseconds = time % 1000;
		if (days > 0) 
			result += "" + days + "d ";
		if (hours > 0)
			result += "" + hours + "h ";
		if (minutes > 0) 
			result += "" + minutes + "m ";
		if (seconds >= 0) {
			if (milliseconds > 0) {
				result += "" + seconds + "." + String.valueOf(milliseconds).substring(0, 1) + "s";
			} else {
				result += "" + seconds + "s";
			}
		}
		return result;
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

	public static Integer getMaxInt(int min, int max, int length) {
		if (max - min < length) {
			return Integer.valueOf(max - min);
		}
		return Integer.valueOf(length);
	}

	public static boolean loadSchematicArea(CommandSender p, String area, String schemFile, World world, Location location) throws WorldEditException, FileNotFoundException, IOException {
		final Path path = AreaReloader.plugin.getDataFolder().toPath().resolve("Areas").resolve(area).resolve(schemFile + ".schem");
		if (!Files.isRegularFile(path)) {
			return false;
		}
		FaweAPI.load(path.toFile()).paste(FaweAPI.getWorld(world.getName()), BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ()));   	
		return true;
	}

	/**
	 * <p>
	 * Creates a copy of the selected region using schematics. <br>
	 * The whole area is split in sub sections to better distribute server load upon
	 * regeneration.
	 * </p>
	 * 
	 * @param player
	 * @param area
	 * @param length
	 * @param copyEntities
	 * @param copyBiomes
	 * @return whether or not the area was successfully created.
	 * @throws WorldEditException
	 */
	public static boolean createNewArea(final Player player, final String area, final int length, final boolean copyEntities, final boolean copyBiomes) throws WorldEditException {
		final Logger logger = AreaReloader.plugin.getLogger();
		if (!PENDING.contains(area)) {
			PENDING.add(area);
		}
		BukkitPlayer lp = BukkitAdapter.adapt(player);
		LocalSession ls = WorldEdit.getInstance().getSessionManager().get(lp);
		Region sel;
		try {
			sel = ls.getSelection(BukkitAdapter.adapt(player.getWorld()));
		} catch (IncompleteRegionException ex) {
			sendMessage(player, "&cYou must first select a region!", true);
			return false;
		}
		if (!(sel instanceof CuboidRegion)) {
			sendMessage(player, "&cOnly cuboid selections are supported!", true);
			return false;
		}

		BlockVector3 min = sel.getMinimumPoint();
		BlockVector3 max = sel.getMaximumPoint();
		final int minX = min.getBlockX();
		final int minY = min.getBlockY();
		final int minZ = min.getBlockZ();
		final int maxX = max.getBlockX();
		final int maxY = max.getBlockY();
		final int maxZ = max.getBlockZ();

		final int sectionsX = ((maxX - minX) / length) + 1;
		final int sectionsZ = ((maxZ - minZ) / length) + 1;
		final boolean fast = Manager.getConfig().getBoolean("Settings.AreaLoading.FastMode");
		for (int ix = 0; ix < sectionsX; ix++) {
			final int x = minX + ix * length;
			for (int iz = 0; iz < sectionsZ; iz++) {
				final int z = minZ + iz * length;

				try (EditSession extent = WorldEdit.getInstance().newEditSessionBuilder().world(sel.getWorld())
						.fastMode(fast)
						.combineStages(true)
						.changeSetNull()
						.checkMemory(false)
						.allowedRegionsEverywhere()
						.limitUnlimited()
						.build()) {

					Location pt1 = new Location(player.getWorld(), x, minY, z);
					Location pt2 = new Location(player.getWorld(), x + getMaxInt(x, maxX, length), maxY, z + getMaxInt(z, maxZ, length));

					BlockVector3 bvmin = BukkitAdapter.asBlockVector(pt1);
					BlockVector3 bvmax = BukkitAdapter.asBlockVector(pt2);
					CuboidRegion region = new CuboidRegion(sel.getWorld(), bvmin, bvmax);

					BlockArrayClipboard cc = new BlockArrayClipboard(region);
					ForwardExtentCopy clipCopy = new ForwardExtentCopy(extent, region, cc, region.getMinimumPoint());
					clipCopy.setCopyingEntities(copyEntities);
					clipCopy.setCopyingBiomes(copyBiomes);
					Manager.printDebug("- PHASE: " + Phase.CREATION.name());
				    Manager.printDebug("Area: " + area);
				    Manager.printDebug("Section: " + ix + "_" + iz);
				    Manager.printDebug("File:" + AreaMethods.getFileName(area, ix, iz));
					try {
						Operations.completeLegacy(clipCopy);
					    Manager.printDebug("Successfully copied the selected clipboard to system.");
					} catch (Exception e) {
						logger.log(Level.WARNING, "An error occurred while processing the selected clipboard, aborting all operations.", e);
						Manager.printDebug("An error occurred when copying the selected clipboard: " + e.getMessage());
						PENDING.remove(area);
						return false;
					}
					final Path path = AreaReloader.plugin.getDataFolder().toPath().resolve("Areas").resolve(area);
					final Path schem = path.resolve(getFileName(area, ix, iz) + ".schem");
					try {
						Files.createDirectories(schem.getParent());
						Manager.printDebug("Succesfully created area's directory.");
					} catch (IOException e) {
						logger.log(Level.WARNING, "Failed to create area's directory, aborting all operations.", e);
						PENDING.remove(area);
						return false;
					}
					try (OutputStream fos = new BufferedOutputStream(Files.newOutputStream(schem, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING));
							ClipboardWriter writer = BuiltInClipboardFormat.FAST.getWriter(fos)) {
						writer.write(cc);
						Manager.printDebug("Saved section to file: " + schem);
					} catch (IOException e) {
						Manager.printDebug("Failed writing schematic: " + e.getMessage());
						logger.log(Level.WARNING, "Failed writing schematic file, aborting all operations.", e);
						PENDING.remove(area);
						return false;
					}
				} catch (Exception e) {
					logger.log(Level.WARNING, "An unexpected error happened while processing the section " + ix + "_" + iz + " for " + area, e);
					Manager.printDebug("An unexpected error happened while processing the section." + e.getMessage());
					PENDING.remove(area);
					return false;
				}
			}
		}

		Manager.getAreasConfig().set("Areas." + area + ".World", sel.getWorld().getName());
		Manager.getAreasConfig().set("Areas." + area + ".HasCopiedEntities", copyEntities);
		Manager.getAreasConfig().set("Areas." + area + ".HasCopiedBiomes", copyBiomes);
		Manager.getAreasConfig().set("Areas." + area + ".Minimum.X", minX);
		Manager.getAreasConfig().set("Areas." + area + ".Minimum.Y", minY);
		Manager.getAreasConfig().set("Areas." + area + ".Minimum.Z", minZ);
		Manager.getAreasConfig().set("Areas." + area + ".Maximum.X", maxX);
		Manager.getAreasConfig().set("Areas." + area + ".Maximum.Y", maxY);
		Manager.getAreasConfig().set("Areas." + area + ".Maximum.Z", maxZ);
		Manager.getAreasConfig().set("Areas." + area + ".Size.X", Math.max(0, sectionsX - 1));
		Manager.getAreasConfig().set("Areas." + area + ".Size.Z", Math.max(0, sectionsZ - 1));
		Manager.getAreasConfig().set("Areas." + area + ".Size.Chunk", Math.max(1, sectionsX * sectionsZ));
		Manager.getAreasConfig().set("Areas." + area + ".Size.Length", length);
		Manager.getAreasConfig().set("Areas." + area + ".Loading.Interval.Global", true);
		Manager.getAreasConfig().set("Areas." + area + ".Loading.Interval.Time", 200);
		Manager.getAreasConfig().set("Areas." + area + ".AutoReload.Enabled", false);
		Manager.getAreasConfig().set("Areas." + area + ".AutoReload.Time", 200000);
		Manager.areas.saveConfig();
		PENDING.remove(area);
		return true;
	}
	
	public static void kill(final Phase phase, String area) {
		if (DisplayCommand.isDisplaying(area)) {
			DisplayCommand.remove(area, null);
		}
		
		final Loader loader = Loader.get(area);
		Manager.printDebug("- PHASE: " + phase.name());
		Manager.printDebug("Area: " + area);
		if (loader == null) {
			Manager.printDebug("The area is not being loaded?");
			return;
		} else {
			Manager.printDebug("Task ID:" + loader.task.getTaskId());
			loader.remove();
			Manager.printDebug("Executed succesfully");
		}
	}

	public static List<String> getAreas() {
		List<String> areas = new ArrayList<String>();
		if (Manager.getAreasConfig().contains("Areas")) {
			for (String keys : Manager.getAreasConfig().getConfigurationSection("Areas").getKeys(false)) {
				areas.add(keys);
			}
		}
		return areas;
	}
	
	public static boolean exists(final String area) {
		if (Manager.getAreasConfig().contains("Areas." + area)) {
			return true;
		}
		return false;
	}
	
	public static long getGlobalInterval() {
		return Manager.getConfig().getLong("Settings.AreaLoading.GlobalInterval");
	}
	
	public static boolean isGlobalInterval(String area) {
		return Manager.getAreasConfig().getBoolean("Areas." + area + ".Loading.Interval.Global");
	}
	
	public static long getInterval(String area) {
		long interval;
		if (!isGlobalInterval(area)) {
			interval = Manager.getAreasConfig().getLong("Areas." + area + ".Loading.Interval.Time");
		} else {
			interval = getGlobalInterval();
		}
		return interval < 1 ? 1 : interval;
	}
	
	public static String getAreaInWorld(String area) {
		return Manager.getAreasConfig().getString("Areas." + area + ".World");
	}
	
	public static World getWorld(String area) {
		return Bukkit.getWorld(getAreaInWorld(area));
	}
	
	public static String getFileName(String file, int x, int z) {
		return file + "_" + x + "_" + z;
	}

	public static Integer getAreaSizeX(String area) {
		return Manager.getAreasConfig().getInt("Areas." + area + ".Size.X");
	}

	public static Integer getAreaSizeZ(String area) {
		return Manager.getAreasConfig().getInt("Areas." + area + ".Size.Z");
	}
	
	public static Integer getAreaMaxX(String area) {
		return Manager.getAreasConfig().getInt("Areas." + area + ".Maximum.X");
	}
	
	public static Integer getAreaMaxY(String area) {
		return Manager.getAreasConfig().getInt("Areas." + area + ".Maximum.Y");
	}
	
	public static Integer getAreaMaxZ(String area) {
		return Manager.getAreasConfig().getInt("Areas." + area + ".Maximum.Z");
	}
	
	public static Integer getAreaX(String area) {
		return Manager.getAreasConfig().getInt("Areas." + area + ".Minimum.X");
	}
	
	public static Integer getAreaY(String area) {
		return Manager.getAreasConfig().getInt("Areas." + area + ".Minimum.Y");
	}
	
	public static Integer getAreaZ(String area) {
		return Manager.getAreasConfig().getInt("Areas." + area + ".Minimum.Z");
	}

	public static Integer getAreaChunk(String area) {
		return Manager.getAreasConfig().getInt("Areas." + area + ".Size.Chunk");
	}
	
	public static Integer getAreaLength(String area) {
		return Manager.getAreasConfig().getInt("Areas." + area + ".Size.Length");
	}

	public static void reloadConfig() {
		AreaReloader.plugin.reloadConfig();
	}
	
	public static ChatColor getPrimaryColor() {
		return ChatColor.of(Manager.getConfig().getString("Settings.Language.Colors.Primary"));
	}
	
	public static ChatColor getSecondaryColor() {
		return ChatColor.of(Manager.getConfig().getString("Settings.Language.Colors.Secondary"));
	}
	
	public static void sendMessage(CommandSender sender, String message, boolean prefix) {
		if (sender == null) {
			return;
		}
		if (prefix) {
			sender.sendMessage(ARCommand.formatColors(getPrefix() + message));
		} else {
			sender.sendMessage(ARCommand.formatColors(message));
		}
	}
	
	public static String getPrefix() {
		return Manager.getConfig().getString("Settings.Language.ChatPrefix");
	}

	public static List<String> getPending() {
		return PENDING;
	}
}
