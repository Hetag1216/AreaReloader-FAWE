package com.hetag.areareloader.fawe.reflection.V1_17;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

import com.hetag.areareloader.fawe.reflection.AreaProtocol;

public class Protocol_1_17 implements AreaProtocol {

    private static Class<?> dustOptions;
    private static Constructor<?> dustConstructor;

    @Override
    public void playRedstoneParticle(World world, Location location, Color color) {
        if(dustConstructor == null) {
            try {
                getClassesAndMethods();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        Object[] dustParam = {org.bukkit.Color.fromBGR(color.getRed(), color.getGreen(), color.getBlue()), 1};

        try {
            Object dust = dustConstructor.newInstance(dustParam);

            world.spawnParticle(Particle.REDSTONE, location, 0, color.getRed() / 255, color.getGreen() / 255, color.getBlue() / 255, 1, dust);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /*
        Get all the classes and constructors once.
     */
    private static void getClassesAndMethods() throws NoSuchMethodException, ClassNotFoundException {
        dustOptions = Class.forName("org.bukkit.Particle$DustOptions");
        dustConstructor = dustOptions.getConstructor(org.bukkit.Color.class, float.class);
    }
}
