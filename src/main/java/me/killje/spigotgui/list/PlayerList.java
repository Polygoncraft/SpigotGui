package me.killje.spigotgui.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import me.killje.spigotgui.guielement.GuiElement;
import me.killje.spigotgui.guielement.SimpleGuiElement;
import me.killje.spigotgui.search.SearchElement;
import me.killje.spigotgui.search.Searchable;
import me.killje.spigotgui.util.GuiSetting;
import me.killje.spigotgui.util.InventoryBase;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class PlayerList extends List implements Searchable {

    private final java.util.List<GuiElement> onlinePlayers = new ArrayList<>();
    private final Map<String, GuiElement> offlinePlayers = new HashMap<>();
    private final java.util.List<GuiElement> offlinePlayerList;

    private final SearchElement searchElement;

    public PlayerList(GuiSetting guiSettings, Player currentPlayer, PlayerListElementFetcher playerListGuiElement) {
        super(guiSettings, currentPlayer);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayers.add(playerListGuiElement.getGuiElement(onlinePlayer));
        }

        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            GuiElement playerElement = playerListGuiElement.getGuiElement(offlinePlayer);
            offlinePlayers.put(offlinePlayer.getName(), playerElement);
        }
        
        offlinePlayerList = new ArrayList<>(offlinePlayers.values());
        this.searchElement = new SearchElement(this);
    }

    @Override
    protected int initInventory(int startIndex, int stopIndex, int maxItemsOnPage) {

        addGuiElement(searchElement);

        int onlineInventorySize = onlinePlayers.isEmpty() ? 0 : onlinePlayers.size() + 9 + (8 - (onlinePlayers.size() - 1) % 9);

        int totalSize = offlinePlayerList.size() + onlineInventorySize;

        int offlineStartLocation = startIndex - onlineInventorySize;
        if (offlineStartLocation < 0) {
            offlineStartLocation = 0;
        }

        int offlineStopLocation = stopIndex - onlineInventorySize;
        if (offlineStopLocation < 0) {
            offlineStopLocation = 0;
        }
        if (offlineStopLocation > offlinePlayerList.size()) {
            offlineStopLocation = offlinePlayerList.size();
        }

        if (onlinePlayers.size() < startIndex) {
            this.addGuiElement(new SimpleGuiElement(guiSettings.getItemStack("allPlayerList")), 4);
            this.nextRow();

            for (GuiElement offlinePlayer : offlinePlayerList.subList(offlineStartLocation, offlineStopLocation)) {
                this.addGuiElement(offlinePlayer);
            }
        } else {
            this.addGuiElement(new SimpleGuiElement(guiSettings.getItemStack("onlinePlayerList")), 4);
            this.nextRow();

            int toIndex = stopIndex;
            if (toIndex > onlinePlayers.size()) {
                toIndex = onlinePlayers.size();
            }

            for (GuiElement offlinePlayer : onlinePlayers.subList(startIndex, toIndex)) {
                this.addGuiElement(offlinePlayer);
            }

            if (stopIndex - onlineInventorySize > 9) {
                this.addGuiElement(new SimpleGuiElement(guiSettings.getItemStack("allPlayerFiller")), (onlineInventorySize % maxItemsOnPage));
                this.addGuiElement(new SimpleGuiElement(guiSettings.getItemStack("allPlayerFiller")), (onlineInventorySize % maxItemsOnPage) + 1);
                this.addGuiElement(new SimpleGuiElement(guiSettings.getItemStack("allPlayerFiller")), (onlineInventorySize % maxItemsOnPage) + 2);
                this.addGuiElement(new SimpleGuiElement(guiSettings.getItemStack("allPlayerFiller")), (onlineInventorySize % maxItemsOnPage) + 3);
                this.addGuiElement(new SimpleGuiElement(guiSettings.getItemStack("allPlayerList")), (onlineInventorySize % maxItemsOnPage) + 4);
                this.addGuiElement(new SimpleGuiElement(guiSettings.getItemStack("allPlayerFiller")), (onlineInventorySize % maxItemsOnPage) + 5);
                this.addGuiElement(new SimpleGuiElement(guiSettings.getItemStack("allPlayerFiller")), (onlineInventorySize % maxItemsOnPage) + 6);
                this.addGuiElement(new SimpleGuiElement(guiSettings.getItemStack("allPlayerFiller")), (onlineInventorySize % maxItemsOnPage) + 7);
                this.addGuiElement(new SimpleGuiElement(guiSettings.getItemStack("allPlayerFiller")), (onlineInventorySize % maxItemsOnPage) + 8);

                for (GuiElement offlinePlayer : offlinePlayerList.subList(offlineStartLocation, offlineStopLocation - offlineStartLocation)) {
                    this.addGuiElement(offlinePlayer);
                }

            }
        }

        return totalSize;

    }

    @Override
    protected String getInventoryName() {
        return guiSettings.getText("addPlayer");
    }

    @Override
    public void closeInventory(HumanEntity humanEntity, boolean isClosed) {
        InventoryOpenEvent.getHandlerList().unregister(this);
        super.closeInventory(humanEntity, isClosed);
    }

    @Override
    public void attachListener() {
        getPluginUtil().registerEvents(this);
    }

    @Override
    public void openInventory(HumanEntity humanEntity) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                    humanEntity.sendMessage("Loading heads. This can take a while the first time");
                } catch (InterruptedException ex) {
                    // Thread interupted, player has inventory open
                }
            }
        });
        thread.start();

        super.openInventory(humanEntity);

        thread.interrupt();
    }

    @Override
    public Map<String, ? extends GuiElement> getElementMap() {
        return offlinePlayers;
    }

    @Override
    public InventoryBase InventoryBeforeSearch() {
        return this;
    }

}
