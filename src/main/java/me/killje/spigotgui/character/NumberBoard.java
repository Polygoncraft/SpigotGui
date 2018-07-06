package me.killje.spigotgui.character;

import me.killje.spigotgui.guielement.Exit;
import me.killje.spigotgui.guielement.GuiElement;
import me.killje.spigotgui.util.GuiSetting;
import me.killje.spigotgui.util.InventoryUtil;
import org.bukkit.entity.Player;

/**
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public abstract class NumberBoard extends InventoryUtil implements StorageUpdateListener {

    private final AmountStorage amountStorage;
    private final Player player;
    private final GuiElement setAmountButton;

    public NumberBoard(GuiSetting guiSettings, Player player, SetAmountButton setAmountButton) {
        super(guiSettings);
        amountStorage = new AmountStorage();
        amountStorage.addListener(this);
        this.player = player;
        this.setAmountButton = setAmountButton;
        setAmountButton.setAmountStorage(amountStorage);
    }

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

    protected final AmountStorage getAmountStorage() {
        return amountStorage;
    }

    protected abstract String getInventoryName();

}
