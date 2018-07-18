package me.killje.spigotgui.guielement;

import me.killje.spigotgui.util.InventoryBase;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Inventory element that can handle being clicked on in a inventory
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public interface InventoryElement {

    /**
     * Event that is fired when this icon is clicked on
     *
     * @param currentInventoryBase The inventory this is fired from
     * @param event                The bukkit event that fired this event
     */
    public void onInventoryClickEvent(InventoryBase currentInventoryBase,
            InventoryClickEvent event);

}
