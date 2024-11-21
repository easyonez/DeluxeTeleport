package com.pixesoj.deluxeteleport.commands.lobby;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.FileManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.PermissionsManager;
import com.pixesoj.deluxeteleport.managers.CheckEnabledManager;
import com.pixesoj.deluxeteleport.utils.LocationUtils;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

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

        MessagesFileManager msg = plugin.getMainMessagesManager();
        MessagesManager m = new MessagesManager(msg.getPrefixLobby(), plugin);
        Player player = (Player) sender;

        String worldName = Objects.requireNonNull(player.getLocation().getWorld()).getName();
        boolean isMultiple = CheckEnabledManager.lobbyMultiple(plugin);
        if (args.length < 1){
            m.sendMessage(player, msg.getGlobalInvalidArguments().replace("%usage%", "/setlobby <name>"), true);
            return;
        }
        String fileName = isMultiple ? args[0] : "general-lobby";
        FileManager fileManager = new FileManager(fileName + ".yml", "data/lobbies", plugin);
        FileConfiguration lobby = fileManager.getConfig();
        String displayName = args[0];
        lobby.set("displayname", displayName);

        lobby.set("world", player.getWorld().getUID().toString());
        lobby.set("world_name", player.getWorld().getName());
        lobby.set("x", player.getLocation().getX());
        lobby.set("y", player.getLocation().getY());
        lobby.set("z", player.getLocation().getZ());
        lobby.set("yaw", player.getLocation().getYaw());
        lobby.set("pitch", player.getLocation().getPitch());
        lobby.set("displayname", displayName);
        lobby.set("lastowner", player.getUniqueId().toString());
        lobby.set("is_main", !isMultiple);
        if (lobby.getConfigurationSection("teleport_actions.actions") == null){
            lobby.set("teleport_actions.actions", new ArrayList<>());
        }
        if (lobby.getConfigurationSection("teleport_conditions.conditions") == null){
            lobby.set("teleport_conditions.conditions", new ArrayList<>());
        }
        fileManager.saveConfig();

        m.sendMessage(player, msg.getLobbyEstablished().replace("%lobby%",  displayName).replace("%world%", worldName), true);
    }
}
