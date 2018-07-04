package me.killje.spigotgui.guielement;

import me.killje.spigotgui.util.GuiSettings;
import me.killje.spigotgui.util.InventoryUtil;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class Exit implements GuiElement {

    @Override
    public ItemStack getItemStack(GuiSettings guiSettings) {
        return guiSettings.getItemStack("exit");
    }

    @Override
    public void onInventoryClickEvent(InventoryUtil currentInventoryUtils, InventoryClickEvent event) {
        currentInventoryUtils.closeInventory(event.getWhoClicked());
    }
    
    
}
