package me.killje.spigotgui.guielement;

import me.killje.spigotgui.util.GuiSettings;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public interface GuiElement extends InventoryElement {
    
    public ItemStack getItemStack(GuiSettings guiSettings);
    
}
