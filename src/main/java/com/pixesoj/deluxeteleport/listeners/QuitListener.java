package com.pixesoj.deluxeteleport.listeners;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.managers.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class QuitListener implements Listener {
    private final DeluxeTeleport plugin;

    public QuitListener(DeluxeTeleport plugin) {
        this.plugin = plugin;
    }

    /*@EventHandler
    public void saveLastLocation(PlayerQuitEvent event) {
        boolean save = plugin.getMainLobbyConfigManager().isLastLocationSave();
        if (!save){
            return;
        }

        List<String> saveType = plugin.getMainLobbyConfigManager().getLastLocationSaveType();
        if (!saveType.contains("OnPlayerQuit")){
            return;
        }

        Player player = event.getPlayer();
        String playerWorld = player.getWorld().getName();

        plugin.removeLastLocationOneTime(player);

        List<String> ignoredWorlds = plugin.getMainLobbyConfigManager().getLastLocationSaveIgnoredWorlds();
        if (ignoredWorlds.contains(playerWorld)){
            return;
        }

        String dataType = plugin.getMainConfigManager().getDataType();
        if (dataType.equals("MySQL")){
            MySQL.playerCreate(plugin.getMySQL(), player.getUniqueId(), player, plugin);
            return;
        }

        if (dataType.equals("localhost")) {
            double x = player.getLocation().getX();
            double y = player.getLocation().getY();
            double z = player.getLocation().getZ();
            float yaw = player.getLocation().getYaw();
            float pitch = player.getLocation().getPitch();

            //plugin.getPlayerDataManager().savePlayerLocation(player, x, y, z, yaw, pitch);
            //plugin.getPlayerDataManager().savePlayer(player);
        }

        else {
            Bukkit.getConsoleSender().sendMessage(MessagesManager.getColoredMessage("&cEl tipo de data no es valido, usa &aMySQL &co &alocalhost"));
        }
    }*/
}
