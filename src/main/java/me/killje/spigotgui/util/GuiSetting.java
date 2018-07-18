package me.killje.spigotgui.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;
import org.bukkit.plugin.Plugin;

/**
 * This class is to load gui icons and text from a file
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 */
public class GuiSetting {

    /**
     * Bukkit chatcolors conversion map
     */
    private final static Map<String, String> chatColors = new HashMap<>();

    /**
     * Populate the chatcolor map
     */
    static {
        ChatColor[] chatColorList = ChatColor.values();
        for (ChatColor chatColor : chatColorList) {
            chatColors.put(chatColor.name(), chatColor.toString());
        }
    }

    /**
     * This class is to load a icon from file. It can handle traversing multiple
     * use entries
     */
    private class GUIElementInformation {

        /**
         * Color from file
         */
        private String color = null;
        /**
         * Section that this object parses
         */
        private final ConfigurationSection configurationSection
                = guiFile.GetConfig().getConfigurationSection("GUIElements");
        /**
         * The name from file
         */
        private String displayName;
        /**
         * The lore from file
         */
        private List<String> lore;
        /**
         * The item type from file
         */
        private String type;
        /**
         * The value from file
         */
        private String value;

        /**
         * Creates a new element from the section
         *
         * @param name The section to get information from
         */
        public GUIElementInformation(String name) {
            extractInformation(name);
        }

        /**
         * Get the information from the given section
         *
         * @param name The section to parse
         */
        private void extractInformation(String name) {

            ConfigurationSection section = configurationSection.
                    getConfigurationSection(name);

            if (section == null) {
                getPluginUtil().getLogger().log(Level.SEVERE,
                        "Could not find GUI element with name ''{0}''", name);
                return;
            }

            if (type == null && section.contains("type")) {
                type = section.getString("type");
            }

            if (value == null && section.contains("value")) {
                value = section.getString("value");
            }

            if (color == null && section.contains("color")) {
                color = section.getString("color");
            }

            if (displayName == null && section.contains("displayName")) {
                displayName = section.getString("displayName");
            }

            if (lore == null && section.contains("lore")) {
                lore = section.getStringList("lore");
            }

            if (section.contains("use")) {
                if (section.isList("use")) {
                    for (String sectionUse : section.getStringList("use")) {
                        extractInformation(sectionUse);
                    }
                } else {
                    extractInformation(section.getString("use"));
                }
            }
        }

        /**
         * Get the color from file
         *
         * @return The color
         */
        public String getColor() {
            return color;
        }

        /**
         * Get the display name from file
         *
         * @param replaceMap Parses values in the display name with these
         *                   values. The keys are being parsed with a % in front
         *                   in the display name
         *
         * @return The parsed name
         */
        public String getDisplayName(Map<String, String> replaceMap) {

            String finalDisplayName = displayName;
            for (Map.Entry<String, String> replaceItem
                    : chatColors.entrySet()) {

                String replaceFrom = replaceItem.getKey();
                String replaceTo = replaceItem.getValue();
                finalDisplayName = finalDisplayName.
                        replaceAll("&" + replaceFrom, replaceTo);

            }
            for (Map.Entry<String, String> replaceItem
                    : replaceMap.entrySet()) {

                String replaceFrom = replaceItem.getKey();
                String replaceTo = replaceItem.getValue();
                finalDisplayName = finalDisplayName.
                        replaceAll("%" + replaceFrom, replaceTo);

            }
            return finalDisplayName;
        }

        /**
         * Get the lore from the file
         *
         * @return The lore
         */
        public List<String> getLore() {
            return lore;
        }

        /**
         * Get the type from file
         *
         * @return The file
         */
        public String getType() {
            return type;
        }

        /**
         * Get the value from file
         *
         * @return The value
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * Get the config file containing the settings
     */
    private final clsConfiguration guiFile;
    /**
     * The plugin util for basic interactions with the initialized plugin
     */
    private final PluginUtil pluginUtil;

    public GuiSetting(Plugin plugin, String guiFile) {
        this.pluginUtil = new PluginUtil(plugin);
        this.guiFile = new clsConfiguration(plugin, guiFile, true);
    }

    /**
     * Creates a dye item stack
     *
     * @param displayName The name of the dye
     * @param lore        The lore to add to the dye
     *
     * @return The parsed ItemStack
     */
    private ItemStack createDyeItemStack(
            String displayName, List<String> lore) {
        return createDyeItemStack(displayName, lore, null);
    }

    /**
     * Creates a dye item stack
     *
     * @param displayName The name of the dye
     * @param lore        The lore to add to the dye
     * @param color       Color of the dye
     *
     * @return The parsed ItemStack
     */
    private ItemStack createDyeItemStack(String displayName, List<String> lore,
            DyeColor color) {
        ItemStack itemStack;
        if (color != null) {
            itemStack = new Dye(color).toItemStack(1);
        } else {
            itemStack = new Dye().toItemStack(1);
        }
        setDataOnItemStack(itemStack, displayName, lore);
        return itemStack;
    }

    /**
     *
     * Creates a item stack from the given material
     *
     * @param material    The material to create the item stack out of
     * @param displayName The name for the ItemStack
     * @param lore        The lore on the ItemStack
     *
     * @return The parsed ItemStack
     */
    private ItemStack createItemStack(Material material, String displayName,
            List<String> lore) {
        return createItemStack(material, displayName, lore, (short) -1);
    }

    /**
     *
     * Creates a item stack from the given material
     *
     * @param material    The material to create the item stack out of
     * @param displayName The name for the ItemStack
     * @param lore        The lore on the ItemStack
     * @param damage      The damage value for the item
     *
     * @return The parsed ItemStack
     */
    private ItemStack createItemStack(Material material, String displayName,
            List<String> lore, short damage) {
        if (material == null) {
            return null;
        }
        ItemStack itemStack;
        if (damage != -1) {
            itemStack = new ItemStack(material, 1, damage);
        } else {
            itemStack = new ItemStack(material);
        }
        setDataOnItemStack(itemStack, displayName, lore);
        return itemStack;
    }

    /**
     * Sets the display name and lore on a item stack
     *
     * @param itemStack   The ItemStack to change
     * @param displayName The display name to set
     * @param lore        The lore to set, can be null to not apply it on the
     *                    item
     */
    private void setDataOnItemStack(ItemStack itemStack, String displayName,
            List<String> lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        if (lore != null) {
            itemMeta.setLore(lore);
        }
        itemStack.setItemMeta(itemMeta);
    }

    /**
     * Gets the item stack with the given name from the gui file
     *
     * @param name The item to retrieve
     *
     * @return The parsed ItemStack
     */
    public ItemStack getItemStack(String name) {
        return getItemStack(name, new HashMap<>());
    }

    /**
     * Gets the item stack with the given name from the gui file with the
     * replace map for parsing of display name and lore
     *
     * @param name       The item to retrieve
     * @param replaceMap The items to replace in the display name and lore
     *
     * @return The parsed ItemStack
     */
    public ItemStack getItemStack(String name, Map<String, String> replaceMap) {

        GUIElementInformation elementInformation
                = new GUIElementInformation(name);

        String type = elementInformation.getType();

        if (type == null) {
            return null;
        }

        String value = elementInformation.getValue();
        String displayName = elementInformation.getDisplayName(replaceMap);
        List<String> lore = elementInformation.getLore();
        String color = elementInformation.getColor();

        switch (type) {

            case "head":
                return HeadUtil.getTexturedHead(value, displayName, lore);
            case "item":
                ItemStack itemStack;
                switch (value) {
                    // Because dye is not a material stack in bukkit -.-
                    case "DYE":
                        if (color != null) {
                            itemStack = createDyeItemStack(displayName, lore,
                                    DyeColor.valueOf(color));
                        } else {
                            itemStack = createDyeItemStack(displayName, lore);
                        }
                        break;
                    default:
                        Material material = Material.getMaterial(value);
                        if (color != null) {
                            itemStack = createItemStack(
                                    material, displayName, lore,
                                    DyeColor.valueOf(color).getWoolData()
                            );

                        } else {
                            itemStack = createItemStack(material, displayName,
                                    lore);
                        }
                        break;
                }
                return itemStack;
            default:
                return null;
        }

    }

    /**
     * Gets the PluginUtil to use plugin functions
     *
     * @return
     */
    public PluginUtil getPluginUtil() {
        return pluginUtil;
    }

    /**
     * Get text from the gui file
     *
     * @param name The item name
     *
     * @return The parsed text from gui file
     */
    public String getText(String name) {
        return getText(name, new HashMap<>());
    }

    /**
     * Get text from the gui file
     *
     * @param name       The item name
     * @param replaceMap The replace map to replace items in the text with
     *
     * @return The parsed text from gui file
     */
    public String getText(String name, Map<String, String> replaceMap) {

        ConfigurationSection configurationSection = guiFile.GetConfig().
                getConfigurationSection("texts");
        String text = configurationSection.getString(name);

        if (text == null) {
            return "";
        }
        for (Map.Entry<String, String> replaceItem : chatColors.entrySet()) {
            String replaceFrom = replaceItem.getKey();
            if (replaceFrom == null) {
                replaceFrom = "";
            }
            String replaceTo = replaceItem.getValue();
            if (replaceTo == null) {
                replaceTo = "";
            }
            text = text.replaceAll("&" + replaceFrom, replaceTo);
        }
        for (Map.Entry<String, String> replaceItem : replaceMap.entrySet()) {
            String replaceFrom = replaceItem.getKey();
            if (replaceFrom == null) {
                replaceFrom = "";
            }
            String replaceTo = replaceItem.getValue();
            if (replaceTo == null) {
                replaceTo = "";
            }
            text = text.replaceAll("%" + replaceFrom, replaceTo);
        }
        return text;
    }

    /**
     * reloads the config file
     */
    public void reloadConfig() {
        guiFile.ReloadConfig();
    }

}
