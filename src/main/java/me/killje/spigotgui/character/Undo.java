package me.killje.spigotgui.character;

import me.killje.spigotgui.guielement.GuiElement;
import me.killje.spigotgui.util.GuiSettings;
import me.killje.spigotgui.util.InventoryUtil;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class Undo implements GuiElement {

    private final AbstractStorage abstractStorage;
    private final String guiSettingName;
    
    public Undo(AbstractStorage abstractStorage, String guiSettingName) {
        this.abstractStorage = abstractStorage;
        this.guiSettingName = guiSettingName;
    }
    
    @Override
    public ItemStack getItemStack(GuiSettings guiSettings) {
        return guiSettings.getItemStack(guiSettingName);
    }

    @Override
    public void onInventoryClickEvent(InventoryUtil currentInventoryUtils, InventoryClickEvent event) {
        abstractStorage.removeLast();
    }
    
}
