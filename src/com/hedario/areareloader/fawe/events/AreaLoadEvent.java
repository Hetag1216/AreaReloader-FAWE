package com.hedario.areareloader.fawe.events;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AreaLoadEvent extends Event implements Cancellable {
	public static final HandlerList handlers = new HandlerList();
	private final String area;
	private final CommandSender sender;
	private boolean cancelled = false;

	public AreaLoadEvent(final String area) {
		this.area = area;
		this.sender = null;
	}

	public AreaLoadEvent(final CommandSender sender, final String area) {
		this.sender = sender;
		this.area = area;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * @return the sender loading the area
	 */
	public CommandSender getPlayer() {
		return sender;
	}

	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @return the cancelled
	 */
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * @param cancelled the cancelled to set
	 */
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
