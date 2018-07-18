package me.killje.spigotgui.character;

import me.killje.spigotgui.guielement.Exit;
import me.killje.spigotgui.guielement.GuiElement;
import me.killje.spigotgui.util.GuiSetting;
import me.killje.spigotgui.util.InventoryBase;
import org.bukkit.entity.Player;

/**
 * Creates a number board inventory
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public abstract class NumberBoard extends InventoryBase implements
        StorageUpdateListener {

    /**
     * The storage to store the entered numbers
     */
    private final AmountStorage amountStorage;
    /**
     * The player using the board
     */
    private final Player player;
    /**
     * The element to set the amount
     */
    private final GuiElement setAmountButton;

    /**
     * Creates a number board to enter a amount for
     *
     * @param guiSettings     The GuiSettings to load the number icons from
     * @param player          The player typing
     * @param setAmountButton The button to set the amount for
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public NumberBoard(GuiSetting guiSettings, Player player,
            SetAmountButton setAmountButton) {
        super(guiSettings);
        amountStorage = new AmountStorage();
        amountStorage.addListener(this);
        this.player = player;
        this.setAmountButton = setAmountButton;
        setAmountButton.setAmountStorage(amountStorage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStorageupdateEvent() {
        player.openInventory(this.getInventory());

        getPluginUtil().runTask(new Runnable() {
            @Override
            public void run() {
                player.updateInventory();
            }
        });
    }

    /**
     * Returns the storage with the current entered amount
     *
     * @return The storage
     */
    protected final AmountStorage getAmountStorage() {
        return amountStorage;
    }

    /**
     * Get the inventory name for the inventory
     *
     * @return The inventory name
     */
    protected abstract String getInventoryName();

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initInventory() {

        this
                .setInventoryName(getInventoryName())
                .addGuiElementTransposed(new Number(1, amountStorage), 1)
                .addGuiElement(new Number(2, amountStorage))
                .addGuiElement(new Number(3, amountStorage))
                .nextRow()
                .addGuiElementTransposed(new Number(4, amountStorage), 1)
                .addGuiElement(new Number(5, amountStorage))
                .addGuiElement(new Number(6, amountStorage))
                .nextRow()
                .addGuiElementTransposed(new Number(7, amountStorage), 1)
                .addGuiElement(new Number(8, amountStorage))
                .addGuiElement(new Number(9, amountStorage))
                .nextRow()
                .addGuiElementTransposed(new Number(0, amountStorage), 2)
                .addGuiElement(new Undo(amountStorage, "undo.number"), 24)
                .addGuiElement(setAmountButton)
                .addGuiElement(new Exit(), 8);
    }

}
