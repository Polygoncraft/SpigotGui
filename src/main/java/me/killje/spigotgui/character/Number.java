package me.killje.spigotgui.character;

import me.killje.spigotgui.guielement.GuiElement;
import me.killje.spigotgui.util.GuiSetting;
import me.killje.spigotgui.util.InventoryBase;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * A number icon for use on a number board
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class Number implements GuiElement {

    /**
     * The storage for a number
     */
    private final AmountStorage amountStorage;
    /**
     * The number this icon represents
     */
    private final int number;

    /**
     * Creates a icon for the given number
     *
     * @param number        The number this icon represents
     * @param amountStorage The storage to add to when clicked
     */
    public Number(int number, AmountStorage amountStorage) {
        this.number = number;
        this.amountStorage = amountStorage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStack getItemStack(GuiSetting guiSettings) {
        return guiSettings.getItemStack("alphaNum." + number);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onInventoryClickEvent(InventoryBase currentinventoryBase,
            InventoryClickEvent event) {
        amountStorage.add(number);
    }

}
