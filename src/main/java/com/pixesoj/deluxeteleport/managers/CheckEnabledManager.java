package com.pixesoj.deluxeteleport.managers;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.filesmanager.*;
import org.bukkit.command.CommandSender;

public class CheckEnabledManager {

    public static boolean lobby (DeluxeTeleport plugin, CommandSender sender, boolean senMessage){
        MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefixLobby(), plugin);
        ConfigLobbyManager lobbyC = plugin.getMainLobbyConfigManager();
        MessagesFileManager msg = plugin.getMainMessagesManager();
        if (!lobbyC.isEnabled()) {
            if (senMessage) {
                m.sendMessage(sender, msg.getLobbyNotEnabled(), true);
            }
            return false;
        }
        return true;
    }

    public static boolean lobbyMultiple (DeluxeTeleport plugin){
        ConfigLobbyManager lobbyC = plugin.getMainLobbyConfigManager();
        return lobbyC.isMultipleLobbies();
    }

    public static boolean spawn (DeluxeTeleport plugin, CommandSender sender, boolean senMessage){
        MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefixSpawn(), plugin);
        ConfigSpawnManager spawnC = plugin.getMainSpawnConfigManager();
        MessagesFileManager msg = plugin.getMainMessagesManager();
        if (!spawnC.isEnabled()) {
            if (senMessage) {
                m.sendMessage(sender, msg.getSpawnNotEnabled(), true);
            }
            return false;
        }
        return true;
    }

    public static boolean tpa (DeluxeTeleport plugin, CommandSender sender, boolean senMessage){
        MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefixTPA(), plugin);
        ConfigTPAManager tpaC = plugin.getMainTPAConfigManager();
        MessagesFileManager msg = plugin.getMainMessagesManager();
        if (!tpaC.isEnabled()) {
            if (senMessage) {
                m.sendMessage(sender, msg.getTPANotEnabled(), true);
            }
            return false;
        }
        return true;
    }

    public static boolean spawnByWorld (DeluxeTeleport plugin){
        ConfigSpawnManager spawnC = plugin.getMainSpawnConfigManager();
        return spawnC.isByWorld();
    }

    public static boolean floodgateSupport(DeluxeTeleport plugin, String type) {
        if (type.equalsIgnoreCase("spawn")) {
            ConfigSpawnManager spawnC = plugin.getMainSpawnConfigManager();
            return spawnC.isFloodgateEnabled();
        }
        return false;
    }

    public static boolean home (DeluxeTeleport plugin, CommandSender sender, boolean senMessage){
        MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefixHome(), plugin);
        ConfigHomeManager homeC = plugin.getMainHomeConfigManager();
        MessagesFileManager msg = plugin.getMainMessagesManager();
        if (!homeC.isEnabled()) {
            if (senMessage) {
                m.sendMessage(sender, msg.getHomeNotEnabled(), true);
            }
            return false;
        }
        return true;
    }

    public static boolean warps(DeluxeTeleport plugin, CommandSender sender, boolean senMessage){
        MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefixWarp(), plugin);
        ConfigWarpsManager congig = plugin.getMainWarpConfigManager();
        MessagesFileManager msg = plugin.getMainMessagesManager();
        if (!congig.isEnabled()) {
            if (senMessage) {
                m.sendMessage(sender, msg.getWarpNotEnabled(), true);
            }
            return false;
        }
        return true;
    }
}
