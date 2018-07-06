package me.killje.spigotgui.guielement;

import me.killje.spigotgui.util.GuiSetting;
import me.killje.spigotgui.util.InventoryBase;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class Exit implements GuiElement {

    @Override
    public ItemStack getItemStack(GuiSetting guiSettings) {
        return guiSettings.getItemStack("exit");
    }

    @Override
    public void onInventoryClickEvent(InventoryBase currentinventoryBase, InventoryClickEvent event) {
        currentinventoryBase.closeInventory(event.getWhoClicked());
    }

}
