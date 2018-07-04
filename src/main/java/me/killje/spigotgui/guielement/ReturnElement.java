package me.killje.spigotgui.guielement;

import me.killje.spigotgui.util.GuiSettings;
import me.killje.spigotgui.util.InventoryUtil;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class ReturnElement implements GuiElement {
    
    private final InventoryUtil prevInventory;

    public ReturnElement(InventoryUtil prevInventory) {
        this.prevInventory = prevInventory;
    }

    @Override
    public ItemStack getItemStack(GuiSettings guiSettings) {
        return guiSettings.getItemStack("back");
    }

    @Override
    public void onInventoryClickEvent(InventoryUtil currentInventoryUtils, InventoryClickEvent event) {
        currentInventoryUtils.openNewInventory(event.getWhoClicked(), prevInventory);
    }
    
}
