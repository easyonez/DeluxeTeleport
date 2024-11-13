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
import org.bukkit.event.entity.EntityDamageEvent;

public class VoidListener implements Listener {

    private final DeluxeTeleport plugin;

    public VoidListener(DeluxeTeleport plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onVoid(EntityDamageEvent event) {
        ConfigManager config = plugin.getMainConfigManager();
        ConfigSpawnManager spawnC = plugin.getMainSpawnConfigManager();
        ConfigLobbyManager lobbyC = plugin.getMainLobbyConfigManager();
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        ConditionsManager conditionsManager = new ConditionsManager(plugin, config.getConfig(), "teleport_on_void.teleport_conditions");
        if (!conditionsManager.isCondition(player)) return;

        if (config.isTeleportOnVoidEnabled() && event.getEntity() instanceof Player) {
            if (!plugin.getMainConfigManager().getTeleportOnVoidIgnoreWorlds().contains(player.getWorld().getName())) {
                if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                    String destination1;
                    String destination2 = "general";
                    String destination3 = null;
                    if (config.getTeleportOnVoidDestinationPlace().equalsIgnoreCase("spawn")){
                        destination1 = "spawn";
                        if (spawnC.isByWorld()){
                            destination2 = "byworld";
                            destination3 = config.getTeleportOnVoidDestination();
                        }
                    } else if (config.getTeleportOnVoidDestinationPlace().equalsIgnoreCase("lobby")) {
                        destination1 = "lobby";
                        if (lobbyC.isMultipleLobbies()){
                            destination2 = "multiple";
                            destination3 = config.getTeleportOnVoidDestination();
                        }
                    } else {
                        return;
                    }
                    player.setFallDistance(0.0F);
                    event.setCancelled(true);
                    Location location = LocationUtils.getLocation(plugin, destination1, destination2, destination3);
                    if (LocationUtils.isNull(plugin, Bukkit.getConsoleSender(), location, destination1, destination3, true)) return;
                    assert location != null;
                    player.teleport(location);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> player.teleport(location), 10L);

                    ActionsManager actionsManager = new ActionsManager(plugin, config.getConfig(), "teleport_on_void.teleport_actions");
                    actionsManager.general("none", player);
                }
            }
        }
    }
}
