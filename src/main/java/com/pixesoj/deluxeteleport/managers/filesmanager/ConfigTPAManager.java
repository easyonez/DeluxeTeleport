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

public class ConfigTPAManager {
    private final FileManager configFile;
    private final DeluxeTeleport plugin;
    MessagesManager m;


    private boolean HighPriority;
    private boolean Enabled;
    private boolean ForceDisable;
    private boolean TpaHimself;
    private boolean IgnoreTpaStatus;
    private boolean ClickTpa;
    private boolean CenteredRequestMessage;
    private boolean GeyserSuport;
    private boolean GeyserClickTpa;
    private boolean GeyserDiferentMessage;
    private boolean StatusDefault;
    private boolean ExpirationEnabled;
    private String ExpirationTime;
    private boolean MultipleTPA;
    private String ListPosition;
    private boolean DelayEnabled;
    private String DelayTime;
    private boolean DelayCancelOnMove;
    public boolean TeleportSoundEnabled;
    public String TeleportSound;
    public int TeleportSoundVolume;
    public int TeleportSoundPitch;
    public String TeleportSoundSend;
    public boolean CommandsEnabled;
    public List<String> CommandsPlayer;
    public List<String> CommandsTargetPlayer;
    private boolean AutocompleteNames;

    public List<String> CommandsConsole;
    public String CommandsSend;

    private boolean CooldownEnabled;
    private String CooldownTime;
    private boolean CooldownCountWhenTeleporting;
    private boolean CooldownByRankEnabled;
    private boolean CooldownByRankAutoRanksEnabled;
    private String CooldownByRankAutoRanksPermissionPlugin;
    private String CooldownByRankPrioritizeTime;
    private String CooldownTo;
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

    public ConfigTPAManager(DeluxeTeleport plugin) {
        this.plugin = plugin;
        this.m = new MessagesManager("&8[&bDeluxeTeleport&8] ", plugin);
        configFile = new FileManager("tpa.yml", "configs", plugin);
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

    public void updateTeleportConfig() {
        FileConfiguration config = getConfig();
        boolean changed = addMissingFields(config, config);

        List<String> commandsAliasTPA = new ArrayList<>();
        commandsAliasTPA.add("tprequest");

        changed = setConfig(changed, "enabled", true);
        changed = setConfig(changed, "force_disable", false);
        changed = setConfig(changed, "high_priority", false);
        changed = setConfig(changed, "commands_alias.tpa", commandsAliasTPA);
        changed = setConfig(changed, "commands_alias.tpaccept", new ArrayList<>());
        changed = setConfig(changed, "commands_alias.tpacancel", new ArrayList<>());
        changed = setConfig(changed, "commands_alias.tpahere", new ArrayList<>());
        changed = setConfig(changed, "commands_alias.tpatoggle", new ArrayList<>());
        changed = setConfig(changed, "autocomplete_names", true);
        changed = setConfig(changed, "status_default", true);
        changed = setConfig(changed, "list_position", "Last");
        changed = setConfig(changed, "send.tpa_himself", false);
        changed = setConfig(changed, "send.ignore_tpa_status", false);
        changed = setConfig(changed, "send.multiple_tpa", true);
        changed = setConfig(changed, "request.click_tpa", true);
        changed = setConfig(changed, "request.centered_message", true);
        changed = setConfig(changed, "expiration.enabled", true);
        changed = setConfig(changed, "expiration.time", "3m");
        changed = setConfig(changed, "delay.enabled", true);
        changed = setConfig(changed, "delay.time", "3s");
        changed = setConfig(changed, "delay.cancel_on_move", true);
        changed = setConfig(changed, "cooldown.enabled", false);
        changed = setConfig(changed, "cooldown.time", "10m");
        changed = setConfig(changed, "cooldown.count_when_teleporting", true);
        changed = setConfig(changed, "cooldown.by_rank.enabled", false);
        changed = setConfig(changed, "cooldown.by_rank.in_multiple.accept_pending", false);
        changed = setConfig(changed, "cooldown.by_rank.ranks.examplerank", "5m");
        changed = setConfig(changed, "cooldown.by_rank.auto_ranks.enabled", true);
        changed = setConfig(changed, "cooldown.by_rank.auto_ranks.permissions_plugin", "LuckPerms");
        changed = setConfig(changed, "cooldown.by_rank.prioritize_time", "SHORTEST");
        changed = setConfig(changed, "cooldown.coldown_for", "TargetPlayer");
        changed = setConfig(changed, "cooldown.save_playerdata", false);
        changed = setConfig(changed, "bedrock_support.enabled", false);
        changed = setConfig(changed, "bedrock_support.click_tpa", false);
        changed = setConfig(changed, "bedrock_support.different_message", true);

        ServerVersion serverVersion = ServerInfo.getServerVersion();
        if (ServerVersion.serverVersionGreaterEqualThan(serverVersion, ServerVersion.v1_18_1)) {
            //addComments(config);
        }

        if (changed) {
            OtherUtils.updateConfig(plugin, "TPA");
            createFile("tpa-new.yml", "configs/tpa.yml", plugin);
            File tempFile = new File(plugin.getDataFolder(), "tpa-new.yml");

            try {
                YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(tempFile), StandardCharsets.UTF_8));
                this.saveConfig();
                m.sendMessage(Bukkit.getConsoleSender(), plugin.getMainMessagesManager().getGlobalUpdatedConfig()
                        .replace("%config%", "TPA"), true);

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

            config.setComments("enabled", Comments.TPAEnabled);
            config.setComments("force_disable", Comments.TPAForceDisable);
            config.setComments("commands_alias", Comments.TPACommandsAlias);
            config.setComments("status_default", Comments.TPAStatusDefault);
            config.setComments("list_position", Comments.TPAListPosition);
            config.setComments("send", Comments.TPASend);
            config.setComments("send.tpa_himself", Comments.TPASendHimSelf);
            config.setComments("send.ignore_tpa_status", Comments.TPASendIgnoreStatus);
            config.setComments("send.multiple_tpa", Comments.TPAMultipleTPA);
            config.setComments("request", Comments.TPARequest);
            config.setComments("request.click_tpa", Comments.TPARequestClickTPA);
            config.setComments("request.centered_message", Comments.TPACenteredMessage);
            config.setComments("expiration", Comments.TPAExpiration);
            config.setComments("expiration.enabled", Comments.TPAExpirationEnabled);
            config.setComments("expiration.time", Comments.TPAExpirationTime);
            config.setComments("delay", Comments.TPADelay);
            config.setComments("delay.enabled", Comments.TPADelayEnabled);
            config.setComments("delay.time", Comments.TPADelayTime);
            config.setComments("delay.cancel_on_move", Comments.TPADelayCancelOnMove);
            config.setComments("sound_settings", Comments.TPASound);
            config.setComments("sound_settings.enabled", Comments.TPASoundEnabled);
            config.setComments("sound_settings.sound", Comments.TPASoundSound);
            config.setComments("sound_settings.volume", Comments.TPASoundVolume);
            config.setComments("sound_settings.pitch", Comments.TPASoundPitch);
            config.setComments("sound_settings.send_to", Comments.TPASoundSendTo);
            config.setComments("commands", Comments.TPACommands);
            config.setComments("commands.enabled", Comments.TPACommandsEnabled);
            config.setComments("commands.player", Comments.TPACommandsPlayer);
            config.setComments("commands.target_player", Comments.TPACommandsTargetPlayer);
            config.setComments("commands.console", Comments.TPACommandsConsole);
            config.setComments("bedrock_support", Comments.TPAGeyserSupport);
            config.setComments("bedrock_support.enabled", Comments.TPAGeyserSupportEnabled);
            config.setComments("bedrock_support.click_tpa", Comments.TPAGeyserSupportClickTPA);
            config.setComments("bedrock_support.different_message", Comments.TPAGeyserSupportDifferetntMessage);
            config.setComments("config_version", Comments.TPAConfigVersion);
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
        TpaHimself = config.getBoolean("send.tpa_himself");
        IgnoreTpaStatus = config.getBoolean("send.ignore_tpa_status");
        ClickTpa = config.getBoolean("request.click_tpa");
        CenteredRequestMessage = config.getBoolean("request.centered_message");
        GeyserSuport = config.getBoolean("bedrock_support.enabled");
        GeyserClickTpa = config.getBoolean("bedrock_support.click_tpa");
        GeyserDiferentMessage = config.getBoolean("bedrock_support.different_message");
        StatusDefault = config.getBoolean("status_default");
        ExpirationEnabled = config.getBoolean("expiration.enabled");
        ExpirationTime = config.getString("expiration.time");
        MultipleTPA = config.getBoolean("send.multiple_tpa");
        ListPosition = config.getString("list_position");
        DelayEnabled = config.getBoolean("delay.enabled");
        DelayTime = config.getString("delay.time");
        DelayCancelOnMove = config.getBoolean("delay.cancel_on_move");
        TeleportSoundEnabled = config.getBoolean("sound_settings.enabled");
        TeleportSound = config.getString("sound_settings.sound");
        TeleportSoundVolume = config.getInt("sound_settings.volume");
        TeleportSoundPitch = config.getInt("sound_settings.pitch");
        TeleportSoundSend = config.getString("sound_settings.send_to");
        CommandsEnabled = config.getBoolean("commands.enabled");
        CommandsPlayer = config.getStringList("commands.player");
        CommandsTargetPlayer = config.getStringList("commands.target_player");
        CommandsConsole = config.getStringList("commands.console");
        CommandsSend = config.getString("commands.send_to");
        AutocompleteNames = config.getBoolean("autocomplete_names");

        CooldownEnabled = config.getBoolean("cooldown.enabled");
        CooldownTime = config.getString("cooldown.time");
        CooldownCountWhenTeleporting = config.getBoolean("cooldown.count_when_teleporting");
        CooldownByRankEnabled = config.getBoolean("cooldown.by_rank.enabled");
        CooldownByRankAutoRanksEnabled = config.getBoolean("cooldown.by_rank.auto_ranks.enabled");
        CooldownByRankAutoRanksPermissionPlugin = config.getString("cooldown.by_rank.auto_ranks.permissions_plugin");
        CooldownByRankPrioritizeTime = config.getString("cooldown.by_rank.prioritize_time");
        CooldownTo = config.getString("cooldown.coldown_for");
        CooldownSavePlayerData = config.getBoolean("cooldown.save_playerdata");
    }

    public boolean isEnabled() {
        return Enabled;
    }

    public boolean isForceDisable() {
        return ForceDisable;
    }

    public boolean isTpaHimself() {
        return TpaHimself;
    }

    public boolean isIgnoreTpaStatus() {
        return IgnoreTpaStatus;
    }

    public boolean isClickTpa() {
        return ClickTpa;
    }

    public boolean isCenteredRequestMessage() {
        return CenteredRequestMessage;
    }

    public boolean isGeyserSuport() {
        return GeyserSuport;
    }

    public boolean isGeyserClickTpa() {
        return GeyserClickTpa;
    }

    public boolean isGeyserDiferentMessage() {
        return GeyserDiferentMessage;
    }

    public boolean isStatusDefault() {
        return StatusDefault;
    }

    public boolean isExpirationEnabled() {
        return ExpirationEnabled;
    }

    public String getExpirationTime() {
        return ExpirationTime;
    }

    public boolean isMultipleTPA() {
        return MultipleTPA;
    }

    public String getListPosition() {
        return ListPosition;
    }

    public boolean isDelayEnabled() {
        return DelayEnabled;
    }

    public String getDelayTime() {
        return DelayTime;
    }

    public boolean isDelayCancelOnMove() {
        return DelayCancelOnMove;
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

    public boolean isCooldownCountWhenTeleporting() {
        return CooldownCountWhenTeleporting;
    }

    public String getCooldownTime() {
        return CooldownTime;
    }

    public boolean isCooldownEnabled() {
        return CooldownEnabled;
    }

    public String getCooldownByRankPrioritizeTime() {
        return CooldownByRankPrioritizeTime;
    }

    public String getCooldownTo() {
        return CooldownTo;
    }

    public boolean isAutocompleteNames() {
        return AutocompleteNames;
    }

    public boolean isCooldownSavePlayerData() {
        return CooldownSavePlayerData;
    }

    public boolean isHighPriority() {
        return HighPriority;
    }
}