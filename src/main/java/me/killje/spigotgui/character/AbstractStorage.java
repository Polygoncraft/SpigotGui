package me.killje.spigotgui.character;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores information and sends updates when that information is modified
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 * @param <TYPE> The storage type
 */
public abstract class AbstractStorage<TYPE> {

    /**
     * List of objects interested in modification updates
     */
    private final List<StorageUpdateListener> listeners = new ArrayList<>();

    /**
     * Sends a update to all listeners
     */
    private void sendUpdate() {
        for (StorageUpdateListener listener : listeners) {
            listener.onStorageupdateEvent();
        }
    }

    /**
     * Add the item to the storage
     *
     * @param amountToAdd The item to add
     */
    public void add(TYPE amountToAdd) {
        addToStorage(amountToAdd);
        sendUpdate();
    }

    /**
     * Adds a listener to the current storage
     *
     * @param listener The listener to add
     */
    public void addListener(StorageUpdateListener listener) {
        listeners.add(listener);
    }

    /**
     * Get the current stored item
     *
     * @return The stored item
     */
    public abstract TYPE getCurrent();

    /**
     * Remove the last item from the storage
     */
    public void removeLast() {
        removeLastFromStorage();
        sendUpdate();
    }

    /**
     * Adds the given item to the storage
     *
     * @param amountToAdd The item to add to the storage
     */
    protected abstract void addToStorage(TYPE amountToAdd);

    /**
     * Called when last item is removed
     */
    protected abstract void removeLastFromStorage();

}
