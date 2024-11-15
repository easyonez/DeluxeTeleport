package com.pixesoj.deluxeteleport.commands.spawn;

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

public class SetSpawnCmd implements CommandExecutor {
    private final DeluxeTeleport plugin;

    public SetSpawnCmd(DeluxeTeleport deluxeteleport) {
        this.plugin = deluxeteleport;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        mainCommand(sender, args);
        return true;
    }

    public void mainCommand(@NotNull CommandSender sender, String[] args) {
        PermissionsManager perm = plugin.getMainPermissionsManager();
        if (!PlayerUtils.isPlayer(plugin, sender, true)) return;
        if (!PlayerUtils.hasPermission(plugin, sender, perm.getSetSpawn(), perm.isSetSpawnDefault(), true)) return;
        if (!CheckEnabledManager.spawn(plugin, sender, true)) return;

        MessagesFileManager msg = plugin.getMainMessagesManager();
        MessagesManager m = new MessagesManager(msg.getPrefixSpawn(), plugin);
        Player player = (Player) sender;

        String worldName = Objects.requireNonNull(player.getLocation().getWorld()).getName();
        boolean isByWorld = CheckEnabledManager.spawnByWorld(plugin);
        String fileName = isByWorld ? worldName : "general-spawn";
        FileManager fileManager = new FileManager(fileName + ".yml", "data/spawns", plugin);
        FileConfiguration spawn = fileManager.getConfig();
        String displayName = (args.length > 0 && args[0] != null) ? args[0] : worldName;
        spawn.set("displayname", displayName);

        spawn.set("world", player.getWorld().getUID().toString());
        spawn.set("world_name", player.getWorld().getName());
        spawn.set("x", player.getLocation().getX());
        spawn.set("y", player.getLocation().getY());
        spawn.set("z", player.getLocation().getZ());
        spawn.set("yaw", player.getLocation().getYaw());
        spawn.set("pitch", player.getLocation().getPitch());
        spawn.set("displayname", displayName);
        spawn.set("lastowner", player.getUniqueId().toString());
        spawn.set("is_main", !isByWorld);
        if (spawn.getConfigurationSection("teleport_actions.actions") == null){
            spawn.set("teleport_actions.actions", new ArrayList<>());
        }
        if (spawn.getConfigurationSection("teleport_conditions.conditions") == null){
            spawn.set("teleport_conditions.conditions", new ArrayList<>());
        }
        fileManager.saveConfig();

        m.sendMessage(player, msg.getSpawnEstablished().replace("%world%", worldName), true);
    }
}