package me.killje.spigotgui.search;

import java.util.Map;
import me.killje.spigotgui.guielement.GuiElement;
import me.killje.spigotgui.util.InventoryBase;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public interface Searchable {

    public default boolean showSearch() {
        return true;
    }

    public abstract Map<String, ? extends GuiElement> getElementMap();
    public abstract InventoryBase InventoryBeforeSearch();

}
