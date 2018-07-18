package me.killje.spigotgui.character;

import me.killje.spigotgui.guielement.GuiElement;

/**
 * Icon for setting the current set amount in the stored
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public abstract class SetAmountButton implements GuiElement {

    /**
     * The storage where the amount is stored
     */
    private AmountStorage amountStorage;

    /**
     * The current storage
     *
     * @return the storage
     */
    protected AmountStorage getAmountStorage() {
        return amountStorage;
    }

    /**
     * Sets the amount storage for the button
     *
     * @param amountStorage The storage
     */
    public void setAmountStorage(AmountStorage amountStorage) {
        this.amountStorage = amountStorage;
    }

}
