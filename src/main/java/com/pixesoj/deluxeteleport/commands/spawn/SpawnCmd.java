package com.pixesoj.deluxeteleport.commands.spawn;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.*;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.PermissionsManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.ConfigSpawnManager;
import com.pixesoj.deluxeteleport.handlers.*;
import com.pixesoj.deluxeteleport.utils.LocationUtils;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import com.pixesoj.deluxeteleport.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCmd implements CommandExecutor {
    private final DeluxeTeleport plugin;
    private final ConfigSpawnManager spawnC;
    private final MessagesFileManager msg;
    private final PermissionsManager perm;
    private final MessagesManager m;
    private final  ActionsManager actionsManager;
    private final ConditionsManager conditionsManager;
    private final boolean defaultMessages;

    private Location location;
    private String targetPlayerName;
    private String spawn;

    public SpawnCmd(DeluxeTeleport plugin) {
        this.plugin = plugin;
        this.spawnC = plugin.getMainSpawnConfigManager();
        this.msg = plugin.getMainMessagesManager();
        this.perm = plugin.getMainPermissionsManager();
        this.m = new MessagesManager(plugin.getMainMessagesManager().getPrefixSpawn(), plugin);

        this.conditionsManager = new ConditionsManager(plugin, spawnC.getConfig(), "teleport_conditions");
        this.actionsManager = new ActionsManager(plugin, spawnC.getConfig(), "teleport_actions");
        this.defaultMessages = plugin.getMainSpawnConfigManager().getConfig().getBoolean("actions.default_messages", true);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        mainCommand(sender, args);
        return true;
    }

    public void mainCommand(@NotNull CommandSender sender, String[] args) {
        MessagesManager m = new com.pixesoj.deluxeteleport.managers.MessagesManager(plugin.getMainMessagesManager().getPrefixSpawn(), plugin);
        PermissionsManager perm = plugin.getMainPermissionsManager();

        if (!CheckEnabledManager.spawn(plugin, sender, true)) return;
        if (!PlayerUtils.hasPermission(plugin, sender, perm.getSpawn(), perm.isSpawnDefault(), true)) return;
        if (!PlayerUtils.isPlayer(plugin, sender, false)) {
            if (args.length > 0) {
                commandConsole(sender, args);
                return;
            }
            m.sendMessage(sender, plugin.getMainMessagesManager().getGlobalInvalidArguments()
                    .replace("%usage%", "&aspawn &6[spawn] &c<player>"), true);
            return;
        }

        commandPlayer(sender, args);
    }

    public void commandConsole(CommandSender sender, String[] args) {
       if (args.length == 1 && spawnC.isByWorld() && spawnC.isTeleportInByWorldEnabled()) {
          if (spawnC.getTeleportInByWorldSpawn().equalsIgnoreCase("general")) {
              location = LocationUtils.getLocation(plugin, "spawn", "general", null);
              if (!LocationUtils.keyPathExist(plugin, sender, "spawn", "general", null, true)) return;
          } else if (spawnC.getTeleportInByWorldSpawn().equalsIgnoreCase("specify")) {
              spawn = spawnC.getTeleportInByWorldSpecify();
              location = LocationUtils.getLocation(plugin, "spawn", "byworld", spawn);
              if (!LocationUtils.keyPathExist(plugin, sender, "spawn", "byworld", spawn, true)) return;
          } else {
              m.sendMessage(sender, msg.getSpawnInvalidSpecified()
                      .replace("%type%", spawnC.getTeleportInByWorldSpawn()), true);
              return;
          }
          targetPlayerName = args[0];
       } else if (spawnC.isByWorld()) {
          if (args.length < 2 || args[1].isEmpty()) {
              m.sendMessage(Bukkit.getConsoleSender(), plugin.getMainMessagesManager().getGlobalInvalidArguments()
                      .replace("%usage%", "&aspawn &6[spawn] &c<player>"), true);
              return;
          }

          spawn = args[0];
          targetPlayerName = args[1];
          location = LocationUtils.getLocation(plugin, "spawn", "byworld", spawn);
          if (!LocationUtils.keyPathExist(plugin, sender, "spawn", "byworld", spawn, true)) return;
       } else {
          if (args.length < 1) {
              m.sendMessage(Bukkit.getConsoleSender(), plugin.getMainMessagesManager().getGlobalInvalidArguments()
                      .replace("%usage%", "&aspawn &c<player>"), true);
              return;
          }

          targetPlayerName = args[0];
          location = LocationUtils.getLocation(plugin, "spawn", "general", null);
          if (!LocationUtils.keyPathExist(plugin, sender, "spawn", "general", null, true)) return;
       }

        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        if (!PlayerUtils.isOnline(plugin, sender, targetPlayer, true)) return;

        if (LocationUtils.isNull(plugin, sender, location, "spawn", spawn, true)) return;

        m.sendMessage(sender, msg.getSpawnOtherTeleported()
                .replace("%player%", targetPlayerName), true);

        String replacedMessage = plugin.getMainConfigManager().getReplacedMessagesConsole() != null ? plugin.getMainConfigManager().getReplacedMessagesConsole() : "Console";
        if (defaultMessages) m.sendMessage(targetPlayer, msg.getSpawnOtherTeleport()
                .replace("%sender%", replacedMessage), true);

        assert location != null;
        targetPlayer.teleport(location);

        actionsManager.general("none", targetPlayer);
    }

    public void commandPlayer(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        boolean isOther = false;

        boolean isByppasCooldownPermission = PlayerUtils.hasPermission(plugin, sender, perm.getSpawnBypassCooldown(), perm.isSpawnBypassCooldownDefault(), false);
        if (spawnC.isCooldownEnabled() && plugin.playerSpawnInCooldown(player) && !isByppasCooldownPermission) {
            m.sendMessage(sender, msg.getSpawnInCooldown(), true);
            return;
        }

        boolean isSpawnOtherPermission = PlayerUtils.hasPermission(plugin, sender, perm.getSpawnOther(), perm.isSpawnOtherDefault(), false);
        if (spawnC.isByWorld()) {
            if (isSpawnOtherPermission) {
                if (args.length != 0) {
                    spawn = args[0];
                    if (args.length >= 2) {
                        targetPlayerName = args[1];
                        isOther = true;
                    } else {
                        targetPlayerName = player.getName();
                    }

                    location = LocationUtils.getLocation(plugin, "spawn", "byworld", spawn);
                    if (!LocationUtils.keyPathExist(plugin, sender, "spawn", "byworld", spawn, true)) return;
                } else {
                    if (spawnC.isTeleportInByWorldEnabled()) {
                        if (spawnC.getTeleportInByWorldSpawn().equalsIgnoreCase("general")) {
                            location = LocationUtils.getLocation(plugin, "spawn", "general", null);
                            if (!LocationUtils.keyPathExist(plugin, sender, "spawn", "general", null, true)) return;
                        } else if (spawnC.getTeleportInByWorldSpawn().equalsIgnoreCase("specify")) {
                            spawn = spawnC.getTeleportInByWorldSpecify();
                            location = LocationUtils.getLocation(plugin, "spawn", "byworld", spawn);
                            if (!LocationUtils.keyPathExist(plugin, sender, "spawn", "byworld", spawn, true)) return;
                        } else {
                            m.sendMessage(sender, msg.getSpawnInvalidSpecified()
                                    .replace("%type%", spawnC.getTeleportInByWorldSpawn()), true);
                            return;
                        }
                        targetPlayerName = player.getName();
                    } else {
                        m.sendMessage(sender, msg.getGlobalInvalidArguments()
                                .replace("%usage%", "&a/spawn &c<spawn> &6[player]"), true);
                        return;
                    }
                }
            } else {
                targetPlayerName = player.getName();
                if (args.length == 0) {
                    if (spawnC.isTeleportInByWorldEnabled()) {
                        if (spawnC.getTeleportInByWorldSpawn().equalsIgnoreCase("general")) {
                            location = LocationUtils.getLocation(plugin, "spawn", "general", null);
                            if (!LocationUtils.keyPathExist(plugin, sender, "spawn", "general", null, true)) return;
                        } else if (spawnC.getTeleportInByWorldSpawn().equalsIgnoreCase("specify")) {
                            spawn = spawnC.getTeleportInByWorldSpecify();
                            location = LocationUtils.getLocation(plugin, "spawn", "byworld", spawn);
                            if (!LocationUtils.keyPathExist(plugin, sender, "spawn", "byworld", spawn, true)) return;
                        } else {
                            m.sendMessage(sender, msg.getSpawnInvalidSpecified()
                                    .replace("%type%", spawnC.getTeleportInByWorldSpawn()), true);
                            return;
                        }
                    } else {
                        m.sendMessage(sender, msg.getGlobalInvalidArguments()
                                .replace("%usage%", "&a/spawn &6<player>"), true);
                        return;
                    }
                } else {
                    spawn = args[0];
                    location = LocationUtils.getLocation(plugin, "spawn", "byworld", spawn);
                    if (!LocationUtils.keyPathExist(plugin, sender, "spawn", "byworld", spawn, true)) return;
                }
            }
        } else {
            targetPlayerName = player.getName();
            if (isSpawnOtherPermission) {
                if (!(args.length == 0)) {
                    targetPlayerName = args[0];
                    isOther = true;
                }
            }

            location = LocationUtils.getLocation(plugin, "spawn", "general", null);
            if (!LocationUtils.keyPathExist(plugin, sender, "spawn", "general", null, true)) return;
        }


        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        if (!PlayerUtils.isOnline(plugin, sender, targetPlayer, true)) return;

        if (LocationUtils.isNull(plugin, sender, location, "spawn", spawn, true)) return;
        assert location != null;

        if (!conditionsManager.isCondition(targetPlayer)) {
            return;
        }

        boolean isBypassDelayPermission = PlayerUtils.hasPermission(plugin, sender, perm.getSpawnBypassDelay(), perm.isSpawnBypassDelayDefault(), false);
        if (!isOther && spawnC.isTeleportDelayEnabled() && !isBypassDelayPermission) {
            int delay = TimeUtils.timerConverter("ticks", spawnC.getTeleportDelay());
            DelayManager delayManager = new DelayManager(plugin, delay, player, location);
            DelayHandler.spawn(plugin, targetPlayer, delayManager);
            actionsManager.general("before_delay", targetPlayer);
            if (defaultMessages) m.sendMessage(targetPlayer, msg.getSpawnDelayInTeleport(), true);
            return;
        }

        targetPlayer.teleport(location);

        boolean isEnabledCooldown = spawnC.isCooldownEnabled();
        if (!isOther) {
            if (isEnabledCooldown) {
                CooldownHandlers.Spawn(plugin, targetPlayer);
            }

            actionsManager.general("none", targetPlayer);
            if (defaultMessages) m.sendMessage(targetPlayer, msg.getSpawnTeleporting(), true);
        } else {
            m.sendMessage(sender, msg.getSpawnOtherTeleported()
                    .replace("%player%", targetPlayerName), true);

            String replacedMessage = plugin.getMainConfigManager().getReplacedMessagesConsole() != null ? plugin.getMainConfigManager().getReplacedMessagesConsole() : "Console";
            if (defaultMessages) m.sendMessage(targetPlayer, msg.getSpawnOtherTeleport()
                    .replace("%sender%", replacedMessage), true);
        }
    }
}

