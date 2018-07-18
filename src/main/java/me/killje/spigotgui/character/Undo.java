package me.killje.spigotgui.character;

import me.killje.spigotgui.guielement.GuiElement;
import me.killje.spigotgui.util.GuiSetting;
import me.killje.spigotgui.util.InventoryBase;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Undo icon for boards that implements Abstract Storage
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class Undo implements GuiElement {

    /**
     * The storage to use when undoing a button
     */
    private final AbstractStorage abstractStorage;
    /**
     * The settings item to use for this icon
     */
    private final String guiSettingName;

    /**
     * Creates a icon to undo the last action on a storage
     *
     * @param abstractStorage The storage to undo the last option for
     * @param guiSettingName  The gui name to get the icon from
     */
    public Undo(AbstractStorage abstractStorage, String guiSettingName) {
        this.abstractStorage = abstractStorage;
        this.guiSettingName = guiSettingName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStack getItemStack(GuiSetting guiSettings) {
        return guiSettings.getItemStack(guiSettingName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onInventoryClickEvent(InventoryBase currentinventoryBase,
            InventoryClickEvent event) {
        abstractStorage.removeLast();
    }

}
