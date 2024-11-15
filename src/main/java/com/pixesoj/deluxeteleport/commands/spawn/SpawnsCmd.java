package com.pixesoj.deluxeteleport.commands.spawn;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.CheckEnabledManager;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.ConfigSpawnManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.ConfigWarpsManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.PermissionsManager;
import com.pixesoj.deluxeteleport.utils.FileUtils;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpawnsCmd implements CommandExecutor {

    private final DeluxeTeleport plugin;
    private final MessagesFileManager msg;
    private final PermissionsManager perm;
    private final ConfigSpawnManager configSpawn;
    private final MessagesManager m;

    public SpawnsCmd(DeluxeTeleport plugin){
        this.plugin = plugin;
        this.msg = plugin.getMainMessagesManager();
        this.perm = plugin.getMainPermissionsManager();
        this.configSpawn = plugin.getMainSpawnConfigManager();
        m = new MessagesManager(msg.getPrefixSpawn(), plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        mainCommand(sender);
        return true;
    }

    public void mainCommand(CommandSender sender){
        if (!CheckEnabledManager.warps(plugin, sender, true)) return;
        if (!PlayerUtils.hasPermission(plugin, sender, perm.getWarps(), perm.isWarpsDefault(), true)) return;

        List<String> listSpawns = FileUtils.getDataNameFiles(plugin, "spawns");

        if (listSpawns.isEmpty()){
            m.sendMessage(sender, msg.getSpawnNoSpawns(), true);
            return;
        }

        String spawnsList = String.join(", ", listSpawns);
        m.sendMessage(sender, msg.getSpawnSpawnsList().replace("%spawns%", spawnsList), true);
    }
}
