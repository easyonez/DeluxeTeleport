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
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private final DeluxeTeleport plugin;
    MessagesManager m;

    private final FileManager configFile;

    private boolean UpdateNotify;
    private boolean UpdateChangelogNotify;
    private boolean UpdateAutoUpdate;
    private String UpdateCheckInterval;
    private boolean UpdateRestartEnabled;
    private boolean UpdateRestartOnlyOnStart;
    private boolean UpdateConfigs;
    private boolean UpdateMessages;
    private boolean UpdatePermissions;

    private boolean TeleportOnJoinEnabled;
    private String TeleportOnJoinDestinationPlace;
    private String TeleportOnJoinDestination;
    private boolean TeleportOnFirstJoinJoinEnabled;
    private String TeleportOnFirstJoinDestinationPlace;
    private String TeleportOnFirstJoinDestination;
    private boolean TeleportOnVoidEnabled;
    private List<String> TeleportOnVoidIgnoreWorlds;
    private String TeleportOnVoidDestinationPlace;
    private String TeleportOnVoidDestination;
    private boolean TeleportOnRespawnEnabled;
    private boolean TeleportOnRespawnIgnoreBed;
    private List<String> TeleportOnRespawnIgnoredWorlds;
    private String TeleportOnRespawnDestinationPlace;
    private String TeleportOnRespawnDestination;
    private String ReplacedMessagesConsole;
    private String DataType;
    private String DataAddress;
    private int DataPort;
    private String Database;
    private String DataUserName;
    private String DataPassword;
    private String DataTableName;


    public void reloadConfig(){
        configFile.reloadConfig();
        loadConfig();
    }

    public void saveConfig(){
        configFile.saveConfig();
        loadConfig();
    }

    public FileConfiguration getConfig(){
        return configFile.getConfig();
    }

    public ConfigManager(DeluxeTeleport plugin){
        this.plugin = plugin;
        this.m = new MessagesManager("&8[&bDeluxeTeleport&8] ", plugin);
        configFile = new FileManager("config.yml", null, plugin);
        configFile.registerFile();
        loadConfig();
    }

    private <T> boolean setConfig(boolean changed, String path, T value) {
        FileConfiguration config = this.getConfig();
        if (!config.contains(path)){
            config.set(path, value);
            changed = true;
        }
        return changed;
    }

    public void updateConfig() {
        FileConfiguration config = getConfig();
        boolean changed = addMissingFields(config, config);

        List<String> commandsAlias = new ArrayList<>();
        commandsAlias.add("dt");
        commandsAlias.add("teleport");

        List<String> onVoidIgnoredWorlds = new ArrayList<>();
        onVoidIgnoredWorlds.add("IgnoredWorld");

        List<String> onRespawnIgnoredWorlds = new ArrayList<>();
        onVoidIgnoredWorlds.add("IgnoredWorld");

        changed = setConfig(changed, "update_config.notify", true);
        changed = setConfig(changed, "update_config.changelog_notify", true);
        changed = setConfig(changed, "update_config.auto_update.enabled", true);
        changed = setConfig(changed, "update_config.auto_update.check_interval", "6h");
        changed = setConfig(changed, "update_config.auto_update.restart.enabled", true);
        changed = setConfig(changed, "update_config.auto_update.restart.only_on_start", true);
        changed = setConfig(changed, "update_config.update_configs", true);
        changed = setConfig(changed, "update_config.update_messages", true);
        changed = setConfig(changed, "update_config.update_permissions", true);

        changed = setConfig(changed, "replaced_messages.console", "Console");
        changed = setConfig(changed, "commands_alias", commandsAlias);
        changed = setConfig(changed, "database.type", "localhost");
        changed = setConfig(changed, "database.address", "00.00.000");
        changed = setConfig(changed, "database.port", "3306");
        changed = setConfig(changed, "database.database", "DeluxeTeleport");
        changed = setConfig(changed, "database.username", "user");
        changed = setConfig(changed, "database.password", "password");
        changed = setConfig(changed, "database.table_name", "deluxeteleport_");
        changed = setConfig(changed, "teleport_on_join.enabled", false);
        changed = setConfig(changed, "teleport_on_join.destination_place_settings.destination_place", "Spawn");
        changed = setConfig(changed, "teleport_on_join.destination_place_settings.destination", "world");
        changed = setConfig(changed, "teleport_on_join.only_first_join.enabled", true);
        changed = setConfig(changed, "teleport_on_join.only_first_join.destination_place_settings.destination_place", "Spawn");
        changed = setConfig(changed, "teleport_on_join.only_first_join.destination_place_settings.destination", "world");
        changed = setConfig(changed, "teleport_on_void.enabled", false);
        changed = setConfig(changed, "teleport_on_void.ignored_worlds", onVoidIgnoredWorlds);
        changed = setConfig(changed, "teleport_on_void.destination_place_settings.destination_place", "Spawn");
        changed = setConfig(changed, "teleport_on_void.destination_place_settings.destination", "world");
        changed = setConfig(changed, "teleport_on_respawn.enabled", true);
        changed = setConfig(changed, "teleport_on_respawn.ignored_worlds", onRespawnIgnoredWorlds);
        changed = setConfig(changed, "teleport_on_respawn.ignore_bed", false);
        changed = setConfig(changed, "teleport_on_respawn.destination_place_settings.destination_place", "Spawn");
        changed = setConfig(changed, "teleport_on_respawn.destination_place_settings.destination", "world");


        ServerVersion serverVersion = ServerInfo.getServerVersion();
        if (ServerVersion.serverVersionGreaterEqualThan(serverVersion, ServerVersion.v1_18_1)) {
            addComments(config);
        }

        if (changed) {
            OtherUtils.updateConfig(plugin, "Config");
            createFile("config-new.yml", "config.yml", plugin);
            File tempFile = new File(plugin.getDataFolder(), "config-new.yml");

            try {
                YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(tempFile), StandardCharsets.UTF_8));
                this.saveConfig();
                m.sendMessage(Bukkit.getConsoleSender(), plugin.getMainMessagesManager().getGlobalUpdatedConfig()
                        .replace("%config%", "Config"), true);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                tempFile.delete();
            }
        }
    }

    public void addComments (FileConfiguration config) {
        /*try {
            Class<?>[] paramString = new Class[] { String.class, List.class };
            config.getClass().getMethod("setComments", paramString);

        } catch (NoSuchMethodException e){
            m.sendMessage(Bukkit.getConsoleSender(), "&cThe setComments method is not available in this version!", true);
        }*/
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


    public void loadConfig() {
        FileConfiguration config = configFile.getConfig();

        UpdateNotify = config.getBoolean("update_config.notify", true);
        UpdateChangelogNotify = config.getBoolean("update_config.changelog_notify", true);
        UpdateAutoUpdate = config.getBoolean("update_config.auto_update.enabled", true);
        UpdateCheckInterval = config.getString("update_config.auto_update.check_interval", "6h");
        UpdateRestartEnabled = config.getBoolean("update_config.auto_update.restart.enabled", true);
        UpdateRestartOnlyOnStart = config.getBoolean("update_config.auto_update.restart.only_on_start", true);
        UpdateConfigs = config.getBoolean("update_config.update_configs", true);
        UpdateMessages = config.getBoolean("update_config.update_messages", true);
        UpdatePermissions = config.getBoolean("update_config.update_permissions", true);

        ReplacedMessagesConsole = config.getString("replaced_messages.console");

        TeleportOnJoinEnabled = config.getBoolean("teleport_on_join.enabled");
        TeleportOnJoinDestinationPlace = config.getString("teleport_on_join.destination_place_settings.destination_place");
        TeleportOnJoinDestination = config.getString("teleport_on_join.destination_place_settings.destination");

        TeleportOnFirstJoinJoinEnabled = config.getBoolean("teleport_on_join.only_first_join.enabled");
        TeleportOnFirstJoinDestinationPlace = config.getString("teleport_on_join.only_first_join.destination_place_settings.destination_place");
        TeleportOnFirstJoinDestination = config.getString("teleport_on_join.only_first_join.destination_place_settings.destination");

        TeleportOnVoidEnabled = config.getBoolean("teleport_on_void.enabled");
        TeleportOnVoidIgnoreWorlds = config.getStringList("teleport_on_void.ignored_worlds");
        TeleportOnVoidDestinationPlace = config.getString("teleport_on_void.destination_place_settings.destination_place");
        TeleportOnVoidDestination = config.getString("teleport_on_void.destination_place_settings.destination");

        TeleportOnRespawnEnabled = config.getBoolean("teleport_on_respawn.enabled");
        TeleportOnRespawnIgnoreBed = config.getBoolean("teleport_on_respawn.ignore_bed");
        TeleportOnRespawnIgnoredWorlds = config.getStringList("teleport_on_respawn.ignored_worlds");
        TeleportOnRespawnDestinationPlace = config.getString("teleport_on_respawn.destination_place_settings.destination_place");
        TeleportOnRespawnDestination = config.getString("teleport_on_respawn.destination_place_settings.destination");

        DataType = config.getString("database.type");
        DataAddress = config.getString("database.address");
        DataPort = config.getInt("database.port");
        Database = config.getString("database.database");
        DataUserName = config.getString("database.username");
        DataPassword = config.getString("database.password");
        DataTableName = config.getString("database.table_name");
    }

    public boolean isTeleportOnJoinEnabled() {
        return TeleportOnJoinEnabled;
    }

    public String getTeleportOnJoinDestinationPlace() {
        return TeleportOnJoinDestinationPlace;
    }

    public String getTeleportOnJoinDestination() {
        return TeleportOnJoinDestination;
    }

    public boolean isTeleportOnFirstJoinJoinEnabled() {
        return TeleportOnFirstJoinJoinEnabled;
    }

    public String getTeleportOnFirstJoinDestinationPlace() {
        return TeleportOnFirstJoinDestinationPlace;
    }

    public String getTeleportOnFirstJoinDestination() {
        return TeleportOnFirstJoinDestination;
    }

    public boolean isTeleportOnVoidEnabled() {
        return TeleportOnVoidEnabled;
    }

    public List<String> getTeleportOnVoidIgnoreWorlds() {
        return TeleportOnVoidIgnoreWorlds;
    }

    public String getTeleportOnVoidDestinationPlace() {
        return TeleportOnVoidDestinationPlace;
    }

    public String getTeleportOnVoidDestination() {
        return TeleportOnVoidDestination;
    }

    public boolean isTeleportOnRespawnEnabled() {
        return TeleportOnRespawnEnabled;
    }

    public boolean isTeleportOnRespawnIgnoreBed() {
        return TeleportOnRespawnIgnoreBed;
    }

    public List<String> getTeleportOnRespawnIgnoredWorlds() {
        return TeleportOnRespawnIgnoredWorlds;
    }

    public String getTeleportOnRespawnDestinationPlace() {
        return TeleportOnRespawnDestinationPlace;
    }

    public String getTeleportOnRespawnDestination() {
        return TeleportOnRespawnDestination;
    }

    public String getReplacedMessagesConsole() {
        return ReplacedMessagesConsole;
    }

    public String getDataType() {
        return DataType;
    }

    public String getDataAddress() {
        return DataAddress;
    }

    public int getDataPort() {
        return DataPort;
    }

    public String getDatabase() {
        return Database;
    }

    public String getDataUserName() {
        return DataUserName;
    }

    public String getDataPassword() {
        return DataPassword;
    }

    public String getDataTableName() {
        return DataTableName;
    }

    public boolean isUpdateNotify() {
        return UpdateNotify;
    }

    public boolean isUpdateAutoUpdate() {
        return UpdateAutoUpdate;
    }

    public String getUpdateCheckInterval() {
        return UpdateCheckInterval;
    }

    public boolean isUpdateRestartEnabled() {
        return UpdateRestartEnabled;
    }

    public boolean isUpdateRestartOnlyOnStart() {
        return UpdateRestartOnlyOnStart;
    }

    public boolean isUpdateConfigs() {
        return UpdateConfigs;
    }

    public boolean isUpdateMessages() {
        return UpdateMessages;
    }

    public boolean isUpdatePermissions() {
        return UpdatePermissions;
    }

    public boolean isUpdateChangelogNotify() {
        return UpdateChangelogNotify;
    }
}
