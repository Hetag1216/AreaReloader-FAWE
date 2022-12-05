package com.hetag.areareloader.fawe;

import java.util.HashMap;
import java.util.Map.Entry;

public class Queue {
	private HashMap<String, Integer> QUEUE;
	private AreaReloader plugin;

	public Queue(AreaReloader plugin) {
		this.plugin = plugin;
		QUEUE = new HashMap<>();
	}

	/**
	 * Returns the queue instance.
	 * 
	 * @return QUEUE
	 */
	public HashMap<String, Integer> get() {
		if (QUEUE != null)
			return QUEUE;
		return null;
	}

	/**
	 * Adds an area to the queue with its given task ID.
	 * 
	 * @param area
	 * @param ID
	 * @return
	 */
	public void add(String area, int ID) {
		get().put(area, ID);
		return;
	}

	/**
	 * Removes an area from the queue.
	 * 
	 * @param area
	 */

	public void remove(String area) {
		get().remove(area);
		return;
	}

	/**
	 * Removes an area from the queue and cancels its running task.
	 * <p>
	 * This method assumes by default that the task is still running.
	 * Throws an error if the task cannot be cancelled successfully.
	 * @param area
	 * @param taskID
	 */
	public void remove(String area, int taskID) {
		try {
			plugin.getServer().getScheduler().cancelTask(taskID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		get().remove(area);
		return;
	}
	
	public String getElements() {
		for (String i : get().keySet()) {
			return i;
		}
		return null;
	}

	/**
	 * Returns the task id number associated with the area's name.
	 * 
	 * @param area
	 * @return taskID if != null
	 * <p>
	 * 0 if == null
	 */

	public int getTaskByName(String area) {
		for (Entry<String, Integer> IDs : get().entrySet()) {
			if (IDs.getKey().equals(area)) {
				return IDs.getValue() != null ? IDs.getValue() : 0;
			}
		}
		return 0;
	}

	/**
	 * Check if the specified area is queued.
	 * <p>
	 * Looks for the area's name.
	 * 
	 * @param area
	 * @return true/false
	 */
	public boolean isQueued(String area) {
		if (get().containsKey(area))
			return true;
		return false;
	}

	/**
	 * Check if the specified area is queued.
	 * <p>
	 * Looks for the area's name and task id. This method is mainly used to deeply
	 * check whether an area is queued with its unique task ID or not.
	 * 
	 * @param area
	 * @param ID
	 * @return true/false
	 */
	public boolean isQueued(String area, int ID) {
		for (Entry<String, Integer> IDs : get().entrySet()) {
			if (IDs.getKey().equals(area) && IDs.getValue() == ID) {
				return true;
			}
		}
		return false;
	}
}
