package com.pixesoj.deluxeteleport.managers.filesmanager.playerdata;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerDataManager {
    private DeluxeTeleport plugin;
    private final File playerDataFolder;

    public PlayerDataManager(DeluxeTeleport plugin) {
        this.plugin = plugin;
        this.playerDataFolder = new File(plugin.getDataFolder(), "data/userdata");
        if (!playerDataFolder.exists()) {
            playerDataFolder.mkdirs();
        }
    }

    public File getPlayerFile(UUID playerUUID) {
        return new File(playerDataFolder, playerUUID.toString() + ".yml");
    }

    public void savePlayerHome(Player player, String h) {
        savePlayerHome(player.getUniqueId(), h, player.getLocation());
    }

    public void savePlayerHome(String uuidS, String home, Location location) {
        savePlayerHome(UUID.fromString(uuidS), home, location);
    }

    private void savePlayerHome(UUID uuid, String home, Location l){
        File playerFile = getPlayerFile(uuid);

        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

        String keyPath = "homes." + home + ".";
        playerData.set(keyPath + "world", l.getWorld().getName());
        playerData.set(keyPath + "x", l.getX());
        playerData.set(keyPath + "y", l.getY());
        playerData.set(keyPath + "z", l.getZ());
        playerData.set(keyPath + "yaw", l.getYaw());
        playerData.set(keyPath + "pitch", l.getPitch());

        try {
            playerData.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveTpaToggle(Player player, boolean b) {
        UUID uuid = player.getUniqueId();
        File playerFile = getPlayerFile(uuid);

        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
        playerData.set("settings.tpa-requests", b);


        try {
            playerData.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delPlayerHome(Player player, String home) {
        UUID uuid = player.getUniqueId();
        File playerFile = getPlayerFile(uuid);
        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
        playerData.set("homes." + home, null);

        try {
            playerData.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}