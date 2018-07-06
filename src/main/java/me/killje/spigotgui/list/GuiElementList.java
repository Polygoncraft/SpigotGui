package me.killje.spigotgui.list;

import java.util.ArrayList;
import java.util.Map;
import me.killje.spigotgui.guielement.GuiElement;
import me.killje.spigotgui.search.SearchElement;
import me.killje.spigotgui.util.GuiSetting;
import org.bukkit.entity.Player;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public abstract class GuiElementList extends List {

    public GuiElementList(GuiSetting guiSettings, Player currentPlayer) {
        super(guiSettings, currentPlayer);
    }

    @Override
    protected int initInventory(int startIndex, int stopIndex, int maxItemsOnPage) {
        Map<String, ? extends GuiElement> elementMap = getElementMap();
        if (addSearch()) {
            this.addGuiElement(new SearchElement(elementMap, this));
        }
        java.util.List<? extends GuiElement> guiElements = new ArrayList<>(elementMap.values());
        this.nextRow();
        for (int i = startIndex; i < stopIndex && i < guiElements.size(); i++) {
            this.addGuiElement(guiElements.get(i));
        }

        return guiElements.size();
    }
    
    protected boolean addSearch() {
        return true;
    }

    protected abstract Map<String, ? extends GuiElement> getElementMap();

}
