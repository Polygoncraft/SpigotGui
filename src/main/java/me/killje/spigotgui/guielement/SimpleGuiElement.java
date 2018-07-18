package me.killje.spigotgui.guielement;

import me.killje.spigotgui.util.GuiSetting;
import me.killje.spigotgui.util.InventoryBase;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * A simple icon that does nothing when clicked on
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class SimpleGuiElement implements GuiElement {

    /**
     * The icon to show in the inventory
     */
    private final ItemStack element;
    /**
     * The name for the icon
     *
     * This is optional
     */
    private final String name;

    /**
     * Creates a icon from the item stack
     *
     * @param element The icon to show
     */
    public SimpleGuiElement(ItemStack element) {
        this(element, null);
    }

    /**
     * Creates a icon from the item stack with the given name
     *
     * @param element The icon to show
     * @param name    The name for the icon
     */
    public SimpleGuiElement(ItemStack element, String name) {
        this.element = element;
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStack getItemStack(GuiSetting guiSettings) {
        if (name != null) {
            ItemMeta itemMeta = element.getItemMeta();
            itemMeta.setDisplayName(name);
            element.setItemMeta(itemMeta);
        }
        return element;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onInventoryClickEvent(InventoryBase currentinventoryBase,
            InventoryClickEvent event) {
    }

}
