package me.killje.spigotgui.character;

import me.killje.spigotgui.guielement.GuiElement;
import me.killje.spigotgui.util.GuiSetting;
import me.killje.spigotgui.util.InventoryBase;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * A character icon used as a keyboard element
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class Character implements GuiElement {

    /**
     * The character this icon represents
     */
    private final char character;
    /**
     * The storage of where to add the character to if this icon is clicked
     */
    private final KeyBoardStringStorage keyBoardStringStorage;

    /**
     * Creates a icon for a character
     *
     * @param character             The character this represents
     * @param keyBoardStringStorage The storage to add the character to if
     *                              clicked
     */
    public Character(char character,
            KeyBoardStringStorage keyBoardStringStorage) {

        this.character = character;
        this.keyBoardStringStorage = keyBoardStringStorage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStack getItemStack(GuiSetting guiSettings) {
        return guiSettings.getItemStack("alphaNum." + character);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onInventoryClickEvent(InventoryBase currentinventoryBase,
            InventoryClickEvent event) {

        keyBoardStringStorage.add(character + "");
    }

}
