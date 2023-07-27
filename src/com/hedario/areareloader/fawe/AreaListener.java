package com.hedario.areareloader.fawe;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.hedario.areareloader.fawe.events.AreaCompleteEvent;
import com.hedario.areareloader.fawe.events.AreaLoadEvent;

public class AreaListener implements Listener {
	private AreaReloader areaReloader;

	public AreaListener(final AreaReloader plugin) {
		this.areaReloader = plugin;
	}

	@EventHandler
	public void onAreaLoad(final AreaLoadEvent event) {
		if (event.isCancelled() || event.getArea() == null)
			return;
		if (TPManager.isEnabled(event.getArea()))
			new TPManager(event.getArea());
	}

	@EventHandler
	public void onAreaComplete(final AreaCompleteEvent event) {
		if (event.isCancelled() || event.getArea() == null)
			return;
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