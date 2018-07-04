package me.killje.spigotgui.guielement;

import me.killje.spigotgui.util.GuiSetting;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public interface GuiElement extends InventoryElement {
    
    public ItemStack getItemStack(GuiSetting guiSettings);
    
}
