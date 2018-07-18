package me.killje.spigotgui.search;

import java.util.Map;
import me.killje.spigotgui.guielement.GuiElement;
import me.killje.spigotgui.util.InventoryBase;

/**
 * Interface to use for searchable inventories
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public interface Searchable {

    /**
     * A boolean to disable a search in a searchable inventory
     *
     * @return True if you want to show the search icon, false otherwise
     */
    public default boolean showSearch() {
        return true;
    }

    /**
     * The elements to search through
     *
     * @return The elements
     */
    public abstract Map<String, ? extends GuiElement> getElementMap();

    /**
     * Gets the inventory before the search icon was clicked
     *
     * @return The previous inventory
     */
    public abstract InventoryBase InventoryBeforeSearch();

}
