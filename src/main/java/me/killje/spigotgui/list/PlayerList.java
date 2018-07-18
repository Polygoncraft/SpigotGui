package me.killje.spigotgui.list;

import java.util.ArrayList;
import java.util.Map;
import me.killje.spigotgui.guielement.SimpleGuiElement;
import me.killje.spigotgui.search.SearchElement;
import me.killje.spigotgui.util.GuiSetting;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.scheduler.BukkitTask;

/**
 * A searchable list of players, first players who are online and then who have
 * ever been on the server
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class PlayerList extends List {

    /**
     * List of offline players to display
     */
    private final java.util.List<OfflinePlayer> offlinePlayerList;
    /**
     * List of online players to display
     */
    private final java.util.List<OfflinePlayer> onlinePlayers
            = new ArrayList<>();
    /**
     * Fetcher for heads of players and executing click events on
     */
    private final PlayerListElementFetcher playerListGuiElement;
    /**
     * Search element to search for players
     */
    private final SearchElement searchElement;

    /**
     * A inventory with searchable players. First online player and then all
     * players
     *
     * @param guiSettings          The gui file to load icons from
     * @param currentPlayer        The player using the inventory
     * @param playerListGuiElement The element fetcher for getting heads
     */
    public PlayerList(GuiSetting guiSettings, Player currentPlayer,
            PlayerListElementFetcher playerListGuiElement) {

        super(guiSettings, currentPlayer);
        this.playerListGuiElement = playerListGuiElement;

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayers.add(onlinePlayer);
        }

        offlinePlayerList = guiSettings.getPluginUtil().getOfflinePlayerList();

        Map<String, OfflinePlayer> offlinePlayers
                = guiSettings.getPluginUtil().getOfflinePlayerMap();

        this.searchElement = new SearchElement(new PlayerListSearcher(
                offlinePlayers, playerListGuiElement, this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attachListener() {
        getPluginUtil().registerEvents(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeInventory(HumanEntity humanEntity, boolean isClosed) {
        InventoryOpenEvent.getHandlerList().unregister(this);
        super.closeInventory(humanEntity, isClosed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void openInventory(HumanEntity humanEntity) {

        BukkitTask runTaskAsynchronously = guiSettings.getPluginUtil().
                runTaskAsynchronously(() -> {
                    try {
                        Thread.sleep(100);
                        humanEntity.sendMessage(
                                "Loading heads."
                                + " This can take a while the first time");
                    } catch (InterruptedException ex) {
                        // Thread interupted, player has inventory open
                    }
                });

        super.openInventory(humanEntity);

        runTaskAsynchronously.cancel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getInventoryName() {
        return guiSettings.getText("addPlayer");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int initInventory(int startIndex, int stopIndex,
            int maxItemsOnPage) {

        addGuiElement(searchElement);

        int onlineInventorySize = onlinePlayers.isEmpty() ? 0 : onlinePlayers.
                size() + 9 + (8 - (onlinePlayers.size() - 1) % 9);

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
            this.addGuiElement(new SimpleGuiElement(
                    guiSettings.getItemStack("allPlayerList")
            ), 4);

            this.nextRow();

            for (OfflinePlayer offlinePlayer : offlinePlayerList.subList(
                    offlineStartLocation, offlineStopLocation)) {

                this.addGuiElement(
                        playerListGuiElement.getGuiElement(offlinePlayer)
                );
            }
        } else {
            this.addGuiElement(new SimpleGuiElement(
                    guiSettings.getItemStack("onlinePlayerList")
            ), 4);

            this.nextRow();

            int toIndex = stopIndex;
            if (toIndex > onlinePlayers.size()) {
                toIndex = onlinePlayers.size();
            }

            for (OfflinePlayer offlinePlayer : onlinePlayers.subList(startIndex,
                    toIndex)) {

                this.addGuiElement(
                        playerListGuiElement.getGuiElement(offlinePlayer)
                );
            }

            if (stopIndex - onlineInventorySize > 9) {

                this.addGuiElement(new SimpleGuiElement(
                        guiSettings.getItemStack("allPlayerFiller")
                ), (onlineInventorySize % maxItemsOnPage));

                this.addGuiElement(new SimpleGuiElement(
                        guiSettings.getItemStack("allPlayerFiller")
                ), (onlineInventorySize % maxItemsOnPage) + 1);

                this.addGuiElement(new SimpleGuiElement(
                        guiSettings.getItemStack("allPlayerFiller")
                ), (onlineInventorySize % maxItemsOnPage) + 2);

                this.addGuiElement(new SimpleGuiElement(
                        guiSettings.getItemStack("allPlayerFiller")
                ), (onlineInventorySize % maxItemsOnPage) + 3);

                this.addGuiElement(new SimpleGuiElement(
                        guiSettings.getItemStack("allPlayerList")
                ), (onlineInventorySize % maxItemsOnPage) + 4);

                this.addGuiElement(new SimpleGuiElement(
                        guiSettings.getItemStack("allPlayerFiller")
                ), (onlineInventorySize % maxItemsOnPage) + 5);

                this.addGuiElement(new SimpleGuiElement(
                        guiSettings.getItemStack("allPlayerFiller")
                ), (onlineInventorySize % maxItemsOnPage) + 6);

                this.addGuiElement(new SimpleGuiElement(
                        guiSettings.getItemStack("allPlayerFiller")
                ), (onlineInventorySize % maxItemsOnPage) + 7);

                this.addGuiElement(new SimpleGuiElement(
                        guiSettings.getItemStack("allPlayerFiller")
                ), (onlineInventorySize % maxItemsOnPage) + 8);

                for (OfflinePlayer offlinePlayer : offlinePlayerList.subList(
                        offlineStartLocation, offlineStopLocation
                        - offlineStartLocation)) {

                    this.addGuiElement(
                            playerListGuiElement.getGuiElement(offlinePlayer)
                    );

                }

            }
        }

        return totalSize;

    }

}
