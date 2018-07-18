package me.killje.spigotgui.util;

import org.bukkit.Bukkit;

/**
 * Class for doing bukkit reflection that uses version numbers
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class BukkitReflection {

    /**
     * The version of the current bukkit and minecraft implementation
     */
    private final static String VERSION = Bukkit.getServer().getClass().
            getPackage().getName().replace(".", ",").split(",")[3] + ".";

    /**
     * Get the class of a bukkit server
     *
     * @param cbClassString The class to get
     *
     * @return The class wanted
     *
     * @throws ClassNotFoundException Thrown when the class is not found
     */
    public static Class<?> getCBClass(String cbClassString)
            throws ClassNotFoundException {

        String name = "org.bukkit.craftbukkit." + VERSION + cbClassString;
        Class<?> nmsClass = Class.forName(name);
        return nmsClass;
    }

    /**
     * Get the class of a minecraft server
     *
     * @param nmsClassString The class to get
     *
     * @return The class wanted
     *
     * @throws ClassNotFoundException Thrown when the class is not found
     */
    public static Class<?> getNMSClass(String nmsClassString)
            throws ClassNotFoundException {

        String name = "net.minecraft.server." + VERSION + nmsClassString;
        Class<?> nmsClass = Class.forName(name);
        return nmsClass;
    }

}
