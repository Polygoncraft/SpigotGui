package me.killje.spigotgui.search;

import java.util.Map;
import me.killje.spigotgui.guielement.GuiElement;
import me.killje.spigotgui.util.GuiSetting;
import me.killje.spigotgui.util.InventoryUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class SearchElement implements GuiElement {
    
    private final Map<String, ? extends GuiElement> searchables;
    private final InventoryUtil prevInventory;

    public SearchElement(Map<String, ? extends GuiElement> searchables) {
        this(searchables, null);
    }
    
    public SearchElement(Map<String, ? extends GuiElement> searchables, InventoryUtil prevInventory) {
        this.searchables = searchables;
        this.prevInventory = prevInventory;
    }
    
    @Override
    public ItemStack getItemStack(GuiSetting guiSettings) {
        return guiSettings.getItemStack("search");
    }

    @Override
    public void onInventoryClickEvent(InventoryUtil currentInventoryUtils, InventoryClickEvent event) {
        
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        
        Search search = new Search(currentInventoryUtils.getGuiSettings(), player, searchables);
        if (prevInventory != null) {
            search.setPrevInventory(prevInventory);
        }
        currentInventoryUtils.openNewInventory(player, search);
    }
    
}
