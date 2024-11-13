package com.pixesoj.deluxeteleport.commands.lobby;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.filesmanager.PermissionsManager;
import com.pixesoj.deluxeteleport.managers.CheckEnabledManager;
import com.pixesoj.deluxeteleport.utils.LocationUtils;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SetLobbyCmd implements CommandExecutor {
    private final DeluxeTeleport plugin;

    public SetLobbyCmd(DeluxeTeleport deluxeteleport) {
        this.plugin = deluxeteleport;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        mainCommand(sender, args);
        return true;
    }

    public void mainCommand(@NotNull CommandSender sender, String[] args) {
        PermissionsManager perm = plugin.getMainPermissionsManager();
        if (!PlayerUtils.isPlayer(plugin, sender, true)) return;
        if (!PlayerUtils.hasPermission(plugin, sender, perm.getSetLobby(), perm.isSetLobbyDefault(), true)) return;
        if (!CheckEnabledManager.lobby(plugin, sender, true)) return;
        if (CheckEnabledManager.lobbyMultiple(plugin)) {
            LocationUtils.setLocation(plugin, sender, "lobby", args, args.length == 0);
            return;
        }
        LocationUtils.setLocation(plugin, sender, "lobby", args, true);
        return;
    }
}
