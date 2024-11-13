package com.pixesoj.deluxeteleport.managers.filesmanager;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.utils.OtherUtils;
import com.pixesoj.deluxeteleport.utils.ServerInfo;
import com.pixesoj.deluxeteleport.utils.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class ConfigMenuManager {
    private final FileManager configFile;
    private final DeluxeTeleport plugin;
    MessagesManager m;

    private boolean registerCommands;

    public void reloadConfig() {
        configFile.reloadConfig();
        loadLobbyConfig();
    }

    public FileConfiguration getConfig() {
        return configFile.getConfig();
    }

    public void saveConfig() {
        configFile.saveConfig();
        loadLobbyConfig();
    }

    public ConfigMenuManager(DeluxeTeleport plugin) {
        this.plugin = plugin;
        this.m = new MessagesManager("&8[&bDeluxeTeleport&8] ", plugin);
        configFile = new FileManager("menus.yml", null, plugin);
        configFile.registerFile();
        loadLobbyConfig();
    }

    private <T> boolean setConfig(boolean changed, String path, T value) {
        FileConfiguration config = this.getConfig();
        if (!config.contains(path)) {
            config.set(path, value);
            changed = true;
        }
        return changed;
    }

    public void updateHomeConfig() {
        FileConfiguration config = getConfig();
        boolean changed = addMissingFields(config, config);

        changed = setConfig(changed, "register_commands", true);

        ServerVersion serverVersion = ServerInfo.getServerVersion();
        if (ServerVersion.serverVersionGreaterEqualThan(serverVersion, ServerVersion.v1_18_1)) {
            addComments(config);
        }

        if (changed) {
            OtherUtils.updateConfig(plugin, "Menus");
            createFile("menus-new.yml", "menus.yml", plugin);
            File tempFile = new File(plugin.getDataFolder(), "menus-new.yml");

            try {
                YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(tempFile), StandardCharsets.UTF_8));
                this.saveConfig();
                m.sendMessage(Bukkit.getConsoleSender(), plugin.getMainMessagesManager().getGlobalUpdatedConfig()
                        .replace("%config%", "Menus"), true);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                tempFile.delete();
            }
        }
    }

    public void addComments(FileConfiguration config) {
        try {
            Class<?>[] paramString = new Class[]{String.class, List.class};
            config.getClass().getMethod("setComments", paramString);



        } catch (NoSuchMethodException ignored) {
        }
    }


    private boolean addMissingFields(FileConfiguration currentConfig, FileConfiguration newConfig) {
        boolean changed = false;
        for (String key : newConfig.getKeys(true)) {
            if (!currentConfig.contains(key)) {
                currentConfig.set(key, newConfig.get(key));
                changed = true;
            }
        }
        return changed;
    }

    private void createFile(String name, String from, DeluxeTeleport plugin) {
        File file = new File(plugin.getDataFolder(), name);
        if (!file.exists()) {
            try {
                Files.copy(plugin.getResource(from), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                m.sendMessage(Bukkit.getConsoleSender(), name + " file for DeluxeTeleport!" + e, true);
            }
        }
    }

    public void loadLobbyConfig() {
        FileConfiguration config = configFile.getConfig();

        registerCommands = config.getBoolean("register_commands", true);
    }

    public boolean isRegisterCommands() {
        return registerCommands;
    }
}