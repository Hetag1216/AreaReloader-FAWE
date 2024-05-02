package com.hedario.areareloader.fawe;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.hedario.areareloader.fawe.commands.Executor;
import com.hedario.areareloader.fawe.configuration.Manager;
import com.hedario.areareloader.fawe.reflection.Metrics;
import com.hedario.areareloader.fawe.reflection.UpdateChecker;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class AreaReloader extends JavaPlugin implements Listener {
	public static AreaReloader plugin;
	public static Logger log;
	public static WorldEditPlugin fawe;
	public static boolean debug, checker;
	private boolean updater, useMetrics, announcer;
	
	public void onEnable() {
		PluginManager pm = Bukkit.getPluginManager();
		if ((WorldEditPlugin) getServer().getPluginManager().getPlugin("FastAsyncWorldEdit") == null) {
			getLogger().warning("FastAsyncWorldEdit hook was not found, the plugin cannot be enabled without this dependency.");
			pm.disablePlugin(this);
		} else {
			getLogger().info("Plugin's dependency has been found!");
			fawe = (WorldEditPlugin) getServer().getPluginManager().getPlugin("FastAsyncWorldEdit");
		}
		
		plugin = this;
		log = getLogger();

		log.info("-=-=-=-= AreaReloader " + plugin.getDescription().getVersion() + " =-=-=-=-");
		
		
		try {
			new Manager();
			log.info("Configurations succesfully registered!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		AreaMethods.performSetup();
		
		// Instantiate settings
		debug = Manager.getConfig().getBoolean("Settings.Debug.Enabled");
		updater = Manager.getConfig().getBoolean("Settings.Updater.Enabled");
		announcer = Manager.getConfig().getBoolean("Settings.Announcer.Enabled");
		
		new Queue(plugin);
		
		// AreaLoader setup
		AreaLoader.init();
		
		// AreaScheduler setup
		AreaScheduler.init();
		
		
		// Instantiate events
		getServer().getPluginManager().registerEvents(new AreaListener(this), this);
		new AreaListener(this);

		try {
			new Executor(this);
			log.info("Commands succesfully registered!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (updater) {
			checkForUpdates();
		}
		useMetrics = Manager.getConfig().getBoolean("Settings.Metrics.Enabled");
		if (useMetrics) {
	        int pluginId = 17011;
	        new Metrics(this, pluginId);
	        log.info("Metrics has been enabled, thank you!");
		} else {
			log.info("Metrics will be disabled.");
		}
		log.info("Succesfully enabled AreaReloader!");
		log.info("-=-=-=-= -=- =-=-=-=-");
		
		if (announcer) {
			Runnable announcer = new Runnable() {
				@Override
				public void run() {
					log.info("AreaReloader is brought to you freely, if you wish to support the project, please consider making a donation!");
					for (Player players : getServer().getOnlinePlayers()) {
						if (players.hasPermission("areareloader.*") || players.isOp()) {
							AreaMethods.sendMessage(players, "AreaReloader is brought to you freely, if you wish to support the project, please consider making a donation!", true);
						}
					}
				}
			};
			getInstance().getServer().getScheduler().runTaskTimerAsynchronously(plugin, announcer, 200L, 36000L);
		}
	}

	public void onDisable() {
		ShutDown();
		log.info("Succesfully disabled AreaReloader!");
	}
	
	/**
	 * Gets the plugin's instance.
	 * @return plugin
	 */
	public static AreaReloader getInstance() {
		return plugin;
	}
	
	public static WorldEditPlugin getWEInstance() {
		return fawe;
	}
	
	/**
	 * Gets the status of AreaReloader's hooks.
	 * @return status
	 */
	public String getStatus() {
		String enabled = ChatColor.GREEN + "Enabled";
		String disabled = ChatColor.RED + "Disabled";
		String status = ChatColor.GOLD + "Status: ";
		if (fawe != null && fawe.isEnabled()) {
			return status + enabled;
		} else {
			return status + disabled;
		}
	}
	
	/**
	 * Checks for plugin's update from the official spigot page.
	 */
	private void checkForUpdates() {
		new UpdateChecker(this, 106585).getVersion(version -> {
			log.info("-=-=-=-= AreaReloader Updater =-=-=-=-");
			if (this.getDescription().getVersion().equals(version)) {
				log.info("You're running the latest version of the plugin!");
			} else {
				log.info("AreaReloader " + version + " is now available!");
				log.info("You're running AreaReloader " + this.getDescription().getVersion());
				log.info("DOWNLOAD IT AT: https://www.spigotmc.org/resources/areareloader-fawe.106585/");
			}
			log.info("-=-=-=-= -=- =-=-=-=-");
		});
	}
	
	/**
	 * Shut down all active tasks.
	 */
	private void ShutDown() {
		if (!getInstance().getServer().getScheduler().getPendingTasks().isEmpty()) {
			getInstance().getServer().getScheduler().getPendingTasks().clear();
		}
		if (!getInstance().getServer().getScheduler().getActiveWorkers().isEmpty()) {
			getInstance().getServer().getScheduler().cancelTasks(getInstance());
			getInstance().getServer().getScheduler().getActiveWorkers().clear();
		}
		if (!Queue.get().isEmpty()) {
			Queue.get().clear();
		}
		if (!AreaLoader.areas.isEmpty()) {
			AreaLoader.areas.clear();
		}
	}
}