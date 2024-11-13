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


public class ConfigLobbyManager {
    private final FileManager configFile;
    private final DeluxeTeleport plugin;
    MessagesManager m;

    public boolean Enabled;

    private boolean HighPriority;
    private String TeleportDelay;
    private boolean TeleportDelayEnabled;
    private boolean TeleportDelayCancelOnMove;
    private boolean MultipleLobbies;
    private boolean TeleportInMultiple;
    private String TeleportInMultipleSpecific;
    private String TeleportInMultipleSpecificType;
    private boolean ForceDisable;
    private String LobbyMode;
    private String SenderServer;

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

    public FileConfiguration getConfig(){
        return configFile.getConfig();
    }

    public void saveConfig(){
        configFile.saveConfig();
        loadLobbyConfig();
    }

    public ConfigLobbyManager(DeluxeTeleport plugin) {
        this.plugin = plugin;
        this.m = new MessagesManager("&8[&bDeluxeTeleport&8] ", plugin);
        configFile = new FileManager("lobby.yml", "configs", plugin);
        configFile.registerFile();
        loadLobbyConfig();
    }

    private <T> boolean setConfig(boolean changed, String path, T value) {
        FileConfiguration config = this.getConfig();
        if (!config.contains(path)){
            config.set(path, value);
            changed = true;
        }
        return changed;
    }

    public void updateLobbyConfig() {
        FileConfiguration config = getConfig();
        boolean changed = addMissingFields(config, config);

        changed = setConfig(changed, "enabled", true);
        changed = setConfig(changed, "force_disable", false);
        changed = setConfig(changed, "high_priority", false);
        changed = setConfig(changed, "commands_alias", new ArrayList<>());
        changed = setConfig(changed, "lobby_mode.mode", "Server");
        changed = setConfig(changed, "lobby_mode.server", "lobby");
        changed = setConfig(changed, "multiple_lobbies", false);
        changed = setConfig(changed, "teleport_in_multiple.enabled", false);
        changed = setConfig(changed, "teleport_in_multiple.lobby", "General");
        changed = setConfig(changed, "teleport_in_multiple.specify", "Lobby");
        changed = setConfig(changed, "teleport_delay.enabled", true);
        changed = setConfig(changed, "teleport_delay.time", "3s");
        changed = setConfig(changed, "teleport_delay.cancel_on_move", true);
        changed = setConfig(changed, "cooldown.enabled", true);
        changed = setConfig(changed, "cooldown.time", "10m");

        ServerVersion serverVersion = ServerInfo.getServerVersion();
        if (ServerVersion.serverVersionGreaterEqualThan(serverVersion, ServerVersion.v1_18_1)) {
            //addComments(config);
        }

        if (changed) {
            OtherUtils.updateConfig(plugin, "Lobby");
            createFile("lobby-new.yml", "configs/lobby.yml", plugin);
            File tempFile = new File(plugin.getDataFolder(), "lobby-new.yml");

            try {
                YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(tempFile), StandardCharsets.UTF_8));
                this.saveConfig();
                m.sendMessage(Bukkit.getConsoleSender(), plugin.getMainMessagesManager().getGlobalUpdatedConfig()
                        .replace("%config%", "Lobby"), true);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                tempFile.delete();
            }
        }
    }

    public void addComments (FileConfiguration config){
        try {
            Class<?>[] paramString = new Class[] { String.class, List.class };
            config.getClass().getMethod("setComments", paramString);

            config.setComments("enabled", Comments.LobbyEnabled);
            config.setComments("force_disable", Comments.LobbyForceDisable);
            config.setComments("commands_alias", Comments.LobbyCommandAlias);
            config.setComments("lobby_mode", Comments.LobbyMode);
            config.setComments("lobby_mode.mode", Comments.LobbyModeMode);
            config.setComments("lobby_mode.server", Comments.LobbySendServers);
            config.setComments("multiple_lobbies", Comments.LobbyMultiple);
            config.setComments("teleport_in_multiple", Comments.LobbyTeleportInMultiple);
            config.setComments("teleport_in_multiple.enabled", Comments.LobbyTeleportInMultipleEnabled);
            config.setComments("teleport_in_multiple.lobby", Comments.LobbyTeleportInMultipleLobby);
            config.setComments("teleport_in_multiple.specify", Comments.LobbyTeleportInMultipleSpecify);
            config.setComments("teleport_delay", Comments.LobbyTeleportDelay);
            config.setComments("teleport_delay.enabled", Comments.LobbyTeleportDelayEnabled);
            config.setComments("teleport_delay.seconds", Comments.LobbyTeleportDelaySeconds);
            config.setComments("teleport_delay.cancel_on_move", Comments.LobbyTeleportDelayCancelOnMove);
            config.setComments("teleport_delay.blindness", Comments.LobbyTeleportDelayBlindness);
            config.setComments("teleport_delay.blindness_time", Comments.LobbyTeleportDelayBlindnessTime);
            config.setComments("teleport_delay.message_type", Comments.LobbyTeleportDelayMessageType);
            config.setComments("cooldown", Comments.LobbyCooldown);
            config.setComments("cooldown.enabled", Comments.LobbyCooldownEnabled);
            config.setComments("cooldown.time", Comments.LobbyCooldownTime);

        } catch (NoSuchMethodException ignored){}
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
        HighPriority = config.getBoolean("high_priority", false);
        MultipleLobbies = config.getBoolean("multiple_lobbies");
        ForceDisable = config.getBoolean("force_disable");
        LobbyMode = config.getString("lobby_mode.mode");
        SenderServer = config.getString("lobby_mode.server");

        TeleportDelayEnabled = config.getBoolean("teleport_delay.enabled");
        TeleportDelay = config.getString("teleport_delay.time");
        TeleportDelayCancelOnMove = config.getBoolean("teleport_delay.cancel_on_move");

        TeleportInMultiple = config.getBoolean("teleport_in_multiple.enabled");
        TeleportInMultipleSpecific = config.getString("teleport_in_multiple.lobby");
        TeleportInMultipleSpecificType = config.getString("teleport_in_multiple.specify");

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

    public boolean isMultipleLobbies() {
        return MultipleLobbies;
    }

    public boolean isTeleportInMultiple() {
        return TeleportInMultiple;
    }

    public String getTeleportInMultipleSpecific() {
        return TeleportInMultipleSpecific;
    }

    public String getTeleportInMultipleSpecificType() {
        return TeleportInMultipleSpecificType;
    }

    public boolean isForceDisable() {
        return ForceDisable;
    }

    public String getLobbyMode() {
        return LobbyMode;
    }


    public String getSenderServer() {
        return SenderServer;
    }

    public boolean isCooldownSavePlayerData() {
        return CooldownSavePlayerData;
    }

    public String getCooldownByRankPrioritizeTime() {
        return CooldownByRankPrioritizeTime;
    }

    public String getCooldownByRankAutoRanksPermissionPlugin() {
        return CooldownByRankAutoRanksPermissionPlugin;
    }

    public boolean isCooldownByRankAutoRanksEnabled() {
        return CooldownByRankAutoRanksEnabled;
    }

    public boolean isCooldownByRankEnabled() {
        return CooldownByRankEnabled;
    }

    public boolean isHighPriority() {
        return HighPriority;
    }
}
