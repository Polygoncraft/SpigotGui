package me.killje.spigotgui.character;

import me.killje.spigotgui.guielement.GuiElement;
import me.killje.spigotgui.util.GuiSetting;
import me.killje.spigotgui.util.InventoryUtil;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class Character implements GuiElement {

    private final char character;
    private final KeyBoardStringStorage keyBoardStringStorage;

    public Character(char character, KeyBoardStringStorage keyBoardStringStorage) {
        this.character = character;
        this.keyBoardStringStorage = keyBoardStringStorage;
    }

    @Override
    public ItemStack getItemStack(GuiSetting guiSettings) {
        return guiSettings.getItemStack("alphaNum." + character);
    }

    @Override
    public void onInventoryClickEvent(InventoryUtil currentInventoryUtils, InventoryClickEvent event) {
        keyBoardStringStorage.add(character + "");
    }

}
