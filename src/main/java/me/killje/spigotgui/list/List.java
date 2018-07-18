package me.killje.spigotgui.list;

import java.util.HashMap;
import java.util.Map;
import me.killje.spigotgui.guielement.Exit;
import me.killje.spigotgui.util.GuiSetting;
import me.killje.spigotgui.util.InventoryBase;
import org.bukkit.entity.Player;

/**
 * Inventory for a pageable list
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public abstract class List extends InventoryBase implements Listable {

    /**
     * The current page the list is on
     */
    private int page = 0;
    /**
     * The player using the list
     */
    private final Player player;

    /**
     * Creates a list that is pageable
     *
     * @param guiSettings   The gui file to get icons from
     * @param currentPlayer The current player using the inventory
     */
    public List(GuiSetting guiSettings, Player currentPlayer) {
        super(guiSettings, 6);
        this.player = currentPlayer;
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
    public void previousPage() {
        page--;
        updateInventory(player);
    }

    /**
     * Get inventory name of the current inventory
     *
     * @return The inventory name
     */
    protected abstract String getInventoryName();

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initInventory() {
        int maxItemsOnPage = 45; // 5 * 9

        int startIndex = page * maxItemsOnPage;
        int stopIndex = (page + 1) * maxItemsOnPage;

        int totalSize = initInventory(startIndex, stopIndex, maxItemsOnPage);

        if (page != 0) {
            this.addGuiElement(new PrevPage(this), 3);
        }

        if (totalSize > stopIndex) {
            this.addGuiElement(new NextPage(this), 5);
        }
        this.addGuiElement(new Exit(), 8);

        Map<String, String> replaceList = new HashMap<>();
        replaceList.put("INVENTORY_NAME", getInventoryName());
        replaceList.put("CURRENT_PAGE", (page + 1) + "");
        replaceList.
                put("LAST_PAGE", ((totalSize - 1) / maxItemsOnPage + 1) + "");

        this.setInventoryName(guiSettings.getText("list", replaceList));

    }

    /**
     * This is called when initializing the gui. Place in this your list code.
     *
     * @param startIndex     Start index of the current page
     * @param stopIndex      End index of the current page
     * @param maxItemsOnPage Max displayed items on the page
     *
     * @return The max items that are possible for this list
     */
    protected abstract int initInventory(int startIndex, int stopIndex,
            int maxItemsOnPage);

}
