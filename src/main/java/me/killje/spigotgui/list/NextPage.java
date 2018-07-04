package me.killje.spigotgui.list;

import me.killje.spigotgui.guielement.GuiElement;
import me.killje.spigotgui.util.GuiSettings;
import me.killje.spigotgui.util.InventoryUtil;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class NextPage implements GuiElement {
    
    private final Listable list;
    
    public NextPage(Listable list) {
        this.list = list;
    }

    @Override
    public void onInventoryClickEvent(InventoryUtil currentInventoryUtils, InventoryClickEvent event) {
        list.nextPage();
    }

    @Override
    public ItemStack getItemStack(GuiSettings guiSettings) {
        return guiSettings.getItemStack("page.next");
    }
    
    
    
}
