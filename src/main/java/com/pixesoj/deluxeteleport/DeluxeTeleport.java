package com.pixesoj.deluxeteleport;

import com.pixesoj.deluxeteleport.managers.filesmanager.*;
import com.pixesoj.deluxeteleport.managers.*;
import com.pixesoj.deluxeteleport.listeners.*;
import com.pixesoj.deluxeteleport.managers.commands.CommandRegisterManager;
import com.pixesoj.deluxeteleport.managers.database.CooldownDatabase;
import com.pixesoj.deluxeteleport.managers.dependencies.DependencyManager;
import com.pixesoj.deluxeteleport.managers.dependencies.PlaceholderAPI;
import com.pixesoj.deluxeteleport.managers.filesmanager.playerdata.PlayerDataManager;
import com.pixesoj.deluxeteleport.utils.OtherUtils;
import com.pixesoj.deluxeteleport.utils.ServerInfo;
import com.pixesoj.deluxeteleport.utils.ServerVersion;
import com.pixesoj.deluxeteleport.managers.dependencies.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeluxeTeleport extends JavaPlugin {

    public String version;

    private ConfigManager mainConfigManager;
    private ConfigLobbyManager mainLobbyConfigManager;
    private ConfigSpawnManager mainSpawnConfigManager;
    private MessagesFileManager mainMessagesManager;
    private PermissionsManager mainPermissionsManager;
    private ConfigTPAManager mainTPAConfigManager;
    private ConfigHomeManager mainHomeConfigManager;
    private MenusFileManager mainMenuManager;
    private ConfigMenuManager mainMenuConfigManager;
    private ConfigWarpsManager mainWarpConfigManager;

    private LocationsManager locationsManager;
    private PlayerDataManager playerDataManager;

    private UpdateCheckManager updateCheckerManager;
    private PlaceholdersManager placeholdersManager;
    private ExecutorService executorService;

    private List<String> avaibleVersions;
    private ArrayList<String> delayPlayers;
    private Map<String, Integer> cooldownLobbyPlayers;
    private Map<String, Integer> cooldownSpawnPlayers;
    private Map<String, Integer> cooldownHomePlayers;
    private Map<String, Integer> cooldownTpaPlayers;
    private Map<String, Integer> cooldownWarpPlayers;
    private Map<Player, MenuManager> openMenus;

    public void onEnable() {
        this.version = getDescription().getVersion();
        this.executorService = Executors.newSingleThreadExecutor();

        updateCheckerManager = new UpdateCheckManager(version);
        updateCheckerManager.check();

        ServerVersion detectedVersion = setVersion();
        ServerInfo.setServerVersion(detectedVersion);

        this.mainMessagesManager = new MessagesFileManager(this);
        this.locationsManager = new LocationsManager(this);
        this.mainConfigManager = new ConfigManager(this);
        this.playerDataManager = new PlayerDataManager(this);
        this.mainLobbyConfigManager = new ConfigLobbyManager(this);
        this.mainSpawnConfigManager = new ConfigSpawnManager(this);
        this.mainPermissionsManager = new PermissionsManager(this);
        this.mainTPAConfigManager = new ConfigTPAManager(this);
        this.mainHomeConfigManager = new ConfigHomeManager(this);
        this.mainMenuManager = new MenusFileManager(this);
        this.placeholdersManager = new PlaceholdersManager(this);
        this.mainMenuConfigManager = new ConfigMenuManager(this);
        this.mainWarpConfigManager = new ConfigWarpsManager(this);
        registerEvents();
        registerMessages();
        registerMenus();

        UpdateManager updateManager = new UpdateManager(this, Bukkit.getConsoleSender());
        updateManager.startUpdate(mainConfigManager.isUpdateRestartEnabled());

        this.avaibleVersions = updateCheckerManager.getAvailableVersions();
        this.delayPlayers = new ArrayList<>();
        this.cooldownLobbyPlayers = new HashMap<>();
        this.cooldownSpawnPlayers = new HashMap<>();
        this.cooldownHomePlayers = new HashMap<>();
        this.cooldownTpaPlayers = new HashMap<>();
        this.cooldownWarpPlayers = new HashMap<>();
        this.openMenus = new HashMap<>();

        new Metrics(this, 23692);

        getServer().getScheduler().runTaskLater(this, this::updateConfigs, 1L);

        if (DependencyManager.isPlaceholderAPI()) new PlaceholderAPI(this).register();

        CommandRegisterManager commandRegisterManager = new CommandRegisterManager(this);
        commandRegisterManager.registerCommands();

        CooldownDatabase cooldownDatabase = new CooldownDatabase(this);
        if (getMainTPAConfigManager().isCooldownSavePlayerData()) cooldownDatabase.loadCooldowns("tpa");
        if (getMainLobbyConfigManager().isCooldownSavePlayerData()) cooldownDatabase.loadCooldowns("lobby");
        if (getMainSpawnConfigManager().isCooldownSavePlayerData()) cooldownDatabase.loadCooldowns("spawn");
        if (getMainHomeConfigManager().isCooldownSavePlayerData()) cooldownDatabase.loadCooldowns("home");
        if (getMainWarpConfigManager().isCooldownSavePlayerData()) cooldownDatabase.loadCooldowns("warp");

        OtherUtils.sendConsoleMessagesOnEnabled(this);
    }

    public void onDisable() {
        executorService.shutdown();
        OtherUtils.sendConsoleMessagesOnDisabled(this);
    }

    public void registerEvents(){
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(new VoidListener(this), this);
        getServer().getPluginManager().registerEvents(new RespawnListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        getServer().getPluginManager().registerEvents(new CommandListener(this), this);
    }

    public ConfigManager getMainConfigManager() {
        return mainConfigManager;
    }

    public ConfigLobbyManager getMainLobbyConfigManager() {
        return mainLobbyConfigManager;
    }

    public ConfigSpawnManager getMainSpawnConfigManager() {
        return mainSpawnConfigManager;
    }

    public MessagesFileManager getMainMessagesManager() {
        return mainMessagesManager;
    }

    public ConfigWarpsManager getMainWarpConfigManager() {
        return mainWarpConfigManager;
    }

    public MenusFileManager getMainMenuManager() {
        return mainMenuManager;
    }

    public ConfigMenuManager getMainMenuConfigManager() {
        return mainMenuConfigManager;
    }

    public UpdateCheckManager getUpdateCheckerManager() {
        return this.updateCheckerManager;
    }

    public void setUpdateCheckerManager(UpdateCheckManager updateCheckManager) {
        this.updateCheckerManager = updateCheckManager;
    }

    public PermissionsManager getMainPermissionsManager() {
        return mainPermissionsManager;
    }

    public ConfigTPAManager getMainTPAConfigManager() {
        return mainTPAConfigManager;
    }

    public ConfigHomeManager getMainHomeConfigManager() {
        return mainHomeConfigManager;
    }

    public PlaceholdersManager getPlaceholdersManager() {
        return placeholdersManager;
    }

    public LocationsManager getLocationsManager() {
        return locationsManager;
    }

    public void updateConfigs(){
        boolean isUpdateConfigs = this.mainConfigManager.isUpdateConfigs();
        boolean isUpdateMessages = this.mainConfigManager.isUpdateMessages();
        boolean isUpdatePermissions = this.mainConfigManager.isUpdatePermissions();

        if (isUpdatePermissions){
            getMainPermissionsManager().updatePermissions();
        }

        if (isUpdateMessages) {
            getMainMessagesManager().updateMessages();
        }

        if (isUpdateConfigs) {
            getMainConfigManager().updateConfig();
            getMainSpawnConfigManager().updateSpawnConfig();
            getMainLobbyConfigManager().updateLobbyConfig();
            getMainTPAConfigManager().updateTeleportConfig();
            getMainHomeConfigManager().updateHomeConfig();
        }
    }

    public void registerMessages(){
        File langFolder = new File(this.getDataFolder(), "lang/");
        if (!langFolder.exists()){
            langFolder.mkdirs();
        }

        File messagesEs = new File(langFolder + "/es-ES", "messages.yml");
        if (!messagesEs.exists()){
            saveResource("lang/es-ES/messages.yml", false);
        }

        File messagesEn = new File(langFolder + "/en-EN", "messages.yml");
        if (!messagesEn.exists()){
            saveResource("lang/en-EN/messages.yml", false);
        }
    }

    public void registerMenus() {
        File langFolder = new File(this.getDataFolder(), "gui_menus/");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }

        FileConfiguration config = getMainMenuConfigManager().getConfig();

        if (config.contains("gui_menus")) {
            ConfigurationSection guiMenus = config.getConfigurationSection("gui_menus");

            for (String menuName : guiMenus.getKeys(false)) {
                ConfigurationSection menuData = guiMenus.getConfigurationSection(menuName);

                if (menuData != null && menuData.contains("file")) {
                    String filePath = menuData.getString("file", "");
                    File menuFile = new File(langFolder, filePath);

                    if (!menuFile.exists()) {
                        saveResource("gui_menus/" + filePath, false);
                    }
                }
            }
        }
    }

    public ServerVersion setVersion() {
        String versionString = Bukkit.getBukkitVersion();
        String[] parts = versionString.split("-");
        versionString = parts[0];

        versionString = convertVersionToEnumFormat(versionString);

        return ServerVersion.valueOf(versionString);
    }

    private String convertVersionToEnumFormat(String version) {
        version = version.replace(".", "_");
        version = "v" + version;
        return version;
    }

    public void addPlayerTeleport(Player player){
        delayPlayers.add(player.getName());
    }

    public void removePlayerTeleport(Player player){
        delayPlayers.remove(player.getName());
    }

    public boolean playerInDelay(Player player){
        return delayPlayers.contains(player.getName());
    }

    public void addLobbyCooldown (String playerName, int time){
        cooldownLobbyPlayers.put(playerName, time);
    }

    public void removeLobbyCooldown (String player){
        cooldownLobbyPlayers.remove(player);
    }

    public boolean playerLobbyInCooldown(Player player){
        return cooldownLobbyPlayers.containsKey(player.getName());
    }

    public void addSpawnCooldown (String playerName, int time){
        cooldownSpawnPlayers.put(playerName, time);
    }

    public void removeSpawnCooldown (String player){
        cooldownSpawnPlayers.remove(player);
    }

    public boolean playerSpawnInCooldown(Player player){
        return cooldownSpawnPlayers.containsKey(player.getName());
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public void addHomeCooldown (String playerName, int time){
        cooldownHomePlayers.put(playerName, time);
    }

    public void removeHomeCooldown (String player){
        cooldownHomePlayers.remove(player);
    }

    public boolean playerHomeInCooldown(Player player){
        return cooldownHomePlayers.containsKey(player.getName());
    }

    public void addTpaCooldown(String playerName, int time) {
        cooldownTpaPlayers.put(playerName, time);
    }

    public void removeTpaCooldown (String player){
        cooldownTpaPlayers.remove(player);
    }

    public boolean playerTpaInCooldown(Player player) {
        return cooldownTpaPlayers.containsKey(player.getName());
    }

    public void addWarpCooldown(String playerName, int time) {
        cooldownWarpPlayers.put(playerName, time);
    }

    public void removeWarpCooldown (String player){
        cooldownWarpPlayers.remove(player);
    }

    public boolean playerWarpInCooldown(Player player) {
        return cooldownWarpPlayers.containsKey(player.getName());
    }

    public void startCooldown(String player, String type, int time) {
        CooldownManager c = new CooldownManager(this, time, player);
        c.cooldown(type);
    }

    public Map<Player, MenuManager> getOpenMenus(){
        return openMenus;
    }

    public List<String> getAvaibleVersions(){
        return avaibleVersions;
    }

    public void detAvaibleVersions(List<String> newAvaibleVersions){
        avaibleVersions.clear();
        avaibleVersions.addAll(newAvaibleVersions);
    }
}