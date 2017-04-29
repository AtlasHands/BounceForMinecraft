package com.keefersands.bounce;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;

/**
 * Created by Alpine Tree on 4/29/2017.
 */
public class FileHandler{
    private FileConfiguration customConfig = null;
    private File customConfigFile = null;
    private Plugin plugin;
    public FileHandler(Plugin plugin){
        this.plugin = plugin;
    }
    public void save(){
        File dir = new File("./plugins/Bounce");
        if(dir == null){
            dir.mkdir();
            File newf = new File("./plugins/Bounce/test.txt");
        }
        else{
            plugin.getConfig().set("path.to.boolean", true);
        }
        plugin.saveConfig();

    }
    public void reloadCustomConfig() {
        if (customConfigFile == null) {
            customConfigFile = new File("./plugins/Bounce/", "customConfig.yml");
        }
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

        // Look for defaults in the jar
        Reader defConfigStream = new InputStreamReader(plugin.getResource("customConfig.yml"));
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            customConfig.setDefaults(defConfig);
        }
    }
    public FileConfiguration getCustomConfig() {
        if (customConfig == null) {
            reloadCustomConfig();
        }
        return customConfig;
    }
    public void saveCustomConfig() {
        if (customConfig == null || customConfigFile == null) {
            return;
        }
        try {
            getCustomConfig().save(customConfigFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
        }
    }
}
