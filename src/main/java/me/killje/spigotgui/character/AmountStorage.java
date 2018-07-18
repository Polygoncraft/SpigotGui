package me.killje.spigotgui.character;

/**
 * Creates a storage for a integer amount
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class AmountStorage extends AbstractStorage<Integer> {

    /**
     * The current amount stored
     */
    private int currentAmount = 0;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getCurrent() {
        return currentAmount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addToStorage(Integer amountToAdd) {
        if (currentAmount > Integer.MAX_VALUE / 10 - amountToAdd) {
            return;
        }
        currentAmount = currentAmount * 10 + amountToAdd;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void removeLastFromStorage() {
        currentAmount /= 10;
    }

}
