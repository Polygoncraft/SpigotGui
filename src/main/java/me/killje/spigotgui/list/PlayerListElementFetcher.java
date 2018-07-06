package me.killje.spigotgui.list;

import me.killje.spigotgui.guielement.GuiElement;
import org.bukkit.OfflinePlayer;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public interface PlayerListElementFetcher {

    public GuiElement getGuiElement(OfflinePlayer offlinePlayer);

}
