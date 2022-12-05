package com.hetag.areareloader.fawe.configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import com.hetag.areareloader.fawe.AreaMethods;
import com.hetag.areareloader.fawe.AreaReloader;
import com.hetag.areareloader.fawe.commands.Executor;

public class Manager {
	public static Config defaultConfig;
	public static Config areas;

	public Manager() {
		initConfigs();
		loadConfig(defaultConfig);
		loadConfig(areas);
		setDebug();
	}
	
	public void initConfigs() {
		defaultConfig = new Config(new File("config.yml"));
		areas = new Config(new File("areas.yml"));
	}

	private void loadConfig(Config configurationFile) {
		FileConfiguration config = null;
		if (configurationFile == defaultConfig) {
			config = defaultConfig.getConfig();

		config.addDefault("Settings.Language.ChatPrefix", "&8[&6AreaReloader&8]&6 ");
		config.addDefault("Settings.Language.NoPermission", "You dont own sufficent permissions to run this command!");
		config.addDefault("Settings.Language.MustBePlayer", "You must be a player to use this command!");
		
		config.addDefault("Settings.Debug.Enabled", false);
		
		config.addDefault("Settings.Updater.Enabled", true);
		
		config.addDefault("Settings.Metrics.Enabled", true);
		
		config.addDefault("Settings.Announcer.Enabled", true);
		
		config.addDefault("Settings.AreaLoading.Interval", 500);
		config.addDefault("Settings.AreaLoading.FastMode", true);
		config.addDefault("Settings.AreaLoading.Percentage", 15);
		
		config.addDefault("Settings.AutoReload.Checker", true);
		config.addDefault("Settings.AutoReload.Interval", 200);
		config.addDefault("Settings.AutoReload.Notify.Admins", true);
		config.addDefault("Settings.AutoReload.Notify.Console", true);
		

		ArrayList<String> helpLines = new ArrayList<>();
		Executor.help = helpLines;
		helpLines.add("&8/&6ar &ehelp &7Display commands help.");
		config.addDefault("Commands.HelpLines", helpLines);
		config.addDefault("Commands.Help.Description", "Shows information about a command.");
		config.addDefault("Commands.Help.InvalidTopic", "&7Please specify a valid topic.");
		config.addDefault("Commands.Help.ProperUsage", "&6Proper usage: &e&o");
		

		config.addDefault("Commands.Create.Description", "&7Creates a new area.");
		config.addDefault("Commands.Create.Preparing", "Preparing to create &e%area%&6...");
		config.addDefault("Commands.Create.Success", "&e%area%&6 has been succesfully created.");
		config.addDefault("Commands.Create.Failure", "Failed to create new area &e%area%&6.");
		config.addDefault("Commands.Create.AlreadyExists", "An area with the name of &e%area%&6 already exists, please choose a different name!");
		config.addDefault("Commands.Create.InvalidValue", "You must select a value: &etrue&7/&efalse");
		config.addDefault("Commands.Create.InvalidName", "&e%area% &6is not a valid name!");
		
		config.addDefault("Commands.Display.Description", "&7Displays corners around existing areas.");
		config.addDefault("Commands.Display.Display", "Displaying &e%area%&6.");
		config.addDefault("Commands.Display.RemoveDisplay", "Removed display for &e%area%&6.");
		config.addDefault("Commands.Display.UseParticles", false);
		config.addDefault("Commands.Display.ParticleEffect", "FLAME");
		config.addDefault("Commands.Display.Block.Material", "PRISMARINE");
		config.addDefault("Commands.Display.Block.RestrictVision", true);
		config.addDefault("Commands.Display.ParticleDelay", 3000);
		config.addDefault("Commands.Display.ForceRendering", false);
		
		config.addDefault("Commands.List.Description", "&7Lists all existing areas.");
		config.addDefault("Commands.List.NoAreasFound", "No areas were found.");

		config.addDefault("Commands.Delete.Description", "&7Deletes an existing area.");
		config.addDefault("Commands.Delete.Success", "&e%area%&6 was succesfully deleted.");
		config.addDefault("Commands.Delete.InvalidArea", "&e%area%&6 does not exist!");

		config.addDefault("Commands.Load.Description", "&7Loads an existing area.");
		config.addDefault("Commands.Load.Preparing", "Preparing to load &e%area%&6.");
		config.addDefault("Commands.Load.Success", "Area &e%area%&6 was succesfully loaded in &e%time%&e.");
		config.addDefault("Commands.Load.Fail", "&e%area&6 has failed to load! Error has been traced and sent to console, enable debug for a more accurate description.");
		config.addDefault("Commands.Load.InvalidArea", "&e%area% &6does not exist.");
		config.addDefault("Commands.Load.AlreadyLoading", "e%area%&6 is already queued and being loaded!");

		config.addDefault("Commands.Version.Description", "&7Shows information about the plugin.");
		
		config.addDefault("Commands.Info.Description", "&7Shows information about the specified existing area.");
		
		config.addDefault("Commands.Reload.Description", "&7Reloads AreaReloader's configuration file.");
		config.addDefault("Commands.Reload.Success", "&eConfigurations and tasks were succesfully reloaded!");
		config.addDefault("Commands.Reload.Fail", "An &cerror &6occurred while reloading configurations, enable debug for a more accurate description.");

		config.addDefault("Commands.Hook.Description", "&7Shows an interface for the plugins dependencies.");
		
		config.addDefault("Commands.Cancel.Description", "&7Cancels the loading process of one or all areas.");
		config.addDefault("Commands.Cancel.Success", "&e%area% &7(&eTask: %id%&7) &6loading has been cancelled!");
		config.addDefault("Commands.Cancel.Fail", "&e%area% &6is not currently being loaded.");
		config.addDefault("Commands.Cancel.CancelAll", "All areas have been cancelled from loading!");
		config.addDefault("Commands.Cancel.NoAreasLoading", "Nothing happened, no areas were being loaded.");
		
		config.addDefault("Config.Version", 1.0);
		defaultConfig.saveConfig();
		} else if (configurationFile == areas) {
			config = areas.getConfig();
			
			config.addDefault("Config.Version", 1.0);
		}
	}

	public static FileConfiguration getConfig() {
		return defaultConfig.getConfig();
	}

	public static void reloadAllInstances() {
		defaultConfig.reloadConfig();
		areas.reloadConfig();
	}

	public static void setDebug() {
		resetDebug();
		try {
			File folder = AreaReloader.getInstance().getDataFolder();
			if (!folder.exists()) {
				folder.mkdir();
			}

			File debug = new File(AreaReloader.getInstance().getDataFolder(), "debug.txt");
			if (!debug.exists()) {
				debug.createNewFile();
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Prints the given string to debug.
	 * @param string
	 */
	public static void printDebug(String string) {
		if (AreaReloader.debug) {
			try {
				final FileWriter fw = new FileWriter(new File(AreaReloader.getInstance().getDataFolder(), "debug.txt"), true);
				final PrintWriter pw = new PrintWriter(fw);
				pw.println(LocalTime.now() + " " + string);
				pw.flush();
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Prints the given params to debug
	 * <p>
	 * Mainly used for Commands debugging
	 * @param cmd
	 * @param e
	 * @param sender
	 */
	public static void printDebug(String cmd, Exception e, CommandSender sender) {
		if (AreaReloader.debug) {
			printDebug("-=-=-=-=-=-=-=-=-=-=- Area Commands -=-=-=-=-=-=-=-=-=-=-");
			printDebug("An issue when running the command " + cmd + " has occurred!");
			printDebug("Writing debugs trace...");
			printDebug("");
			printDebug("" + e.getMessage());
			printDebug("-=-=-=-=-=-=-=-=-=-=- -=- -=-=-=-=-=-=-=-=-=-=-");
			if (sender != null)
				AreaMethods.sendDebugMessage(sender, "An error has been generated and registered to the debugs file.");
			else
				AreaReloader.log.warning("An error has been generated and registered to the debug's file.");
		}
	}
	
	public static void resetDebug() {
		File file = new File(AreaReloader.getInstance().getDataFolder(), "debug.txt");
		if (file.exists()) {
			file.delete();
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return;
	}
	
}
