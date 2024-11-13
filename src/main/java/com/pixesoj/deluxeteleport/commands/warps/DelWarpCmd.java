package com.pixesoj.deluxeteleport.commands.warps;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.CheckEnabledManager;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.*;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class DelWarpCmd implements CommandExecutor {
    private final DeluxeTeleport plugin;

    public DelWarpCmd(DeluxeTeleport deluxeteleport) {
        this.plugin = deluxeteleport;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        mainCommand(sender, args);
        return true;
    }

    public void mainCommand(@NotNull CommandSender sender, String[] args) {
        PermissionsManager perm = plugin.getMainPermissionsManager();
        MessagesFileManager msg = plugin.getMainMessagesManager();
        MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefixWarp(), plugin);

        if (!CheckEnabledManager.warps(plugin, sender, true)) return;
        if (!PlayerUtils.hasPermission(plugin, sender, perm.getSetWarps(), perm.isDelWarpsDefault(), true)) return;

        if (args.length == 0) {
            m.sendMessage(sender, msg.getWarpDeletedError(), true);
            return;
        }

        String warpName = args[0];
        FileManager fileManager = new FileManager(warpName + ".yml", "data/warps", plugin);
        File warp = fileManager.getFile();

        if (warp.exists()) {
            if (warp.delete()) {
                m.sendMessage(sender, msg.getWarpDeletedSuccessfully().replace("%warp%", warpName), true);
            }
        } else {
            m.sendMessage(sender, msg.getWarpDeletedError().replace("%warp%", warpName), true);
        }
    }
}