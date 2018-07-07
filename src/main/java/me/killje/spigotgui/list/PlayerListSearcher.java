package me.killje.spigotgui.list;

import java.util.Map;
import me.killje.spigotgui.util.InventoryBase;
import org.bukkit.OfflinePlayer;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class PlayerListSearcher {

    private final Map<String, OfflinePlayer> playerMap;
    private final PlayerListElementFetcher playerListElementFetcher;
    private final InventoryBase prevPage;

    public PlayerListSearcher(Map<String, OfflinePlayer> playerMap, PlayerListElementFetcher playerListElementFetcher, InventoryBase prevPage) {
        this.playerMap = playerMap;
        this.playerListElementFetcher = playerListElementFetcher;
        this.prevPage = prevPage;
    }
    
    public Map<String, OfflinePlayer> getPlayerMap() {
        return playerMap;
    }
    
    public PlayerListElementFetcher getPlayerListElementFetcher() {
        return playerListElementFetcher;
    }

    public InventoryBase InventoryBeforeSearch() {
        return prevPage;
    }
    
}
