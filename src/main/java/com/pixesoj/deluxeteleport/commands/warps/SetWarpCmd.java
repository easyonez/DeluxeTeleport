package com.pixesoj.deluxeteleport.commands.warps;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.CheckEnabledManager;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.FileManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.PermissionsManager;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SetWarpCmd implements CommandExecutor {
    private final DeluxeTeleport plugin;

    public SetWarpCmd(DeluxeTeleport deluxeteleport) {
        this.plugin = deluxeteleport;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        mainCommand(sender, args);
        return true;
    }

    public void mainCommand(@NotNull CommandSender sender, String[] args) {
        PermissionsManager perm = plugin.getMainPermissionsManager();
        if (!PlayerUtils.isPlayer(plugin, sender, true)) return;
        if (!PlayerUtils.hasPermission(plugin, sender, perm.getSetWarps(), perm.isSetWarpsDefault(), true)) return;
        if (!CheckEnabledManager.warps(plugin, sender, true)) return;

        MessagesFileManager msg = plugin.getMainMessagesManager();
        MessagesManager m = new MessagesManager(msg.getPrefixWarp(), plugin);
        Player player = (Player) sender;

        if (args.length == 0 ){
            m.sendMessage(player, msg.getWarpSetError(), true);
            return;
        }

        String warpName = args[0];
        FileManager fileManager = new FileManager(warpName + ".yml", "data/warps", plugin);
        FileConfiguration warp = fileManager.getConfig();

        warp.set("world", player.getWorld().getUID().toString());
        warp.set("world_name", player.getWorld().getName());
        warp.set("x", player.getLocation().getX());
        warp.set("y", player.getLocation().getY());
        warp.set("z", player.getLocation().getZ());
        warp.set("yaw", player.getLocation().getYaw());
        warp.set("pitch", player.getLocation().getPitch());
        warp.set("name", warpName);
        warp.set("lastowner", player.getUniqueId().toString());
        if (warp.getConfigurationSection("teleport_actions.actions") == null){
            warp.set("teleport_actions.actions", new ArrayList<>());
        }
        if (warp.getConfigurationSection("teleport_conditions.conditions") == null){
            warp.set("teleport_conditions.conditions", new ArrayList<>());
        }
        fileManager.saveConfig();

        m.sendMessage(player, msg.getWarpSetSuccessfully().replace("%warp%", warpName), true);
    }
}