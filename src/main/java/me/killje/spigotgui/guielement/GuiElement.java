package me.killje.spigotgui.guielement;

import me.killje.spigotgui.util.GuiSetting;
import org.bukkit.inventory.ItemStack;

/**
 * A icon for a inventory
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public interface GuiElement extends InventoryElement {

    /**
     * The item stack that represents the icon in the inventory menu
     *
     * @param guiSettings The gui file to get icon information from
     *
     * @return The ItemsStack representing the GuiElement
     */
    public ItemStack getItemStack(GuiSetting guiSettings);

}
