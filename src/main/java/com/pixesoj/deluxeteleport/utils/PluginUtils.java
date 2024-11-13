package com.pixesoj.deluxeteleport.utils;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import org.bukkit.configuration.file.FileConfiguration;

public class PluginUtils {

    public static FileConfiguration getConfig(DeluxeTeleport plugin, String configType){
        switch (configType){
            case "spawn": return plugin.getMainSpawnConfigManager().getConfig();
            case "lobby": return plugin.getMainLobbyConfigManager().getConfig();
            case "home": return plugin.getMainHomeConfigManager().getConfig();
            case "tpa": return plugin.getMainTPAConfigManager().getConfig();
            default: return plugin.getMainConfigManager().getConfig();
        }
    }

    public static FileConfiguration getConfig(DeluxeTeleport plugin, String configType, String menu){
        switch (configType){
            case "spawn": return plugin.getMainSpawnConfigManager().getConfig();
            case "lobby": return plugin.getMainLobbyConfigManager().getConfig();
            case "home": return plugin.getMainHomeConfigManager().getConfig();
            case "tpa": return plugin.getMainTPAConfigManager().getConfig();
            case "menu": return plugin.getMainMenuManager().getConfig(menu);
            default: return plugin.getMainConfigManager().getConfig();
        }
    }
}
