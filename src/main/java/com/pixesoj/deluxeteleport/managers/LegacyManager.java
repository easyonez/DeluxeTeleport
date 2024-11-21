package com.pixesoj.deluxeteleport.managers;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.filesmanager.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class LegacyManager {
    private final DeluxeTeleport plugin;

    public LegacyManager(DeluxeTeleport plugin){
        this.plugin = plugin;

    }

    public void start() {
        spawn();
        lobby();

        MessagesManager m = new MessagesManager("&8[&bDeluxeTeleport&8] ", plugin);
        FileConfiguration locationsFile = plugin.getLocationsManager().getLocationsConfig();
        if (locationsFile == null) return;
        File file = plugin.getLocationsManager().getLocationsFile();
        if (locationsFile.getKeys(false).isEmpty() && file.exists() && file.delete()) {
            m.sendMessage(Bukkit.getConsoleSender(), "&cThe locations.yml file is obsolete, removing...", true);
        }
    }

    private void spawn(){
        FileConfiguration locationsFile = plugin.getLocationsManager().getLocationsConfig();
        if (locationsFile == null) return;

        ConfigurationSection spawnSection;
        FileManager fileManager;
        FileConfiguration spawn;
        spawnSection = locationsFile.getConfigurationSection("Spawn.General.Global");
        if (spawnSection != null){
            fileManager = new FileManager("general-spawn.yml", "data/spawns", plugin);
            World world = Bukkit.getWorld(Objects.requireNonNull(spawnSection.getString("world")));
            spawn = fileManager.getConfig();

            assert world != null;
            spawn.set("world", world.getUID().toString());
            spawn.set("world_name", spawnSection.getString("world"));
            spawn.set("x", spawnSection.getDouble("x", 0));
            spawn.set("y", spawnSection.getDouble("y", 0));
            spawn.set("z", spawnSection.getDouble("z", 0));
            spawn.set("yaw", spawnSection.getDouble("yaw", 0));
            spawn.set("pitch", spawnSection.getDouble("pitch", 0));
            spawn.set("displayname", world.getName());
            spawn.set("lastowner", "");
            spawn.set("is_main", true);
            if (spawn.getConfigurationSection("teleport_actions.actions") == null){
                spawn.set("teleport_actions.actions", new ArrayList<>());
            }
            if (spawn.getConfigurationSection("teleport_conditions.conditions") == null){
                spawn.set("teleport_conditions.conditions", new ArrayList<>());
            }

            fileManager.saveConfig();
            locationsFile.set("Spawn.General", null);
        }

        ConfigurationSection spawnByWorldSection = locationsFile.getConfigurationSection("Spawn.ByWorld");
        if (spawnByWorldSection != null) {
            Set<String> spawnList = spawnByWorldSection.getKeys(false);
            for (String key : spawnList) {
                spawnSection = locationsFile.getConfigurationSection("Spawn.ByWorld." + key);
                if (spawnSection != null) {
                    fileManager = new FileManager(key + ".yml", "data/spawns", plugin);
                    World world = Bukkit.getWorld(spawnSection.getString("world", key));
                    spawn = fileManager.getConfig();

                    assert world != null;
                    spawn.set("world", world.getUID().toString());
                    spawn.set("world_name", spawnSection.getString("world", key));
                    spawn.set("x", spawnSection.getDouble("x", 0));
                    spawn.set("y", spawnSection.getDouble("y", 0));
                    spawn.set("z", spawnSection.getDouble("z", 0));
                    spawn.set("yaw", spawnSection.getDouble("yaw", 0));
                    spawn.set("pitch", spawnSection.getDouble("pitch", 0));
                    spawn.set("displayname", key);
                    spawn.set("lastowner", "");
                    spawn.set("is_main", false);
                    if (spawn.getConfigurationSection("teleport_actions.actions") == null) {
                        spawn.set("teleport_actions.actions", new ArrayList<>());
                    }
                    if (spawn.getConfigurationSection("teleport_conditions.conditions") == null) {
                        spawn.set("teleport_conditions.conditions", new ArrayList<>());
                    }

                    fileManager.saveConfig();
                    locationsFile.set("Spawn.ByWorld." + key, null);
                    if (locationsFile.getConfigurationSection("Spawn.ByWorld").getKeys(false).isEmpty()) {
                        locationsFile.set("Spawn.ByWorld", null);
                    }
                }
            }
        }

        ConfigurationSection spawnsSection = locationsFile.getConfigurationSection("Spawn");
        if (spawnsSection != null && spawnsSection.getKeys(false).isEmpty()){
            locationsFile.set("Spawn", null);
        }
        plugin.getLocationsManager().saveLocationsFile();
    }

    private void lobby(){
        FileConfiguration locationsFile = plugin.getLocationsManager().getLocationsConfig();

        if (locationsFile == null) return;

        ConfigurationSection lobbySection;
        FileManager fileManager;
        FileConfiguration lobby;
        lobbySection = locationsFile.getConfigurationSection("Lobby.General");
        if (lobbySection != null){
            fileManager = new FileManager("general-lobby.yml", "data/lobbies", plugin);
            World world = Bukkit.getWorld(Objects.requireNonNull(lobbySection.getString("world")));
            lobby = fileManager.getConfig();

            assert world != null;
            lobby.set("world", world.getUID().toString());
            lobby.set("world_name", lobbySection.getString("world"));
            lobby.set("x", lobbySection.getDouble("x", 0));
            lobby.set("y", lobbySection.getDouble("y", 0));
            lobby.set("z", lobbySection.getDouble("z", 0));
            lobby.set("yaw", lobbySection.getDouble("yaw", 0));
            lobby.set("pitch", lobbySection.getDouble("pitch", 0));
            lobby.set("displayname", world.getName());
            lobby.set("lastowner", "");
            lobby.set("is_main", true);
            if (lobby.getConfigurationSection("teleport_actions.actions") == null){
                lobby.set("teleport_actions.actions", new ArrayList<>());
            }
            if (lobby.getConfigurationSection("teleport_conditions.conditions") == null){
                lobby.set("teleport_conditions.conditions", new ArrayList<>());
            }

            fileManager.saveConfig();
            locationsFile.set("Lobby.General", null);
        }

        ConfigurationSection lobbyMultipleSection = locationsFile.getConfigurationSection("Lobby.Multiple");
        if (lobbyMultipleSection != null) {
            Set<String> lobbyList = lobbyMultipleSection.getKeys(false);
            for (String key : lobbyList) {
                lobbySection = locationsFile.getConfigurationSection("Lobby.Multiple." + key);
                if (lobbySection != null) {
                    fileManager = new FileManager(key + ".yml", "data/lobbies", plugin);
                    World world = Bukkit.getWorld(lobbySection.getString("world", key));
                    lobby = fileManager.getConfig();

                    assert world != null;
                    lobby.set("world", world.getUID().toString());
                    lobby.set("world_name", lobbySection.getString("world"));
                    lobby.set("x", lobbySection.getDouble("x", 0));
                    lobby.set("y", lobbySection.getDouble("y", 0));
                    lobby.set("z", lobbySection.getDouble("z", 0));
                    lobby.set("yaw", lobbySection.getDouble("yaw", 0));
                    lobby.set("pitch", lobbySection.getDouble("pitch", 0));
                    lobby.set("displayname", world.getName());
                    lobby.set("lastowner", "");
                    lobby.set("is_main", false);
                    if (lobby.getConfigurationSection("teleport_actions.actions") == null){
                        lobby.set("teleport_actions.actions", new ArrayList<>());
                    }
                    if (lobby.getConfigurationSection("teleport_conditions.conditions") == null){
                        lobby.set("teleport_conditions.conditions", new ArrayList<>());
                    }

                    fileManager.saveConfig();
                    locationsFile.set("Lobby.Multiple." + key, null);
                    if (locationsFile.getConfigurationSection("Lobby.Multiple.").getKeys(false).isEmpty()) {
                        locationsFile.set("Lobby.Multiple", null);
                    }
                }
            }
        }

        ConfigurationSection spawnsSection = locationsFile.getConfigurationSection("Lobby");
        if (spawnsSection != null && spawnsSection.getKeys(false).isEmpty()){
            locationsFile.set("Lobby", null);
        }
        plugin.getLocationsManager().saveLocationsFile();
    }
}
