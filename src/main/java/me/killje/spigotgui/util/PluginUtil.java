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
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class PluginUtil implements Listener {

    private final Plugin plugin;
    private final List<OfflinePlayer> offlinePlayerList;
    private final Map<String, OfflinePlayer> offlinePlayerMap = new HashMap<>();

    public PluginUtil(Plugin plugin) {
        this.plugin = plugin;
        offlinePlayerList = new ArrayList<>(Arrays.asList(plugin.getServer().getOfflinePlayers()));
        for (OfflinePlayer offlinePlayer : offlinePlayerList) {
            offlinePlayerMap.put(offlinePlayer.getName(), offlinePlayer);
        }
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!offlinePlayerList.contains(player)) {
            offlinePlayerList.add(player);
            offlinePlayerMap.put(player.getName(), player);
        }
    }

    public Plugin getPlugin() {
        if (plugin == null) {
            throw new NullPointerException("Plugin reference is not set. Please add a plugin before referencing the PluginUtils");
        }
        return plugin;
    }

    public Logger getLogger() {
        return getPlugin().getLogger();
    }

    public BukkitTask runTask(Runnable runnable) {
        return Bukkit.getScheduler().runTask(getPlugin(), runnable);
    }

    public BukkitTask runTaskAsynchronously(Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), runnable);
    }

    public BukkitTask runTaskLater(Runnable runnable, long delay) {
        return Bukkit.getScheduler().runTaskLater(getPlugin(), runnable, delay);
    }

    public BukkitTask runTaskTimer(Runnable runnable, long delay, long period) {
        return Bukkit.getScheduler().runTaskTimer(getPlugin(), runnable, delay, period);
    }

    public BukkitTask runTaskTimerAsynchronously(Runnable runnable, long delay, long period) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(getPlugin(), runnable, delay, period);
    }

    public void registerEvents(Listener listener) {
        getPlugin().getServer().getPluginManager().registerEvents(listener, getPlugin());
    }

    public FileConfiguration getConfig() {
        return getPlugin().getConfig();
    }

    public List<OfflinePlayer> getOfflinePlayerList() {
        return offlinePlayerList;
    }

    public Map<String, OfflinePlayer> getOfflinePlayerMap() {
        return offlinePlayerMap;
    }

}
