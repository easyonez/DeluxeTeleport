package com.pixesoj.deluxeteleport.commands.spawn;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.filesmanager.ConfigSpawnManager;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.managers.CheckEnabledManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.FileManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.PermissionsManager;
import com.pixesoj.deluxeteleport.utils.LocationUtils;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

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
        ConfigSpawnManager spawnConfig = plugin.getMainSpawnConfigManager();
        PermissionsManager perm = plugin.getMainPermissionsManager();
        MessagesFileManager msg = plugin.getMainMessagesManager();
        MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefixSpawn(), plugin);

        if (!CheckEnabledManager.spawn(plugin, sender, true)) return;
        if (!PlayerUtils.hasPermission(plugin, sender, perm.getDelSpawn(), perm.isDelSpawnDefault(), true)) return;

        boolean isByWorld = spawnConfig.isByWorld();
        if (isByWorld && args.length < 1){
            m.sendMessage(sender, msg.getSpawnDeletedError(), true);
            return;
        }

        String spawnName = isByWorld ? args[0] : "general-spawn";
        FileManager fileManager = new FileManager(spawnName + ".yml", "data/spawns", false, plugin);
        File spawn = fileManager.getFile();

        if (spawn.exists()) {
            if (spawn.delete()) {
                m.sendMessage(sender, msg.getSpawnDeletedSuccessfully().replace("%spawn%", spawnName), true);
            }
        } else {
            m.sendMessage(sender, msg.getSpawnNotExists().replace("%spawn%", spawnName), true);
        }
    }
}