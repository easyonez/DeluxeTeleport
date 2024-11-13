package com.pixesoj.deluxeteleport.listeners;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.ActionsManager;
import com.pixesoj.deluxeteleport.managers.ConditionsManager;
import com.pixesoj.deluxeteleport.managers.UpdateCheckManager;
import com.pixesoj.deluxeteleport.managers.database.DataDatabase;
import com.pixesoj.deluxeteleport.managers.filesmanager.ConfigManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.ConfigLobbyManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.ConfigSpawnManager;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.model.internal.UpdateCheckResult;
import com.pixesoj.deluxeteleport.subcommands.ChangelogSubCmd;
import com.pixesoj.deluxeteleport.utils.LocationUtils;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JoinListener implements Listener {
    private final DeluxeTeleport plugin;

    public JoinListener(DeluxeTeleport plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void notifyUpdate (PlayerJoinEvent event){
        Player player = event.getPlayer();
        MessagesManager m = new MessagesManager("DeluxeTeleport", plugin);
        if (!PlayerUtils.hasPermission(plugin, player, plugin.getMainPermissionsManager().getNotifyUpdate(), plugin.getMainPermissionsManager().isNotifyUpdateDefault(), false)) return;
        UpdateCheckManager updateCheckManager = new UpdateCheckManager(plugin.version);
        UpdateCheckResult result = updateCheckManager.check();
        if (plugin.getMainConfigManager().isUpdateNotify() && result.isUpdateAvailable()) {
            List<String> message = plugin.getMainMessagesManager().getUpdateNewUpdate();
            for (String msg : message) {
                m.sendMessage(player, msg
                        .replace("%last_version%", result.getLatestVersion())
                        .replace("%latest_version%", result.getLatestVersion())
                        .replace("%plugin_url%", "https://modrinth.com/plugin/deluxeteleport/version/" + result.getLatestVersion()), false);
            }
        }

        if (player.isOp()){
            DataDatabase dataDatabase = new DataDatabase(plugin);
            if (dataDatabase.getNotificationStatus(player.getName(), plugin.version)) {
                ChangelogSubCmd changelogSubCmd = new ChangelogSubCmd(plugin);
                String[] args = {plugin.version, "1"};
                changelogSubCmd.mainSubCommand(player, args, true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void teleportOnJoin (PlayerJoinEvent event){
        ConfigManager config = plugin.getMainConfigManager();
        ConfigSpawnManager spawnC = plugin.getMainSpawnConfigManager();
        ConfigLobbyManager lobbyC = plugin.getMainLobbyConfigManager();
        Player player = event.getPlayer();
        String destination1;
        String destination2 = "general";
        String destination3 = null;

        ConditionsManager conditionsManager = new ConditionsManager(plugin, config.getConfig(), "teleport_on_join.teleport_conditions");
        if (!conditionsManager.isCondition(player)) return;

        if (event.getPlayer().hasPlayedBefore()) {
            if (config.isTeleportOnJoinEnabled()){
                if (config.getTeleportOnJoinDestinationPlace().equalsIgnoreCase("spawn")){
                    destination1 = "spawn";
                    if (spawnC.isByWorld()){
                        destination2 = "byworld";
                        destination3 = config.getTeleportOnJoinDestination();
                    }
                } else if (config.getTeleportOnJoinDestinationPlace().equalsIgnoreCase("lobby")) {
                    destination1 = "lobby";
                    if (lobbyC.isMultipleLobbies()){
                        destination2 = "multiple";
                        destination3 = config.getTeleportOnJoinDestination();
                    }
                } else {
                    return;
                }

                Location location = LocationUtils.getLocation(plugin, destination1, destination2, destination3);
                if (LocationUtils.isNull(plugin, Bukkit.getConsoleSender(), location, destination1, destination3, true)) return;
                assert location != null;
                player.teleport(location);

                ActionsManager actionsManager = new ActionsManager(plugin, config.getConfig(), "teleport_on_join.teleport_actions");
                actionsManager.general("none", player);
            }
        } else {
            conditionsManager = new ConditionsManager(plugin, config.getConfig(), "teleport_on_join.only_first_join.teleport_conditions");
            if (!conditionsManager.isCondition(player)) return;

            if (config.isTeleportOnFirstJoinJoinEnabled()) {
                if (config.getTeleportOnFirstJoinDestinationPlace().equalsIgnoreCase("spawn")) {
                    destination1 = "spawn";
                    if (spawnC.isByWorld()) {
                        destination2 = "byworld";
                        destination3 = config.getTeleportOnFirstJoinDestination();
                    }
                } else if (config.getTeleportOnFirstJoinDestinationPlace().equalsIgnoreCase("lobby")) {
                    destination1 = "lobby";
                    if (lobbyC.isMultipleLobbies()) {
                        destination2 = "multiple";
                        destination3 = config.getTeleportOnFirstJoinDestination();
                    }
                } else {
                    return;
                }

                Location location = LocationUtils.getLocation(plugin, destination1, destination2, destination3);
                if (LocationUtils.isNull(plugin, Bukkit.getConsoleSender(), location, destination1, destination3, true)) return;
                assert location != null;

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ActionsManager actionsManager = new ActionsManager(plugin, config.getConfig(), "teleport_on_join.only_first_join.teleport_actions");
                        actionsManager.general("none", player);

                        player.teleport(location);
                    }
                }.runTaskLater(plugin, 5L);
            }
        }
    }
}
