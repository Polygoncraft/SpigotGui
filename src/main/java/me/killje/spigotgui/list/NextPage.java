package me.killje.spigotgui.list;

import me.killje.spigotgui.guielement.GuiElement;
import me.killje.spigotgui.util.GuiSetting;
import me.killje.spigotgui.util.InventoryBase;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Icon for going to the next page of a list
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class NextPage implements GuiElement {

    /**
     * The list to go to the next page to
     */
    private final Listable list;

    /**
     * Creates a icon for going to the next page for a list
     *
     * @param list The list to go to the next page on if clicked
     */
    public NextPage(Listable list) {
        this.list = list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStack getItemStack(GuiSetting guiSettings) {
        return guiSettings.getItemStack("page.next");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onInventoryClickEvent(InventoryBase currentinventoryBase,
            InventoryClickEvent event) {
        list.nextPage();
    }

}
