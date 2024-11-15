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
import java.util.*;

public class ConfigSpawnManager {
    private final FileManager configFile;
    private final DeluxeTeleport plugin;
    MessagesManager m;

    private boolean HighPriority;
    private boolean ByWorld;
    private String TeleportDelay;
    private boolean TeleportDelayEnabled;
    private boolean TeleportDelayCancelOnMove;
    private boolean Enabled;
    private boolean TeleportInByWorldEnabled;
    private String TeleportInByWorldSpawn;
    private String TeleportInByWorldSpecify;
    private boolean ForceDisable;
    private boolean FloodgateEnabled;
    private boolean FloodgateSplitSpawn;
    private boolean MenuEnabled;

    private boolean CooldownEnabled;
    private String CooldownTime;
    private boolean CooldownByRankEnabled;
    private boolean CooldownByRankAutoRanksEnabled;
    private String CooldownByRankAutoRanksPermissionPlugin;
    private String CooldownByRankPrioritizeTime;
    private boolean CooldownSavePlayerData;

    public void reloadConfig() {
        configFile.reloadConfig();
        loadSpawnConfig();
    }

    public FileConfiguration getConfig(){
        return configFile.getConfig();
    }

    public void saveConfig(){
        configFile.saveConfig();
        loadSpawnConfig();
    }

    public ConfigSpawnManager(DeluxeTeleport plugin) {
        this.plugin = plugin;
        this.m = new MessagesManager("&8[&bDeluxeTeleport&8] ", plugin);
        configFile = new FileManager("spawn.yml", "configs", plugin);
        configFile.registerFile();
        loadSpawnConfig();
    }

    private <T> boolean setConfig(boolean changed, String path, T value) {
        FileConfiguration config = this.getConfig();
        if (!config.contains(path)){
            config.set(path, value);
            changed = true;
        }
        return changed;
    }

    public void updateSpawnConfig() {
        FileConfiguration config = getConfig();
        boolean changed = addMissingFields(config, config);

        changed = setConfig(changed, "enabled", true);
        changed = setConfig(changed, "force_disable", false);
        changed = setConfig(changed, "high_priority", false);
        changed = setConfig(changed, "commands_alias.spawn", new ArrayList<>());
        changed = setConfig(changed, "commands_alias.spawns", new ArrayList<>());
        changed = setConfig(changed, "aliases", new ArrayList<>());
        changed = setConfig(changed, "by_world", false);
        changed = setConfig(changed, "teleport_in_by_world.enabled", false);
        changed = setConfig(changed, "teleport_in_by_world.spawn", "General");
        changed = setConfig(changed, "teleport_in_by_world.specify", "world");
        changed = setConfig(changed, "teleport_delay.enabled", true);
        changed = setConfig(changed, "teleport_delay.time", "3s");
        changed = setConfig(changed, "teleport_delay.cancel_on_move", true);
        changed = setConfig(changed, "cooldown.enabled", true);
        changed = setConfig(changed, "cooldown.time", "10m");

        getConfig().set("teleport_delay.blindness", null);
        getConfig().set("teleport_delay.blindness_time", null);
        getConfig().set("teleport_delay.message_type", null);
        getConfig().set("sound_settings", null);
        getConfig().set("commands", null);


        ServerVersion serverVersion = ServerInfo.getServerVersion();
        if (ServerVersion.serverVersionGreaterEqualThan(serverVersion, ServerVersion.v1_18_1)) {
            //addComments(config);
        }

        if (changed) {
            OtherUtils.updateConfig(plugin, "Spawn");
            createFile("spawn-new.yml", "configs/spawn.yml", plugin);
            File tempFile = new File(plugin.getDataFolder(), "spawn-new.yml");

            try {
                YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(tempFile), StandardCharsets.UTF_8));
                this.saveConfig();
                m.sendMessage(Bukkit.getConsoleSender(), plugin.getMainMessagesManager().getGlobalUpdatedConfig()
                        .replace("%config%", "Spawn"), true);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                tempFile.delete();
            }
        }
    }

    public void addComments (FileConfiguration config){
        try {
            Class<?>[] paramString = new Class[]{String.class, List.class};
            config.getClass().getMethod("setComments", paramString);

            config.setComments("enabled", Comments.SpawnEnabled);
            config.setComments("force_disable", Comments.SpawnForceDisable);
            config.setComments("commands_alias", Comments.SpawnCommandAlias);
            config.setComments("aliases", Comments.SpawnAlias);
            config.setComments("by_world", Comments.SpawnByWorld);
            config.setComments("teleport_in_by_world", Comments.SpawnTeleportInByWorld);
            config.setComments("teleport_in_by_world.enabled", Comments.SpawnTeleportInByWorldEnabled);
            config.setComments("teleport_in_by_world.lobby", Comments.SpawnTeleportInByWorldSpawn);
            config.setComments("teleport_in_by_world.specify", Comments.SpawnTeleportInByWorldSpecify);
            config.setComments("teleport_delay", Comments.SpawnTeleportDelay);
            config.setComments("teleport_delay.enabled", Comments.SpawnTeleportDelayEnabled);
            config.setComments("teleport_delay.seconds", Comments.SpawnTeleportDelaySeconds);
            config.setComments("teleport_delay.cancel_on_move", Comments.SpawnTeleportDelayCancelOnMove);
            config.setComments("cooldown", Comments.SpawnCooldown);
            config.setComments("cooldown.enabled", Comments.SpawnCooldownEnabled);
            config.setComments("cooldown.time", Comments.SpawnCooldownTime);
            config.setComments("teleport_other_player", Comments.SpawnTeleportOtherPlayer);
            config.setComments("teleport_other_player.blindness", Comments.SpawnTeleportOtherPlayerBlindness);
            config.setComments("teleport_other_player.cooldown", Comments.SpawnTeleportOtherPlayerCooldown);
            config.setComments("teleport_other_player.commands", Comments.SpawnTeleportOtherPlayerCommands);
            config.setComments("teleport_other_player.sound", Comments.SpawnTeleportOtherPlayerSound);
            config.setComments("cost", Comments.SpawnCost);
            config.setComments("cost.money", Comments.SpawnCostMoney);
            config.setComments("cost.money.enabled", Comments.SpawnCostMoneyEnabled);
            config.setComments("cost.money.amount", Comments.SpawnCostMoneyAmount);
            config.setComments("cost.money.is_optional", Comments.SpawnCostMoneyIsOptional);
            config.setComments("menu", Comments.SpawnMenu);
            config.setComments("menu.admin", Comments.SpawnMenuAdmin);
            config.setComments("menu.admin.enabled", Comments.SpawnMenuAdminEnabled);
            config.setComments("config_version", Comments.SpawnConfigVersion);

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


    public void loadSpawnConfig() {
        FileConfiguration config = configFile.getConfig();

        Enabled = config.getBoolean("enabled");
        ByWorld = config.getBoolean("by_world");
        ForceDisable = config.getBoolean("force_disable");
        HighPriority = config.getBoolean("high_priority", false);

        TeleportInByWorldEnabled = config.getBoolean("teleport_in_by_world.enabled");
        TeleportInByWorldSpawn = config.getString("teleport_in_by_world.spawn");
        TeleportInByWorldSpecify = config.getString("teleport_in_by_world.specify");

        TeleportDelayEnabled = config.getBoolean("teleport_delay.enabled");
        TeleportDelay = config.getString("teleport_delay.time");
        TeleportDelayCancelOnMove = config.getBoolean("teleport_delay.cancel_on_move");

        FloodgateEnabled = config.getBoolean("floodgate_support.enabled");
        FloodgateSplitSpawn = config.getBoolean("floodgate_support.split_spawn");

        CooldownEnabled = config.getBoolean("cooldown.enabled");
        CooldownTime = config.getString("cooldown.time");
        CooldownByRankEnabled = config.getBoolean("cooldown.by_rank.enabled");
        CooldownByRankAutoRanksEnabled = config.getBoolean("cooldown.by_rank.auto_ranks.enabled");
        CooldownByRankAutoRanksPermissionPlugin = config.getString("cooldown.by_rank.auto_ranks.permissions_plugin");
        CooldownByRankPrioritizeTime = config.getString("cooldown.by_rank.prioritize_time");
        CooldownSavePlayerData = config.getBoolean("cooldown.save_playerdata");
    }

    public boolean isByWorld() {
        return ByWorld;
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

    public boolean isEnabled() {
        return Enabled;
    }

    public boolean isTeleportInByWorldEnabled() {
        return TeleportInByWorldEnabled;
    }

    public String getTeleportInByWorldSpawn() {
        return TeleportInByWorldSpawn;
    }

    public String getTeleportInByWorldSpecify() {
        return TeleportInByWorldSpecify;
    }

    public boolean isForceDisable() {
        return ForceDisable;
    }

    public boolean isFloodgateEnabled() {
        return FloodgateEnabled;
    }

    public boolean isFloodgateSplitSpawn() {
        return FloodgateSplitSpawn;
    }

    public boolean isMenuEnabled() {
        return MenuEnabled;
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
