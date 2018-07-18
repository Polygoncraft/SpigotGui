package me.killje.spigotgui.character;

import me.killje.spigotgui.guielement.GuiElement;
import me.killje.spigotgui.util.GuiSetting;
import me.killje.spigotgui.util.InventoryBase;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Icon for setting the current string in the storage
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public abstract class SetStringButton implements GuiElement {

    /**
     * The current stored string
     */
    private KeyBoardStringStorage keyBoardStringStorage;

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStack getItemStack(GuiSetting guiSettings) {
        if (keyBoardStringStorage.getCurrent().equals("")) {
            return noNameYetItem(guiSettings);
        }
        return confirmItem(guiSettings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onInventoryClickEvent(InventoryBase currentinventoryBase,
            InventoryClickEvent event) {
        if (keyBoardStringStorage.getCurrent().equals("")) {
            event.getWhoClicked().sendMessage(textForEmpty(currentinventoryBase.
                    getGuiSettings()));
            return;
        }
        executeSet(currentinventoryBase, event);
    }

    /**
     * Icon for confirming the entered string
     *
     * @param guiSettings The settings file for where to retrieve the icon for
     *
     * @return The icon for the confirm item
     */
    protected abstract ItemStack confirmItem(GuiSetting guiSettings);

    /**
     * Action to take when clicking the icon
     *
     * @param currentinventoryBase The inventory this is clicked from
     * @param event                The click event that happened
     */
    protected abstract void executeSet(InventoryBase currentinventoryBase,
            InventoryClickEvent event);

    /**
     * Get the string storage with the current entered string
     *
     * @return The storage
     */
    protected KeyBoardStringStorage getKeyBoardStringStorage() {
        return keyBoardStringStorage;
    }

    /**
     * Sets the storage for the keyboard
     * 
     * @param keyBoardStringStorage The storage
     */
    public void setKeyBoardStringStorage(
            KeyBoardStringStorage keyBoardStringStorage) {
        this.keyBoardStringStorage = keyBoardStringStorage;
    }

    /**
     * The icon for when nothing is entered yet in the storage
     * 
     * @param guiSettings The gui file for where to get the icon from
     * 
     * @return The icon for when nothing is entered yet
     */
    protected abstract ItemStack noNameYetItem(GuiSetting guiSettings);

    /**
     * The text to display when no text is entered yet
     * 
     * @param guiSettings The gui file to load the text from
     * 
     * @return The string for when the storage is empty
     */
    protected abstract String textForEmpty(GuiSetting guiSettings);

}
