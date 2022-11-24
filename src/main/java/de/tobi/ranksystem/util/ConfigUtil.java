package de.tobi.ranksystem.util;

import de.tobi.ranksystem.main.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigUtil {

    private Main plugin;

    private File file = new File("plugins/RankSystem/", "config.yml");
    private File folder = new File("plugins/RankSystem/");
    private FileConfiguration config;

    public ConfigUtil(Main plugin) {
        this.plugin = plugin;
        try {
            if(!file.exists()) {
                folder.mkdirs();
                file.createNewFile();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        config = YamlConfiguration.loadConfiguration(file);
        if(config.getString("MySQL.host") == null) {
            config.set("MySQL.host", "localhost");
            config.set("MySQL.user", "root");
            config.set("MySQL.password", "password");
            config.set("MySQL.database", "database");
            config.set("MySQL.port", "3306");
            try{
                config.save(file);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }


}
