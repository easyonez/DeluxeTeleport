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

public class PermissionsManager {
    private final FileManager permissionsFile;
    private final DeluxeTeleport plugin;
    MessagesManager m;

    private String Lobby;
    private boolean LobbyDefault;
    private String SetLobby;
    private boolean SetLobbyDefault;
    private String LobbyBypassDelay;
    private boolean LobbyBypassDelayDefault;
    private String LobbyBypassCooldown;
    private boolean LobbyBypassCooldownDefault;
    private String DelLobby;
    private boolean DelLobbyDefault;
    private String LobbyOther;
    private boolean LobbyOtherDefault;
    private String LobbyCooldownRank;
    private boolean LobbyDenyDefault;


    private String Spawn;
    private boolean SpawnDefault;
    private String SetSpawn;
    private boolean SetSpawnDefault;
    private String SpawnBypassDelay;
    private boolean SpawnBypassDelayDefault;
    private String DelSpawn;
    private boolean DelSpawnDefault;
    private String SpawnBypassCooldown;
    private boolean SpawnBypassCooldownDefault;
    private String SpawnOther;
    private boolean SpawnOtherDefault;
    private String SpawnCooldownRank;
    private boolean SpawnDenyDefault;
    private String Spawns;
    private boolean SpawnsDefault;



    private boolean TpaDefault;
    private String Tpa;
    private boolean TpAcceptDefault;
    private String TpAccept;
    private boolean TpaCancelDefault;
    private String TpaCancel;
    private boolean TpaHereDefault;
    private String TpaHere;
    private boolean TpaToggleDefault;
    private String TpaToggle;
    private boolean TpaToggleOtherDefault;
    private String TpaToggleOther;
    private boolean TpaBypassCooldownDefault;
    private String TpaBypassCooldown;
    private String TpaCooldownRank;
    private boolean TpaDenyDefault;
    private String TpaDeny;


    private boolean HomeDefault;
    private String Home;
    private boolean SetHomeDefault;
    private String SetHome;
    private boolean SetHomeMultimpleDefault;
    private String SetHomeMultiple;
    private boolean DelHomeDefault;
    private String DelHome;
    private boolean HomesDefault;
    private String Homes;
    private boolean HomesOtherDefault;
    private String HomesOther;
    private boolean HomeBypassDelayDefault;
    private String HomeBypassDelay;
    private boolean HomeBypassCooldownDefault;
    private String HomeBypassCooldown;
    private String HomeCooldownRank;
    private boolean HomeDenyDefault;

    private String Warps;
    private boolean WarpsDefault;
    private String SetWarps;
    private boolean SetWarpsDefault;
    private String WarpsBypassDelay;
    private boolean WarpsBypassDelayDefault;
    private String WarpsBypassCooldown;
    private boolean WarpsBypassCooldownDefault;
    private String DelWarps;
    private boolean DelWarpsDefault;
    private String WarpsOther;
    private boolean WarpsOtherDefault;
    private String WarpsCooldownRank;
    private boolean WarpsDenyDefault;

    private String Reload;
    private boolean ReloadDefault;

    private String Help;
    private boolean HelpDefault;

    private String NotifyUpdate;
    private boolean NotifyUpdateDefault;

    private String Update;
    private boolean UpdateDefault;

    private boolean MenuSpawnAdminDefault;
    private String MenuSpawnAdmin;

    private boolean ResetDefault;
    private String Reset;

    private boolean InfoDefault;
    private String Info;

    private boolean ImportDefault;
    private String Import;

    private String Changelog;
    private boolean ChangelogDefault;

    public void reloadPermissions() {
        permissionsFile.reloadConfig();
        loadPermissions();
    }

    public FileConfiguration getPermissions(){
        return permissionsFile.getConfig();
    }

    public void savePermissions(){
        permissionsFile.saveConfig();
        loadPermissions();
    }

    public PermissionsManager(DeluxeTeleport plugin) {
        this.plugin = plugin;
        this.m = new MessagesManager("&8[&bDeluxeTeleport&8] ", plugin);
        permissionsFile = new FileManager("permissions.yml", null, plugin);
        permissionsFile.registerFile();
        loadPermissions();
    }

    private <T> boolean setPermission(boolean changed, String path, T value) {
        FileConfiguration config = this.getPermissions();
        if (!config.contains(path)){
            config.set(path, value);
            changed = true;
        }
        return changed;
    }

    public void updatePermissions() {
        FileConfiguration config = getPermissions();
        boolean changed = addMissingFields(config, config);

        changed = setPermission(changed, "lobby.permission", "deluxeteleport.command.lobby");
        changed = setPermission(changed, "lobby.default", true);
        changed = setPermission(changed, "lobby.description", "Allows the player to teleport to the lobby.");
        changed = setPermission(changed, "lobby_other.permission", "deluxeteleport.command.lobby.other");
        changed = setPermission(changed, "lobby_other.default", false);
        changed = setPermission(changed, "lobby_other.description", "Allows the player to send another player to the lobby.");
        changed = setPermission(changed, "setlobby.permission", "deluxeteleport.command.setlobby");
        changed = setPermission(changed, "setlobby.default", false);
        changed = setPermission(changed, "setlobby.description", "Allows the player to set a lobby location.");
        changed = setPermission(changed, "dellobby.permission", "deluxeteleport.command.dellobby");
        changed = setPermission(changed, "dellobby.default", false);
        changed = setPermission(changed, "dellobby.description", "Allows the player to delete the lobby location.");
        changed = setPermission(changed, "lobby_bypass_delay.permission", "deluxeteleport.bypass.delay.lobby");
        changed = setPermission(changed, "lobby_bypass_delay.default", false);
        changed = setPermission(changed, "lobby_bypass_delay.description", "Allows the player to bypass the teleport delay when teleporting to the lobby.");
        changed = setPermission(changed, "lobby_bypass_cooldown.permission", "deluxeteleport.bypass.cooldown.lobby");
        changed = setPermission(changed, "lobby_bypass_cooldown.default", false);
        changed = setPermission(changed, "lobby_bypass_cooldown.description", "Allows the player to bypass the cooldown between uses of the /lobby command.");
        changed = setPermission(changed, "lobby_cooldown_rank.permission", "deluxeteleport.cooldown.lobby.rank");
        changed = setPermission(changed, "lobby_cooldown_rank.description", "This is the permission that will be used to assign cooldown by rank");


        changed = setPermission(changed, "spawn.permission", "deluxeteleport.command.spawn");
        changed = setPermission(changed, "spawn.default", true);
        changed = setPermission(changed, "spawn.description", "Allows the player to teleport to the spawn point.");
        changed = setPermission(changed, "spawns.permission", "deluxeteleport.command.spawns");
        changed = setPermission(changed, "spawns.default", true);
        changed = setPermission(changed, "spawns.description", "Allows the player to view the spawn list.");
        changed = setPermission(changed, "spawn_other.permission", "deluxeteleport.command.spawn.other");
        changed = setPermission(changed, "spawn_other.default", false);
        changed = setPermission(changed, "spawn_other.description", "Allows the player to send another player to the spawn point.");
        changed = setPermission(changed, "setspawn.permission", "deluxeteleport.command.setspawn");
        changed = setPermission(changed, "setspawn.default", false);
        changed = setPermission(changed, "setspawn.description", "Allows the player to set a spawn location.");
        changed = setPermission(changed, "delspawn.permission", "deluxeteleport.command.delspawn");
        changed = setPermission(changed, "delspawn.default", false);
        changed = setPermission(changed, "delspawn.description", "Allows the player to delete a specified spawn location.");
        changed = setPermission(changed, "spawn_bypass_delay.permission", "deluxeteleport.bypass.delay.spawn");
        changed = setPermission(changed, "spawn_bypass_delay.default", false);
        changed = setPermission(changed, "spawn_bypass_delay.description", "Allows the player to bypass the teleport delay when teleporting to spawn.");
        changed = setPermission(changed, "spawn_bypass_cooldown.permission", "deluxeteleport.bypass.cooldown.spawn");
        changed = setPermission(changed, "spawn_bypass_cooldown.default", false);
        changed = setPermission(changed, "spawn_bypass_cooldown.description", "Allows the player to bypass the cooldown between uses of the /spawn command.");
        changed = setPermission(changed, "spawn_cooldown_rank.permission", "deluxeteleport.cooldown.spawn.rank");
        changed = setPermission(changed, "spawn_cooldown_rank.description", "This is the permission that will be used to assign cooldown by rank");
        changed = setPermission(changed, "spawn_menu_admin.permission", "deluxeteleport.menu.spawn.admin");
        changed = setPermission(changed, "spawn_menu_admin.default", false);
        changed = setPermission(changed, "spawn_menu_admin.description", "Access to the spawn administration menu");


        changed = setPermission(changed, "tpa.permission", "deluxeteleport.command.tpa");
        changed = setPermission(changed, "tpa.default", true);
        changed = setPermission(changed, "tpa.description", "Allows the player to request teleportation to another player.");
        changed = setPermission(changed, "tpaccept.permission", "deluxeteleport.command.tpaccept");
        changed = setPermission(changed, "tpaccept.default", true);
        changed = setPermission(changed, "tpaccept.description", "Allows the player to accept teleportation requests.");
        changed = setPermission(changed, "tpacancel.permission", "deluxeteleport.command.tpacancel");
        changed = setPermission(changed, "tpacancel.default", true);
        changed = setPermission(changed, "tpacancel.description", "Allows the player to cancel teleportation requests.");
        changed = setPermission(changed, "tpadeny.permission", "deluxeteleport.command.tpadeny");
        changed = setPermission(changed, "tpadeny.default", true);
        changed = setPermission(changed, "tpadeny.description", "Allows the player to reject teleport requests.");
        changed = setPermission(changed, "tpahere.permission", "deluxeteleport.command.tpahere");
        changed = setPermission(changed, "tpahere.default", true);
        changed = setPermission(changed, "tpahere.description", "Allows the player to request teleportation to another player's location.");
        changed = setPermission(changed, "tpatoggle.permission", "deluxeteleport.command.tpatoggle");
        changed = setPermission(changed, "tpatoggle.default", true);
        changed = setPermission(changed, "tpatoggle.description", "Allows the player to toggle teleportation requests on or off.");
        changed = setPermission(changed, "tpatoggle_other.permission", "deluxeteleport.command.tpatoggle.other");
        changed = setPermission(changed, "tpatoggle_other.default", false);
        changed = setPermission(changed, "tpatoggle_other.description", "Allows the player to toggle another player's teleportation requests on or off.");
        changed = setPermission(changed, "tpa_cooldown_rank.permission", "deluxeteleport.cooldown.tpa.rank");
        changed = setPermission(changed, "tpa_cooldown_rank.description", "This is the permission that will be used to assign cooldown by rank");
        changed = setPermission(changed, "tpa_bypass_cooldown.permission", "deluxeteleport.bypass.cooldown.tpa");
        changed = setPermission(changed, "tpa_bypass_cooldown.default", false);
        changed = setPermission(changed, "tpa_bypass_cooldown.description", "Allows the player to bypass the cooldown between uses of the /tpa command.");


        changed = setPermission(changed, "home.permission", "deluxeteleport.command.home");
        changed = setPermission(changed, "home.default", true);
        changed = setPermission(changed, "home.description", "Allows the player to teleport to their home location.");
        changed = setPermission(changed, "homes.permission", "deluxeteleport.command.homes");
        changed = setPermission(changed, "homes.default", true);
        changed = setPermission(changed, "homes.description", "Show a list of your own homes.");
        changed = setPermission(changed, "homes_other.permission", "deluxeteleport.command.homes.other");
        changed = setPermission(changed, "homes_other.default", false);
        changed = setPermission(changed, "homes_other.description", "Shows a list of another player's homes.");
        changed = setPermission(changed, "sethome.permission", "deluxeteleport.command.sethome");
        changed = setPermission(changed, "sethome.default", true);
        changed = setPermission(changed, "sethome.description", "Allows the player to set their home location.");
        changed = setPermission(changed, "sethome_multiple.permission", "deluxeteleport.command.sethome.multiple");
        changed = setPermission(changed, "sethome_multiple.description", "Allows the player to set multiple home locations, when giving permission to a player you must add one point plus the number, example deluxeteleport.command.sethome.multiple.5");
        changed = setPermission(changed, "delhome.permission", "deluxeteleport.command.delhome");
        changed = setPermission(changed, "delhome.default", true);
        changed = setPermission(changed, "delhome.description", "Allows the player to delete their home location.");
        changed = setPermission(changed, "home_bypass_delay.permission", "deluxeteleport.bypass.delay.home");
        changed = setPermission(changed, "home_bypass_delay.default", false);
        changed = setPermission(changed, "home_bypass_delay.description", "Allows the player to avoid teleport delay when teleporting home.");
        changed = setPermission(changed, "home_bypass_cooldown.permission", "deluxeteleport.bypass.cooldown.home");
        changed = setPermission(changed, "home_bypass_cooldown.default", false);
        changed = setPermission(changed, "home_bypass_cooldown.description", "Allows the player to bypass the cooldown between uses of the /home command.");
        changed = setPermission(changed, "home_cooldown_rank.permission", "deluxeteleport.cooldown.home.rank");
        changed = setPermission(changed, "home_cooldown_rank.description", "This is the permission that will be used to assign cooldown by rank");


        changed = setPermission(changed, "warp.permission", "deluxeteleport.command.warp");
        changed = setPermission(changed, "warp.default", true);
        changed = setPermission(changed, "warp.description", "Allows the player to teleport to the warp.");
        changed = setPermission(changed, "warp_other.permission", "deluxeteleport.command.warp.other");
        changed = setPermission(changed, "warp_other.default", false);
        changed = setPermission(changed, "warp_other.description", "Allows the player to send another player to the warp.");
        changed = setPermission(changed, "setwarp.permission", "deluxeteleport.command.setwarp");
        changed = setPermission(changed, "setwarp.default", false);
        changed = setPermission(changed, "setwarp.description", "Allows the player to set a warp location.");
        changed = setPermission(changed, "delwarp.permission", "deluxeteleport.command.delwarp");
        changed = setPermission(changed, "delwarp.default", false);
        changed = setPermission(changed, "delwarp.description", "Allows the player to delete the warp location.");
        changed = setPermission(changed, "warp_bypass_delay.permission", "deluxeteleport.bypass.delay.warp");
        changed = setPermission(changed, "warp_bypass_delay.default", false);
        changed = setPermission(changed, "warp_bypass_delay.description", "Allows the player to bypass the teleport delay when teleporting to the warp.");
        changed = setPermission(changed, "warp_bypass_cooldown.permission", "deluxeteleport.bypass.cooldown.warp");
        changed = setPermission(changed, "warp_bypass_cooldown.default", false);
        changed = setPermission(changed, "warp_bypass_cooldown.description", "Allows the player to bypass the cooldown between uses of the /warp command.");
        changed = setPermission(changed, "warp_cooldown_rank.permission", "deluxeteleport.cooldown.warp.rank");
        changed = setPermission(changed, "warp_cooldown_rank.description", "This is the permission that will be used to assign cooldown by rank");


        changed = setPermission(changed, "reload.permission", "deluxeteleport.command.reload");
        changed = setPermission(changed, "reload.default", false);
        changed = setPermission(changed, "reload.description", "Allows the player to reload plugin configuration.");

        changed = setPermission(changed, "reset.permission", "deluxeteleport.command.reset");
        changed = setPermission(changed, "reset.default", false);
        changed = setPermission(changed, "reset.description", "Allows the player to use the /deluxeteleport restart command");

        changed = setPermission(changed, "version.permission", "deluxeteleport.command.version");
        changed = setPermission(changed, "version.default", true);
        changed = setPermission(changed, "version.description", "Allows the player to view the plugin version.");

        changed = setPermission(changed, "help.permission", "deluxeteleport.command.help");
        changed = setPermission(changed, "help.default", false);
        changed = setPermission(changed, "help.description", "Allows the player to view help information about plugin commands.");

        changed = setPermission(changed, "notify.permission", "deluxeteleport.notify");
        changed = setPermission(changed, "notify.default", false);
        changed = setPermission(changed, "notify.description", "Allows the player to receive notifications about errors.");

        changed = setPermission(changed, "notify_update.permission", "deluxeteleport.notify.update");
        changed = setPermission(changed, "notify_update.default", false);
        changed = setPermission(changed, "notify_update.description", "Allows the player to receive notifications about new updates available for the plugin.");

        changed = setPermission(changed, "update.permission", "deluxeteleport.command.update");
        changed = setPermission(changed, "update.default", false);
        changed = setPermission(changed, "update.description", "Allows the player to update the plugin to the latest version.");

        changed = setPermission(changed, "info.permission", "deluxeteleport.command.info");
        changed = setPermission(changed, "info.default", false);
        changed = setPermission(changed, "info.description", "Displays information about the plugin.");

        changed = setPermission(changed, "import.permission", "deluxeteleport.command.import");
        changed = setPermission(changed, "import.default", false);
        changed = setPermission(changed, "import.description", "Allows the use of the /dt import command");

        changed = setPermission(changed, "changelog.permission", "deluxeteleport.command.changelog");
        changed = setPermission(changed, "changelog.default", false);
        changed = setPermission(changed, "changelog.description", "Allows the use of the /dt changelog command");

        changed = setPermission(changed, "permissions_version", plugin.version);

        ServerVersion serverVersion = ServerInfo.getServerVersion();
        if (ServerVersion.serverVersionGreaterEqualThan(serverVersion, ServerVersion.v1_18_1)) {
            //addComments(config);
        }

        if (changed) {
            OtherUtils.updateConfig(plugin, "Permissions");
            createFile("permissions-new.yml", "permissions.yml", plugin);
            File tempFile = new File(plugin.getDataFolder(), "permissions-new.yml");

            try {
                YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(tempFile), StandardCharsets.UTF_8));
                this.savePermissions();
                m.sendMessage(Bukkit.getConsoleSender(), plugin.getMainMessagesManager().getGlobalUpdatedConfig()
                        .replace("%config%", "Permissions"), true);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                tempFile.delete();
            }
        }
    }

    private void createFile(String name, String from, DeluxeTeleport plugin) {
        File file = new File(plugin.getDataFolder(), name);
        if (!file.exists()) {
            try {
                Files.copy(plugin.getResource(from), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                m.sendMessage(Bukkit.getConsoleSender(),name + " file for DeluxeTeleport!" + e, true);
            }
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

    public void loadPermissions() {
        FileConfiguration permissions = permissionsFile.getConfig();
        Lobby = permissions.getString("lobby.permission");
        LobbyDefault = permissions.getBoolean("lobby.default");
        SetLobby = permissions.getString("setlobby.permission");
        SetLobbyDefault = permissions.getBoolean("setlobby.default");
        LobbyBypassDelay = permissions.getString("lobby_bypass_delay.permission");
        LobbyBypassDelayDefault = permissions.getBoolean("lobby_bypass_delay.default");
        LobbyBypassCooldown = permissions.getString("lobby_bypass_cooldown.permission");
        LobbyBypassCooldownDefault = permissions.getBoolean("lobby_bypass_cooldown.default");
        DelLobby = permissions.getString("dellobby.permission");
        DelLobbyDefault = permissions.getBoolean("dellobby.default");
        LobbyOther = permissions.getString("lobby_other.permission");
        LobbyOtherDefault = permissions.getBoolean("lobby_other.default");
        LobbyCooldownRank = permissions.getString("lobby_cooldown_rank.permission");


        Spawn = permissions.getString("spawn.permission");
        SpawnDefault = permissions.getBoolean("spawn.default");
        SetSpawn = permissions.getString("setspawn.permission");
        SetSpawnDefault = permissions.getBoolean("setspawn.default");
        SpawnBypassDelay = permissions.getString("spawn_bypass_delay.permission");
        SpawnBypassDelayDefault = permissions.getBoolean("spawn_bypass_delay.default");
        SpawnBypassCooldown = permissions.getString("spawn_bypass_cooldown.permission");
        SpawnBypassCooldownDefault = permissions.getBoolean("spawn_bypass_cooldown.default");
        DelSpawn = permissions.getString("delspawn.permission");
        DelSpawnDefault = permissions.getBoolean("delspawn.default");
        SpawnOther = permissions.getString("spawn_other.permission");
        SpawnOtherDefault = permissions.getBoolean("spawn_other.default");
        SpawnCooldownRank = permissions.getString("spawn_cooldown_rank.permission");
        Spawns = permissions.getString("spawns.permission");
        SpawnsDefault = permissions.getBoolean("spawns.default");


        TpaDefault = permissions.getBoolean("tpa.default");
        Tpa = permissions.getString("tpa.permission");
        TpAcceptDefault = permissions.getBoolean("tpaccept.default");
        TpAccept = permissions.getString("tpaccept.permission");
        TpaCancelDefault = permissions.getBoolean("tpacancel.default");
        TpaCancel = permissions.getString("tpacancel.permission");
        TpaHereDefault = permissions.getBoolean("tpahere.default");
        TpaHere = permissions.getString("tpahere.permission");
        TpaToggleDefault = permissions.getBoolean("tpatoggle.default");
        TpaToggle = permissions.getString("tpatoggle.permission");
        TpaToggleOtherDefault = permissions.getBoolean("tpatoggle_other.default");
        TpaToggleOther = permissions.getString("tpatoggle_other.permission");
        TpaBypassCooldown = permissions.getString("tpa_bypass_cooldown.permission");
        TpaBypassCooldownDefault = permissions.getBoolean("tpa_bypass_cooldown.default");
        TpaCooldownRank = permissions.getString("tpa_cooldown_rank.permission");
        TpaDenyDefault = permissions.getBoolean("tpadeny.default");
        TpaDeny = permissions.getString("tpadeny.permission");


        HomeDefault = permissions.getBoolean("home.default");
        Home = permissions.getString("home.permission");
        SetHomeDefault = permissions.getBoolean("sethome.default");
        SetHome = permissions.getString("sethome.permission");
        SetHomeMultimpleDefault = permissions.getBoolean("sethome_multiple.default");
        SetHomeMultiple = permissions.getString("sethome_multiple.permission");
        DelHomeDefault = permissions.getBoolean("delhome.default");
        DelHome = permissions.getString("delhome.permission");
        Homes = permissions.getString("homes.permission");
        HomesDefault = permissions.getBoolean("homes.default");
        HomesOtherDefault = permissions.getBoolean("homes_other.default");
        HomesOther = permissions.getString("homes_other.permission");
        HomeBypassDelayDefault = permissions.getBoolean("home_bypass_delay.default");
        HomeBypassDelay = permissions.getString("home_bypass_delay.permission");
        HomeBypassCooldown = permissions.getString("home_bypass_cooldown.permission");
        HomeBypassCooldownDefault = permissions.getBoolean("home_bypass_cooldown.default");
        HomeCooldownRank = permissions.getString("home_cooldown_rank.permission");


        Warps = permissions.getString("warp.permission");
        WarpsDefault = permissions.getBoolean("warp.default");
        SetWarps = permissions.getString("setwarp.permission");
        SetWarpsDefault = permissions.getBoolean("setwarp.default");
        WarpsBypassDelay = permissions.getString("warp_bypass_delay.permission");
        WarpsBypassDelayDefault = permissions.getBoolean("warp_bypass_delay.default");
        WarpsBypassCooldown = permissions.getString("warp_bypass_cooldown.permission");
        WarpsBypassCooldownDefault = permissions.getBoolean("warp_bypass_cooldown.default");
        DelWarps = permissions.getString("delwarp.permission");
        DelWarpsDefault = permissions.getBoolean("delwarp.default");
        WarpsOther = permissions.getString("warp_other.permission");
        WarpsOtherDefault = permissions.getBoolean("warp_other.default");
        WarpsCooldownRank = permissions.getString("warp_cooldown_rank.permission");


        MenuSpawnAdminDefault = permissions.getBoolean("spawn_menu_admin.default");
        MenuSpawnAdmin = permissions.getString("spawn_menu_admin.permission");

        Reload = permissions.getString("reload.permission");
        ReloadDefault = permissions.getBoolean("reload.default");

        Help = permissions.getString("help.permission");
        HelpDefault = permissions.getBoolean("help.default");

        NotifyUpdate = permissions.getString("notify_update.permission");
        NotifyUpdateDefault = permissions.getBoolean("notify_update.default");

        Update = permissions.getString("update.permission");
        UpdateDefault = permissions.getBoolean("update.default");

        ResetDefault = permissions.getBoolean("reset.default");
        Reset = permissions.getString("reset.permission");

        InfoDefault = permissions.getBoolean("info.default", false);
        Info = permissions.getString("info.permission", "deluxeteleport.command.info");

        ImportDefault = permissions.getBoolean("import.default", false);
        Import = permissions.getString("import.permission", "deluxeteleport.command.info");

        Changelog = permissions.getString("changelog.permission");
        ChangelogDefault = permissions.getBoolean("changelog.default");
    }

    public String getLobby() {
        return Lobby;
    }

    public boolean isLobbyDefault() {
        return LobbyDefault;
    }

    public String getSetLobby() {
        return SetLobby;
    }

    public boolean isSetLobbyDefault() {
        return SetLobbyDefault;
    }

    public String getSpawn() {
        return Spawn;
    }

    public boolean isSpawnDefault() {
        return SpawnDefault;
    }

    public String getSetSpawn() {
        return SetSpawn;
    }

    public boolean isSetSpawnDefault() {
        return SetSpawnDefault;
    }

    public String getReload() {
        return Reload;
    }

    public boolean isReloadDefault() {
        return ReloadDefault;
    }

    public String getHelp() {
        return Help;
    }

    public boolean isHelpDefault() {
        return HelpDefault;
    }

    public String getNotifyUpdate() {
        return NotifyUpdate;
    }

    public boolean isNotifyUpdateDefault() {
        return NotifyUpdateDefault;
    }

    public String getLobbyBypassDelay() {
        return LobbyBypassDelay;
    }

    public boolean isLobbyBypassDelayDefault() {
        return LobbyBypassDelayDefault;
    }

    public String getSpawnBypassDelay() {
        return SpawnBypassDelay;
    }

    public boolean isSpawnBypassDelayDefault() {
        return SpawnBypassDelayDefault;
    }

    public String getLobbyBypassCooldown() {
        return LobbyBypassCooldown;
    }

    public boolean isLobbyBypassCooldownDefault() {
        return LobbyBypassCooldownDefault;
    }

    public String getSpawnBypassCooldown() {
        return SpawnBypassCooldown;
    }

    public boolean isSpawnBypassCooldownDefault() {
        return SpawnBypassCooldownDefault;
    }

    public String getDelSpawn() {
        return DelSpawn;
    }

    public boolean isDelSpawnDefault() {
        return DelSpawnDefault;
    }

    public String getDelLobby() {
        return DelLobby;
    }

    public boolean isDelLobbyDefault() {
        return DelLobbyDefault;
    }

    public String getLobbyOther() {
        return LobbyOther;
    }

    public boolean isLobbyOtherDefault() {
        return LobbyOtherDefault;
    }

    public String getUpdate() {
        return Update;
    }

    public boolean isUpdateDefault() {
        return UpdateDefault;
    }

    public String getSpawnOther() {
        return SpawnOther;
    }

    public boolean isSpawnOtherDefault() {
        return SpawnOtherDefault;
    }

    public boolean isTpaDefault() {
        return TpaDefault;
    }

    public String getTpa() {
        return Tpa;
    }

    public boolean isTpAcceptDefault() {
        return TpAcceptDefault;
    }

    public String getTpAccept() {
        return TpAccept;
    }

    public boolean isTpaCancelDefault() {
        return TpaCancelDefault;
    }

    public String getTpaCancel() {
        return TpaCancel;
    }

    public boolean isTpaHereDefault() {
        return TpaHereDefault;
    }

    public String getTpaHere() {
        return TpaHere;
    }

    public boolean isTpaToggleDefault() {
        return TpaToggleDefault;
    }

    public String getTpaToggle() {
        return TpaToggle;
    }

    public boolean isTpaToggleOtherDefault() {
        return TpaToggleOtherDefault;
    }

    public String getTpaToggleOther() {
        return TpaToggleOther;
    }

    public boolean isHomeDefault() {
        return HomeDefault;
    }

    public String getHome() {
        return Home;
    }

    public boolean isSetHomeDefault() {
        return SetHomeDefault;
    }

    public String getSetHome() {
        return SetHome;
    }

    public boolean isSetHomeMultimpleDefault() {
        return SetHomeMultimpleDefault;
    }

    public String getSetHomeMultiple() {
        return SetHomeMultiple;
    }

    public boolean isDelHomeDefault() {
        return DelHomeDefault;
    }

    public String getDelHome() {
        return DelHome;
    }

    public boolean isMenuSpawnAdminDefault() {
        return MenuSpawnAdminDefault;
    }

    public String getMenuSpawnAdmin() {
        return MenuSpawnAdmin;
    }

    public boolean isHomesDefault() {
        return HomesDefault;
    }

    public String getHomes() {
        return Homes;
    }

    public boolean isHomesOtherDefault() {
        return HomesOtherDefault;
    }

    public String getHomesOther() {
        return HomesOther;
    }

    public boolean isHomeBypassDelayDefault() {
        return HomeBypassDelayDefault;
    }

    public String getHomeBypassDelay() {
        return HomeBypassDelay;
    }

    public boolean isHomeBypassCooldownDefault() {
        return HomeBypassCooldownDefault;
    }

    public String getHomeBypassCooldown() {
        return HomeBypassCooldown;
    }

    public String getTpaBypassCooldown() {
        return TpaBypassCooldown;
    }

    public boolean isTpaBypassCooldownDefault() {
        return TpaBypassCooldownDefault;
    }

    public String getTpaCooldownRank() {
        return TpaCooldownRank;
    }

    public String getTpaDeny() {
        return TpaDeny;
    }

    public boolean isTpaDenyDefault() {
        return TpaDenyDefault;
    }

    public String getReset() {
        return Reset;
    }

    public boolean isResetDefault() {
        return ResetDefault;
    }

    public boolean isInfoDefault() {
        return InfoDefault;
    }

    public String getInfo() {
        return Info;
    }

    public String getImport() {
        return Import;
    }

    public boolean isImportDefault() {
        return ImportDefault;
    }

    public String getSpawnCooldownRank() {
        return SpawnCooldownRank;
    }

    public String getHomeCooldownRank() {
        return HomeCooldownRank;
    }

    public String getLobbyCooldownRank() {
        return LobbyCooldownRank;
    }

    public boolean isChangelogDefault() {
        return ChangelogDefault;
    }

    public String getChangelog() {
        return Changelog;
    }

    public boolean isWarpsDenyDefault() {
        return WarpsDenyDefault;
    }

    public String getWarpsCooldownRank() {
        return WarpsCooldownRank;
    }

    public boolean isWarpsOtherDefault() {
        return WarpsOtherDefault;
    }

    public String getWarpsOther() {
        return WarpsOther;
    }

    public boolean isDelWarpsDefault() {
        return DelWarpsDefault;
    }

    public String getDelWarps() {
        return DelWarps;
    }

    public boolean isWarpsBypassCooldownDefault() {
        return WarpsBypassCooldownDefault;
    }

    public String getWarpsBypassCooldown() {
        return WarpsBypassCooldown;
    }

    public boolean isWarpsBypassDelayDefault() {
        return WarpsBypassDelayDefault;
    }

    public String getWarpsBypassDelay() {
        return WarpsBypassDelay;
    }

    public boolean isSetWarpsDefault() {
        return SetWarpsDefault;
    }

    public String getSetWarps() {
        return SetWarps;
    }

    public boolean isWarpsDefault() {
        return WarpsDefault;
    }

    public String getWarps() {
        return Warps;
    }

    public boolean isSpawnsDefault() {
        return SpawnsDefault;
    }

    public String getSpawns() {
        return Spawns;
    }
}