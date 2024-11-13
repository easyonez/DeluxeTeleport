package com.pixesoj.deluxeteleport.commands.lobby;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.filesmanager.ConfigLobbyManager;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.managers.CheckEnabledManager;
import com.pixesoj.deluxeteleport.utils.LocationUtils;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

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
        ConfigLobbyManager lobbyC = plugin.getMainLobbyConfigManager();
        if (!CheckEnabledManager.lobby(plugin, sender, true)) return;
        if (!PlayerUtils.hasPermission(plugin, sender, plugin.getMainPermissionsManager().getDelLobby(),
                plugin.getMainPermissionsManager().isDelLobbyDefault(), true)) return;
        if (args.length == 0 || !lobbyC.isMultipleLobbies()) {
            deleteGeneralLobby(sender);
        } else {
            String lobby = args[0];
            deleteSpecificLobby(sender, lobby);
        }
    }

    private void deleteGeneralLobby(CommandSender sender) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        if (!LocationUtils.keyPathExist(plugin, sender, "lobby", "general", null, true)) return;
        locations.set("Lobby.General", null);
        plugin.getLocationsManager().saveLocationsFile();
        MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefixLobby(), plugin);
        m.sendMessage(sender, plugin.getMainMessagesManager().getLobbyDeletedSuccessfully()
                .replace("%lobby%", "general"), true);
    }

    private void deleteSpecificLobby(CommandSender sender, String lobbyName) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        if (!LocationUtils.keyPathExist(plugin, sender, "lobby", "multiple", lobbyName, true)) return;
        locations.set("Lobby.Multiple." + lobbyName, null);
        plugin.getLocationsManager().saveLocationsFile();
        MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefixLobby(), plugin);
        m.sendMessage(sender, plugin.getMainMessagesManager().getLobbyDeletedSuccessfully()
                .replace("%lobby%", lobbyName), true);
    }
}
