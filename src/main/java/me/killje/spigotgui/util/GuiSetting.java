package me.killje.spigotgui.util;

import de.tr7zw.itemnbtapi.NBTContainer;
import de.tr7zw.itemnbtapi.NBTItem;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

/**
 * This class is to load gui icons and text from a file
 *
 * @author Patrick Beuks (killje) <code@beuks.net>
 */
public class GuiSetting {

    /**
     * Bukkit chatcolors conversion map
     */
    private final static Map<String, String> chatColors = new HashMap<>();

    private final static Map<String, String> conversionMap = new HashMap<>();

    /**
     * Populate the chatcolor map
     */
    static {
        ChatColor[] chatColorList = ChatColor.values();
        for (ChatColor chatColor : chatColorList) {
            chatColors.put(chatColor.name(), chatColor.toString());
        }

        conversionMap.put("YELLOW_DYE", Material.DANDELION_YELLOW.name());
        conversionMap.put("GREEN_DYE", Material.CACTUS_GREEN.name());
        conversionMap.put("RED_DYE", Material.ROSE_RED.name());
        conversionMap.put("BLUE_DYE", Material.LAPIS_LAZULI.name());
        conversionMap.put("BLACK_DYE", Material.INK_SAC.name());
        conversionMap.put("WHITE_DYE", Material.BONE_MEAL.name());
        conversionMap.put("BROWN_DYE", Material.COCOA_BEANS.name());
    }

    /**
     * This class is to load a icon from file. It can handle traversing multiple
     * use entries
     */
    private class GUIElementInformation {

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
        private ItemStack item;

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

            if (section.contains("type")) {
                updateSection(section);
            }

            if (item == null && section.contains("item")) {
                item = new ItemStack(Material.getMaterial(
                        section.getString("item")
                ));
                if (section.contains("nbt")) {
                    String nbt = section.getString("nbt");
                    NBTContainer nbtContainer = new NBTContainer(nbt);
                    NBTItem itemNBT = new NBTItem(item);
                    itemNBT.mergeCompound(nbtContainer);
                    item = itemNBT.getItem();
                }
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

        private void updateSection(ConfigurationSection section) {
            String value = section.getString("value");
            String color = section.getString("color");

            DyeColor dyeColor = DyeColor.WHITE;
            if (color != null) {
                section.set("color", null);
                System.out.println(color);

                dyeColor = DyeColor.valueOf(color);
                System.out.println(dyeColor);
            }

            switch (section.getString("type")) {
                case "head":
                    String nbt = "{SkullOwner:{Id:\""
                            + UUID.randomUUID().toString()
                            + "\",Properties:{textures:[{Value:\""
                            + value + "\"}]}}}";
                    section.set("nbt", nbt);
                    section.set("item", Material.PLAYER_HEAD.name());
                    section.set("value", null);
                    break;
                case "item":
                    if (color != null) {
                        String dyeName = dyeColor.name() + "_" + value;
                        if (conversionMap.containsKey(dyeName)) {
                            section.set("item", Material.getMaterial(
                                    conversionMap.get(dyeName)
                            ).name());
                            break;
                        }
                        if (Material.getMaterial(dyeName) != null) {
                            section.set("item", Material.getMaterial(dyeName)
                                    .name());
                            break;
                        }
                    }
                    if (Material.getMaterial(value) != null) {
                        section.set("item", Material.getMaterial(value).name());
                        break;
                    }
                    if (Material.getMaterial(value, true) != null) {
                        section.set("item", Material.getMaterial(value, true)
                                .name());
                        break;
                    }
                    getPluginUtil().getLogger().log(Level.SEVERE,
                            "Could not find a matching material for: ''{0}''",
                            value);
                    break;

            }
            section.set("type", null);
            section.set("value", null);
            guiFile.SaveConfig();
        }

        /**
         * Get the display name from file
         *
         * @param replaceMap Parses values in the display name with these
         * values. The keys are being parsed with a % in front in the display
         * name
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

        public ItemStack getItem() {
            return item;
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
     * Sets the display name and lore on a item stack
     *
     * @param itemStack The ItemStack to change
     * @param displayName The display name to set
     * @param lore The lore to set, can be null to not apply it on the item
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
     * @param name The item to retrieve
     * @param replaceMap The items to replace in the display name and lore
     *
     * @return The parsed ItemStack
     */
    public ItemStack getItemStack(String name, Map<String, String> replaceMap) {

        GUIElementInformation elementInformation
                = new GUIElementInformation(name);

        ItemStack item = elementInformation.getItem();

        if (item == null) {
            return null;
        }

        String displayName = elementInformation.getDisplayName(replaceMap);
        List<String> lore = elementInformation.getLore();

        setDataOnItemStack(item, displayName, lore);
        return item;
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
     * @param name The item name
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
