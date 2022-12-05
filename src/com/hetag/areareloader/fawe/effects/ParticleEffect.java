package com.hetag.areareloader.fawe.effects;

import java.util.Properties;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Particle.DustTransition;
import org.bukkit.Vibration;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

import com.hetag.areareloader.fawe.configuration.Manager;


public enum ParticleEffect {
	/**
	 * Applicable data: {@link BlockData}
	 */
	BLOCK_CRACK(Particle.BLOCK_CRACK),
	
	/**
	 * Applicable data: {@link BlockData}
	 */
	BLOCK_DUST(Particle.BLOCK_DUST),
	
	/**
	 * Applicable data: {@link BlockData}
	 */
	BLOCK_MARKER(Particle.BLOCK_MARKER),
	
	/**
	 * Applicable data: {@link BlockData}
	 */
	FALLING_DUST(Particle.FALLING_DUST),
	
	/**
	 * Applicable data: {@link DustOptions}
	 */
	REDSTONE(Particle.REDSTONE),
	
	/**
	 * Applicable data: {@link Vibration}
	 */
	VIBRATION(Particle.VIBRATION),

	/**
	 * Applicable data: {@link DustTransition}
	 */
	DUST_COLOR_TRANSITION(Particle.DUST_COLOR_TRANSITION),

	/**
	 * Applicable data: {@link ItemStack}
	 */
	ITEM_CRACK(Particle.ITEM_CRACK),

	ASH(Particle.ASH), WHITE_ASH(Particle.WHITE_ASH),

	BUBBLE_COLUMN_UP(Particle.BUBBLE_COLUMN_UP),
	BUBBLE_POP(Particle.BUBBLE_POP),
	
	CAMPFIRE_COSY_SMOKE(Particle.CAMPFIRE_COSY_SMOKE),
	CAMPFIRE_SIGNAL_SMOKE(Particle.CAMPFIRE_SIGNAL_SMOKE),
	
	CLOUD(Particle.CLOUD),
	
	COMPOSTER(Particle.COMPOSTER),
	
	CRIMSON_SPORE(Particle.CRIMSON_SPORE),
	
	CRIT(Particle.CRIT),
	CRIT_MAGIC(Particle.CRIT_MAGIC),
	
	CURRENT_DOWN(Particle.CURRENT_DOWN),
	
	DAMAGE_INDICATOR(Particle.DAMAGE_INDICATOR),
	
	DOLPHIN(Particle.DOLPHIN),
	
	DRAGON_BREATH(Particle.DRAGON_BREATH),
	
	DRIP_LAVA(Particle.DRIP_LAVA),
	DRIP_WATER(Particle.DRIP_WATER),
	
	DRIPPING_DRIPSTONE_LAVA(Particle.DRIPPING_DRIPSTONE_LAVA),
	DRIPPING_DRIPSTONE_WATER(Particle.DRIPPING_DRIPSTONE_WATER),
	
	DRIPPING_HONEY(Particle.DRIPPING_HONEY),
	DRIPPING_OBSIDIAN_TEAR(Particle.DRIPPING_OBSIDIAN_TEAR),
	
	ELECTRIC_SPARK(Particle.ELECTRIC_SPARK),
	
	ENCHANTMENT_TABLE(Particle.ENCHANTMENT_TABLE),
	
	END_ROD(Particle.END_ROD),

	EXPLOSION_HUGE(Particle.EXPLOSION_HUGE),
	EXPLOSION_LARGE(Particle.EXPLOSION_LARGE),
	EXPLOSION_NORMAL(Particle.EXPLOSION_NORMAL),

	FALLING_DRIPSTONE_LAVA(Particle.FALLING_DRIPSTONE_LAVA),
	FALLING_DRIPSTONE_WATER(Particle.FALLING_DRIPSTONE_WATER),
	
	FALLING_HONEY(Particle.FALLING_HONEY),
	FALLING_LAVA(Particle.FALLING_LAVA),
	FALLING_NECTAR(Particle.FALLING_NECTAR),
	FALLING_OBSIDIAN_TEAR(Particle.FALLING_OBSIDIAN_TEAR),
	FALLING_SPORE_BLOSSOM(Particle.FALLING_SPORE_BLOSSOM),
	FALLING_WATER(Particle.FALLING_WATER),

	FIREWORKS_SPARK(Particle.FIREWORKS_SPARK),

	FLASH(Particle.FLASH),

	GLOW(Particle.GLOW),

	GLOW_SQUID_INK(Particle.GLOW_SQUID_INK),

	HEART(Particle.HEART),

	LANDING_HONEY(Particle.LANDING_HONEY),

	LANDING_LAVA(Particle.LANDING_LAVA),

	LANDING_OBSIDIAN_TEAR(Particle.LANDING_OBSIDIAN_TEAR),

	LAVA(Particle.LAVA), MOB_APPEARANCE(Particle.MOB_APPEARANCE),

	NAUTILUS(Particle.NAUTILUS),

	NOTE(Particle.NOTE),

	PORTAL(Particle.PORTAL),

	REVERSE_PORTAL(Particle.REVERSE_PORTAL),

	SCRAPE(Particle.SCRAPE),

	SLIME(Particle.SLIME),

	FLAME_SMALL(Particle.SMALL_FLAME),
	FLAME(Particle.FLAME),
	FLAME_SOUL_FIRE(Particle.SOUL_FIRE_FLAME),

	SMOKE_LARGE(Particle.SMOKE_LARGE),
	SMOKE_NORMAL(Particle.SMOKE_NORMAL),
	
	SNEEZE(Particle.SNEEZE),

	SNOW_SHOVEL(Particle.SNOW_SHOVEL),
	SNOWBALL(Particle.SNOWBALL),
	SNOWFLAKE(Particle.SNOWFLAKE),

	SOUL(Particle.SOUL),


	SPELL(Particle.SPELL),
	SPELL_INSTANT(Particle.SPELL_INSTANT),
	SPELL_MOB(Particle.SPELL_MOB),
	SPELL_MOB_AMBIENT(Particle.SPELL_MOB_AMBIENT),
	SPELL_WITCH(Particle.SPELL_WITCH),

	SPIT(Particle.SPIT),

	SPORE_BLOSSOM_AIR(Particle.SPORE_BLOSSOM_AIR),

	SQUID_INK(Particle.SQUID_INK),

	SUSPENDED(Particle.SUSPENDED),
	SUSPENDED_DEPTH(Particle.SUSPENDED_DEPTH),

	SWEEP_ATTACK(Particle.SWEEP_ATTACK),

	TOTEM(Particle.TOTEM),

	TOWN_AURA(Particle.TOWN_AURA),

	VILLAGER_ANGRY(Particle.VILLAGER_ANGRY),
	VILLAGER_HAPPY(Particle.VILLAGER_HAPPY),
	
	WARPED_SPORE(Particle.WARPED_SPORE),
	
	WATER_BUBBLE(Particle.WATER_BUBBLE),
	WATER_DROP(Particle.WATER_DROP),
	WATER_SPLASH(Particle.WATER_SPLASH),
	WATER_WAKE(Particle.WATER_WAKE),
	
	WAX_OFF(Particle.WAX_OFF),
	WAX_ON(Particle.WAX_ON);
	
	
	Particle particle;
	Class<?> dataClass;
	boolean force;
	
	private ParticleEffect(Particle particle) {
		this.particle = particle;
		this.dataClass = particle.getDataType();
		this.force = Manager.getConfig().getBoolean("Commands.Display.ForceRendering");
	}
	
	public Particle getParticle() {
		return particle;
	}
	
	/**
	 * Displays the particle at the given location, with the given parameters
	 * 
	 * @param location where to spawn the particle
	 * @param amount of particles to spawn
	 * @param offsetX x axis
	 * @param offsetY y axis
	 * @param offsetZ z axis
	 * @param speed at which the particles move (sprinkle)
	 * @param data to display the particle with, only applicable on several particle types
	 * @param force whether to send the particle to players within an extended range and encourage their client to render it regardless of their settings
	 */
	
	public void display(final Location location, final int amount, final double offsetX, final double offsetY, final double offsetZ, final double speed, final Object data, final boolean force) {
		location.getWorld().spawnParticle(particle, location, amount, offsetX, offsetY, offsetZ, speed, data, force);
	}
	/**
	 * Displays the particle at the given location, with the given parameters
	 * 
	 * @param location where to spawn the particle
	 * @param amount of particles to spawn
	 * @param offsetX x axis
	 * @param offsetY y axis
	 * @param offsetZ z axis
	 * @param speed at which the particles move (sprinkle)
	 * @param data to display the particle with, only applicable on several particle types
	 * 
	 * @apiNote This method will decide whether or not to force render the particles to clients depending on the config value {@link Properties.Particles.ForceRendering}
	 */
	public void display(final Location location, final int amount, final double offsetX, final double offsetY, final double offsetZ, final double speed, final Object data) {
		this.display(location, amount, offsetX, offsetY, offsetZ, speed, data, this.force);
	}
	
	/**
	 * Displays the particle at the given location, with the given parameters
	 * 
	 * @param location where to spawn the particle
	 * @param amount of particles to spawn
	 * @param offsetX x axis
	 * @param offsetY y axis
	 * @param offsetZ z axis
	 * @param data to display the particle with, only applicable on several particle types
	 * 
	 * @apiNote This method will decide whether or not to force render the particles to clients depending on the config value {@link Properties.Particles.ForceRendering}
	 */
	public void display(final Location location, final int amount, final double offsetX, final double offsetY, final double offsetZ, final Object data) {
		this.display(location, amount, offsetX, offsetY, offsetZ, 0, data);
	}
	
	/**
	 * Displays the particle at the given location, with the given parameters
	 * 
	 * @param location where to spawn the particle
	 * @param amount of particles to spawn
	 * @param offsetX x axis
	 * @param offsetY y axis
	 * @param offsetZ z axis
	 * @param data to display the particle with, only applicable on several particle types
	 * 
	 * @apiNote This method will decide whether or not to force render the particles to clients depending on the config value {@link Properties.Particles.ForceRendering}
	 */
	public void display(final Location location, final int amount, final double offsetX, final double offsetY, final double offsetZ, final double speed) {
		this.display(location, amount, offsetX, offsetY, offsetZ, speed, null);
	}
	
	/**
	 * Displays the particle at the given location, with the given parameters
	 * 
	 * @param location where to spawn the particle
	 * @param amount of particles to spawn
	 * @param offsetX x axis
	 * @param offsetY y axis
	 * @param offsetZ z axis
	 * 
	 * @apiNote This method will decide whether or not to force render the particles to clients depending on the config value {@link Properties.Particles.ForceRendering}
	 */
	public void display(final Location location, final int amount, final double offsetX, final double offsetY, final double offsetZ) {
		this.display(location, amount, offsetX, offsetY, offsetZ, 0);
	}
	
	/**
	 * Displays the particle at the given location, with the given parameters
	 * 
	 * @param location where to spawn the particle
	 * @param amount of particles to spawn
	 * 
	 * @apiNote This method will decide whether or not to force render the particles to clients depending on the config value {@link Properties.Particles.ForceRendering}
	 */
	public void display(final Location location, final int amount) {
		this.display(location, amount, 0, 0, 0);
	}
	
	/**
	 * Displays the particle at the given location, with the given parameters
	 * 
	 * @param location where to spawn the particle
	 * 
	 * @apiNote This method will decide whether or not to force render the particles to clients depending on the config value {@link Properties.Particles.ForceRendering}
	 */
	public void display(final Location location) {
		this.display(location, 1);
	}
}
