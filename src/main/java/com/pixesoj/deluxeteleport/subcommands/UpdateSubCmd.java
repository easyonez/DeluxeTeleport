package com.pixesoj.deluxeteleport.subcommands;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.managers.UpdateCheckManager;
import com.pixesoj.deluxeteleport.managers.UpdateManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import com.pixesoj.deluxeteleport.model.internal.UpdateCheckResult;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import com.pixesoj.deluxeteleport.utils.SubCommand;
import org.bukkit.command.CommandSender;

public class UpdateSubCmd implements SubCommand {

    private final DeluxeTeleport plugin;

    public UpdateSubCmd(DeluxeTeleport plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefix(), plugin);
        MessagesFileManager msg = plugin.getMainMessagesManager();
        if (!PlayerUtils.hasPermission(plugin, sender, plugin.getMainPermissionsManager().getUpdate(), plugin.getMainPermissionsManager().isUpdateDefault(), true)) return true;
        m.sendMessage(sender, plugin.getMainMessagesManager().getUpdateLooking(), true);
        UpdateCheckManager updateCheckerManager = new UpdateCheckManager(plugin.version);
        UpdateCheckResult result = updateCheckerManager.check();
        plugin.setUpdateCheckerManager(updateCheckerManager);
        if (result.isError()) {
            m.sendMessage(sender, msg.getUpdateErrorCheck(), true);
        } else if (result.isUpdateAvailable()) {
            m.sendMessage(sender, msg.getUpdateAvailable().replace("%latest_version%", "v" + result.getLatestVersion()), true);

            UpdateManager update = new UpdateManager(plugin, sender);
            update.downloadUpdate(false);
        } else {
            m.sendMessage(sender, msg.getUpdateItsUpdated().replace("%latest_version%", "v" + result.getLatestVersion()), true);
        }

        return true;
    }
}