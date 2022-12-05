package com.hetag.areareloader.fawe.reflection;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;

import com.hetag.areareloader.fawe.AreaReloader;

public interface AreaProtocol {
	    void playRedstoneParticle(World world, Location location, Color color);
	    
	    public class DustManager {
	    	public static void display(World world, Location loc, Color color) {
	    	AreaProtocol p = AreaReloader.getProtocol();
	    		p.playRedstoneParticle(world, loc, color);
	    	}
	    }
}
