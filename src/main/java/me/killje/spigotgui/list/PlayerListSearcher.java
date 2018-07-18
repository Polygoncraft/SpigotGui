package me.killje.spigotgui.list;

import java.util.Map;
import me.killje.spigotgui.util.InventoryBase;
import org.bukkit.OfflinePlayer;

/**
 * Class for searching based on player name
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class PlayerListSearcher {

    /**
     * Fetcher for the player icons given the player
     */
    private final PlayerListElementFetcher playerListElementFetcher;
    /**
     * Map of players to search through
     */
    private final Map<String, OfflinePlayer> playerMap;
    /**
     * The inventory previous to the search
     */
    private final InventoryBase prevPage;

    /**
     * Searcher for looking through a list of players
     *
     * @param playerMap                The map of players to search through
     * @param playerListElementFetcher The fetcher to get player icons
     * @param prevPage                 The previous inventory before the search
     */
    public PlayerListSearcher(Map<String, OfflinePlayer> playerMap,
            PlayerListElementFetcher playerListElementFetcher,
            InventoryBase prevPage) {

        this.playerMap = playerMap;
        this.playerListElementFetcher = playerListElementFetcher;
        this.prevPage = prevPage;
    }

    /**
     * The inventory before the search
     *
     * @return The inventory
     */
    public InventoryBase InventoryBeforeSearch() {
        return prevPage;
    }

    /**
     * The fetcher for player icons
     *
     * @return The fetcher
     */
    public PlayerListElementFetcher getPlayerListElementFetcher() {
        return playerListElementFetcher;
    }

    /**
     * The player elements to search through
     *
     * @return The player map
     */
    public Map<String, OfflinePlayer> getPlayerMap() {
        return playerMap;
    }

}
