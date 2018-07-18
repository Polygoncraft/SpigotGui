package me.killje.spigotgui.character;

/**
 * Creates a storage for a string
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class KeyBoardStringStorage extends AbstractStorage<String> {

    /**
     * The current string stored
     */
    private String currentStorage = "";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrent() {
        return currentStorage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLastFromStorage() {
        if (currentStorage.isEmpty()) {
            return;
        }
        currentStorage
                = currentStorage.substring(0, currentStorage.length() - 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addToStorage(String character) {
        currentStorage += character;
    }

}
