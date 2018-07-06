package me.killje.spigotgui.guielement;

import me.killje.spigotgui.util.InventoryBase;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public interface InventoryElement {

    public void onInventoryClickEvent(InventoryBase currentInventoryBase, InventoryClickEvent event);

}
