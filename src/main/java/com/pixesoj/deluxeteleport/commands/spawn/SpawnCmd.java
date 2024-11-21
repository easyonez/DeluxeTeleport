package com.pixesoj.deluxeteleport.commands.spawn;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.*;
import com.pixesoj.deluxeteleport.managers.filesmanager.FileManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.PermissionsManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.ConfigSpawnManager;
import com.pixesoj.deluxeteleport.handlers.*;
import com.pixesoj.deluxeteleport.utils.FileUtils;
import com.pixesoj.deluxeteleport.utils.LocationUtils;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import com.pixesoj.deluxeteleport.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.UUID;

public class SpawnCmd implements CommandExecutor {
    private final DeluxeTeleport plugin;
    private final ConfigSpawnManager configSpawn;
    private final MessagesFileManager msg;
    private final PermissionsManager perm;
    private final MessagesManager m;
    private final ActionsManager actionsManager;
    private final ConditionsManager conditionsManager;
    private final boolean defaultMessages;

    private ConditionsManager spawnConditionsManager;
    private ActionsManager spawnActionsManager;
    private Location location;
    private String targetPlayerName;

    public SpawnCmd(DeluxeTeleport plugin) {
        this.plugin = plugin;
        this.configSpawn = plugin.getMainSpawnConfigManager();
        this.msg = plugin.getMainMessagesManager();
        this.perm = plugin.getMainPermissionsManager();
        this.m = new MessagesManager(plugin.getMainMessagesManager().getPrefixSpawn(), plugin);

        this.conditionsManager = new ConditionsManager(plugin, configSpawn.getConfig(), "teleport_conditions");
        this.actionsManager = new ActionsManager(plugin, configSpawn.getConfig(), "teleport_actions");
        this.defaultMessages = plugin.getMainSpawnConfigManager().getConfig().getBoolean("actions.default_messages", true);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        mainCommand(sender, args);
        return true;
    }

    public void mainCommand(@NotNull CommandSender sender, String[] args) {
        PermissionsManager perm = plugin.getMainPermissionsManager();
        if (!CheckEnabledManager.spawn(plugin, sender, true)) return;
        if (!PlayerUtils.hasPermission(plugin, sender, perm.getSpawn(), perm.isSpawnDefault(), true)) return;
        if (!PlayerUtils.isPlayer(plugin, sender, false)) {
            commandConsole(sender, args);
        } else {
            commandPlayer(sender, args);
        }
    }

    public void commandConsole(CommandSender sender, String[] args) {
        String spawnName;
        if (configSpawn.isByWorld()){
            if (args.length < 2) {
                String usage = "spawn <spawn> [player]";
                m.sendMessage(sender, msg.getGlobalInvalidArguments().replace("%usage%", usage), true);
                return;
            } else {
                spawnName = args[0];
                targetPlayerName = args[1];
            }
        } else {
            if (args.length < 1){
                String usage = "spawn [player]";
                m.sendMessage(sender, msg.getGlobalInvalidArguments().replace("%usage%", usage), true);
                return;
            } else {
                spawnName = "general-spawn";
                targetPlayerName = args[0];
            }
        }

        FileManager fileManager = new FileManager(spawnName + ".yml", "data/spawns", false, plugin);
        FileConfiguration spawn = fileManager.getConfig();

        try {
            UUID worldUUID = UUID.fromString(spawn.getString("world", ""));
            World world = Bukkit.getWorld(worldUUID);
            if (world == null) {
                m.sendMessage(sender, msg.getSpawnExeption().replace("%warp%", spawnName), true);
                return;
            }
            double x = spawn.getDouble("x");
            double y = spawn.getDouble("y");
            double z = spawn.getDouble("z");
            float yaw = spawn.getInt("yaw");
            float pitch = spawn.getInt("pitch");
            location = new Location(world, x, y, z, yaw, pitch);
        } catch (NullPointerException | IllegalArgumentException e){
            m.sendMessage(sender, msg.getSpawnExeption().replace("%warp%", spawnName), true);
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        if (!PlayerUtils.isOnline(plugin, sender, targetPlayer, false)) {
            m.sendMessage(sender, msg.getGlobalPlayerOffline().replace("%player%", targetPlayerName), true);
            return;
        }

        targetPlayer.teleport(location);
        m.sendMessage(sender, msg.getSpawnOtherTeleported()
                .replace("%player%", targetPlayerName), true);

        String replacedMessage = plugin.getMainConfigManager().getReplacedMessagesConsole() != null ? plugin.getMainConfigManager().getReplacedMessagesConsole() : "Console";
        if (defaultMessages) m.sendMessage(targetPlayer, msg.getSpawnOtherTeleport()
                .replace("%sender%", replacedMessage), true);

    }

    public void commandPlayer(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        boolean isOther = false;

        boolean isSpawnOtherPermission = PlayerUtils.hasPermission(plugin, sender, perm.getSpawnOther(), perm.isSpawnOtherDefault(), false);
        boolean isByppasCooldownPermission = PlayerUtils.hasPermission(plugin, sender, perm.getSpawnBypassCooldown(), perm.isSpawnBypassCooldownDefault(), false);

        if (configSpawn.isCooldownEnabled() && plugin.playerSpawnInCooldown(player) && !isByppasCooldownPermission) {
            m.sendMessage(sender, msg.getSpawnInCooldown(), true);
            return;
        }

        String spawnName = "general-spawn";
        targetPlayerName = player.getName();
        if (configSpawn.isByWorld()){
            if (configSpawn.isTeleportInByWorldEnabled() && args.length == 0){
                spawnName = configSpawn.getTeleportInByWorldSpawn().equalsIgnoreCase("specify") ? configSpawn.getTeleportInByWorldSpecify() : "general-spawn";
            } else if (args.length > 0) {
                spawnName = args[0];
            } else {
                String usage = isSpawnOtherPermission ? "/spawn <spawn> [player]" : "/spawn <spawn>";
                m.sendMessage(sender, msg.getGlobalInvalidArguments().replace("%usage%", usage), true);
                return;
            }

            if (isSpawnOtherPermission && args.length > 1) {
                targetPlayerName = args[1];
                isOther = true;
            }
        } else {
            if (isSpawnOtherPermission && args.length > 0) {
                targetPlayerName = args[0];
                isOther = true;
            }
        }

        FileManager fileManager = new FileManager(spawnName + ".yml", "data/spawns", false, plugin);
        FileConfiguration spawn = fileManager.getConfig();

        if (!FileUtils.exist(plugin, "spawns", spawnName)) {
            m.sendMessage(sender, msg.getSpawnNotExists().replace("%spawn%", spawnName), true);
            return;
        }

        spawnActionsManager = new ActionsManager(plugin, spawn, "teleport_actions");
        spawnConditionsManager = new ConditionsManager(plugin, spawn, "teleport_conditions");

        location = LocationUtils.getDestinationPlace(plugin, player, "spawn", spawnName);
        if (location == null) return;
        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        if (!PlayerUtils.isOnline(plugin, sender, targetPlayer, false)) {
            m.sendMessage(sender, msg.getGlobalPlayerOffline().replace("%player%", targetPlayerName), true);
            return;
        }

        if (!conditionsManager.isCondition(targetPlayer) || !spawnConditionsManager.isCondition(targetPlayer)) {
            return;
        }

        boolean isBypassDelayPermission = PlayerUtils.hasPermission(plugin, sender, perm.getSpawnBypassDelay(), perm.isSpawnBypassDelayDefault(), false);
        if (!isOther && configSpawn.isTeleportDelayEnabled() && !isBypassDelayPermission) {
            int delay = TimeUtils.timerConverter("ticks", configSpawn.getTeleportDelay());
            DelayManager delayManager = new DelayManager(plugin, delay, player, location);
            DelayHandler.spawn(plugin, targetPlayer, delayManager, spawnName);
            actionsManager.general("before_delay", targetPlayer);
            spawnActionsManager.general("before_delay", targetPlayer);
            if (defaultMessages) m.sendMessage(targetPlayer, msg.getSpawnDelayInTeleport(), true);
            return;
        }

        targetPlayer.teleport(location);

        boolean isEnabledCooldown = configSpawn.isCooldownEnabled();
        if (!isOther) {
            if (isEnabledCooldown) {
                CooldownHandlers.Spawn(plugin, targetPlayer);
            }

            actionsManager.general("none", targetPlayer);
            spawnActionsManager.general("none", targetPlayer);
            if (defaultMessages) m.sendMessage(targetPlayer, msg.getSpawnTeleporting(), true);
        } else {
            m.sendMessage(sender, msg.getSpawnOtherTeleported()
                    .replace("%player%", targetPlayerName), true);

            if (defaultMessages) m.sendMessage(targetPlayer, msg.getSpawnOtherTeleport()
                    .replace("%sender%", sender.getName()), true);
        }
    }
}

