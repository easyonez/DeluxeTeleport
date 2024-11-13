package com.pixesoj.deluxeteleport.managers.filesmanager;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.utils.Comments;
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
import java.util.ArrayList;
import java.util.List;

public class ConfigHomeManager {
    private final FileManager configFile;
    private final DeluxeTeleport plugin;
    MessagesManager m;

    private boolean HighPriority;
    private boolean Enabled;
    private boolean ForceDisable;
    private String DefaultName;
    private int DefaultMaxHomes;
    private boolean OverwriteExistingHome;
    private String TeleportDelay;
    private boolean TeleportDelayEnabled;
    private boolean TeleportDelayCancelOnMove;

    private boolean CooldownEnabled;
    private String CooldownTime;
    private boolean CooldownByRankEnabled;
    private boolean CooldownByRankAutoRanksEnabled;
    private String CooldownByRankAutoRanksPermissionPlugin;
    private String CooldownByRankPrioritizeTime;
    private boolean CooldownSavePlayerData;

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

    public ConfigHomeManager(DeluxeTeleport plugin) {
        this.plugin = plugin;
        this.m = new MessagesManager("&8[&bDeluxeTeleport&8] ", plugin);
        configFile = new FileManager("home.yml", "configs", plugin);
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

        changed = setConfig(changed, "enabled", true);
        changed = setConfig(changed, "force_disable", false);
        changed = setConfig(changed, "high_priority", false);
        changed = setConfig(changed, "commands_alias.home", new ArrayList<>());
        changed = setConfig(changed, "commands_alias.homes", new ArrayList<>());
        changed = setConfig(changed, "commands_alias.sethome", new ArrayList<>());
        changed = setConfig(changed, "commands_alias.delhome", new ArrayList<>());
        changed = setConfig(changed, "default_name", "Home");
        changed = setConfig(changed, "default_max_homes", 2);
        changed = setConfig(changed, "overwrite_existing_home", false);
        changed = setConfig(changed, "teleport_delay.enabled", false);
        changed = setConfig(changed, "teleport_delay.time", "3s");
        changed = setConfig(changed, "teleport_delay.cancel_on_move", true);
        changed = setConfig(changed, "cooldown.enabled", false);
        changed = setConfig(changed, "cooldown.time", "10m");

        ServerVersion serverVersion = ServerInfo.getServerVersion();
        if (ServerVersion.serverVersionGreaterEqualThan(serverVersion, ServerVersion.v1_18_1)) {
            //addComments(config);
        }

        if (changed) {
            OtherUtils.updateConfig(plugin, "Home");
            createFile("home-new.yml", "configs/home.yml", plugin);
            File tempFile = new File(plugin.getDataFolder(), "home-new.yml");

            try {
                YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(tempFile), StandardCharsets.UTF_8));
                this.saveConfig();
                m.sendMessage(Bukkit.getConsoleSender(), plugin.getMainMessagesManager().getGlobalUpdatedConfig()
                        .replace("%config%", "Home"), true);

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

            config.setComments("enabled", Comments.LobbyEnabled);
            config.setComments("config_version", Comments.LobbyConfigVersion);

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

        Enabled = config.getBoolean("enabled");
        ForceDisable = config.getBoolean("force_disable");
        HighPriority = config.getBoolean("high_priority", false);
        DefaultName = config.getString("default_name");
        DefaultMaxHomes = config.getInt("default_max_homes");
        OverwriteExistingHome = config.getBoolean("overwrite_existing_home");

        TeleportDelayEnabled = config.getBoolean("teleport_delay.enabled");
        TeleportDelay = config.getString("teleport_delay.time");
        TeleportDelayCancelOnMove = config.getBoolean("teleport_delay.cancel_on_move");

        CooldownEnabled = config.getBoolean("cooldown.enabled");
        CooldownTime = config.getString("cooldown.time");
        CooldownByRankEnabled = config.getBoolean("cooldown.by_rank.enabled");
        CooldownByRankAutoRanksEnabled = config.getBoolean("cooldown.by_rank.auto_ranks.enabled");
        CooldownByRankAutoRanksPermissionPlugin = config.getString("cooldown.by_rank.auto_ranks.permissions_plugin");
        CooldownByRankPrioritizeTime = config.getString("cooldown.by_rank.prioritize_time");
        CooldownSavePlayerData = config.getBoolean("cooldown.save_playerdata");
    }

    public boolean isEnabled() {
        return Enabled;
    }

    public boolean isForceDisable() {
        return ForceDisable;
    }

    public String getDefaultName() {
        return DefaultName;
    }

    public int getDefaultMaxHomes() {
        return DefaultMaxHomes;
    }

    public boolean isOverwriteExistingHome() {
        return OverwriteExistingHome;
    }

    public String getTeleportDelay() {
        return TeleportDelay;
    }

    public boolean isTeleportDelayEnabled() {
        return TeleportDelayEnabled;
    }

    public boolean isTeleportDelayCancelOnMove() {
        return TeleportDelayCancelOnMove;
    }


    public boolean isCooldownEnabled() {
        return CooldownEnabled;
    }

    public String getCooldownTime() {
        return CooldownTime;
    }

    public boolean isCooldownSavePlayerData() {
        return CooldownSavePlayerData;
    }

    public boolean isCooldownByRankAutoRanksEnabled() {
        return CooldownByRankAutoRanksEnabled;
    }

    public String getCooldownByRankAutoRanksPermissionPlugin() {
        return CooldownByRankAutoRanksPermissionPlugin;
    }

    public String getCooldownByRankPrioritizeTime() {
        return CooldownByRankPrioritizeTime;
    }

    public boolean isCooldownByRankEnabled() {
        return CooldownByRankEnabled;
    }

    public boolean isHighPriority() {
        return HighPriority;
    }
}