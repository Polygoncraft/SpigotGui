package me.killje.spigotgui.character;

import me.killje.spigotgui.guielement.GuiElement;
import me.killje.spigotgui.util.GuiSetting;
import me.killje.spigotgui.util.InventoryBase;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class Number implements GuiElement {

    private final int number;
    private final AmountStorage amountStorage;

    public Number(int number, AmountStorage amountStorage) {
        this.number = number;
        this.amountStorage = amountStorage;
    }

    @Override
    public ItemStack getItemStack(GuiSetting guiSettings) {
        return guiSettings.getItemStack("alphaNum." + number);
    }

    @Override
    public void onInventoryClickEvent(InventoryBase currentinventoryBase, InventoryClickEvent event) {
        amountStorage.add(number);
    }

}
