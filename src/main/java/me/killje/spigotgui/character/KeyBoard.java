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
public abstract class KeyBoard extends InventoryUtil implements StorageUpdateListener {

    private final KeyBoardStringStorage keyBoardStringStorage;
    private final Player player;
    private final GuiElement setStringButton;

    public KeyBoard(GuiSetting guiSettings, Player player) {
        this(guiSettings, player, null);
    }

    public KeyBoard(GuiSetting guiSettings, Player player, SetStringButton setStringButton) {
        super(guiSettings);
        keyBoardStringStorage = new KeyBoardStringStorage();
        keyBoardStringStorage.addListener(this);
        this.player = player;
        this.setStringButton = setStringButton;
        if (setStringButton != null) {
            setStringButton.setKeyBoardStringStorage(keyBoardStringStorage);
        }
    }

    public KeyBoard(GuiSetting guiSettings, Player player, int rows) {
        this(guiSettings, player, rows, null);
    }

    public KeyBoard(GuiSetting guiSettings, Player player, int rows, SetStringButton setStringButton) {
        super(guiSettings, rows);
        keyBoardStringStorage = new KeyBoardStringStorage();
        keyBoardStringStorage.addListener(this);
        this.player = player;
        this.setStringButton = setStringButton;
        if (setStringButton != null) {
            setStringButton.setKeyBoardStringStorage(keyBoardStringStorage);
        }
    }

    @Override
    protected void initInventory() {

        this
                .setInventoryName(getInventoryName());

        if (setStringButton != null) {

            this
                    .addGuiElement(new Undo(keyBoardStringStorage, "undo.character"))
                    .addGuiElement(setStringButton);
        } else {
            this.addGuiElement(new Undo(keyBoardStringStorage, "undo.character"), 4);
        }

        this
                .addGuiElement(new Exit(), 8)
                .addGuiElement(new Character('1', keyBoardStringStorage))
                .addGuiElement(new Character('2', keyBoardStringStorage))
                .addGuiElement(new Character('3', keyBoardStringStorage))
                .addGuiElement(new Character('4', keyBoardStringStorage))
                .addGuiElement(new Character('5', keyBoardStringStorage))
                .addGuiElement(new Character('6', keyBoardStringStorage))
                .addGuiElement(new Character('7', keyBoardStringStorage))
                .addGuiElement(new Character('8', keyBoardStringStorage))
                .addGuiElement(new Character('9', keyBoardStringStorage))
                .addGuiElement(new Character('0', keyBoardStringStorage))
                .addGuiElement(new Character('A', keyBoardStringStorage))
                .addGuiElement(new Character('B', keyBoardStringStorage))
                .addGuiElement(new Character('C', keyBoardStringStorage))
                .addGuiElement(new Character('D', keyBoardStringStorage))
                .addGuiElement(new Character('E', keyBoardStringStorage))
                .addGuiElement(new Character('F', keyBoardStringStorage))
                .addGuiElement(new Character('G', keyBoardStringStorage))
                .addGuiElement(new Character('H', keyBoardStringStorage))
                .addGuiElement(new Character('I', keyBoardStringStorage))
                .addGuiElement(new Character('J', keyBoardStringStorage))
                .addGuiElement(new Character('K', keyBoardStringStorage))
                .addGuiElement(new Character('L', keyBoardStringStorage))
                .addGuiElement(new Character('M', keyBoardStringStorage))
                .addGuiElement(new Character('N', keyBoardStringStorage))
                .addGuiElement(new Character('O', keyBoardStringStorage))
                .addGuiElement(new Character('P', keyBoardStringStorage))
                .addGuiElement(new Character('Q', keyBoardStringStorage))
                .addGuiElement(new Character('R', keyBoardStringStorage))
                .addGuiElement(new Character('S', keyBoardStringStorage))
                .addGuiElement(new Character('T', keyBoardStringStorage))
                .addGuiElement(new Character('U', keyBoardStringStorage))
                .addGuiElement(new Character('V', keyBoardStringStorage))
                .addGuiElement(new Character('W', keyBoardStringStorage))
                .addGuiElement(new Character('X', keyBoardStringStorage))
                .addGuiElement(new Character('Y', keyBoardStringStorage))
                .addGuiElement(new Character('Z', keyBoardStringStorage));
    }

    @Override
    public void onStorageupdateEvent() {
        updateInventory(player);
    }

    protected final KeyBoardStringStorage getKeyBoardStringStorage() {
        return keyBoardStringStorage;
    }

    protected String getInventoryName() {
        return getKeyBoardStringStorage().getCurrent();
    }

}
