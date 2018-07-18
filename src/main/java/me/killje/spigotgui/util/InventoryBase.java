package me.killje.spigotgui.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.killje.spigotgui.guielement.GuiElement;
import me.killje.spigotgui.guielement.InventoryElement;
import me.killje.spigotgui.util.HeadUtil.AsyncPlayerHead;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * The base for inventories with a lot of functionality to create inventories
 * with ease
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public abstract class InventoryBase implements Listener {

    /**
     * Enum for the kinds of inventories possible
     */
    public enum InventoryBaseType {

        /**
         * Dynamic inventory of 9 items per row up to 6 rows
         */
        CUSTOM(9, 54, false, InventoryType.CHEST),
        /**
         * Fixed inventory of 3 items per row and 3 rows
         */
        DROPPER(3, 9, true, InventoryType.DROPPER),
        /**
         * Fixed inventory of 3 items per row and 3 rows
         */
        DISPENSER(3, 9, true, InventoryType.DISPENSER),
        /**
         * Fixed inventory of 1 items per row and 3 rows
         */
        ANVIL(1, 3, true, InventoryType.ANVIL),
        /**
         * Fixed inventory of 1 items per row and 1 rows
         */
        BEACON(1, 1, true, InventoryType.BEACON),
        /**
         * Fixed inventory of 1 items per row and 5 rows
         */
        BREWING(1, 5, true, InventoryType.BREWING),
        /**
         * Fixed inventory of 1 items per row and 9 rows
         */
        CRAFTING(1, 9, true, InventoryType.CRAFTING),
        /**
         * Fixed inventory of 2 items per row and 1 rows
         */
        ENCHANTING(2, 2, true, InventoryType.ENCHANTING),
        /**
         * Fixed inventory of 3 items per row and 3 rows
         */
        FURNACE(1, 3, true, InventoryType.FURNACE),
        /**
         * Fixed inventory of 1 items per row and 3 rows
         */
        MERCHANT(1, 3, true, InventoryType.MERCHANT),
        /**
         * Fixed inventory of 9 items per row and 3 rows
         */
        SHULKER_BOX(9, 27, true, InventoryType.SHULKER_BOX),
        /**
         * Fixed inventory of 9 items per row and 10 items
         */
        WORKBENCH(9, 10, true, InventoryType.WORKBENCH),
        /**
         * Fixed inventory of 5 items per row and 1 rows
         */
        HOPPER(5, 5, true, InventoryType.HOPPER);

        /**
         * Row size for the inventory
         */
        private final int rowSize;
        /**
         * The amount of slots in the inventory
         */
        private final int MaxInventorySize;
        /**
         * If it is possible to set the amount of rows or now
         */
        private final boolean fixedRows;
        /**
         * The bukkit inventory type
         */
        private final InventoryType inventoryType;

        /**
         * A type of inventory to use
         *
         * @param rowSize          The row size of the inventory
         * @param maxInventorySize The maximum amount of inventory slots
         * @param fixedRows        If it is possible to have a different amount
         *                         of rows
         * @param inventoryType    The bukkit inventory type
         */
        private InventoryBaseType(int rowSize, int maxInventorySize,
                boolean fixedRows, InventoryType inventoryType) {

            this.rowSize = rowSize;
            this.MaxInventorySize = maxInventorySize;
            this.fixedRows = fixedRows;
            this.inventoryType = inventoryType;
        }

        /**
         * Returns the maximum amount of slots in the inventory
         *
         * @return The amount of slots available
         */
        public int getMaxInventorySize() {
            return MaxInventorySize;
        }

        /**
         * The size per row of the inventory
         *
         * @return The row size
         */
        public int getRowSize() {
            return rowSize;
        }

        /**
         * If it is possible to have the amount of rows dynamically
         *
         * @return True if the rows can be set dynamically
         */
        public boolean isFixedRows() {
            return fixedRows;
        }

        /**
         * Get the bukkit type for the inventory
         *
         * @return The bukkit inventory type
         */
        public InventoryType getInventoryType() {
            return inventoryType;
        }

    }

    /**
     * Map of heads that need to be loaded asynchronous when opening the
     * inventory
     */
    private final Map<Integer, AsyncPlayerHead> asyncHeadItems
            = new HashMap<>();

    /**
     * The amount of rows to have the inventory open
     */
    private int fixedRows = 0;
    /**
     * List of elements to draw
     */
    private final List<GuiElement> guiElements = new ArrayList<>();
    /**
     * True if clicks in the player inventory should be ignored and canceled. So
     * if the player clicks something nothing will happen
     */
    private boolean ignorePlayerInventory = true;

    /**
     * Index to add new items
     */
    private int index = 0;

    /**
     * The current bukkit inventory this inventory is linked to
     */
    private Inventory inventory;
    /**
     * The base type and information for the inventory
     */
    private final InventoryBaseType inventoryBaseType;
    /**
     * List of inventory elements used when a slot is cliked
     */
    private final List<InventoryElement> inventoryElements = new ArrayList<>();
    /**
     * Check if the inventory is initialized
     */
    private boolean isInit = true;
    /**
     * The name of the inventory
     */
    private String name = null;
    /**
     * The PluginUtil to use for function of the plugin itself
     */
    private final PluginUtil pluginUtil;
    /**
     * The gui settings for getting information from the gui file
     */
    protected GuiSetting guiSettings;

    /**
     * Creates a dynamic inventory of 9 items per row. If you add more as 9
     * items it will create a new row up to 54 items (6 rows of 9)
     *
     * @param guiSettings File to get icon information from
     */
    public InventoryBase(GuiSetting guiSettings) {
        this.guiSettings = guiSettings;
        this.pluginUtil = guiSettings.getPluginUtil();
        this.inventoryBaseType = InventoryBaseType.CUSTOM;
    }

    /**
     * Creates a dynamic inventory of 9 items per row with a fixed amount of
     * rows
     *
     * The max items are thus 9 * [rows]
     *
     * @param guiSettings File to get icon information from
     * @param rows        The amount of rows this inventory should have
     */
    public InventoryBase(GuiSetting guiSettings, int rows) {
        this.guiSettings = guiSettings;
        this.fixedRows = rows;
        this.pluginUtil = guiSettings.getPluginUtil();
        this.inventoryBaseType = InventoryBaseType.CUSTOM;
    }

    /**
     * Creates a inventory of the given inventory type.
     *
     * @param guiSettings       File to get icon information from
     * @param inventoryBaseType The inventory type to use
     */
    public InventoryBase(GuiSetting guiSettings,
            InventoryBaseType inventoryBaseType) {
        this.guiSettings = guiSettings;
        this.inventoryBaseType = inventoryBaseType;
        this.pluginUtil = guiSettings.getPluginUtil();
        if (inventoryBaseType.isFixedRows()) {
            this.fixedRows = inventoryBaseType.MaxInventorySize;
        }
    }

    /**
     * @deprecated
     *
     * Backwards compatibility for minecraft 1.11
     *
     * Gets the inventory for the clicked inventory
     *
     * @param event The click event triggering this
     *
     * @return The inventory clicked in
     */
    private Inventory getClickedInventory(InventoryClickEvent event) {
        if (event.getRawSlot() < 0) {
            return null;
        } else if (event.getView().getTopInventory() != null && event.
                getRawSlot() < event.getView().getTopInventory().getSize()) {
            return event.getView().getTopInventory();
        } else {
            return event.getView().getBottomInventory();
        }
    }

    /**
     * Fired when a inventory is opened
     *
     * @see InventoryOpenEvent
     * @param event The opening event of a inventory
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void OnInventoryOpenEvent(InventoryOpenEvent event) {
        if (asyncHeadItems.isEmpty()) {
            return;
        }
        for (Map.Entry<Integer, AsyncPlayerHead> headItem : asyncHeadItems.
                entrySet()) {
            Integer index = headItem.getKey();
            AsyncPlayerHead playerHead = headItem.getValue();

            getGuiSettings().getPluginUtil().runTaskAsynchronously(
                    new Runnable() {
                @Override
                public void run() {
                    playerHead.loadHead();
                    inventory.setItem(index, playerHead);
                }
            });
        }
    }

    /**
     * Adds a gui element for the inventory appending the last element or on the
     * first slot of the inventory.
     *
     * This can override already set elements
     *
     * @param guiElement The element to add
     *
     * @return Returns this object for easy building of the inventory
     */
    public InventoryBase addGuiElement(GuiElement guiElement) {
        return addGuiElement(guiElement, index);
    }

    /**
     * Adds a gui element on the given position.
     *
     * This can override already set elements
     *
     * @param guiElement The element to add
     * @param position   The position to insert the element into
     *
     * @return Returns this object for easy building of the inventory
     */
    public InventoryBase addGuiElement(GuiElement guiElement, int position) {
        if (position >= inventoryBaseType.getMaxInventorySize()) {
            throw new ArrayIndexOutOfBoundsException(
                    "This index is to big to handel. Current postion: "
                    + position + ". Max position: " + inventoryBaseType.
                            getMaxInventorySize());
        }

        while (position > this.guiElements.size() - 1) {
            this.guiElements.add(null);
            this.inventoryElements.add(null);
        }
        this.guiElements.set(position, guiElement);
        this.inventoryElements.set(position, guiElement);

        index = position + 1;
        return this;
    }

    /**
     * Adds a gui element to the inventory based on the last inserted element or
     * the first slot of the inventory
     *
     * This can override already set elements
     *
     * @param guiElement     The element to add
     * @param positionsMoved The positions to move from
     *
     * @return Returns this object for easy building of the inventory
     */
    public InventoryBase addGuiElementTransposed(GuiElement guiElement,
            int positionsMoved) {
        return addGuiElement(guiElement, index + positionsMoved);
    }

    /**
     * Adds a inventory element for the inventory appending the last element or
     * on the first slot of the inventory.
     *
     * This can override already set elements
     *
     * @param inventoryElement The element to add
     *
     * @return Returns this object for easy building of the inventory
     */
    public InventoryBase addInventoryElement(
            InventoryElement inventoryElement) {

        return addInventoryElement(inventoryElement, index);
    }

    /**
     * Adds a inventory element on the given position.
     *
     * This can override already set elements
     *
     * @param inventoryElement The element to add
     * @param position         The position to insert the element into
     *
     * @return Returns this object for easy building of the inventory
     */
    public InventoryBase addInventoryElement(InventoryElement inventoryElement,
            int position) {
        if (position >= inventoryBaseType.getMaxInventorySize()) {
            throw new ArrayIndexOutOfBoundsException(
                    "This index is to big to handel. Current postion: "
                    + position + ". Max position: " + inventoryBaseType.
                            getMaxInventorySize());
        }

        while (position > this.guiElements.size() - 1) {
            this.guiElements.add(null);
            this.inventoryElements.add(null);
        }
        this.guiElements.set(position, null);
        this.inventoryElements.set(position, inventoryElement);

        index = position + 1;
        return this;
    }

    /**
     * Adds a inventory element for the inventory appending the last element or
     * on the first slot of the inventory.
     *
     * This can override already set elements
     *
     * @param inventoryElement The element to add
     * @param positionsMoved   positions to move from the previous one
     *
     * @return Returns this object for easy building of the inventory
     */
    public InventoryBase addInventoryElementTransposed(
            InventoryElement inventoryElement, int positionsMoved) {
        return addInventoryElement(inventoryElement, index + positionsMoved);
    }

    /**
     * Closes the current inventory and removes all listeners this object has
     * attached
     *
     * This will also make it close for the player
     *
     * @param humanEntity The entity to close this inventory for
     */
    public void closeInventory(HumanEntity humanEntity) {
        closeInventory(humanEntity, false);
    }

    /**
     * Closes the current inventory and removes all listeners this object has
     * attached
     *
     * This will also make it close for the player if is closed is false
     *
     * @param humanEntity The player to close it for
     * @param isClosed    True if the inventory is already closed for the
     *                    client. It will not try to close it again for the
     *                    client
     */
    public void closeInventory(HumanEntity humanEntity, boolean isClosed) {

        InventoryClickEvent.getHandlerList().unregister(this);
        InventoryDragEvent.getHandlerList().unregister(this);
        InventoryCloseEvent.getHandlerList().unregister(this);
        InventoryOpenEvent.getHandlerList().unregister(this);
        if (!isClosed) {
            humanEntity.closeInventory();
            if (humanEntity instanceof Player) {
                ((Player) humanEntity).updateInventory();
            }
        }
        isInit = false;
    }

    /**
     * Returns the gui file for the inventory
     *
     * @return The gui settings
     */
    public GuiSetting getGuiSettings() {
        return guiSettings;
    }

    /**
     * Creates and returns the inventory
     *
     * @return The inventory created
     */
    public Inventory getInventory() {
        guiElements.clear();
        inventoryElements.clear();
        initInventory();
        int realRows = guiElements.size() / inventoryBaseType.getRowSize();

        if (guiElements.size() % inventoryBaseType.getRowSize() != 0) {
            realRows++;
        }

        asyncHeadItems.clear();
        ItemStack[] inventoryItems
                = new ItemStack[realRows * inventoryBaseType.getRowSize()];

        for (int i = 0; i < guiElements.size(); i++) {
            GuiElement guiElement = guiElements.get(i);
            if (guiElement == null) {
                continue;
            }
            ItemStack itemStack = guiElement.getItemStack(guiSettings);
            if (itemStack instanceof AsyncPlayerHead) {
                asyncHeadItems.put(i, (AsyncPlayerHead) itemStack);
            }
            inventoryItems[i] = itemStack;
        }

        int rowsToDraw = realRows;
        if (fixedRows != 0) {
            rowsToDraw = fixedRows;
        }

        if (name != null) {
            if (inventoryBaseType.isFixedRows()) {
                this.inventory = Bukkit.createInventory(null, inventoryBaseType.
                        getInventoryType(), name);
            } else {
                this.inventory = Bukkit.createInventory(null, rowsToDraw
                        * inventoryBaseType.getRowSize(), name);
            }
        } else {
            if (inventoryBaseType.isFixedRows()) {
                this.inventory = Bukkit.createInventory(null, inventoryBaseType.
                        getInventoryType());
            } else {
                this.inventory = Bukkit.createInventory(null, rowsToDraw
                        * inventoryBaseType.getRowSize());
            }
        }

        inventory.setContents(inventoryItems);

        isInit = false;
        index = 0;
        return inventory;
    }

    /**
     * Gets the PluginUtil for the current inventory
     *
     * @return The PluginUtil used
     */
    public PluginUtil getPluginUtil() {
        return pluginUtil;
    }

    /**
     * Sets if the inventory has to disable actions in the players inventory or
     * not
     *
     * @param ignorePlayerInventory True if you want to disable actions in the
     *                              player inventory
     *
     * @return Returns this object for easy building of the inventory
     */
    public InventoryBase isIgnorePlayerInventory(
            boolean ignorePlayerInventory) {

        this.ignorePlayerInventory = ignorePlayerInventory;
        return this;
    }

    /**
     * Sets the index for the next entry for the next row.
     *
     * If the last entry was already at the last index of a row a new row will
     * already be created and calling this will create a empty row, if you do
     * not want this use nextRow(true)
     *
     * @return Returns this object for easy building of the inventory
     */
    public InventoryBase nextRow() {
        return nextRow(false);
    }

    /**
     * Sets the index for the next entry for the next row.
     *
     * @param preventEmptyRow True if you want to prevent a empty row forming
     *                        when the next entry would already have created a
     *                        new row
     *
     * @return Returns this object for easy building of the inventory
     */
    public InventoryBase nextRow(boolean preventEmptyRow) {
        if (preventEmptyRow && (index % inventoryBaseType.getRowSize()) == 0) {
            return this;
        }
        index += inventoryBaseType.getRowSize() - (index % inventoryBaseType.
                getRowSize());
        return this;
    }

    /**
     * Fired when a inventory is clicked in
     *
     * @see InventoryClickEvent
     * @param event The clicking event of a inventory
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryClickEvent(InventoryClickEvent event) {

        if (isInit) {
            return;
        }

        if (!event.getInventory().equals(inventory)) {
            return;
        }

        event.setCancelled(true);

        if (!ignorePlayerInventory && getClickedInventory(event) != null
                && getClickedInventory(event).equals(
                        event.getWhoClicked().getInventory()
                )) {

            if (event.getAction().
                    equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {

                for (int i = 0; i < inventoryElements.size(); i++) {
                    InventoryElement inventoryElement
                            = inventoryElements.get(i);

                    if (inventoryElement == null) {
                        continue;
                    }
                    if (guiElements.get(i) != null) {
                        continue;
                    }
                    inventoryElement.onInventoryClickEvent(this, event);
                }
            } else {
                event.setCancelled(false);
            }
            return;
        }

        if (getClickedInventory(event) == null
                || !getClickedInventory(event).equals(inventory)) {
            return;
        }

        int index = event.getRawSlot();
        if (index >= inventoryElements.size()) {
            return;
        }

        InventoryElement inventoryElement = inventoryElements.get(index);

        if (inventoryElement == null) {
            return;
        }

        inventoryElement.onInventoryClickEvent(this, event);

    }

    /**
     * Fired when a inventory is closed
     *
     * @see InventoryCloseEvent
     * @param event The closing event of a inventory
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        if (event.getInventory().equals(inventory)) {
            closeInventory(event.getPlayer(), true);
        }
    }

    /**
     * Fired when a inventory is draged in
     *
     * @see InventoryDragEvent
     * @param event The draging event of a inventory
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryDragEvent(InventoryDragEvent event) {
        if (event.getInventory().equals(inventory)) {
            event.setCancelled(true);
        }
    }

    /**
     * Attaches all the listeners for the inventory and opens it for the entity
     *
     * @param humanEntity The entity to open the inventory for
     */
    public void openInventory(HumanEntity humanEntity) {
        attachListener();
        humanEntity.openInventory(getInventory());
    }

    /**
     * Closes the current inventory and opens the new inventory for the given
     * player
     *
     * If the inventories are of the same type and size it will redraw the
     * inventory instead of closing and opening. This makes the cursor not move
     * to the middle again
     *
     * @param humanEntity   The player to close and open the new inventory for
     * @param inventoryBase The new inventory to open
     */
    public void openNewInventory(HumanEntity humanEntity,
            InventoryBase inventoryBase) {
        Inventory inventory_ = inventoryBase.getInventory();

        if (inventory_.getType() == inventory.getType()
                && inventory_.getSize() == inventory.getSize()
                && humanEntity instanceof Player) {

            Player player = (Player) humanEntity;
            inventoryBase.attachListener();
            closeInventory(humanEntity, true);

            player.openInventory(inventory_);
            getPluginUtil().runTask(new Runnable() {
                @Override
                public void run() {
                    player.updateInventory();
                }
            });
        } else {

            closeInventory(humanEntity);

            getPluginUtil().runTask(new Runnable() {
                @Override
                public void run() {
                    inventoryBase.attachListener();
                    humanEntity.openInventory(inventory_);
                }
            });
        }

    }

    /**
     * Reloads the current inventory. This will fire the function
     * GuiElement#getItemStack(GuiSettings) again.
     */
    public void reloadInventory() {
        int realRows = guiElements.size() / inventoryBaseType.getRowSize();

        if (guiElements.size() % inventoryBaseType.getRowSize() != 0) {
            realRows++;
        }

        asyncHeadItems.clear();
        ItemStack[] inventoryItems
                = new ItemStack[realRows * inventoryBaseType.getRowSize()];

        for (int i = 0; i < guiElements.size(); i++) {
            GuiElement guiElement = guiElements.get(i);
            if (guiElement == null) {
                continue;
            }
            ItemStack itemStack = guiElement.getItemStack(guiSettings);
            if (itemStack instanceof AsyncPlayerHead) {
                asyncHeadItems.put(i, (AsyncPlayerHead) itemStack);
            }
            inventoryItems[i] = itemStack;
        }

        inventory.setContents(inventoryItems);
    }

    /**
     * Sends a update event for the given entity if it is a player.
     *
     * This will fire the getInventory() function, the initInventory() function
     * and GuiElement#getItemStack(GuiSettings) function again
     *
     * @param humanEntity The entity to update the inventory for
     */
    public void updateInventory(HumanEntity humanEntity) {

        if (!(humanEntity instanceof Player)) {
            return;
        }
        Player player = (Player) humanEntity;
        player.openInventory(this.getInventory());
        getPluginUtil().runTask(new Runnable() {
            @Override
            public void run() {
                player.updateInventory();
            }
        });
    }

    /**
     * Updates a specific element in the inventory
     *
     * @param item the element to update
     */
    public void updateItem(GuiElement item) {
        inventory.setItem(
                guiElements.indexOf(item), item.getItemStack(guiSettings)
        );
    }

    /**
     * Attaches the listeners of this class to the bukkit manager
     */
    protected void attachListener() {
        getPluginUtil().registerEvents(this);
    }

    /**
     * Initializes the inventory.
     *
     * This is called when getInventory is called in InventoryBase()
     */
    protected abstract void initInventory();

    /**
     * Sets the name of the inventory to the given name
     *
     * @param name The name for the inventory
     *
     * @return Returns this object for easy building of the inventory
     */
    protected InventoryBase setInventoryName(String name) {
        this.name = name;
        return this;
    }
}
