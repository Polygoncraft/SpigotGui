package me.killje.spigotgui.list;

import java.util.ArrayList;
import java.util.Map;
import me.killje.spigotgui.guielement.GuiElement;
import me.killje.spigotgui.search.SearchElement;
import me.killje.spigotgui.search.Searchable;
import me.killje.spigotgui.util.GuiSetting;
import me.killje.spigotgui.util.InventoryBase;
import org.bukkit.entity.Player;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public abstract class GuiElementList extends List implements Searchable {

    public GuiElementList(GuiSetting guiSettings, Player currentPlayer) {
        super(guiSettings, currentPlayer);
    }

    @Override
    protected int initInventory(int startIndex, int stopIndex, int maxItemsOnPage) {
        Map<String, ? extends GuiElement> elementMap = getElementMap();
        if (showSearch()) {
            this.addGuiElement(new SearchElement(this));
        }
        java.util.List<? extends GuiElement> guiElements = new ArrayList<>(elementMap.values());
        this.nextRow();
        for (int i = startIndex; i < stopIndex && i < guiElements.size(); i++) {
            this.addGuiElement(guiElements.get(i));
        }

        return guiElements.size();
    }

    @Override
    public InventoryBase InventoryBeforeSearch() {
        return this;
    }

}
