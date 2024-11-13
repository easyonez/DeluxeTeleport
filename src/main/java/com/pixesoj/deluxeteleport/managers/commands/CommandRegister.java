package com.pixesoj.deluxeteleport.managers.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class CommandRegister extends BukkitCommand {

    private final CommandExecutor commandExecutor;
    private final TabCompleter globalTabCompleter;

    public CommandRegister(@NotNull String name, CommandExecutor commandExecutor, TabCompleter globalTabCompleter) {
        super(name);
        this.commandExecutor = commandExecutor;
        this.globalTabCompleter = globalTabCompleter;
    }

    public CommandRegister(@NotNull String name) {
        super(name);
        this.commandExecutor = null;
        this.globalTabCompleter = null;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (commandExecutor != null) {
            return commandExecutor.onCommand(sender, this, commandLabel, args);
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (globalTabCompleter != null) {
            return Objects.requireNonNull(globalTabCompleter.onTabComplete(sender, this, alias, args));
        }
        return super.tabComplete(sender, alias, args);
    }
}