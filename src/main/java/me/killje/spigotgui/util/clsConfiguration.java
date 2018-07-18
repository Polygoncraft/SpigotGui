package me.killje.spigotgui.util;

import com.google.common.io.ByteStreams;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * Util class for loading yaml configurations
 *
 * @author Patrick Beuks (killje) <patrick.beuks@gmail.com>
 *
 * This is not original my file. But the source is unknown and has been heavily
 * edited
 */
public class clsConfiguration {

    /**
     * The loaded configuration
     */
    private FileConfiguration conf = null;
    /**
     * The file to load the configuration from
     */
    private File file = null;
    /**
     * The file name
     */
    private String fname = null;
    /**
     * The Plugin for which to load the file from
     */
    private final Plugin plugin;

    /**
     * Loads a configuration in the plugin directory or in the plugin itself
     *
     * It will always use the configuration from the package as default
     *
     * @param plugin   The plugin to load the configuration from
     * @param filename The name of the file to load
     */
    public clsConfiguration(Plugin plugin, String filename) {
        this(plugin, filename, false);
    }

    /**
     * Loads a configuration in the plugin directory or in the plugin itself.
     *
     * It will always use the configuration from the package as default
     *
     * @param plugin      The plugin to load the configuration from
     * @param filename    The name of the file to load
     * @param saveDefault If true and if the configuration does not exists in
     *                    the directory it will create a new file there with the
     *                    information from the file in the plugin jar itself
     */
    public clsConfiguration(
            Plugin plugin, String filename, boolean saveDefault) {

        this.plugin = plugin;
        fname = filename;
        if (saveDefault) {
            file = new File(plugin.getDataFolder(), fname);
            if (!file.exists()) {
                InputStream isDefaults = plugin.getResource(fname);
                try {
                    try (FileOutputStream outputStream = new FileOutputStream(
                            file)) {
                        ByteStreams.copy(isDefaults, outputStream);
                        outputStream.flush();
                        isDefaults.close();
                    }
                } catch (FileNotFoundException ex) {
                    plugin.getLogger().log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    plugin.getLogger().log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Returns the configuration
     *
     * @return The configuration.
     */
    public FileConfiguration GetConfig() {
        if (conf == null) {
            ReloadConfig();	//If it's null, try reloading the configs
        }
        return conf;
    }

    /**
     * This loads the config file and search for the config in the jar package
     */
    public void ReloadConfig() {
        if (file == null) {
            file = new File(plugin.getDataFolder(), fname);
        }

        conf = YamlConfiguration.loadConfiguration(file);

        //Look for defaults in the jar
        InputStream isDefaults = plugin.getResource(fname);
        if (isDefaults != null) {
            Reader reader = new InputStreamReader(isDefaults);
            YamlConfiguration confDefault = YamlConfiguration.loadConfiguration(
                    reader);
            conf.setDefaults(confDefault);
        }
    }

    /**
     * Saves changes made to the config file and puts them in the plugin
     * directory
     */
    public void SaveConfig() {
        if (conf == null || file == null) {
            return;
        }
        try {
            conf.save(file); //Save the memory configurations to the config file
        } catch (IOException ex) {
            plugin.getLogger().log(
                    Level.SEVERE,
                    "IOException: Error saving configuration file '{0}'!",
                    fname
            );

            plugin.getLogger().log(Level.SEVERE, ex.toString());
        }
    }
}
