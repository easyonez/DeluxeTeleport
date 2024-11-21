package com.pixesoj.deluxeteleport.managers.filesmanager;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LocationsManager {
    private DeluxeTeleport plugin;
    private String fileName = "locations.yml";
    private FileConfiguration fileConfiguration;
    private File file;

    public LocationsManager(DeluxeTeleport plugin) {
        this.plugin = plugin;
    }

    public void saveLocationsFile() {
        if (fileConfiguration != null && file != null) {
            try {
                fileConfiguration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public FileConfiguration getLocationsConfig() {
        return fileConfiguration;
    }

    public File getLocationsFile(){
        return file;
    }
}