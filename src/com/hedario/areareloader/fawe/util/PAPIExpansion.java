package com.hedario.areareloader.fawe.util;

import org.bukkit.Bukkit;
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
		return "AreaReloader-FAWE";
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

			// Debugging to see if we're processing the right area name
			Bukkit.broadcastMessage("Processing remaining time for area: " + areaName);

			// Check if the area exists
			if (AreaMethods.exists(areaName)) {
				// Return the remaining time
				return String.valueOf(AreaScheduler.getRemainingTime(areaName));

			} else {
				return "Area does not exist";
			}
		}

		return null; // Fallback for unrecognized placeholders
	}

}
