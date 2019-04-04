package me.killje.spigotgui.util;

import de.tr7zw.itemnbtapi.NBTCompound;
import de.tr7zw.itemnbtapi.NBTItem;
import de.tr7zw.itemnbtapi.NBTListCompound;
import de.tr7zw.itemnbtapi.NBTType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Patrick Beuks (killje) <code@beuks.net>
 */
public class HeadUtil {

    private final static Map<String, ItemStack> LOADED_PLAYER_HEADS
            = new HashMap<>();
    private final static int MAX_REQUESTS_PER_INTERVAL = 500;
    private final static int REQUEST_INTERVAL_MILISECONDS = 1800000;
    private final static HashMap<String, ItemStack> TEXTURES = new HashMap<>();

    private final static ItemStack BASE_HEAD;

    private static long requestTimestamp = System.currentTimeMillis();
    private static int requests = 0;

    public static class AsyncPlayerHead extends ItemStack {

        private final ItemStack original;
        private final OfflinePlayer player;

        public AsyncPlayerHead(OfflinePlayer player)
                throws IllegalArgumentException {

            super(getBaseHead());
            original = getBaseHead();
            this.player = player;
        }

        protected ItemStack getOriginal() {
            return original;
        }

        /**
         * Load the player head item stack and the original player head
         *
         * This is because the item stack meta data might have changed
         */
        public void loadHead() {
            setPlayerHead(this, player);
            setPlayerHead(original, player);
        }
    }

    static {

        String versionString = Bukkit.getServer().getClass().getPackage()
                .getName().replaceAll("\\D", "");

        if (Integer.parseInt(versionString) < 1130) {
            // -1.12
            BASE_HEAD = new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, (short) 3);
        } else {
            // 1.13+
            BASE_HEAD = new ItemStack(Material.PLAYER_HEAD);
        }
    }

    private static ItemStack getBaseHead() {
        return BASE_HEAD.clone();
    }

    private static void setPlayerHead(ItemStack head, OfflinePlayer player) {
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        if (skullMeta == null) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not get ItemMeta from skull!");
            return;
        }
        skullMeta.setOwningPlayer(player);
        head.setItemMeta(skullMeta);
    }

    @Deprecated
    public static ItemStack getPlayerHead(OfflinePlayer player, Plugin plugin) {

        String playerUUID = player.getUniqueId().toString();

        if (LOADED_PLAYER_HEADS.containsKey(playerUUID)) {
            return LOADED_PLAYER_HEADS.get(playerUUID).clone();
        }

        if (requestTimestamp + REQUEST_INTERVAL_MILISECONDS < System.
                currentTimeMillis()) {
            requests = 0;
            requestTimestamp = System.currentTimeMillis();
        }

        if (requests > MAX_REQUESTS_PER_INTERVAL) {
            return getBaseHead();
        }

        requests++;

        HeadUtil.AsyncPlayerHead baseHead = new HeadUtil.AsyncPlayerHead(player);

        LOADED_PLAYER_HEADS.put(playerUUID, baseHead.getOriginal());

        return baseHead;
    }

    /**
     * Gets the player head for the given player
     *
     * @param player The player head to get
     *
     * @return ItemStack for a player head
     */
    public static ItemStack getPlayerHead(OfflinePlayer player) {

        String playerUUID = player.getUniqueId().toString();

        if (LOADED_PLAYER_HEADS.containsKey(playerUUID)) {
            return LOADED_PLAYER_HEADS.get(playerUUID).clone();
        }

        if (requestTimestamp + REQUEST_INTERVAL_MILISECONDS < System.
                currentTimeMillis()) {
            requests = 0;
            requestTimestamp = System.currentTimeMillis();
        }

        if (requests > MAX_REQUESTS_PER_INTERVAL) {
            return getBaseHead();
        }

        requests++;

        AsyncPlayerHead baseHead = new AsyncPlayerHead(player);

        LOADED_PLAYER_HEADS.put(playerUUID, baseHead.getOriginal());

        return baseHead;
    }

    /**
     * Gets a textured head for the given texture
     *
     * @param texture The texture to get
     *
     * @return The textured player head
     */
    public static ItemStack getTexturedHead(String texture) {

        if (TEXTURES.containsKey(texture)) {
            return TEXTURES.get(texture).clone();
        }

        String uuid = UUID.randomUUID().toString();
        ItemStack head = getBaseHead();

        NBTItem headNBT = new NBTItem(head);
        NBTCompound skull = headNBT.addCompound("SkullOwner");
        skull.setString("Id", uuid);
        NBTListCompound skull_properties_textures = skull
                .addCompound("Properties")
                .getList("textures", NBTType.NBTTagCompound)
                .addCompound();
        skull_properties_textures.setString("Value", texture);
        head = headNBT.getItem();

        TEXTURES.put(texture, head.clone());

        return head;

    }

    /**
     * Gets a textured head for the given texture
     *
     * @param texture The texture to get
     * @param displayName The display name for the item
     *
     * @return The textured player head
     */
    @Deprecated
    public static ItemStack getTexturedHead(
            String texture, String displayName) {
        return getTexturedHead(texture, displayName, null);
    }

    /**
     * Gets a textured head for the given texture
     *
     * @param texture The texture to get
     * @param displayName The display name for the item
     * @param lore The lore for the item
     *
     * @return The textured player head
     */
    @Deprecated
    public static ItemStack getTexturedHead(String texture, String displayName,
            List<String> lore) {

        if (TEXTURES.containsKey(texture)) {
            ItemStack head = TEXTURES.get(texture).clone();
            ItemMeta itemMeta = head.getItemMeta();
            itemMeta.setDisplayName(displayName);
            if (lore != null) {
                itemMeta.setLore(lore);
            }
            head.setItemMeta(itemMeta);
            return head;
        }

        String uuid = UUID.randomUUID().toString();
        ItemStack head = getBaseHead();

        NBTItem headNBT = new NBTItem(head);

        NBTCompound skull = headNBT.addCompound("SkullOwner");
        skull.setString("Id", uuid);
        NBTListCompound skull_properties_textures = skull
                .addCompound("Properties")
                .getList("textures", NBTType.NBTTagCompound)
                .addCompound();
        skull_properties_textures.setString("Value", texture);
        head = headNBT.getItem();

        TEXTURES.put(texture, head.clone());
        ItemMeta itemMeta = head.getItemMeta();
        itemMeta.setDisplayName(displayName);
        if (lore != null) {
            itemMeta.setLore(lore);
        }
        head.setItemMeta(itemMeta);

        return head;

    }

}
