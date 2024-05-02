package com.hedario.areareloader.fawe;

import java.util.HashMap;
import java.util.Map.Entry;
public class Queue {
	private static HashMap<String, Integer> QUEUE;
	private static AreaReloader plugin;

	public Queue(AreaReloader plugin) {
		Queue.plugin = plugin;
		QUEUE = new HashMap<>();
	}
	
	public static HashMap<String, Integer> get() {
		return QUEUE;
	}
	
	/**
	 * Removes an area from the queue and cancels its running task.
	 * <p>
	 * This method assumes by default that the task is still running.
	 * Throws an error if the task cannot be cancelled successfully.
	 * @param area
	 * @param taskID
	 */
	public static void remove(String area, int taskID) {
		try {
			plugin.getServer().getScheduler().cancelTask(taskID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		get().remove(area);
		return;
	}

	/**
	 * Returns the task id number associated with the area's name.
	 * 
	 * @param area
	 * @return taskID if != null
	 * <p>
	 * 0 if == null
	 */

	public static int getTaskByName(String area) {
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
	public static boolean isQueued(String area) {
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
	public static boolean isQueued(String area, int ID) {
		for (Entry<String, Integer> IDs : get().entrySet()) {
			if (IDs.getKey().equals(area) && IDs.getValue() == ID) {
				return true;
			}
		}
		return false;
	}
}
