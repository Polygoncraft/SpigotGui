package me.killje.spigotgui.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.killje.spigotgui.character.KeyBoard;
import me.killje.spigotgui.guielement.GuiElement;
import me.killje.spigotgui.guielement.ReturnElement;
import me.killje.spigotgui.list.Listable;
import me.killje.spigotgui.list.NextPage;
import me.killje.spigotgui.list.PrevPage;
import me.killje.spigotgui.util.GuiSetting;
import me.killje.spigotgui.util.InventoryUtil;
import org.bukkit.entity.Player;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class Search extends KeyBoard implements Listable {
    
    private final Map<String, ? extends GuiElement> searchable;
    private InventoryUtil prevInventory = null;
    private final Player player;
    private int page = 0;
    
    public Search(GuiSetting guiSettings, Player player, Map<String, ? extends GuiElement> searchable) {
        super(guiSettings, player, 6);
        this.searchable = searchable;
        this.player = player;
    }
    
    @Override
    protected void initInventory() {
        super.initInventory();
        
        int maxItemsOnPage = 9; // 5 * 9
        
        int startIndex = page * maxItemsOnPage;
        int stopIndex = (page + 1) * maxItemsOnPage;
        
        String searchKey = getKeyBoardStringStorage().getCurrent().toLowerCase();
        
        List<GuiElement> matches = getMatches(searchKey);
        
        
        
        for (int i = startIndex; i < stopIndex && i < matches.size(); i++) {
            addGuiElement(matches.get(i));
        }
        
        if (page != 0) {
            this.addGuiElement(new PrevPage(this), 3);
        }
        
        if (matches.size() > stopIndex) {
            this.addGuiElement(new NextPage(this), 5);
        }
        
        if (prevInventory != null) {
            this.addGuiElement(new ReturnElement(prevInventory), 7);
        }
        
        Map<String, String> replaceList = new HashMap<>();
        replaceList.put("SEARCH", getInventoryName());
        replaceList.put("MATCHES", "" + matches.size());
        replaceList.put("CURRENT_PAGE", (page + 1) + "");
        replaceList.put("LAST_PAGE", ((matches.size() - 1)/maxItemsOnPage + 1) + "");
        
        this.setInventoryName(guiSettings.getText(getGuiSettingsName(), replaceList));
    }

    public void setPrevInventory(InventoryUtil prevInventory) {
        this.prevInventory = prevInventory;
    }
    
    protected String getGuiSettingsName() {
        return "search";
    }
    
    private List<GuiElement> getMatches(String searchKey) {
        searchKey = searchKey.toLowerCase();
        List<GuiElement> matches = new ArrayList<>();
        
        for (Map.Entry<String, ? extends GuiElement> entry : searchable.entrySet()) {
            String key = entry.getKey();
            GuiElement value = entry.getValue();
            
            if (!key.toLowerCase().matches(".*" + searchKey + ".*")) {
                continue;
            }
            
            matches.add(value);
        }
        return matches;
    }
    
    @Override
    public void onStorageupdateEvent() {
        page = 0;
        super.onStorageupdateEvent();
    }
    
    @Override
    public void nextPage() {
        page++;
        updateInventory(player);
    }
    
    @Override
    public void previousPage() {
        page--;
        updateInventory(player);
    }
    
    
}
