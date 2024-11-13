package com.pixesoj.deluxeteleport.utils;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeUtils {

    public static List<Map<String, Object>> getHomes(DeluxeTeleport plugin, Player player) {
        File playerFile = plugin.getPlayerDataManager().getPlayerFile(player.getUniqueId());
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

        List<Map<String, Object>> homes = new ArrayList<>();

        if (playerData.isConfigurationSection("homes")) {
            for (String homeName : playerData.getConfigurationSection("homes").getKeys(false)) {
                String path = "homes." + homeName + ".";
                String worldName = playerData.getString(path + "world");
                double x = playerData.getDouble(path + "x");
                double y = playerData.getDouble(path + "y");
                double z = playerData.getDouble(path + "z");
                float yaw = (float) playerData.getDouble(path + "yaw");
                float pitch = (float) playerData.getDouble(path + "pitch");

                if (worldName != null) {
                    Location location = new Location(player.getServer().getWorld(worldName), x, y, z, yaw, pitch);

                    Map<String, Object> homeMap = new HashMap<>();
                    homeMap.put("name", homeName);
                    homeMap.put("location", location);

                    homes.add(homeMap);
                }
            }
        }

        return homes;
    }
}
