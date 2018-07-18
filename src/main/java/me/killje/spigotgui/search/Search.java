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
import me.killje.spigotgui.list.PlayerListElementFetcher;
import me.killje.spigotgui.list.PlayerListSearcher;
import me.killje.spigotgui.list.PrevPage;
import me.killje.spigotgui.util.GuiSetting;
import me.killje.spigotgui.util.InventoryBase;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * A inventory to search for a element. This has a keyboard to enter a name and
 * a row with results
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class Search extends KeyBoard implements Listable {

    /**
     * The current page of the search
     */
    private int page = 0;
    /**
     * The player using the inventory
     */
    private final Player player;
    /**
     * The searcher to use for searching through player elements, this or
     * searchable needs to be set
     */
    private final PlayerListSearcher playerListSearcher;
    /**
     * The inventory before searching
     */
    private InventoryBase prevInventory = null;
    /**
     * The map to search through, this or playerListSearcher needs to be set
     */
    private final Map<String, ? extends GuiElement> searchable;

    /**
     * Creates a inventory to use for searching of elements
     *
     * @param guiSettings The gui file to use to get icons from file
     * @param player      The player using the inventory
     * @param searchable  The map to use as search
     */
    public Search(GuiSetting guiSettings, Player player,
            Map<String, ? extends GuiElement> searchable) {
        super(guiSettings, player, 6);
        this.searchable = searchable;
        this.playerListSearcher = null;
        this.player = player;
    }

    /**
     * Creates a inventory to use for searching of players
     *
     * @param guiSettings        The gui file to get information from file
     * @param player             The player using the inventory
     * @param playerListSearcher The searcher for players
     */
    public Search(GuiSetting guiSettings, Player player,
            PlayerListSearcher playerListSearcher) {
        super(guiSettings, player, 6);
        this.searchable = null;
        this.playerListSearcher = playerListSearcher;
        prevInventory = playerListSearcher.InventoryBeforeSearch();
        this.player = player;
    }

    /**
     * Get the matches that matches the given search string
     *
     * @param searchKey The string to search for in the list
     *
     * @return The matches matching the string
     */
    private List<GuiElement> getMatches(String searchKey) {
        searchKey = searchKey.toLowerCase();
        List<GuiElement> matches = new ArrayList<>();

        if (searchable != null) {
            for (Map.Entry<String, ? extends GuiElement> entry : searchable.
                    entrySet()) {
                String key = entry.getKey();
                GuiElement value = entry.getValue();

                if (!key.toLowerCase().matches(".*" + searchKey + ".*")) {
                    continue;
                }

                matches.add(value);
            }
        } else if (playerListSearcher != null) {
            PlayerListElementFetcher playerListElementFetcher
                    = playerListSearcher.getPlayerListElementFetcher();

            for (Map.Entry<String, OfflinePlayer> entry : playerListSearcher.
                    getPlayerMap().entrySet()) {
                String key = entry.getKey();
                OfflinePlayer value = entry.getValue();

                if (!key.toLowerCase().matches(".*" + searchKey + ".*")) {
                    continue;
                }

                matches.add(playerListElementFetcher.getGuiElement(value));

            }
        }
        return matches;
    }

    /**
     * Sets the previous inventory to return to when the return icon is clicked
     *
     * If no inventory is set the icon will not appear
     *
     * @param prevInventory The previous inventory
     */
    public void setPrevInventory(InventoryBase prevInventory) {
        this.prevInventory = prevInventory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void nextPage() {
        page++;
        updateInventory(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStorageupdateEvent() {
        page = 0;
        super.onStorageupdateEvent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void previousPage() {
        page--;
        updateInventory(player);
    }

    /**
     * The name for the text on top of the inventory from the gui file
     *
     * @return The gui text name
     */
    protected String getGuiSettingsName() {
        return "search";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initInventory() {
        super.initInventory();

        int maxItemsOnPage = 9; // 5 * 9

        int startIndex = page * maxItemsOnPage;
        int stopIndex = (page + 1) * maxItemsOnPage;

        String searchKey
                = getKeyBoardStringStorage().getCurrent().toLowerCase();

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
        replaceList.put("LAST_PAGE", ((matches.size() - 1) / maxItemsOnPage + 1)
                + "");

        this.setInventoryName(guiSettings.getText(getGuiSettingsName(),
                replaceList));
    }

}
