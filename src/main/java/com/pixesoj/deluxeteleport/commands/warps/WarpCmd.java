package com.pixesoj.deluxeteleport.commands.warps;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.handlers.CooldownHandlers;
import com.pixesoj.deluxeteleport.managers.*;
import com.pixesoj.deluxeteleport.managers.filesmanager.ConfigWarpsManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.FileManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.PermissionsManager;
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

public class WarpCmd implements CommandExecutor {

    private final DeluxeTeleport plugin;
    private final MessagesFileManager msg;
    private final PermissionsManager perm;
    private final ConfigWarpsManager configWarp;
    private final MessagesManager m;

    public WarpCmd(DeluxeTeleport plugin){
        this.plugin = plugin;
        this.msg = plugin.getMainMessagesManager();
        this.perm = plugin.getMainPermissionsManager();
        this.configWarp = plugin.getMainWarpConfigManager();
        m = new MessagesManager(msg.getPrefixWarp(), plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        mainCommand(sender, args);
        return true;
    }

    public void mainCommand(CommandSender sender, String[] args){
        if (!CheckEnabledManager.warps(plugin, sender, true)) return;
        if (!PlayerUtils.hasPermission(plugin, sender, perm.getWarps(), perm.isWarpsDefault(), true)) return;
        if (PlayerUtils.isPlayer(plugin, sender, false)){
            playerCommand(sender, args);
        } else {
            consoleCommand(sender, args);
        }
    }

    private void playerCommand(CommandSender sender, String[] args){
        Player player = (Player) sender;
        if (args.length == 0){
            m.sendMessage(player, msg.getWarpNotSpecify(), true);
            return;
        }

        String warpName = args[0];
        FileManager warp = new FileManager(warpName + ".yml", "data/warps", false, plugin);
        FileConfiguration warpData = warp.getConfig();
        File warpFile = warp.getFile();

        if (!warpFile.exists() || !warpFile.getName().endsWith(".yml")){
            m.sendMessage(player, msg.getWarpNotExist().replace("%warp%", warpName), true);
            return;
        }

        boolean isByppasCooldownPermission = PlayerUtils.hasPermission(plugin, sender, perm.getWarpsBypassCooldown(), perm.isWarpsBypassCooldownDefault(), false);
        if (configWarp.isCooldownEnabled() && plugin.playerWarpInCooldown(player) && !isByppasCooldownPermission) {
            m.sendMessage(sender, msg.getWarpInCooldown(), true);
            return;
        }

        ConditionsManager conditions = new ConditionsManager(plugin, configWarp.getConfig(), "teleport_conditions");
        ConditionsManager warpConditions = new ConditionsManager(plugin, warpData, "teleport_conditions");
        if (!conditions.isCondition(player) || !warpConditions.isCondition(player)) return;

        Location location;
        try {
            UUID worldUUID = UUID.fromString(warpData.getString("world", ""));
            World world = Bukkit.getWorld(worldUUID);
            if (world == null){
                m.sendMessage(player, msg.getWarpExeption().replace("%warp%", warpName), true);
                return;
            }
            double x = warpData.getDouble("x");
            double y = warpData.getDouble("y");
            double z = warpData.getDouble("z");
            float yaw = (float) warpData.getDouble("yaw");
            float pitch = (float) warpData.getDouble("pitch");
            location = new Location(world, x, y, z, yaw, pitch);
        } catch (NullPointerException | IllegalArgumentException e){
            m.sendMessage(player, msg.getWarpExeption().replace("%warp%", warpName), true);
            return;
        }

        boolean defaultMessages = plugin.getMainSpawnConfigManager().getConfig().getBoolean("actions.default_messages", true);
        ActionsManager actions = new ActionsManager(plugin, plugin.getMainWarpConfigManager().getConfig(), "teleport_actions");
        ActionsManager warpActions = new ActionsManager(plugin, warpData, "teleport_actions");
        boolean isDelayByppasPermission = PlayerUtils.hasPermission(plugin, player, perm.getWarpsBypassDelay(), perm.isWarpsBypassDelayDefault(), false);
        if (!isDelayByppasPermission && configWarp.isTeleportDelayEnabled()){
            int delay = TimeUtils.timerConverter("ticks", configWarp.getTeleportDelay());
            DelayManager delayManager = new DelayManager(plugin, delay, player, location);
            delayManager.warp(warpName);

            actions.general("before_delay", player);
            warpActions.general("before_delay", player);
            if (defaultMessages) m.sendMessage(player, msg.getWarpDelayInTeleport(), true);
            return;
        }

        if (configWarp.isCooldownEnabled()){
            CooldownHandlers.warp(plugin, player);
        }

        Player targetPlayer = player;
        boolean isOther = false;
        if (args.length >= 2 && PlayerUtils.hasPermission(plugin, sender, perm.getWarpsOther(), perm.isWarpsOtherDefault(), false)){
            targetPlayer = Bukkit.getPlayer(args[1]);
            if (!PlayerUtils.isOnline(plugin, sender, targetPlayer, false)) {
                m.sendMessage(sender, plugin.getMainMessagesManager().getGlobalPlayerOffline()
                        .replace("%player%", args[1]), true);
                return;
            }
            isOther = true;
        }

        targetPlayer.teleport(location);

        if (isOther){
            m.sendMessage(sender, msg.getWarpOtherTeleported().replace("%player%", player.getName()).replace("%warp%", warpName), true);
            m.sendMessage(targetPlayer, msg.getWarpOtherTeleport().replace("%sender%", sender.getName()).replace("%warp%", warpName), true);
            return;
        }

        if (defaultMessages) m.sendMessage(player, msg.getWarpTeleporting().replace("%warp%", warpName), true);

        actions.general("none", player);
        warpActions.general("none", player);
    }

    private void consoleCommand(CommandSender sender, String[] args){
        if (args.length < 2){
            m.sendMessage(sender, msg.getGlobalInvalidArguments().replace("%usage%", "warp <warp> <player>"), true);
            return;
        }

        String warpName = args[0];
        String playerName = args[1];

        Player player = Bukkit.getPlayer(playerName);
        if (!PlayerUtils.isOnline(plugin, sender, player, false)) {
            m.sendMessage(sender, plugin.getMainMessagesManager().getGlobalPlayerOffline()
                    .replace("%player%", playerName), true);
            return;
        }

        FileManager warp = new FileManager(warpName + ".yml", "data/warps", false, plugin);
        FileConfiguration warpData = warp.getConfig();
        File warpFile = warp.getFile();

        if (!warpFile.exists() || !warpFile.getName().endsWith(".yml")){
            m.sendMessage(sender, msg.getWarpNotExist().replace("%warp%", warpName), true);
            return;
        }

        Location location;
        try {
            UUID worldUUID = UUID.fromString(warpData.getString("world", "null"));
            World world = Bukkit.getWorld(worldUUID);
            if (world == null){
                m.sendMessage(sender, msg.getWarpExeption().replace("%warp%", warpName), true);
                return;
            }
            double x = warpData.getDouble("x");
            double y = warpData.getDouble("y");
            double z = warpData.getDouble("z");
            float yaw = (float) warpData.getDouble("yaw");
            float pitch = (float) warpData.getDouble("pitch");
            location = new Location(world, x, y, z, yaw, pitch);
        } catch (NullPointerException | IllegalArgumentException e){
            m.sendMessage(sender, msg.getWarpExeption().replace("%warp%", warpName), true);
            return;
        }

        player.teleport(location);
        m.sendMessage(sender, msg.getWarpOtherTeleported().replace("%player%", player.getName()).replace("%warp%", warpName), true);
        String replacedConsole = plugin.getMainConfigManager().getReplacedMessagesConsole() != null ? plugin.getMainConfigManager().getReplacedMessagesConsole() : "Console";
        m.sendMessage(player, msg.getWarpOtherTeleport().replace("%sender%", replacedConsole).replace("%warp%", warpName), true);
    }
}
