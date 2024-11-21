package com.pixesoj.deluxeteleport.commands.lobby;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.filesmanager.*;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.managers.CheckEnabledManager;
import com.pixesoj.deluxeteleport.utils.LocationUtils;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class DelLobbyCmd implements CommandExecutor {
    private final DeluxeTeleport plugin;

    public DelLobbyCmd(DeluxeTeleport deluxeteleport) {
        this.plugin = deluxeteleport;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        mainCommand(sender, args);
        return true;
    }

    public void mainCommand(@NotNull CommandSender sender, String[] args) {
        ConfigLobbyManager lobbyConfig = plugin.getMainLobbyConfigManager();
        PermissionsManager perm = plugin.getMainPermissionsManager();
        MessagesFileManager msg = plugin.getMainMessagesManager();
        MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefixLobby(), plugin);

        if (!CheckEnabledManager.lobby(plugin, sender, true)) return;
        if (!PlayerUtils.hasPermission(plugin, sender, perm.getDelLobby(), perm.isDelLobbyDefault(), true)) return;

        boolean isMultiple = lobbyConfig.isMultipleLobbies();
        if (isMultiple && args.length < 1){
            m.sendMessage(sender, msg.getLobbyDeletedError(), true);
            return;
        }

        String lobbyName = isMultiple ? args[0] : "general-lobby";
        FileManager fileManager = new FileManager(lobbyName + ".yml", "data/lobbies", false, plugin);
        File lobby = fileManager.getFile();

        if (lobby.exists()) {
            if (lobby.delete()) {
                m.sendMessage(sender, msg.getLobbyDeletedSuccessfully().replace("%lobby%", lobbyName), true);
            }
        } else {
            m.sendMessage(sender, msg.getLobbyNotExists().replace("%lobby%", lobbyName), true);
        }
    }
}
