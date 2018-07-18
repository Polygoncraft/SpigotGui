package me.killje.spigotgui.guielement;

import me.killje.spigotgui.util.GuiSetting;
import me.killje.spigotgui.util.InventoryBase;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * A icon to return to a previous inventory screen
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class ReturnElement implements GuiElement {

    /**
     * The previous inventory
     */
    private final InventoryBase previousInventory;

    /**
     * Creates a icon for returning to the previous inventory
     *
     * @param previousInventory The previous inventory to return to when clicked
     */
    public ReturnElement(InventoryBase previousInventory) {
        this.previousInventory = previousInventory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStack getItemStack(GuiSetting guiSettings) {
        return guiSettings.getItemStack("back");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onInventoryClickEvent(InventoryBase currentinventoryBase,
            InventoryClickEvent event) {
        currentinventoryBase.openNewInventory(event.getWhoClicked(),
                previousInventory);
    }

}
