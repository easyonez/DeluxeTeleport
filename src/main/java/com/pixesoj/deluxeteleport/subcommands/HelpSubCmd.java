package com.pixesoj.deluxeteleport.subcommands;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import com.pixesoj.deluxeteleport.utils.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpSubCmd implements SubCommand {

    private final DeluxeTeleport plugin;

    public HelpSubCmd(DeluxeTeleport plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!PlayerUtils.hasPermission(plugin, sender, plugin.getMainPermissionsManager().getHelp(), plugin.getMainPermissionsManager().isHelpDefault(), true)) return true;
        MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefix(), plugin);
        List<String> message = plugin.getMainMessagesManager().getGlobalHelp();
        for (String msg : message) {
            m.sendMessage(sender, msg, false);
        }
        return true;
    }
}
