package com.pixesoj.deluxeteleport.handlers;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import com.pixesoj.deluxeteleport.managers.DelayManager;
import org.bukkit.entity.Player;

import java.util.Objects;

public class DelayHandler {
    public static void lobby(DeluxeTeleport plugin, Player player, DelayManager delayManager){
        MessagesManager m = new com.pixesoj.deluxeteleport.managers.MessagesManager(plugin.getMainMessagesManager().getPrefixLobby(), plugin);
        MessagesFileManager msg = plugin.getMainMessagesManager();
        if (!plugin.playerInDelay(player)) {
            plugin.addPlayerTeleport(player);
            delayManager.lobby();
        } else {
            m.sendMessage(player, msg.getLobbyInTeleport(), true);
        }
    }

    public static void lobbyProxy(DeluxeTeleport plugin, Player player, DelayManager delayManager){
        MessagesManager m = new com.pixesoj.deluxeteleport.managers.MessagesManager(plugin.getMainMessagesManager().getPrefixLobby(), plugin);
        MessagesFileManager msg = plugin.getMainMessagesManager();
        if (!plugin.playerInDelay(player)) {
            plugin.addPlayerTeleport(player);
            delayManager.lobbyProxy();
        } else {
            m.sendMessage(player, msg.getLobbyInTeleport(), true);
        }
    }

    public static void spawn(DeluxeTeleport plugin, Player player, DelayManager delayManager){
        MessagesManager m = new com.pixesoj.deluxeteleport.managers.MessagesManager(plugin.getMainMessagesManager().getPrefixSpawn(), plugin);
        MessagesFileManager msg = plugin.getMainMessagesManager();
        if (!plugin.playerInDelay(player)) {
            plugin.addPlayerTeleport(player);
            delayManager.spawn();
        } else {
            m.sendMessage(player, msg.getSpawnInTeleport(), true);
        }
    }

    public static void tpa (DeluxeTeleport plugin, Player player, Player targetPlayer, DelayManager delayManager){
        MessagesManager m = new com.pixesoj.deluxeteleport.managers.MessagesManager(plugin.getMainMessagesManager().getPrefixTPA(), plugin);
        MessagesFileManager msg = plugin.getMainMessagesManager();
        boolean defaultMessage = plugin.getMainTPAConfigManager().getConfig().getBoolean("actions.default_messages", true);
        if (!plugin.playerInDelay(targetPlayer)) {
            plugin.addPlayerTeleport(targetPlayer);
            delayManager.tpa(targetPlayer);
            if (defaultMessage) {
                m.sendMessage(targetPlayer, msg.getTPADelayInTeleport()
                        .replace("%time%", String.valueOf(plugin.getMainTPAConfigManager().getDelayTime())), true);
                m.sendMessage(player, msg.getTPADelayInTeleport()
                        .replace("%time%", String.valueOf(plugin.getMainTPAConfigManager().getDelayTime())), true);
            }
        } else {
            m.sendMessage(targetPlayer, msg.getGlobalInTeleport()
                    .replace("%time%", String.valueOf(plugin.getMainTPAConfigManager().getDelayTime())), true);
        }
    }

    public static void home (DeluxeTeleport plugin, Player player, DelayManager delayManager, String home){
        MessagesManager m = new com.pixesoj.deluxeteleport.managers.MessagesManager(plugin.getMainMessagesManager().getPrefixHome(), plugin);
        MessagesFileManager msg = plugin.getMainMessagesManager();
        if (!plugin.playerInDelay(player)) {
            plugin.addPlayerTeleport(player);
            delayManager.home(home);
        } else {
            m.sendMessage(player, msg.getGlobalInTeleport()
                    .replace("%time%", String.valueOf(plugin.getMainHomeConfigManager().getTeleportDelay())), true);
        }
    }
}
