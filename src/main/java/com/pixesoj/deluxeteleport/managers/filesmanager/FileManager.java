package com.pixesoj.deluxeteleport.managers.filesmanager;


import com.pixesoj.deluxeteleport.DeluxeTeleport;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileManager {
    private DeluxeTeleport plugin;
    private String fileName;
    private FileConfiguration fileConfiguration = null;
    private File file = null;
    private String folderName;
    private boolean create;

    public FileManager(String fileName, String folderName, DeluxeTeleport plugin) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.folderName = folderName;
        this.create = true;
        this.registerFile();
    }

    public FileManager(String fileName, String folderName, boolean create, DeluxeTeleport plugin) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.folderName = folderName;
        this.create = create;
        this.registerFile();
    }

    public void registerFile() {
        if (folderName != null) {
            file = new File(plugin.getDataFolder() + File.separator + folderName, fileName);
        } else {
            file = new File(plugin.getDataFolder(), fileName);
        }

        if (folderName != null) {
            File folder = new File(plugin.getDataFolder(), folderName);
            if (!folder.exists()) {
                folder.mkdirs();
            }
        }

        if (create) {
            if (!file.exists()) {
                String resourcePath = folderName != null ? folderName + "/" + fileName : fileName;
                if (isResourcePresent(resourcePath)) {
                    if (folderName != null) {
                        plugin.saveResource(folderName + File.separator + fileName, false);
                    } else {
                        plugin.saveResource(fileName, false);
                    }
                } else {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            fileConfiguration = YamlConfiguration.loadConfiguration(file);

            try {
                fileConfiguration.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveConfig() {
        if (fileConfiguration != null && file != null) {
            try {
                fileConfiguration.save(file);
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to save file: " + file.getPath());
                e.printStackTrace();
            }
        }
    }

    public FileConfiguration getConfig() {
        if (fileConfiguration == null) {
            reloadConfig();
        }
        return fileConfiguration;
    }

    public void reloadConfig() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
        if (file != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(file);
            fileConfiguration.setDefaults(defConfig);
        }
    }

    public File getFile() {
        return file;
    }

    public boolean isResourcePresent(String resourcePath) {
        return plugin.getResource(resourcePath) != null;
    }
}