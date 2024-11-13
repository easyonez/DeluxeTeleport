package com.pixesoj.deluxeteleport.commands.lobby;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.*;
import com.pixesoj.deluxeteleport.managers.filesmanager.ConfigLobbyManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.PermissionsManager;
import com.pixesoj.deluxeteleport.handlers.*;
import com.pixesoj.deluxeteleport.utils.LocationUtils;
import com.pixesoj.deluxeteleport.utils.OtherUtils;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import com.pixesoj.deluxeteleport.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LobbyCmd implements CommandExecutor {

    private final DeluxeTeleport plugin;
    private final ConfigLobbyManager lobbyC;
    private final MessagesFileManager msg;
    private final MessagesManager m;
    private final PermissionsManager perm;
    private final  ActionsManager actionsManager;
    private final boolean defaultMessages;
    private final ConditionsManager conditionsManager;

    private Location location;
    private String targetPlayerName;
    private String lobby;
    private Player targetPlayer;

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
        if (!PlayerUtils.isPlayer(plugin, sender, false)) {
            if (args.length > 0) {
                commandConsole(sender, args);
                return;
            }
            m.sendMessage(Bukkit.getConsoleSender(), plugin.getMainMessagesManager().getGlobalInvalidArguments()
                    .replace("%usage%", "deluxeteleport &6[lobby] &c<player>"), true);
            return;
        }
        commandPlayer(sender, args);
    }

    public void commandConsole(CommandSender sender, String[] args) {
        if (lobbyC.getLobbyMode().equalsIgnoreCase("server")) {
            if (args.length == 1 && lobbyC.isMultipleLobbies() && lobbyC.isTeleportInMultiple()) {

                if (lobbyC.getTeleportInMultipleSpecific().equalsIgnoreCase("general")) {
                    location = LocationUtils.getLocation(plugin, "lobby", "general", null);
                    if (!LocationUtils.keyPathExist(plugin, sender, "lobby", "general", null, true)) return;
                }

                else if (lobbyC.getTeleportInMultipleSpecific().equalsIgnoreCase("specify")) {
                    lobby = lobbyC.getTeleportInMultipleSpecificType();
                    location = LocationUtils.getLocation(plugin, "lobby", "multiple", lobby);
                    if (!LocationUtils.keyPathExist(plugin, sender, "lobby", "multiple", lobby, true)) return;
                }

                else {
                    m.sendMessage(sender, msg.getLobbyInvalidSpecified()
                            .replace("%type%", lobbyC.getTeleportInMultipleSpecific()), true);
                    return;
                }
                targetPlayerName = args[0];
            }

            else if (lobbyC.isMultipleLobbies()) {
                if (args.length < 2 || args[1].isEmpty()) {
                    m.sendMessage(Bukkit.getConsoleSender(), plugin.getMainMessagesManager().getGlobalInvalidArguments()
                            .replace("%usage%", "deluxeteleport &6[lobby] &c<player>"), true);
                    return;
                }

                lobby = args[0];
                if (!LocationUtils.keyPathExist(plugin, sender, "lobby", "multiple", lobby, true)) return;
                targetPlayerName = args[1];
                location = LocationUtils.getLocation(plugin, "lobby", "multiple", lobby);
            }

            else {
                if (args.length < 1) {
                    m.sendMessage(Bukkit.getConsoleSender(), plugin.getMainMessagesManager().getGlobalInvalidArguments()
                            .replace("%usage%", "deluxeteleport &c<player>"), true);
                    return;
                }

                targetPlayerName = args[0];
                location = LocationUtils.getLocation(plugin, "lobby", "general", null);
                if (!LocationUtils.keyPathExist(plugin, sender, "lobby", "general", null, true)) return;
            }

            targetPlayer = Bukkit.getPlayer(targetPlayerName);
            if (targetPlayer == null || !targetPlayer.isOnline()) {
                m.sendMessage(sender, msg.getGlobalPlayerOffline()
                        .replace("%player%", targetPlayerName), true);
                return;
            }

            if (LocationUtils.isNull(plugin, sender, location, "lobby", lobby, true)) return;

            m.sendMessage(sender, msg.getLobbyOtherTeleported()
                    .replace("%player%", targetPlayerName), true);

            String messageToSend;
            if (msg.getLobbyOtherTeleport() != null) {
                messageToSend = msg.getLobbyOtherTeleport()
                        .replace("%sender%", plugin.getMainConfigManager().getReplacedMessagesConsole());
            } else {
                messageToSend = "Console";
            }
            m.sendMessage(targetPlayer, messageToSend, true);

            assert location != null;
            targetPlayer.teleport(location);

        } else if (lobbyC.getLobbyMode().equalsIgnoreCase("proxy")) {
            if (args.length == 0){
                m.sendMessage(Bukkit.getConsoleSender(), plugin.getMainMessagesManager().getGlobalInvalidArguments()
                        .replace("%usage%", "deluxeteleport &c<player>"), true);
            } else {
                if (!OtherUtils.isRunningOnProxy(plugin)){
                    m.sendMessage(sender, msg.getGlobalNotExecuteInProxy(), true);
                    return;
                }

                if (OtherUtils.isRunningOnBungeeCord(plugin)) {
                    BungeeMessagingManager bungeeMessagingManager = new BungeeMessagingManager(plugin);
                    targetPlayerName = args[0];
                    String serverName = lobbyC.getSenderServer();
                    Player player = Bukkit.getPlayer(targetPlayerName);
                    if (player == null || !player.isOnline()) {
                        m.sendMessage(sender, msg.getGlobalPlayerOffline()
                                .replace("%player%", targetPlayerName), true);
                        return;
                    }

                    bungeeMessagingManager.sendToServer(sender, player, serverName, true);
                    assert serverName != null;
                }
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
        boolean isSucces = true;
        String lobbyMode = lobbyC.getLobbyMode();
        plugin.getLogger().info("Command Player");

        boolean isOtherPermission = PlayerUtils.hasPermission(plugin, sender, perm.getLobbyOther(), perm.isLobbyOtherDefault(), false);
        if (lobbyMode.equalsIgnoreCase("server")) {
            if (lobbyC.isMultipleLobbies()) {
                if (isOtherPermission) {
                    if (args.length != 0) {
                        lobby = args[0];
                        if (args.length >= 2) {
                          targetPlayerName = args[1];
                            isOther = true;
                        } else {
                          targetPlayerName = sender.getName();
                        }

                        location = LocationUtils.getLocation(plugin, "lobby", "multiple", lobby);
                        if (!LocationUtils.keyPathExist(plugin, sender, "lobby", "multiple", lobby, true)) return;
                    } else {
                        if (lobbyC.isTeleportInMultiple()) {
                            if (lobbyC.getTeleportInMultipleSpecific().equalsIgnoreCase("general")) {
                                location = LocationUtils.getLocation(plugin, "lobby", "general", null);
                                if (!LocationUtils.keyPathExist(plugin, sender, "lobby", "general", null, true)) return;
                            } else if (lobbyC.getTeleportInMultipleSpecific().equalsIgnoreCase("specify")) {
                                lobby = lobbyC.getTeleportInMultipleSpecificType();
                                location = LocationUtils.getLocation(plugin, "lobby", "multiple", lobby);
                                if (!LocationUtils.keyPathExist(plugin, sender, "lobby", "multiple", lobby, true)) return;
                            } else {
                                m.sendMessage(sender, msg.getLobbyInvalidSpecified()
                                        .replace("%type%", lobbyC.getTeleportInMultipleSpecific()), true);
                                return;
                            }
                            targetPlayerName = player.getName();
                        } else {
                            m.sendMessage(sender, msg.getGlobalInvalidArguments()
                                    .replace("%usage%", "&a/deluxeteleport&a &c<lobby> &6[player]"), true);
                            return;
                        }
                    }
                } else {
                    targetPlayerName = player.getName();
                    if (args.length == 0) {
                        if (lobbyC.isTeleportInMultiple()) {
                            if (lobbyC.getTeleportInMultipleSpecific().equalsIgnoreCase("general")) {
                                location = LocationUtils.getLocation(plugin, "lobby", "general", null);
                                if (!LocationUtils.keyPathExist(plugin, sender, "lobby", "general", null, true)) return;
                            } else if (lobbyC.getTeleportInMultipleSpecific().equalsIgnoreCase("specify")) {
                                lobby = lobbyC.getTeleportInMultipleSpecificType();
                                location = LocationUtils.getLocation(plugin, "lobby", "multiple", lobby);
                                if (!LocationUtils.keyPathExist(plugin, sender, "lobby", "multiple", lobby, true)) return;
                            } else {
                                m.sendMessage(sender, msg.getLobbyInvalidSpecified()
                                        .replace("%type%", lobbyC.getTeleportInMultipleSpecific()), true);
                                return;
                            }
                        } else {
                            m.sendMessage(sender, msg.getGlobalInvalidArguments()
                                    .replace("%usage%", "&a/deluxeteleport &6<player>"), true);
                            return;
                        }
                    } else {
                        lobby = args[0];
                        location = LocationUtils.getLocation(plugin, "lobby", "multiple", lobby);
                        if (!LocationUtils.keyPathExist(plugin, sender, "lobby", "multiple", lobby, true)) return;
                    }
                }
            } else {
                targetPlayerName = player.getName();
                if (isOtherPermission) {
                    if (!(args.length == 0)) {
                        targetPlayerName = args[0];
                        isOther = true;
                    }
                }

                location = LocationUtils.getLocation(plugin, "lobby", "general", null);
                if (!LocationUtils.keyPathExist(plugin, sender, "lobby", "general", null, true)) return;
            }


            targetPlayer = Bukkit.getPlayer(targetPlayerName);
            if (!PlayerUtils.isOnline(plugin, sender, targetPlayer, true)) return;

            if (LocationUtils.isNull(plugin, sender, location, "lobby", lobby, true)) return;
            assert location != null;
        } else if (lobbyMode.equalsIgnoreCase("proxy")) {
            if (isOtherPermission) {
                if (args.length != 0) {
                    targetPlayerName = args[0];
                    isOther = true;
                } else {
                    targetPlayerName = sender.getName();
                }

                if (!OtherUtils.isRunningOnProxy(plugin)) {
                    m.sendMessage(sender, msg.getGlobalNotExecuteInProxy(), true);
                    return;
                } else {
                    targetPlayer = Bukkit.getPlayer(targetPlayerName);
                    if (!PlayerUtils.isOnline(plugin, sender, targetPlayer, true)) return;
                }
            } else {
                targetPlayerName = sender.getName();
                if (!OtherUtils.isRunningOnProxy(plugin)) {
                    m.sendMessage(sender, msg.getGlobalNotExecuteInProxy(), true);
                } else {
                    targetPlayer = Bukkit.getPlayer(targetPlayerName);
                    if (!PlayerUtils.isOnline(plugin, sender, targetPlayer, true)) return;
                }
            }

            targetPlayer = Bukkit.getPlayer(targetPlayerName);
        } else {
            m.sendMessage(Bukkit.getConsoleSender(), msg.getLobbyInvalidMode()
                    .replace("%mode%", lobbyC.getLobbyMode())
                    .replace("%modes%", "Server, Proxy"), true);
            isSucces = false;
        }

        if (!isSucces) return;

        if (!conditionsManager.isCondition(targetPlayer)) return;

        boolean isBypassCooldownPermission = PlayerUtils.hasPermission(plugin, sender, perm.getLobbyBypassCooldown(), perm.isLobbyBypassCooldownDefault(), false);
        if (!isOther && lobbyC.isCooldownEnabled() && plugin.playerLobbyInCooldown(player) && !isBypassCooldownPermission) {
            m.sendMessage(sender, msg.getLobbyInCooldown(), true);
            return;
        }

        boolean isDelayBypassPermission = PlayerUtils.hasPermission(plugin, sender, perm.getLobbyBypassDelay(), perm.isLobbyBypassDelayDefault(), false);
        if (!isOther && lobbyC.isTeleportDelayEnabled() && !isDelayBypassPermission) {
            int delay = TimeUtils.timerConverter("ticks", lobbyC.getTeleportDelay());
            DelayManager delayManager = new DelayManager(plugin, delay, player, location);
            if (lobbyMode.equalsIgnoreCase("proxy")) {
                DelayHandler.lobbyProxy(plugin, targetPlayer, delayManager);
            } else if (lobbyMode.equalsIgnoreCase("server")) {
                DelayHandler.lobby(plugin, targetPlayer, delayManager);
            }
            actionsManager.general("before_delay", targetPlayer);
            if (defaultMessages) m.sendMessage(targetPlayer, msg.getLobbyDelayInTeleport(), true);
            return;
        }

        if (lobbyMode.equalsIgnoreCase("proxy")) {
            BungeeMessagingManager bungeeMessagingManager = new BungeeMessagingManager(plugin);
            bungeeMessagingManager.sendToServer(sender, player, lobbyC.getSenderServer(), isOther);
        } else if (lobbyMode.equalsIgnoreCase("server")) {
            targetPlayer.teleport(location);
        }

        if (!isOther) {
            if (lobbyC.isCooldownEnabled()){
                CooldownHandlers.Lobby(plugin, targetPlayer);
            }

            actionsManager.general("none", targetPlayer);
            if (defaultMessages) m.sendMessage(sender, msg.getLobbyTeleporting(), true);
        } else {
            m.sendMessage(sender, msg.getLobbyOtherTeleported()
                    .replace("%player%", targetPlayerName), true);

            String replacedMessage = plugin.getMainConfigManager().getReplacedMessagesConsole() != null ? plugin.getMainConfigManager().getReplacedMessagesConsole() : "Console";
            if (defaultMessages) m.sendMessage(targetPlayer, msg.getLobbyOtherTeleport()
                    .replace("%sender%", replacedMessage), true);
        }
    }
}