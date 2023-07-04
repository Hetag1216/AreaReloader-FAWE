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
import com.hedario.areareloader.fawe.reflection.AreaProtocol;
import com.hedario.areareloader.fawe.reflection.Metrics;
import com.hedario.areareloader.fawe.reflection.UpdateChecker;
import com.hedario.areareloader.fawe.reflection.V1_13.Protocol_1_13;
import com.hedario.areareloader.fawe.reflection.V1_14.Protocol_1_14;
import com.hedario.areareloader.fawe.reflection.V1_15.Protocol_1_15;
import com.hedario.areareloader.fawe.reflection.V1_16.Protocol_1_16;
import com.hedario.areareloader.fawe.reflection.V1_17.Protocol_1_17;
import com.hedario.areareloader.fawe.reflection.V1_18.Protocol_1_18;
import com.hedario.areareloader.fawe.reflection.V1_19.Protocol_1_19;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class AreaReloader extends JavaPlugin implements Listener {
	public static AreaReloader plugin;
	public static Logger log;
	public static WorldEditPlugin fawe;
	public static AreaProtocol ap;
	public static boolean debug, checker;
	private Queue queue;
	private boolean updater;
	private boolean useMetrics;
	private boolean announcer;

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
		
		checkProtocol();
		
		try {
			new Manager();
			log.info("Configurations succesfully registered!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		AreaMethods.performSetup();
		// Instantiate queue
		queue = new Queue(this);
		
		// Instantiate settings
		debug = Manager.getConfig().getBoolean("Settings.Debug.Enabled");
		updater = Manager.getConfig().getBoolean("Settings.Updater.Enabled");
		announcer = Manager.getConfig().getBoolean("Settings.Announcer.Enabled");
		
		// AreaLoader setup
		AreaLoader.init();
		
		// AreaScheduler setup
		AreaScheduler.init();

		try {
			new Executor(this);
			log.info("Commands succesfully registered!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		getServer().getPluginManager().registerEvents(new AreaListener(this), this);
		if (updater) {
			checkForUpdates();
		}
		useMetrics = Manager.getConfig().getBoolean("Settings.Metrics.Enabled");
		if (useMetrics) {
	        int pluginId = 106585;
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
					for (Player players : getServer().getOnlinePlayers()) {
						if (players.hasPermission("areareloader.*") || players.isOp()) {
							players.sendMessage(AreaLoader.prefix() + "AreaReloader is brought to you freely, if you wish to support the project, please consider making a donation!");
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

	public void checkProtocol() {
		String version = Bukkit.getServer().getClass().getPackage().getName();
		final String formattedVersion = version.substring(version.lastIndexOf(".") + 1);
		if (formattedVersion.contains("1_13")) {
			ap = new Protocol_1_13();
			log.info("Using protocol for 1.13 versions compatibility!");
		} else if (formattedVersion.contains("1_14")) {
			ap = new Protocol_1_14();
			log.info("Using protocol for 1.14 versions compatibility!");
		} else if (formattedVersion.contains("1_15")) {
			ap = new Protocol_1_15();
			log.info("Using protocol for 1.15 versions compatibility!");
		} else if (formattedVersion.contains("1_16")) {
			ap = new Protocol_1_16();
			log.info("Using protocol for 1.16 versions compatibility!");
		} else if (formattedVersion.contains("1_17")) {
			ap = new Protocol_1_17();
			log.info("Using protocol for 1.17 versions compatibility!");
		} else if (formattedVersion.contains("1_19")) {
			ap = new Protocol_1_19();
			log.info("Using protocol for 1.19 versions compatibility!");
		} else {
			ap = new Protocol_1_18();
			log.info("Using default protocol (1.18) for versions compatibility!");
		}
	}
	
	/**
	 * Returns the protocol version to allow graceful fall back for different spigot's versions.
	 * @return ap
	 */
	public static AreaProtocol getProtocol() {
		return ap;
	}
	
	/**
	 * Gets the plugin's instance.
	 * @return plugin
	 */
	public static AreaReloader getInstance() {
		return plugin;
	}

	/**
	 * Returns class utility instance.
	 * @return queue.class
	 */
	public Queue getQueue() {
		return queue;
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
		String status = ChatColor.DARK_AQUA + "Status: ";
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
		new UpdateChecker(this, 70655).getVersion(version -> {
			log.info("-=-=-=-= AreaReloader Updater =-=-=-=-");
			if (this.getDescription().getVersion().equals(version)) {
				log.info("You're running the latest version of the plugin!");
			} else {
				log.info("AreaReloader " + version + " is now available!");
				log.info("You're running AreaReloader " + this.getDescription().getVersion());
				log.info("DOWNLOAD IT AT: https://www.spigotmc.org/resources/areareloader.70655/");
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
		if (!getQueue().get().isEmpty()) {
			getQueue().get().clear();
		}
		if (!AreaLoader.areas.isEmpty()) {
			AreaLoader.areas.clear();
		}
	}
}