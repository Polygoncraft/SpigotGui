package me.killje.spigotgui.util;

import java.util.ArrayList;
import me.killje.spigotgui.guielement.GuiElement;
import me.killje.spigotgui.guielement.InventoryElement;
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
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public abstract class InventoryBase implements Listener {

    public enum InventoryBaseType {

        CUSTOM(9, 54, false, InventoryType.CHEST),
        HOPPER(5, 5, true, InventoryType.HOPPER);

        private final int rowSize;
        private final int MaxInventorySize;
        private final boolean fixedRows;
        private final InventoryType inventoryType;

        private InventoryBaseType(int rowSize, int maxInventorySize, boolean fixedRows, InventoryType inventoryType) {
            this.rowSize = rowSize;
            this.MaxInventorySize = maxInventorySize;
            this.fixedRows = fixedRows;
            this.inventoryType = inventoryType;
        }

        public int getMaxInventorySize() {
            return MaxInventorySize;
        }

        public int getRowSize() {
            return rowSize;
        }

        public boolean isFixedRows() {
            return fixedRows;
        }

        public InventoryType getInventoryType() {
            return inventoryType;
        }

    }

    private Inventory inventory;
    private final InventoryBaseType inventoryBaseType;
    private final ArrayList<GuiElement> guiElements = new ArrayList<>();
    private final ArrayList<InventoryElement> inventoryElements = new ArrayList<>();
    private final PluginUtil pluginUtil;
    private int index = 0;
    private String name = null;
    private boolean isInit = true;
    private int fixedRows = 0;
    private boolean ignorePlayerInventory = true;

    protected GuiSetting guiSettings;

    public InventoryBase(GuiSetting guiSettings) {
        this.guiSettings = guiSettings;
        this.pluginUtil = guiSettings.getPluginUtil();
        this.inventoryBaseType = InventoryBaseType.CUSTOM;
    }

    public InventoryBase(GuiSetting guiSettings, int rows) {
        this.guiSettings = guiSettings;
        this.fixedRows = rows;
        this.pluginUtil = guiSettings.getPluginUtil();
        this.inventoryBaseType = InventoryBaseType.CUSTOM;
    }

    public InventoryBase(GuiSetting guiSettings, InventoryBaseType inventoryBaseType) {
        this.guiSettings = guiSettings;
        this.inventoryBaseType = inventoryBaseType;
        this.pluginUtil = guiSettings.getPluginUtil();
        if (inventoryBaseType.isFixedRows()) {
            this.fixedRows = inventoryBaseType.MaxInventorySize;
        }
    }

    protected abstract void initInventory();

    public Inventory getInventory() {
        guiElements.clear();
        inventoryElements.clear();
        initInventory();
        int realRows = guiElements.size() / inventoryBaseType.getRowSize();

        if (guiElements.size() % inventoryBaseType.getRowSize() != 0) {
            realRows++;
        }

        ItemStack[] inventoryItems = new ItemStack[realRows * inventoryBaseType.getRowSize()];

        for (int i = 0; i < guiElements.size(); i++) {
            GuiElement guiElement = guiElements.get(i);
            if (guiElement == null) {
                continue;
            }
            inventoryItems[i] = guiElement.getItemStack(guiSettings);
        }

        int rowsToDraw = realRows;
        if (fixedRows != 0) {
            rowsToDraw = fixedRows;
        }

        if (name != null) {
            if (inventoryBaseType.isFixedRows()) {
                this.inventory = Bukkit.createInventory(null, inventoryBaseType.getInventoryType(), name);
            } else {
                this.inventory = Bukkit.createInventory(null, rowsToDraw * inventoryBaseType.getRowSize(), name);
            }
        } else {
            if (inventoryBaseType.isFixedRows()) {
                this.inventory = Bukkit.createInventory(null, inventoryBaseType.getInventoryType());
            } else {
                this.inventory = Bukkit.createInventory(null, rowsToDraw * inventoryBaseType.getRowSize());
            }
        }

        inventory.setContents(inventoryItems);

        isInit = false;
        index = 0;
        return inventory;
    }

    public InventoryBase isIgnorePlayerInventory(boolean ignorePlayerInventory) {
        this.ignorePlayerInventory = ignorePlayerInventory;
        return this;
    }

    public GuiSetting getGuiSettings() {
        return guiSettings;
    }

    public InventoryBase nextRow() {
        return nextRow(false);
    }

    public InventoryBase nextRow(boolean preventEmptyRow) {
        if (preventEmptyRow && (index % inventoryBaseType.getRowSize()) == 0) {
            return this;
        }
        index += inventoryBaseType.getRowSize() - (index % inventoryBaseType.getRowSize());
        return this;
    }

    public InventoryBase addGuiElementTransposed(GuiElement guiElement, int positionsMoved) {
        return addGuiElement(guiElement, index + positionsMoved);
    }

    public InventoryBase addGuiElement(GuiElement guiElement) {
        return addGuiElement(guiElement, index);
    }

    public InventoryBase addGuiElement(GuiElement guiElement, int position) {
        if (position >= inventoryBaseType.getMaxInventorySize()) {
            throw new ArrayIndexOutOfBoundsException("This index is to big to handel. Current postion: " + position + ". Max position: " + inventoryBaseType.getMaxInventorySize());
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

    public InventoryBase addInventoryElementTransposed(InventoryElement inventoryElement, int positionsMoved) {
        return addInventoryElement(inventoryElement, index + positionsMoved);
    }

    public InventoryBase addInventoryElement(InventoryElement inventoryElement) {
        return addInventoryElement(inventoryElement, index);
    }

    public InventoryBase addInventoryElement(InventoryElement inventoryElement, int position) {
        if (position >= inventoryBaseType.getMaxInventorySize()) {
            throw new ArrayIndexOutOfBoundsException("This index is to big to handel. Current postion: " + position + ". Max position: " + inventoryBaseType.getMaxInventorySize());
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

    protected InventoryBase setInventoryName(String name) {
        this.name = name;
        return this;
    }

    protected void attachListener() {
        getPluginUtil().registerEvents(this);
    }

    public void closeInventory(HumanEntity humanEntity) {
        closeInventory(humanEntity, false);
    }

    public void closeInventory(HumanEntity humanEntity, boolean isClosed) {
        InventoryClickEvent.getHandlerList().unregister(this);
        InventoryDragEvent.getHandlerList().unregister(this);
        InventoryCloseEvent.getHandlerList().unregister(this);
        if (!isClosed) {
            humanEntity.closeInventory();
            if (humanEntity instanceof Player) {
                ((Player) humanEntity).updateInventory();
            }
        }
        isInit = false;
    }

    public void openNewInventory(HumanEntity humanEntity, InventoryBase inventoryBase) {
        Inventory inventory_ = inventoryBase.getInventory();

        if (inventory_.getSize() == inventory.getSize() && humanEntity instanceof Player) {

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

    public void openInventory(HumanEntity humanEntity) {
        attachListener();
        humanEntity.openInventory(getInventory());
    }

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

    public void reloadInventory() {
        int realRows = guiElements.size() / inventoryBaseType.getRowSize();

        if (guiElements.size() % inventoryBaseType.getRowSize() != 0) {
            realRows++;
        }

        ItemStack[] inventoryItems = new ItemStack[realRows * inventoryBaseType.getRowSize()];

        for (int i = 0; i < guiElements.size(); i++) {
            GuiElement guiElement = guiElements.get(i);
            if (guiElement == null) {
                continue;
            }
            inventoryItems[i] = guiElement.getItemStack(guiSettings);
        }

        inventory.setContents(inventoryItems);
    }

    public void updateItem(GuiElement item) {
        inventory.setItem(guiElements.indexOf(item), item.getItemStack(guiSettings));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryDragEvent(InventoryDragEvent event) {
        if (event.getInventory().equals(inventory)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryClickEvent(InventoryClickEvent event) {

        if (isInit) {
            return;
        }

        if (!event.getInventory().equals(inventory)) {
            return;
        }

        event.setCancelled(true);

        if (!ignorePlayerInventory && getClickedInventory(event) != null && getClickedInventory(event).equals(event.getWhoClicked().getInventory())) {

            if (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                for (int i = 0; i < inventoryElements.size(); i++) {
                    InventoryElement inventoryElement = inventoryElements.get(i);
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

        if (getClickedInventory(event) == null || !getClickedInventory(event).equals(inventory)) {
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

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        if (event.getInventory().equals(inventory)) {
            closeInventory(event.getPlayer(), true);
        }
    }

    private Inventory getClickedInventory(InventoryClickEvent event) {
        if (event.getRawSlot() < 0) {
            return null;
        } else if (event.getView().getTopInventory() != null && event.getRawSlot() < event.getView().getTopInventory().getSize()) {
            return event.getView().getTopInventory();
        } else {
            return event.getView().getBottomInventory();
        }
    }

    public PluginUtil getPluginUtil() {
        return pluginUtil;
    }
}
