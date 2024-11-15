package com.pixesoj.deluxeteleport.managers;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.filesmanager.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Set;

public class LegacyManager {
    private final DeluxeTeleport plugin;

    public LegacyManager(DeluxeTeleport plugin){
        this.plugin = plugin;

    }

    public void start(){
        spawn();
    }

    private void spawn(){
        FileConfiguration locationsFile = plugin.getLocationsManager().getLocationsFile();

        if (locationsFile == null) return;

        ConfigurationSection spawnSection;
        FileManager fileManager;
        FileConfiguration spawn;
        spawnSection = locationsFile.getConfigurationSection("Spawn.General.Global");
        if (spawnSection != null){
            fileManager = new FileManager("general-spawn.yml", "data/spawns", plugin);
            World world = Bukkit.getWorld(spawnSection.getString("world"));
            spawn = fileManager.getConfig();

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
}
