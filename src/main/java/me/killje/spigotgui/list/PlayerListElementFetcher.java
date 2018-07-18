package me.killje.spigotgui.list;

import me.killje.spigotgui.guielement.GuiElement;
import org.bukkit.OfflinePlayer;

/**
 * Interface for fetching a gui element for a given player
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public interface PlayerListElementFetcher {

    /**
     * Gets the icon for the given player
     *
     * @param offlinePlayer The player to get the icon for
     *
     * @return The icon for the given player
     */
    public GuiElement getGuiElement(OfflinePlayer offlinePlayer);

}
