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
        if (!config.isTeleportOnRespawnEnabled()) return;
        ConditionsManager conditionsManager = new ConditionsManager(plugin, config.getConfig(), "teleport_on_respawn.teleport_conditions");
        if (!conditionsManager.isCondition(player)) return;
        Location location = LocationUtils.getDestinationPlace(plugin, player, config.getTeleportOnRespawnDestinationPlace(), config.getTeleportOnRespawnDestination());
        if (location == null) return;
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.teleport(location), 5L);
        ActionsManager actionsManager = new ActionsManager(plugin, config.getConfig(), "teleport_on_respawn.teleport_actions");
        actionsManager.general("none", player);
    }
}

