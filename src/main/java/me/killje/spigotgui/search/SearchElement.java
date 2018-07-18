package me.killje.spigotgui.search;

import me.killje.spigotgui.guielement.GuiElement;
import me.killje.spigotgui.list.PlayerListSearcher;
import me.killje.spigotgui.util.GuiSetting;
import me.killje.spigotgui.util.InventoryBase;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * A icon that when clicked will open a search inventory
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class SearchElement implements GuiElement {

    /**
     * The searcher for player searching.
     *
     * This of searchable needs to be set
     */
    private final PlayerListSearcher playerListSearcher;
    /**
     * The map to be used for searching
     *
     * This or playerListSearcher needs to be set
     */
    private final Searchable searchable;

    /**
     * Creates a icon that when clicked opens a inventory to search items
     *
     * @param searchable The map to search through
     */
    public SearchElement(Searchable searchable) {
        this.searchable = searchable;
        playerListSearcher = null;
    }

    /**
     * Creates a icon that when clicked opens a inventory to search players
     *
     * @param playerListSearcher The players to search through
     */
    public SearchElement(PlayerListSearcher playerListSearcher) {
        this.searchable = null;
        this.playerListSearcher = playerListSearcher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStack getItemStack(GuiSetting guiSettings) {
        return guiSettings.getItemStack("search");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onInventoryClickEvent(InventoryBase currentinventoryBase,
            InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        Search search;
        if (searchable != null) {
            search = new Search(currentinventoryBase.getGuiSettings(), player,
                    searchable.getElementMap());

            if (searchable.InventoryBeforeSearch() != null) {
                search.setPrevInventory(searchable.InventoryBeforeSearch());
            }
        } else {
            search = new Search(currentinventoryBase.getGuiSettings(), player,
                    playerListSearcher);
        }

        currentinventoryBase.openNewInventory(player, search);
    }

}
