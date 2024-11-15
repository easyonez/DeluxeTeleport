package com.pixesoj.deluxeteleport.managers;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.filesmanager.FileManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import com.pixesoj.deluxeteleport.handlers.CooldownHandlers;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Map;

public class DelayManager {
    public boolean playerLobbyMovedDuringDelay;
    public boolean playerSpawnMovedDuringDelay;
    public boolean playerHomeMovedDuringDelay;
    public boolean playerTpaMovedDuringDelay;
    int TaskID;
    private final DeluxeTeleport plugin;
    int time;
    private final Player player;
    private final Location l;

    private static final Map<String, DelayManager> delaySpawnMap = new HashMap<>();
    private static final Map<String, DelayManager> delayLobbyMap = new HashMap<>();
    private static final Map<String, DelayManager> delayHomeMap = new HashMap<>();
    private static final Map<String, DelayManager> delayTPAMap = new HashMap<>();
    private static final Map<String, DelayManager> delayWarpMap = new HashMap<>();

    public DelayManager(DeluxeTeleport plugin, int time, Player player, Location l){
        this.plugin = plugin;
        this.time = time;
        this.player = player;
        this.l = l;
    }

    public void lobby(){
        boolean defaultMessage = plugin.getMainLobbyConfigManager().getConfig().getBoolean("actions.default_messages", true);
        ActionsManager actionsManager = new ActionsManager(plugin, plugin.getMainLobbyConfigManager().getConfig(), "teleport_actions");
        boolean cancelOnMove = plugin.getMainLobbyConfigManager().isTeleportDelayCancelOnMove();
        BukkitScheduler sh = Bukkit.getServer().getScheduler();
        delayLobbyMap.put(player.getName(), this);
        TaskID = sh.runTaskTimerAsynchronously(plugin, new Runnable() {
            final int initialX = player.getLocation().getBlockX();
            final int initialY = player.getLocation().getBlockY();
            final int initialZ = player.getLocation().getBlockZ();

            @Override
            public void run() {
                if (time == 0){
                    Bukkit.getScheduler().cancelTask(TaskID);
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        com.pixesoj.deluxeteleport.managers.MessagesManager m = new com.pixesoj.deluxeteleport.managers.MessagesManager(plugin.getMainMessagesManager().getPrefixLobby(), plugin);
                        if (defaultMessage) m.sendMessage(player, plugin.getMainMessagesManager().getLobbyTeleporting(), true);
                        playerLobbyMovedDuringDelay = false;
                        player.teleport(l);
                        plugin.removePlayerTeleport(player);
                        if (plugin.getMainLobbyConfigManager().isCooldownEnabled()) {
                            CooldownHandlers.Lobby(plugin, player);
                        }
                        delayLobbyMap.remove(player.getName());
                        actionsManager.general("after_delay", player);
                        actionsManager.general("none", player);
                    });
                } else {
                    com.pixesoj.deluxeteleport.managers.MessagesManager m = new com.pixesoj.deluxeteleport.managers.MessagesManager(plugin.getMainMessagesManager().getPrefixLobby(), plugin);
                    if (cancelOnMove && (player.getLocation().getBlockX() != initialX ||
                            player.getLocation().getBlockY() != initialY ||
                            player.getLocation().getBlockZ() != initialZ)) {
                        if (defaultMessage) m.sendMessage(player, plugin.getMainMessagesManager().getLobbyCanceledMove(), true);
                        Bukkit.getScheduler().cancelTask(TaskID);
                        playerLobbyMovedDuringDelay = true;
                        plugin.removePlayerTeleport(player);
                        delayLobbyMap.remove(player.getName());
                        actionsManager.general("cancel_delay", player);
                        return;
                    }
                    actionsManager.general("during_delay", player);
                    time--;
                }
            }
        }, 0L, 20L).getTaskId();
    }

    public void lobbyProxy(){
        boolean defaultMessage = plugin.getMainLobbyConfigManager().getConfig().getBoolean("actions.default_messages", true);
        ActionsManager actionsManager = new ActionsManager(plugin, plugin.getMainLobbyConfigManager().getConfig(), "teleport_actions");
        boolean cancelOnMove = plugin.getMainLobbyConfigManager().isTeleportDelayCancelOnMove();
        BukkitScheduler sh = Bukkit.getServer().getScheduler();
        delayLobbyMap.put(player.getName(), this);
        TaskID = sh.runTaskTimerAsynchronously(plugin, new Runnable() {
            final int initialX = player.getLocation().getBlockX();
            final int initialY = player.getLocation().getBlockY();
            final int initialZ = player.getLocation().getBlockZ();

            @Override
            public void run() {
                if (time == 0){
                    Bukkit.getScheduler().cancelTask(TaskID);
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        BungeeMessagingManager bungeeMessagingManager = new BungeeMessagingManager(plugin);
                        bungeeMessagingManager.sendToServer(player, player, plugin.getMainLobbyConfigManager().getSenderServer(), false);
                        playerLobbyMovedDuringDelay = false;
                        plugin.removePlayerTeleport(player);
                        if (plugin.getMainLobbyConfigManager().isCooldownEnabled()) {
                            CooldownHandlers.Lobby(plugin, player);
                        }
                        delayLobbyMap.remove(player.getName());
                        actionsManager.general("after_delay", player);
                        actionsManager.general("none", player);
                    });
                } else {
                    com.pixesoj.deluxeteleport.managers.MessagesManager m = new com.pixesoj.deluxeteleport.managers.MessagesManager(plugin.getMainMessagesManager().getPrefixLobby(), plugin);
                    if (cancelOnMove && (player.getLocation().getBlockX() != initialX ||
                            player.getLocation().getBlockY() != initialY ||
                            player.getLocation().getBlockZ() != initialZ)) {
                        if (defaultMessage) m.sendMessage(player, plugin.getMainMessagesManager().getLobbyCanceledMove(), true);
                        Bukkit.getScheduler().cancelTask(TaskID);
                        playerLobbyMovedDuringDelay = true;
                        plugin.removePlayerTeleport(player);
                        delayLobbyMap.remove(player.getName());
                        actionsManager.general("cancel_delay", player);
                        return;
                    }
                    actionsManager.general("during_delay", player);
                    time--;
                }
            }
        }, 0L, 20L).getTaskId();
    }

    public void spawn(){
        boolean defaultMessage = plugin.getMainSpawnConfigManager().getConfig().getBoolean("actions.default_messages", true);
        ActionsManager actionsManager = new ActionsManager(plugin, plugin.getMainSpawnConfigManager().getConfig(), "teleport_actions");
        ActionsManager spawnActionsManager = new ActionsManager(plugin, plugin.getMainSpawnConfigManager().getConfig(), "teleport_actions");
        boolean cancelOnMove = plugin.getMainSpawnConfigManager().isTeleportDelayCancelOnMove();
        BukkitScheduler sh = Bukkit.getServer().getScheduler();
        delaySpawnMap.put(player.getName(), this);
        TaskID = sh.runTaskTimerAsynchronously(plugin, new Runnable() {
            final int initialX = player.getLocation().getBlockX();
            final int initialY = player.getLocation().getBlockY();
            final int initialZ = player.getLocation().getBlockZ();

            @Override
            public void run() {
                MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefixSpawn(), plugin);
                if (time == 0){
                    Bukkit.getScheduler().cancelTask(TaskID);
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        if (defaultMessage) m.sendMessage(player, plugin.getMainMessagesManager().getSpawnTeleporting(), true);
                        playerSpawnMovedDuringDelay = false;
                        player.teleport(l);
                        plugin.removePlayerTeleport(player);
                        delaySpawnMap.remove(player.getName());

                        if (plugin.getMainSpawnConfigManager().isCooldownEnabled()) {
                            CooldownHandlers.Spawn(plugin, player);
                        }
                        actionsManager.general("after_delay", player);
                        actionsManager.general("none", player);

                        spawnActionsManager.general("after_delay", player);
                        spawnActionsManager.general("none", player);
                    });
                } else {
                    if (cancelOnMove && (player.getLocation().getBlockX() != initialX ||
                            player.getLocation().getBlockY() != initialY ||
                            player.getLocation().getBlockZ() != initialZ)) {
                        if (defaultMessage) m.sendMessage(player, plugin.getMainMessagesManager().getSpawnCanceledMove(), true);
                        Bukkit.getScheduler().cancelTask(TaskID);
                        playerSpawnMovedDuringDelay = true;
                        plugin.removePlayerTeleport(player);
                        delaySpawnMap.remove(player.getName());
                        actionsManager.general("cancel_delay", player);
                        spawnActionsManager.general("cancel_delay", player);
                        return;
                    }
                    actionsManager.general("during_delay", player);
                    spawnActionsManager.general("during_delay", player);
                    time--;
                }
            }
        }, 0L, 20L).getTaskId();
    }

    public void tpa(Player targetPlayer){
        boolean defaultMessage = plugin.getMainTPAConfigManager().getConfig().getBoolean("actions.default_messages", true);
        ActionsManager actionsManager = new ActionsManager(plugin, plugin.getMainTPAConfigManager().getConfig(), "teleport_actions");
        BukkitScheduler sh = Bukkit.getServer().getScheduler();
        delayTPAMap.put(player.getName(), this);
        delayTPAMap.put(targetPlayer.getName(), this);
        TaskID = sh.runTaskTimerAsynchronously(plugin, new Runnable() {
            final int initialX = targetPlayer.getLocation().getBlockX();
            final int initialY = targetPlayer.getLocation().getBlockY();
            final int initialZ = targetPlayer.getLocation().getBlockZ();

            @Override
            public void run() {
                MessagesManager m = new com.pixesoj.deluxeteleport.managers.MessagesManager(plugin.getMainMessagesManager().getPrefixTPA(), plugin);
                MessagesFileManager msg = plugin.getMainMessagesManager();
                boolean cancelOnMove = plugin.getMainTPAConfigManager().isDelayCancelOnMove();
                if (time == 0){
                    Bukkit.getScheduler().cancelTask(TaskID);
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        if (ListManager.isInTpaTeleport(player) && ListManager.isInTpaTeleport(targetPlayer)) {
                            playerTpaMovedDuringDelay = false;
                            targetPlayer.teleport(player);
                            if (defaultMessage) m.sendMessage(targetPlayer, msg.getTPATeleport()
                                    .replace("%player%", player.getName()), true);
                            if (defaultMessage) m.sendMessage(player, msg.getTPATeleportTargetPlayer()
                                    .replace("%player%", targetPlayer.getName()), true);
                            ListManager.removeTpa(player, targetPlayer.getName());
                            plugin.removePlayerTeleport(targetPlayer);

                            if (plugin.getMainTPAConfigManager().isCooldownEnabled() && plugin.getMainTPAConfigManager().isCooldownCountWhenTeleporting()) {
                                String cooldownTo = plugin.getMainTPAConfigManager().getCooldownTo();
                                switch (cooldownTo.toLowerCase()) {
                                    case "player":
                                        CooldownHandlers.tpa(plugin, player);
                                        break;

                                    case "targetplayer":
                                        CooldownHandlers.tpa(plugin, targetPlayer);
                                        break;

                                    default:
                                        CooldownHandlers.tpa(plugin, targetPlayer);
                                        CooldownHandlers.tpa(plugin, player);
                                        break;
                                }
                            }
                            ListManager.removeTpaTeleport(targetPlayer);
                        }

                        ListManager.removeTpa(player, targetPlayer.getName());
                        plugin.removePlayerTeleport(targetPlayer);
                        plugin.removePlayerTeleport(player);
                        ListManager.removeTpaTeleport(player);
                        ListManager.removeTpaTeleport(targetPlayer);
                        delayTPAMap.remove(player.getName());
                        delayTPAMap.remove(targetPlayer.getName());
                        actionsManager.tpa("after_delay", player, targetPlayer);
                        actionsManager.tpa("none", player, targetPlayer);
                    });
                } else {
                    if (cancelOnMove && (targetPlayer.getLocation().getBlockX() != initialX
                            || targetPlayer.getLocation().getBlockY() != initialY
                            || targetPlayer.getLocation().getBlockZ() != initialZ)) {
                        if (defaultMessage) m.sendMessage(targetPlayer, msg.getTPACanceledMoving(), true);
                        if (defaultMessage) m.sendMessage(player, msg.getTPACanceledMoving(), true);
                        Bukkit.getScheduler().cancelTask(TaskID);
                        playerTpaMovedDuringDelay = true;
                        plugin.removePlayerTeleport(targetPlayer);
                        ListManager.removeTpa(player, targetPlayer.getName());
                        ListManager.removeTpaTeleport(player);
                        ListManager.removeTpaTeleport(targetPlayer);
                        actionsManager.tpa("cancel_delay", player, targetPlayer);
                        delayTPAMap.remove(player.getName());
                        delayTPAMap.remove(targetPlayer.getName());
                        return;
                    }
                    actionsManager.tpa("during_delay", player, targetPlayer);
                    time--;
                }
            }
        }, 0L, 20L).getTaskId();
    }

    public void home(String home){
        boolean defaultMessage = plugin.getMainHomeConfigManager().getConfig().getBoolean("actions.default_messages", true);
        ActionsManager actionsManager = new ActionsManager(plugin, plugin.getMainHomeConfigManager().getConfig(), "teleport_actions");
        boolean cancelOnMove = plugin.getMainHomeConfigManager().isTeleportDelayCancelOnMove();
        BukkitScheduler sh = Bukkit.getServer().getScheduler();
        delayHomeMap.put(player.getName(), this);
        TaskID = sh.runTaskTimerAsynchronously(plugin, new Runnable() {
            final int initialX = player.getLocation().getBlockX();
            final int initialY = player.getLocation().getBlockY();
            final int initialZ = player.getLocation().getBlockZ();

            @Override
            public void run() {
                MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefixHome(), plugin);
                if (time == 0){
                    Bukkit.getScheduler().cancelTask(TaskID);
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        if (defaultMessage) m.sendMessage(player, plugin.getMainMessagesManager().getHomeTeleporting()
                                .replace("%home%", home), true);
                        playerHomeMovedDuringDelay = false;
                        player.teleport(l);
                        plugin.removePlayerTeleport(player);
                        delayHomeMap.remove(player.getName());

                        if (plugin.getMainHomeConfigManager().isCooldownEnabled()) {
                            CooldownHandlers.home(plugin, player);
                        }
                        actionsManager.general("after_delay", player);
                        actionsManager.general("none", player);
                    });
                } else {
                    if (cancelOnMove && (player.getLocation().getBlockX() != initialX ||
                            player.getLocation().getBlockY() != initialY ||
                            player.getLocation().getBlockZ() != initialZ)) {
                        if (defaultMessage) m.sendMessage(player, plugin.getMainMessagesManager().getHomeCanceledMove(), true);
                        Bukkit.getScheduler().cancelTask(TaskID);
                        playerHomeMovedDuringDelay = true;
                        plugin.removePlayerTeleport(player);
                        delayHomeMap.remove(player.getName());
                        actionsManager.general("cancel_delay", player);
                        return;
                    }
                    actionsManager.general("during_delay", player);
                    time--;
                }
            }
        }, 0L, 20L).getTaskId();
    }

    public void warp(String warp){
        FileManager warpFile = new FileManager(warp + ".yml", "data/warps", false, plugin);
        FileConfiguration warpData = warpFile.getConfig();
        boolean defaultMessage = plugin.getMainWarpConfigManager().getConfig().getBoolean("actions.default_messages", true);
        ActionsManager actions = new ActionsManager(plugin, plugin.getMainWarpConfigManager().getConfig(), "teleport_actions");
        ActionsManager warpActions = new ActionsManager(plugin, warpData, "teleport_actions");
        boolean cancelOnMove = plugin.getMainWarpConfigManager().isTeleportDelayCancelOnMove();
        BukkitScheduler sh = Bukkit.getServer().getScheduler();
        delayWarpMap.put(player.getName(), this);
        TaskID = sh.runTaskTimerAsynchronously(plugin, new Runnable() {
            final int initialX = player.getLocation().getBlockX();
            final int initialY = player.getLocation().getBlockY();
            final int initialZ = player.getLocation().getBlockZ();

            @Override
            public void run() {
                MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefixWarp(), plugin);
                if (time == 0){
                    Bukkit.getScheduler().cancelTask(TaskID);
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        if (defaultMessage) m.sendMessage(player, plugin.getMainMessagesManager().getWarpTeleporting()
                                .replace("%warp%", warp), true);
                        playerHomeMovedDuringDelay = false;
                        player.teleport(l);
                        plugin.removePlayerTeleport(player);
                        delayWarpMap.remove(player.getName());

                        if (plugin.getMainWarpConfigManager().isCooldownEnabled()) {
                            CooldownHandlers.warp(plugin, player);
                        }

                        actions.general("after_delay", player);
                        actions.general("none", player);
                        warpActions.general("after_delay", player);
                        warpActions.general("none", player);
                    });
                } else {
                    if (cancelOnMove && (player.getLocation().getBlockX() != initialX ||
                            player.getLocation().getBlockY() != initialY ||
                            player.getLocation().getBlockZ() != initialZ)) {
                        if (defaultMessage) m.sendMessage(player, plugin.getMainMessagesManager().getWarpCanceledMove(), true);
                        Bukkit.getScheduler().cancelTask(TaskID);
                        playerHomeMovedDuringDelay = true;
                        plugin.removePlayerTeleport(player);
                        delayWarpMap.remove(player.getName());
                        actions.general("cancel_delay", player);
                        warpActions.general("cancel_delay", player);
                        return;
                    }
                    actions.general("during_delay", player);
                    warpActions.general("during_delay", player);
                    time--;
                }
            }
        }, 0L, 20L).getTaskId();
    }

    public int getRemainingTime() {
        return time;
    }

    public static int getRemainingSpawnTime(Player player) {
        DelayManager delayManager = delaySpawnMap.get(player.getName());
        return (delayManager != null) ? delayManager.getRemainingTime() : 0;
    }

    public static int getRemainingLobbyTime(Player player) {
        DelayManager delayManager = delayLobbyMap.get(player.getName());
        return (delayManager != null) ? delayManager.getRemainingTime() : 0;
    }

    public static int getRemainingHomeTime(Player player) {
        DelayManager delayManager = delayHomeMap.get(player.getName());
        return (delayManager != null) ? delayManager.getRemainingTime() : 0;
    }

    public static int getRemainingTPATime(Player player) {
        DelayManager delayManager = delayTPAMap.get(player.getName());
        return (delayManager != null) ? delayManager.getRemainingTime() : 0;
    }

    public static int getRemainingWarpTime(Player player) {
        DelayManager delayManager = delayWarpMap.get(player.getName());
        return (delayManager != null) ? delayManager.getRemainingTime() : 0;
    }
}