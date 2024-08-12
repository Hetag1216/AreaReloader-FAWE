package com.hedario.areareloader.fawe.util;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import com.hedario.areareloader.fawe.AreaMethods;
import com.hedario.areareloader.fawe.AreaReloader;
import com.hedario.areareloader.fawe.AreaScheduler;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PAPIExpansion extends PlaceholderExpansion {
	private AreaReloader instance;

	public PAPIExpansion(AreaReloader instance) {
		this.instance = instance;
	}

	@Override
	public @NotNull String getIdentifier() {
		return "AreaReloader";
	}

	@Override
	public @NotNull String getAuthor() {
		return "Hedario";
	}

	@Override
	public @NotNull String getVersion() {
		return "1.5";
	}

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public String onRequest(OfflinePlayer player, @NotNull String params) {
		if (params.startsWith("remaining_time_")) {
			String areaName = params.substring("remaining_time_".length());
			if (AreaMethods.exists(areaName)) {
				return "" + AreaMethods.formatTime(AreaScheduler.getRemainingTime(areaName));
			} else {
				return "Area does not exist";
			}
		} else if (params.startsWith("world_")) {
			String areaName = params.substring("world_".length());
			if (AreaMethods.exists(areaName)) {
				return AreaMethods.getAreaInWorld(areaName);
			} else {
				return "Area does not exist";
			}
		} else if (params.startsWith("corner1_")) {
			String areaName = params.substring("corner1_".length());
			if (AreaMethods.exists(areaName)) {
				return "" + AreaMethods.getAreaX(areaName) + ", " + AreaMethods.getAreaY(areaName) + ", " + AreaMethods.getAreaZ(areaName);
			} else {
				return "Area does not exist";
			}
		} else if (params.startsWith("corner2_")) {
			String areaName = params.substring("corner2_".length());
			if (AreaMethods.exists(areaName)) {
				return "" + AreaMethods.getAreaMaxX(areaName) + ", " + AreaMethods.getAreaMaxY(areaName) + ", " + AreaMethods.getAreaMaxZ(areaName);
			} else {
				return "Area does not exist";
			}
		} else if (params.startsWith("chunks_")) {
			String areaName = params.substring("chunks_".length());
			if (AreaMethods.exists(areaName)) {
				return "" + AreaMethods.getAreaChunk(areaName);
			} else {
				return "Area does not exist";
			}
		} else if (params.startsWith("loading_interval_")) {
			String areaName = params.substring("loading_interval_".length());
			if (AreaMethods.exists(areaName)) {
				return "" + AreaMethods.getInterval(areaName);
			} else {
				return "Area does not exist";
			}
		} else if (params.equalsIgnoreCase("global_loading_interval")) {
			return "" + AreaMethods.getGlobalInterval();
		} else {
			return null;
		}
	}
}
