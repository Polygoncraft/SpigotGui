package me.killje.spigotgui.list;

/**
 * A interface for listing in inventories
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public interface Listable {

    /**
     * Goes to the next page of the list
     */
    public void nextPage();

    /**
     * Goes to the previous page of the list
     */
    public void previousPage();

}
