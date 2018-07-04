package me.killje.spigotgui.guielement;

import me.killje.spigotgui.util.InventoryUtil;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public interface InventoryElement {

    public void onInventoryClickEvent(InventoryUtil currentInventoryUtils, InventoryClickEvent event);

}
