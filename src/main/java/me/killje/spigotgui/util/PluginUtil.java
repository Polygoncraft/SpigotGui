package me.killje.spigotgui.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class PluginUtil implements Listener {

    /**
     * List of offline players
     */
    private final List<OfflinePlayer> offlinePlayerList;
    /**
     * map of offline players by there name
     */
    private final Map<String, OfflinePlayer> offlinePlayerMap = new HashMap<>();
    /**
     * The plugin to reference
     */
    private final Plugin plugin;

    /**
     * Initializes the util. It loads a offline player list and map to get them
     * easy and without much calculations
     *
     * @param plugin The plugin to create this for
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public PluginUtil(Plugin plugin) {
        this.plugin = plugin;
        offlinePlayerList
                = new ArrayList<>(Arrays.asList(
                        plugin.getServer().getOfflinePlayers()
                ));

        for (OfflinePlayer offlinePlayer : offlinePlayerList) {
            offlinePlayerMap.put(offlinePlayer.getName(), offlinePlayer);
        }
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * @see Plugin#getConfig()
     *
     * Returns the config file of the plugin.
     *
     * @return The config of the plugin
     */
    public FileConfiguration getConfig() {
        return getPlugin().getConfig();
    }

    /**
     * Get the logger of the plugin
     *
     * @see Plugin#getLogger()
     *
     * @return The logger
     */
    public Logger getLogger() {
        return getPlugin().getLogger();
    }

    /**
     * Gets a list of all offline players that have been on this server
     *
     * @return The list of players
     */
    public List<OfflinePlayer> getOfflinePlayerList() {
        return offlinePlayerList;
    }

    /**
     * Gets a map of all offline players that have been on this server with
     * their name as key
     *
     * @return The map of players
     */
    public Map<String, OfflinePlayer> getOfflinePlayerMap() {
        return offlinePlayerMap;
    }

    /**
     * Returns the plugin instance itself
     *
     * @return
     */
    public Plugin getPlugin() {
        if (plugin == null) {
            throw new NullPointerException(
                    "Plugin reference is not set. Please add a plugin before referencing the PluginUtils");
        }
        return plugin;
    }

    /**
     * Fired when a player joins the server
     *
     * @see PlayerJoinEvent
     * @param event The joining event of a player
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            offlinePlayerList.add(player);
            offlinePlayerMap.put(player.getName(), player);
        }
    }

    /**
     * @see PluginManager#registerEvents(Listener, Plugin)
     *
     * Register the events in a class to the bukkit plugin manager
     *
     * @param listener The listener class to register
     */
    public void registerEvents(Listener listener) {
        getPlugin().getServer().getPluginManager().registerEvents(listener,
                getPlugin());
    }

    /**
     * @see BukkitScheduler#runTask(Plugin, Runnable)
     *
     * @param runnable The runnable class to run
     *
     * @return The task created
     */
    public BukkitTask runTask(Runnable runnable) {
        return Bukkit.getScheduler().runTask(getPlugin(), runnable);
    }

    /**
     * @see BukkitScheduler#runTaskAsynchronously(Plugin, Runnable)
     *
     * @param runnable The runnable class to run asynchronous
     *
     * @return The task created
     */
    public BukkitTask runTaskAsynchronously(Runnable runnable) {
        return Bukkit.getScheduler().
                runTaskAsynchronously(getPlugin(), runnable);
    }

    /**
     * @see BukkitScheduler#runTaskLater(Plugin, Runnable, long)
     *
     * @param runnable The runnable class to run after a period
     * @param delay    The delay in server ticks. On normal tps a second is
     *                 equal to 20 delay
     *
     * @return The task created
     */
    public BukkitTask runTaskLater(Runnable runnable, long delay) {
        return Bukkit.getScheduler().runTaskLater(getPlugin(), runnable, delay);
    }

    /**
     * @see BukkitScheduler#runTaskTimer(Plugin, Runnable, long, long)
     *
     * @param runnable The runnable class to run after a period periodically
     * @param delay    The delay in server ticks. On normal tps a second is
     *                 equal to 20 delay
     * @param period   The period to wait before running again. On a normal tps
     *                 a second is to to a period of 20
     *
     * @return The task created
     */
    public BukkitTask runTaskTimer(Runnable runnable, long delay, long period) {
        return Bukkit.getScheduler().runTaskTimer(getPlugin(), runnable, delay,
                period);
    }

    /**
     * @see BukkitScheduler#runTaskTimerAsynchronously(Plugin, Runnable, long,
     * long)
     *
     * @param runnable The runnable class to run after a period periodically
     *                 asynchronously
     * @param delay    The delay in server ticks. On normal tps a second is
     *                 equal to 20 delay
     * @param period   The period to wait before running again. On a normal tps
     *                 a second is to to a period of 20
     *
     * @return The task created
     */
    public BukkitTask runTaskTimerAsynchronously(Runnable runnable, long delay,
            long period) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(getPlugin(),
                runnable, delay, period);
    }

}
