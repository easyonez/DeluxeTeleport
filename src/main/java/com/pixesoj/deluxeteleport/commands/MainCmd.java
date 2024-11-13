package com.pixesoj.deluxeteleport.commands;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.subcommands.*;
import com.pixesoj.deluxeteleport.utils.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MainCmd implements CommandExecutor{

    private final DeluxeTeleport plugin;
    private final Map<String, SubCommand> subCommands;

    public MainCmd(DeluxeTeleport deluxeteleport) {
        this.plugin = deluxeteleport;

        this.subCommands = new HashMap<>();
        subCommands.put("help", new HelpSubCmd(deluxeteleport));
        subCommands.put("reload", new ReloadSubCmd(deluxeteleport));
        subCommands.put("update", new UpdateSubCmd(deluxeteleport));
        subCommands.put("reset", new ResetSubCmd(deluxeteleport));
        subCommands.put("info", new InfoSubCmd(deluxeteleport));
        subCommands.put("import", new ImportSubCmd(deluxeteleport));
        subCommands.put("changelog", new ChangelogSubCmd(deluxeteleport));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(MessagesManager.getColoredMessage("&bDeluxeTeleport &7by &fPixesoj"));
            sender.sendMessage(MessagesManager.getColoredMessage(plugin.getMainMessagesManager().getGlobalUsage()));
            return true;
        }

        String subCommandName = args[0].toLowerCase();
        SubCommand subCommand = subCommands.get(subCommandName);

        if (subCommand != null) {
            return subCommand.execute(sender, Arrays.copyOfRange(args, 1, args.length));
        } else {
            sender.sendMessage(MessagesManager.getColoredMessage("&bDeluxeTeleport &7by &fPixesoj"));
            sender.sendMessage(MessagesManager.getColoredMessage(plugin.getMainMessagesManager().getGlobalUsage()));
            return true;
        }
    }
}


