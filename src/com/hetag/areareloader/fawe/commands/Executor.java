package com.hetag.areareloader.fawe.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import com.hetag.areareloader.fawe.AreaReloader;
import com.hetag.areareloader.fawe.configuration.Manager;

public class Executor {
	private AreaReloader plugin;

	public Executor(AreaReloader plugin) {
		this.plugin = plugin;
		init();
	}

	public static String[] commandaliases = { "ar", "areareloader" };
	public static List<String> help;

	private void init() {
		PluginCommand ar = this.plugin.getCommand("ar");
		new HelpCommand();
		new CreateCommand();
		new ListCommand();
		new DeleteCommand();
		new LoadCommand();
		new VersionCommand();
		new HookCommand();
		new InfoCommand();
		new DisplayCommand();
		new ConfigReloadCommand();
		new DisplayCommand();
		new CancelCommand();
		help = Manager.getConfig().getStringList("Commands.HelpLines");

		CommandExecutor exe = new CommandExecutor() {
			public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
				if ((args.length == 0) && (Arrays.asList(Executor.commandaliases).contains(label.toLowerCase()))) {
					for (String line : Executor.help) {
						s.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
					}
					return true;
				}
				List<String> sendingArgs = Arrays.asList(args).subList(1, args.length);
				for (ARCommand command : ARCommand.instances.values()) {
					if (Arrays.asList(command.getAliases()).contains(args[0].toLowerCase())) {
						try {
						command.execute(s, sendingArgs);
						} catch (Exception e) {
							Manager.printDebug(command.getName(), e, s);
						}
						return true;
					}
				}
				return true;
			}
		};
		ar.setExecutor(exe);
		ar.setTabCompleter(new TabCompletion());
	}
}
