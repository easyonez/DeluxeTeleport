package com.pixesoj.deluxeteleport.listeners;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.ActionsManager;
import com.pixesoj.deluxeteleport.managers.ConditionsManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.ConfigManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.ConfigLobbyManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.ConfigSpawnManager;

import com.pixesoj.deluxeteleport.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {
    private final DeluxeTeleport plugin;

    public RespawnListener(DeluxeTeleport plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        ConfigManager config = plugin.getMainConfigManager();
        ConfigSpawnManager spawnC = plugin.getMainSpawnConfigManager();
        ConfigLobbyManager lobbyC = plugin.getMainLobbyConfigManager();

        ConditionsManager conditionsManager = new ConditionsManager(plugin, config.getConfig(), "teleport_on_respawn.teleport_conditions");
        if (!conditionsManager.isCondition(player)) return;

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (config.isTeleportOnRespawnEnabled()){
                if (config.getTeleportOnRespawnIgnoredWorlds().contains(player.getWorld().getName())) return;
                if (!config.isTeleportOnRespawnIgnoreBed()){
                    if (player.getBedSpawnLocation() != null) return;
                }
                String destination1;
                String destination2 = "general";
                String destination3 = null;
                if (config.getTeleportOnRespawnDestinationPlace().equalsIgnoreCase("spawn")){
                    destination1 = "spawn";
                    if (spawnC.isByWorld()){
                        destination2 = "byworld";
                        destination3 = config.getTeleportOnRespawnDestination();
                    }
                } else if (config.getTeleportOnRespawnDestinationPlace().equalsIgnoreCase("lobby")) {
                    destination1 = "lobby";
                    if (lobbyC.isMultipleLobbies()){
                        destination2 = "multiple";
                        destination3 = config.getTeleportOnRespawnDestination();
                    }

                } else {
                    return;
                }
                Location location = LocationUtils.getLocation(plugin, destination1, destination2, destination3);
                if (LocationUtils.isNull(plugin, Bukkit.getConsoleSender(), location, destination1, destination3, true)) return;
                assert location != null;
                player.teleport(location);

                ActionsManager actionsManager = new ActionsManager(plugin, config.getConfig(), "teleport_on_respawn.teleport_actions");
                actionsManager.general("none", player);
            }
        }, 1L);

    }
}

