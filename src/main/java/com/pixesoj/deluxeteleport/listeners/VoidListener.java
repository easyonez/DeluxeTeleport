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
        if (!(event.getEntity() instanceof Player) || !config.isTeleportOnVoidEnabled()) return;
        Player player = (Player) event.getEntity();
        ConditionsManager conditionsManager = new ConditionsManager(plugin, config.getConfig(), "teleport_on_void.teleport_conditions");
        if (!conditionsManager.isCondition(player)) return;
        if (!plugin.getMainConfigManager().getTeleportOnVoidIgnoreWorlds().contains(player.getWorld().getName()) && event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            Location location = LocationUtils.getDestinationPlace(plugin, player, config.getTeleportOnVoidDestinationPlace(), config.getTeleportOnVoidDestination());
            if (location == null) return;
            player.teleport(location);
            player.setFallDistance(0.0F);
            event.setCancelled(true);
            Bukkit.getScheduler().runTaskLater(plugin, () -> player.teleport(location), 5L);
            ActionsManager actionsManager = new ActionsManager(plugin, config.getConfig(), "teleport_on_void.teleport_actions");
            actionsManager.general("none", player);
        }
    }
}
