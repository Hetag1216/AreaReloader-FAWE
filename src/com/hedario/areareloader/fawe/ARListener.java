package com.hedario.areareloader.fawe;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.hedario.areareloader.fawe.commands.LocationCommand;
import com.hedario.areareloader.fawe.events.AreaCompleteEvent;
import com.hedario.areareloader.fawe.events.AreaLoadEvent;

public class ARListener implements Listener {
	private AreaReloader areaReloader;

	public ARListener(final AreaReloader plugin) {
		this.areaReloader = plugin;
	}

	@EventHandler
	public void onAreaLoad(final AreaLoadEvent event) {
		if (event.isCancelled() || event.getArea() == null)
			return;
		if (event.getPlayer() != null) {
			Bukkit.broadcastMessage(event.getPlayer().getName());
		}
		Bukkit.broadcastMessage("fired " + event.getArea());
		LocationCommand.teleportPlayers(event.getArea());
	}
	
	@EventHandler
	public void onAreaComplete(final AreaCompleteEvent event) {
		if (event.isCancelled() || event.getArea() == null)
			return;
		if (event.getPlayer() != null) {
			Bukkit.broadcastMessage(event.getPlayer().getName());
		}
		Bukkit.broadcastMessage("completed " + event.getArea());
		
	}

	/**
	 * @return the plugin's instance
	 */
	public AreaReloader getAreaReloader() {
		return areaReloader;
	}

	/**
	 * @param areaReloader the instance to set
	 */
	public void setAreaReloader(AreaReloader areaReloader) {
		this.areaReloader = areaReloader;
	}

}
