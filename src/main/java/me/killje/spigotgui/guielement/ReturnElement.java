package me.killje.spigotgui.guielement;

import me.killje.spigotgui.util.GuiSetting;
import me.killje.spigotgui.util.InventoryBase;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class ReturnElement implements GuiElement {

    private final InventoryBase prevInventory;

    public ReturnElement(InventoryBase prevInventory) {
        this.prevInventory = prevInventory;
    }

    @Override
    public ItemStack getItemStack(GuiSetting guiSettings) {
        return guiSettings.getItemStack("back");
    }

    @Override
    public void onInventoryClickEvent(InventoryBase currentinventoryBase, InventoryClickEvent event) {
        currentinventoryBase.openNewInventory(event.getWhoClicked(), prevInventory);
    }

}
