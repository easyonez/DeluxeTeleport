package com.pixesoj.deluxeteleport.subcommands;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import com.pixesoj.deluxeteleport.utils.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitScheduler;

public class ReloadSubCmd implements SubCommand {

    private final DeluxeTeleport plugin;

    public ReloadSubCmd(DeluxeTeleport plugin) {
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String[] args) {
        MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefix(), plugin);

        if (!PlayerUtils.hasPermission(plugin, sender, plugin.getMainPermissionsManager().getReload(), plugin.getMainPermissionsManager().isReloadDefault(), true))
            return true;

        long startTime = System.currentTimeMillis();

        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskAsynchronously(plugin, () -> {
            plugin.getMainConfigManager().updateConfig();
            plugin.getMainLobbyConfigManager().updateLobbyConfig();
            plugin.getMainSpawnConfigManager().updateSpawnConfig();
            plugin.getMainMessagesManager().updateMessages();
            plugin.getMainPermissionsManager().updatePermissions();
            plugin.getMainTPAConfigManager().updateTeleportConfig();
            plugin.getMainHomeConfigManager().updateHomeConfig();
            plugin.getPlaceholdersManager().updatePlaceholders();
            plugin.getMainMenuConfigManager().updateHomeConfig();
            plugin.getMainWarpConfigManager().updateHomeConfig();

            plugin.getMainConfigManager().reloadConfig();
            plugin.getMainLobbyConfigManager().reloadConfig();
            plugin.getMainSpawnConfigManager().reloadConfig();
            plugin.getMainPermissionsManager().reloadPermissions();
            plugin.getLocationsManager().reloadLocationsFile();
            plugin.getMainTPAConfigManager().reloadConfig();
            plugin.getMainHomeConfigManager().reloadConfig();
            plugin.getPlaceholdersManager().reloadConfig();
            plugin.getMainMenuConfigManager().reloadConfig();
            plugin.getMainMenuManager().reloadMenus();
            plugin.getMainWarpConfigManager().reloadConfig();

            plugin.setMainMessagesManager(new MessagesFileManager(plugin));
            plugin.getMainMessagesManager().reloadMessages();

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;

            scheduler.runTask(plugin, () -> m.sendMessage(sender, plugin.getMainMessagesManager().getGlobalReload()
                    .replace("%version%", plugin.getDescription().getVersion())
                    .replace("%time%", elapsedTime + " ms"), true));
        });

        return true;
    }
}
