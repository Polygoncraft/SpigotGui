package me.killje.spigotgui.list;

import me.killje.spigotgui.guielement.GuiElement;
import me.killje.spigotgui.util.GuiSetting;
import me.killje.spigotgui.util.InventoryBase;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class PrevPage implements GuiElement {

    private final Listable list;

    public PrevPage(Listable list) {
        this.list = list;
    }

    @Override
    public void onInventoryClickEvent(InventoryBase currentinventoryBase, InventoryClickEvent event) {
        list.previousPage();
    }

    @Override
    public ItemStack getItemStack(GuiSetting guiSettings) {
        return guiSettings.getItemStack("page.prev");
    }

}
