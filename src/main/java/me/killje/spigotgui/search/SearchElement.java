package me.killje.spigotgui.search;

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

    private final Searchable searchable;

    public SearchElement(Searchable searchable) {
        this.searchable = searchable;
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

        Search search = new Search(currentinventoryBase.getGuiSettings(), player, searchable.getElementMap());
        if (searchable.InventoryBeforeSearch() != null) {
            search.setPrevInventory(searchable.InventoryBeforeSearch());
        }
        currentinventoryBase.openNewInventory(player, search);
    }

}
