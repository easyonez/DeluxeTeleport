package com.pixesoj.deluxeteleport.subcommands;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.ImportManager;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import com.pixesoj.deluxeteleport.utils.SubCommand;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import org.bukkit.command.CommandSender;

public class ImportSubCmd implements SubCommand {

    private final DeluxeTeleport plugin;

    public ImportSubCmd(DeluxeTeleport plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        MessagesFileManager msg = plugin.getMainMessagesManager();
        MessagesManager m = new MessagesManager(msg.getPrefix(), plugin);
        if (!PlayerUtils.hasPermission(plugin, sender, plugin.getMainPermissionsManager().getImport(),
                plugin.getMainPermissionsManager().isImportDefault(), true)) return true;

        if (args.length < 1) {
            m.sendMessage(sender, msg.getMigrateErrorPluginNotSpecified().replace("%compatible_plugins%", "Essentials"), true);
            return false;
        }

        if (!args[0].equalsIgnoreCase("Essentials")) {
            m.sendMessage(sender, msg.getMigrateErrorInvalidPlugin().replace("%compatible_plugins%", "Essentials")
                    .replace("%plugin%", args[0]), true);
            return true;
        }

        if (args.length < 2) {
            m.sendMessage(sender, msg.getMigrateErrorDataNotSpecified().replace("%compatible_data%", "Homes"), true);
            return true;
        }

        if (args[1].equalsIgnoreCase("homes")) {
            ImportManager.importEssentialsHomes(plugin, sender);
            return true;
        } else {
            m.sendMessage(sender, msg.getMigrateErrorInvalidData().replace("%compatible_data%", "Homes")
                    .replace("%data%", args[1]), true);
        }

        return true;
    }
}