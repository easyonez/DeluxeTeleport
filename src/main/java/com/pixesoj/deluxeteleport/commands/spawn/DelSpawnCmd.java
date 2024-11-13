package com.pixesoj.deluxeteleport.commands.spawn;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.filesmanager.ConfigSpawnManager;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.managers.CheckEnabledManager;
import com.pixesoj.deluxeteleport.utils.LocationUtils;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class DelSpawnCmd implements CommandExecutor {
    private final DeluxeTeleport plugin;

    public DelSpawnCmd(DeluxeTeleport deluxeteleport) {
        this.plugin = deluxeteleport;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        mainCommand(sender, args);
        return true;
    }

    public void mainCommand(@NotNull CommandSender sender, String[] args) {
        ConfigSpawnManager spawnC = plugin.getMainSpawnConfigManager();
        if (!CheckEnabledManager.spawn(plugin, sender, true)) return;
        if (!PlayerUtils.hasPermission(plugin, sender, plugin.getMainPermissionsManager().getDelSpawn(),
                plugin.getMainPermissionsManager().isDelSpawnDefault(), true)) return;
        if (args.length == 0 || !spawnC.isByWorld()) {
            deleteGeneralSpawn(sender);
        } else {
            String spawn = args[0];
            deleteSpecificSpawn(sender, spawn);
        }
    }

    private void deleteGeneralSpawn(CommandSender sender) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        if (!LocationUtils.keyPathExist(plugin, sender, "spawn", "general", null, true)) return;
        locations.set("Spawn.General", null);
        plugin.getLocationsManager().saveLocationsFile();
        MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefixSpawn(), plugin);
        m.sendMessage(sender, plugin.getMainMessagesManager().getSpawnDeletedSuccessfully()
                .replace("%spawn%", "general"), true);
    }

    private void deleteSpecificSpawn(CommandSender sender, String spawnName) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        if (!LocationUtils.keyPathExist(plugin, sender, "spawn", "byworld", spawnName, true)) return;
        locations.set("Spawn.ByWorld." + spawnName, null);
        plugin.getLocationsManager().saveLocationsFile();
        MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefixSpawn(), plugin);
        m.sendMessage(sender, plugin.getMainMessagesManager().getSpawnDeletedSuccessfully()
                .replace("%spawn%", spawnName), true);
    }
}