package com.hetag.areareloader.fawe.configuration;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.hetag.areareloader.fawe.AreaReloader;

public class Config {
	private final AreaReloader plugin;
	private final File file;
	private final FileConfiguration config;

	public Config(File file) {
		this.plugin = AreaReloader.plugin;
		this.file = new File(this.plugin.getDataFolder() + File.separator + file);
		this.config = YamlConfiguration.loadConfiguration(this.file);
		reloadConfig();
	}

	public void create() {
		if (!this.file.getParentFile().exists()) {
			try {
				this.file.getParentFile().mkdir();
				this.plugin.getLogger().info("Generating new directory for " + this.file.getName() + "!");
			} catch (Exception e) {
				this.plugin.getLogger().info("Failed to generate directory!");
				e.printStackTrace();
			}
		}
		if (!this.file.exists()) {
			try {
				this.file.createNewFile();
				this.plugin.getLogger().info("Generating new " + this.file.getName() + "!");
			} catch (Exception e) {
				this.plugin.getLogger().info("Failed to generate " + this.file.getName() + "!");
				e.printStackTrace();
			}
		}
	}

	public FileConfiguration getConfig() {
		return this.config;
	}

	public void reloadConfig() {
		create();
		try {
			this.config.load(this.file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveConfig() {
		try {
			this.config.options().copyDefaults(true);
			this.config.save(this.file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
