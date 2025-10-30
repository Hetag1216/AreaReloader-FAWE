package com.hedario.areareloader.fawe.effects;

import java.util.Properties;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Particle.DustTransition;
import org.bukkit.Vibration;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

import com.hedario.areareloader.fawe.configuration.Manager;

public enum ParticleEffect {
	/**
	 * Applicable data: {@link BlockData}
	 */
	BLOCK(Particle.BLOCK),
	
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
	DUST(Particle.DUST),
	
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
	ITEM(Particle.ITEM),

	ASH(Particle.ASH), WHITE_ASH(Particle.WHITE_ASH),

	BUBBLE_COLUMN_UP(Particle.BUBBLE_COLUMN_UP),
	BUBBLE_POP(Particle.BUBBLE_POP),
	
	CAMPFIRE_COSY_SMOKE(Particle.CAMPFIRE_COSY_SMOKE),
	CAMPFIRE_SIGNAL_SMOKE(Particle.CAMPFIRE_SIGNAL_SMOKE),
	
	CLOUD(Particle.CLOUD),
	
	COMPOSTER(Particle.COMPOSTER),
	
	CRIMSON_SPORE(Particle.CRIMSON_SPORE),
	
	CRIT(Particle.CRIT),
	ENCHANTED_HIT(Particle.ENCHANTED_HIT),
	
	CURRENT_DOWN(Particle.CURRENT_DOWN),
	
	DAMAGE_INDICATOR(Particle.DAMAGE_INDICATOR),
	
	DOLPHIN(Particle.DOLPHIN),
	
	DRAGON_BREATH(Particle.DRAGON_BREATH),
	
	DRIPPING_LAVA(Particle.DRIPPING_LAVA),
	DRIPPING_WATER(Particle.DRIPPING_WATER),
	
	DRIPPING_DRIPSTONE_LAVA(Particle.DRIPPING_DRIPSTONE_LAVA),
	DRIPPING_DRIPSTONE_WATER(Particle.DRIPPING_DRIPSTONE_WATER),
	
	DRIPPING_HONEY(Particle.DRIPPING_HONEY),
	DRIPPING_OBSIDIAN_TEAR(Particle.DRIPPING_OBSIDIAN_TEAR),
	
	ELECTRIC_SPARK(Particle.ELECTRIC_SPARK),
	
	ENCHANT(Particle.ENCHANT),
	
	END_ROD(Particle.END_ROD),

	EXPLOSION(Particle.EXPLOSION),
	EXPLOSION_EMITTER(Particle.EXPLOSION_EMITTER),

	FALLING_DRIPSTONE_LAVA(Particle.FALLING_DRIPSTONE_LAVA),
	FALLING_DRIPSTONE_WATER(Particle.FALLING_DRIPSTONE_WATER),
	
	FALLING_HONEY(Particle.FALLING_HONEY),
	FALLING_LAVA(Particle.FALLING_LAVA),
	FALLING_NECTAR(Particle.FALLING_NECTAR),
	FALLING_OBSIDIAN_TEAR(Particle.FALLING_OBSIDIAN_TEAR),
	FALLING_SPORE_BLOSSOM(Particle.FALLING_SPORE_BLOSSOM),
	FALLING_WATER(Particle.FALLING_WATER),

	FIREWORK(Particle.FIREWORK),

	FLASH(Particle.FLASH),

	GLOW(Particle.GLOW),

	GLOW_SQUID_INK(Particle.GLOW_SQUID_INK),

	HEART(Particle.HEART),

	LANDING_HONEY(Particle.LANDING_HONEY),

	LANDING_LAVA(Particle.LANDING_LAVA),

	LANDING_OBSIDIAN_TEAR(Particle.LANDING_OBSIDIAN_TEAR),

	LAVA(Particle.LAVA), ELDER_GUARDIAN(Particle.ELDER_GUARDIAN),

	NAUTILUS(Particle.NAUTILUS),

	NOTE(Particle.NOTE),

	PORTAL(Particle.PORTAL),

	REVERSE_PORTAL(Particle.REVERSE_PORTAL),

	SCRAPE(Particle.SCRAPE),

	SLIME(Particle.ITEM_SLIME),

	FLAME_SMALL(Particle.SMALL_FLAME),
	FLAME(Particle.FLAME),
	FLAME_SOUL_FIRE(Particle.SOUL_FIRE_FLAME),

	SMOKE(Particle.SMOKE),
	LARGE_SMOKE(Particle.LARGE_SMOKE),
	
	SNEEZE(Particle.SNEEZE),

	POOF(Particle.POOF),
	SNOWFLAKE(Particle.SNOWFLAKE),

	SOUL(Particle.SOUL),

	EFFECT(Particle.EFFECT),
	INSTANT_EFFECT(Particle.INSTANT_EFFECT),
    ENTITY_EFFECT(Particle.ENTITY_EFFECT),
    WITCH(Particle.WITCH),

	SPIT(Particle.SPIT),

	SPORE_BLOSSOM_AIR(Particle.SPORE_BLOSSOM_AIR),

	SQUID_INK(Particle.SQUID_INK),

	SWEEP_ATTACK(Particle.SWEEP_ATTACK),

	TOTEM_OF_UNDYING(Particle.TOTEM_OF_UNDYING),

	MYCELIUM(Particle.MYCELIUM),

	VILLAGER_ANGRY(Particle.ANGRY_VILLAGER),
	VILLAGER_HAPPY(Particle.HAPPY_VILLAGER),
	
	WARPED_SPORE(Particle.WARPED_SPORE),
	
	BUBBLE(Particle.BUBBLE),
	SPLASH(Particle.SPLASH),
	UNDERWATER(Particle.UNDERWATER),
	
	WAX_OFF(Particle.WAX_OFF),
	WAX_ON(Particle.WAX_ON),
	
    GUST(Particle.GUST),
    SMALL_GUST(Particle.SMALL_GUST),
    GUST_EMITTER_LARGE(Particle.GUST_EMITTER_LARGE),
    GUST_EMITTER_SMALL(Particle.GUST_EMITTER_SMALL),

    // Trial spawner & ominous vault
    TRIAL_SPAWNER_DETECTION(Particle.TRIAL_SPAWNER_DETECTION),
    TRIAL_SPAWNER_DETECTION_OMINOUS(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS),
    TRIAL_OMEN(Particle.TRIAL_OMEN),
    OMINOUS_SPAWNING(Particle.OMINOUS_SPAWNING),
    RAID_OMEN(Particle.RAID_OMEN),
    VAULT_CONNECTION(Particle.VAULT_CONNECTION),

    // Ambient foliage / firefly
    FIREFLY(Particle.FIREFLY),
    CHERRY_LEAVES(Particle.CHERRY_LEAVES),
    PALE_OAK_LEAVES(Particle.PALE_OAK_LEAVES),
    TINTED_LEAVES(Particle.TINTED_LEAVES),

    // Misc new particles
    BLOCK_CRUMBLE(Particle.BLOCK_CRUMBLE),
    DUST_PILLAR(Particle.DUST_PILLAR),
    DUST_PLUME(Particle.DUST_PLUME),
    WHITE_SMOKE(Particle.WHITE_SMOKE),
    RAIN(Particle.RAIN),
    SCULK_CHARGE(Particle.SCULK_CHARGE),
    SCULK_CHARGE_POP(Particle.SCULK_CHARGE_POP),
    SCULK_SOUL(Particle.SCULK_SOUL),
    SHRIEK(Particle.SHRIEK),
    SONIC_BOOM(Particle.SONIC_BOOM),
    ITEM_SNOWBALL(Particle.ITEM_SNOWBALL),
    ITEM_COBWEB(Particle.ITEM_COBWEB),
    INFESTED(Particle.INFESTED),
    TRAIL(Particle.TRAIL);
	
	
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
