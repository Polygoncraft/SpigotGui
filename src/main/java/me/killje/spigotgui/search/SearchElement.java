package me.killje.spigotgui.search;

import java.util.Map;
import me.killje.spigotgui.guielement.GuiElement;
import me.killje.spigotgui.util.GuiSetting;
import me.killje.spigotgui.util.InventoryBase;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class SearchElement implements GuiElement {

    private final Map<String, ? extends GuiElement> searchables;
    private final InventoryBase prevInventory;

    public SearchElement(Map<String, ? extends GuiElement> searchables) {
        this(searchables, null);
    }

    public SearchElement(Map<String, ? extends GuiElement> searchables, InventoryBase prevInventory) {
        this.searchables = searchables;
        this.prevInventory = prevInventory;
    }

    @Override
    public ItemStack getItemStack(GuiSetting guiSettings) {
        return guiSettings.getItemStack("search");
    }

    @Override
    public void onInventoryClickEvent(InventoryBase currentinventoryBase, InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        Search search = new Search(currentinventoryBase.getGuiSettings(), player, searchables);
        if (prevInventory != null) {
            search.setPrevInventory(prevInventory);
        }
        currentinventoryBase.openNewInventory(player, search);
    }

}
