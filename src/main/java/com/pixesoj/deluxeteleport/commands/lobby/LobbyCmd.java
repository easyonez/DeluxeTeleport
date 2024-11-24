package com.pixesoj.deluxeteleport.commands.lobby;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.*;
import com.pixesoj.deluxeteleport.managers.filesmanager.ConfigLobbyManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.FileManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.PermissionsManager;
import com.pixesoj.deluxeteleport.handlers.*;
import com.pixesoj.deluxeteleport.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LobbyCmd implements CommandExecutor {

    private final DeluxeTeleport plugin;
    private final ConfigLobbyManager lobbyC;
    private final MessagesFileManager msg;
    private final MessagesManager m;
    private final PermissionsManager perm;
    private final ActionsManager actionsManager;
    private final ConditionsManager conditionsManager;
    private final boolean defaultMessages;

    private Location location;
    private String targetPlayerName;
    private String lobbyName;
    private Player targetPlayer;
    private ActionsManager lobbyActionsManager;
    private ConditionsManager lobbyConditionsManager;

    public LobbyCmd(DeluxeTeleport plugin) {
        this.plugin = plugin;
        this.lobbyC = plugin.getMainLobbyConfigManager();
        this.msg = plugin.getMainMessagesManager();
        this.m = new MessagesManager(plugin.getMainMessagesManager().getPrefixLobby(), plugin);
        this.perm = plugin.getMainPermissionsManager();
        this.defaultMessages = plugin.getMainSpawnConfigManager().getConfig().getBoolean("actions.default_messages", true);

        this.actionsManager = new ActionsManager(plugin, lobbyC.getConfig(), "teleport_actions");
        this.conditionsManager = new ConditionsManager(plugin, lobbyC.getConfig(), "teleport_conditions");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        mainCommand(sender, args);
        return true;
    }

    public void mainCommand(CommandSender sender, String[] args) {
        PermissionsManager perm = plugin.getMainPermissionsManager();
        if (!CheckEnabledManager.lobby(plugin, sender, true)) return;
        if (!PlayerUtils.hasPermission(plugin, sender, perm.getLobby(), perm.isLobbyDefault(), true)) return;
        if (PlayerUtils.isPlayer(plugin, sender, false)) {
            commandPlayer(sender, args);
        } else {
            commandConsole(sender, args);
        }
    }

    public void commandConsole(CommandSender sender, String[] args) {
        if (lobbyC.getLobbyMode().equalsIgnoreCase("server")) {
            lobbyName = "general-lobby";
            if (lobbyC.isMultipleLobbies()) {
                if (args.length < 2){
                    m.sendMessage(sender, msg.getGlobalInvalidArguments().replace("%usage%", "/lobby <lobby> <player>"), true);
                    return;
                }

                lobbyName = args[0];
                targetPlayerName = args[1];
            } else {
                if (args.length < 1){
                    m.sendMessage(sender, msg.getGlobalInvalidArguments().replace("%usage%", "/lobby <player>"), true);
                    return;
                }

                targetPlayerName = args[0];
            }
            targetPlayer = Bukkit.getPlayer(targetPlayerName);
            if (targetPlayer == null || !targetPlayer.isOnline()) {
                m.sendMessage(sender, msg.getGlobalPlayerOffline()
                        .replace("%player%", targetPlayerName), true);
                return;
            }
            location = LocationUtils.getDestinationPlace(plugin, sender, "lobby", lobbyName);
            if (location == null) return;
            targetPlayer.teleport(location);
            m.sendMessage(sender, msg.getLobbyOtherTeleported()
                    .replace("%player%", targetPlayer.getName()), true);
            String replacedMessage = plugin.getMainConfigManager().getReplacedMessagesConsole() != null ? plugin.getMainConfigManager().getReplacedMessagesConsole() : "Console";
            if (defaultMessages) m.sendMessage(targetPlayer, msg.getLobbyOtherTeleport()
                    .replace("%sender%", replacedMessage), true);


        } else if (lobbyC.getLobbyMode().equalsIgnoreCase("proxy")) {
            if (args.length == 0){
                m.sendMessage(Bukkit.getConsoleSender(), plugin.getMainMessagesManager().getGlobalInvalidArguments()
                        .replace("%usage%", "lobby <player>"), true);
            } else {
                if (!OtherUtils.isRunningOnProxy(plugin)){
                    m.sendMessage(sender, msg.getGlobalNotExecuteInProxy(), true);
                    return;
                }

                BungeeMessagingManager bungeeMessagingManager = new BungeeMessagingManager(plugin);
                targetPlayerName = args[0];
                String serverName = lobbyC.getSenderServer();
                Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
                if (!PlayerUtils.isOnline(plugin, sender, targetPlayer, false)) {
                    m.sendMessage(sender, msg.getGlobalPlayerOffline().replace("%player%", targetPlayerName), true);
                    return;
                }

                bungeeMessagingManager.sendToServer(targetPlayer, serverName);
            }
        } else {
            m.sendMessage(Bukkit.getConsoleSender(), msg.getLobbyInvalidMode()
                    .replace("%mode%", lobbyC.getLobbyMode())
                    .replace("%modes%", "Server, Proxy"), true);
        }
    }

    public void commandPlayer(CommandSender sender,  String[] args) {
        Player player = (Player) sender;
        boolean isOther = false;
        boolean isMultiple = lobbyC.isMultipleLobbies();
        String lobbyMode = lobbyC.getLobbyMode();
        boolean isProxyMode = lobbyMode.equalsIgnoreCase("proxy");

        boolean isLobbyOtherHasPermission = PlayerUtils.hasPermission(plugin, sender, perm.getLobbyOther(), perm.isLobbyOtherDefault(), false);
        boolean isBypassCooldownPermission = PlayerUtils.hasPermission(plugin, sender, perm.getLobbyBypassCooldown(), perm.isLobbyBypassCooldownDefault(), false);
        boolean isDelayBypassPermission = PlayerUtils.hasPermission(plugin, sender, perm.getLobbyBypassDelay(), perm.isLobbyBypassDelayDefault(), false);

        if (lobbyC.isCooldownEnabled() && plugin.playerLobbyInCooldown(player) && !isBypassCooldownPermission) {
            m.sendMessage(sender, msg.getLobbyInCooldown(), true);
            return;
        }

        String lobbyName = "general-lobby";
        targetPlayerName = player.getName();
        if (lobbyMode.equalsIgnoreCase("server")) {
            if (isMultiple) {
                if (lobbyC.isTeleportInMultiple() && args.length == 0){
                    lobbyName = lobbyC.getTeleportInMultipleSpecificType().equalsIgnoreCase("specify") ? lobbyC.getTeleportInMultipleSpecific() : "general-lobby";
                } else if (args.length > 0) {
                    lobbyName = args[0];
                } else {
                    String usage = isLobbyOtherHasPermission ? "/lobby <lobby> [player]" : "/lobby <lobby>";
                    m.sendMessage(sender, msg.getGlobalInvalidArguments().replace("%usage%", usage), true);
                    return;
                }

                if (isLobbyOtherHasPermission && args.length > 1){
                    targetPlayerName = args[1];
                    isOther = true;
                }
            } else {
                if (isLobbyOtherHasPermission && args.length > 0) {
                    targetPlayerName = args[0];
                    isOther = true;
                }
            }
        } else if (isProxyMode) {
            if (isLobbyOtherHasPermission && args.length > 0) {
                targetPlayerName = args[0];
                isOther = true;
            }

            if (!OtherUtils.isRunningOnProxy(plugin)) {
                m.sendMessage(sender, msg.getGlobalNotExecuteInProxy(), true);
                return;
            }
        } else {
            m.sendMessage(Bukkit.getConsoleSender(), msg.getLobbyInvalidMode()
                    .replace("%mode%", lobbyC.getLobbyMode())
                    .replace("%modes%", "Server, Proxy"), true);
            return;
        }


        FileManager fileManager = new FileManager(lobbyName + ".yml", "data/lobbies", false, plugin);
        FileConfiguration lobby = fileManager.getConfig();

        if (!isProxyMode){
            if (!FileUtils.exist(plugin, "lobbies", lobbyName)) {
                m.sendMessage(sender, msg.getLobbyNotExists().replace("%lobby%", lobbyName), true);
                return;
            }

            lobbyActionsManager = new ActionsManager(plugin, lobby, "teleport_actions");
            lobbyConditionsManager = new ConditionsManager(plugin, lobby, "teleport_conditions");
        }

        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        if (!PlayerUtils.isOnline(plugin, sender, targetPlayer, false)) {
            m.sendMessage(sender, msg.getGlobalPlayerOffline().replace("%player%", targetPlayerName), true);
            return;
        }

        if (!conditionsManager.isCondition(targetPlayer) || (lobbyMode.equalsIgnoreCase("server") && !lobbyConditionsManager.isCondition(targetPlayer))) {
            return;
        }

        if (!isOther && lobbyC.isTeleportDelayEnabled() && !isDelayBypassPermission) {
            int delay = TimeUtils.timerConverter("ticks", lobbyC.getTeleportDelay());
            DelayManager delayManager = new DelayManager(plugin, delay, player, location);
            if (lobbyMode.equalsIgnoreCase("proxy")) {
                DelayHandler.lobbyProxy(plugin, targetPlayer, delayManager, lobbyName);
            } else if (lobbyMode.equalsIgnoreCase("server")) {
                DelayHandler.lobby(plugin, targetPlayer, delayManager, lobbyName);
            }
            actionsManager.general("before_delay", targetPlayer);
            if (lobbyActionsManager != null) lobbyActionsManager.general("before_delay", targetPlayer);
            if (defaultMessages) m.sendMessage(targetPlayer, msg.getLobbyDelayInTeleport(), true);
            return;
        }

        if (lobbyMode.equalsIgnoreCase("proxy")) {
            BungeeMessagingManager bungeeMessagingManager = new BungeeMessagingManager(plugin);
            bungeeMessagingManager.sendToServer(player, lobbyC.getSenderServer());
        } else if (lobbyMode.equalsIgnoreCase("server")) {
            location = LocationUtils.getDestinationPlace(plugin, player, "lobby", lobbyName);
            if (location == null) return;
            targetPlayer.teleport(location);
        }

        if (!isOther) {
            if (lobbyC.isCooldownEnabled()){
                CooldownHandlers.Lobby(plugin, targetPlayer);
            }

            actionsManager.general("none", targetPlayer);
            if (lobbyActionsManager != null) lobbyActionsManager.general("none", targetPlayer);
            if (defaultMessages) m.sendMessage(sender, msg.getLobbyTeleporting(), true);
        } else {
            m.sendMessage(sender, msg.getLobbyOtherTeleported()
                    .replace("%player%", targetPlayerName), true);
            if (defaultMessages) m.sendMessage(targetPlayer, msg.getLobbyOtherTeleport()
                    .replace("%sender%", sender.getName()), true);
        }
    }
}