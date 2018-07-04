package me.killje.spigotgui.list;

import java.util.ArrayList;
import java.util.Map;
import me.killje.spigotgui.guielement.GuiElement;
import me.killje.spigotgui.search.SearchElement;
import me.killje.spigotgui.util.GuiSettings;
import org.bukkit.entity.Player;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public abstract class GuiElementList extends List {
    
    private final String inventoryName;

    public GuiElementList(GuiSettings guiSettings, Player currentPlayer, String inventoryName) {
        super(guiSettings, currentPlayer);
        this.inventoryName = inventoryName;
    }

    @Override
    protected int initInventory(int startIndex, int stopIndex, int maxItemsOnPage) {
        Map<String, ? extends GuiElement> elementMap = getElementMap();
        this.addGuiElement(new SearchElement(elementMap, this));
        java.util.List<? extends GuiElement> guiElements = new ArrayList<>(elementMap.values());
        this.nextRow();
        for (int i = startIndex; i < stopIndex && i < guiElements.size(); i++) {
            this.addGuiElement(guiElements.get(i));
        }
        
        return guiElements.size();
    }

    @Override
    protected String getInventoryName() {
        return inventoryName;
    }
    
    protected abstract Map<String, ? extends GuiElement> getElementMap();
    
}
