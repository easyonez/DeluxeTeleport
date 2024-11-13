package com.pixesoj.deluxeteleport.commands.world;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpWorldCMD implements CommandExecutor {
    private final DeluxeTeleport plugin;

    public TpWorldCMD(DeluxeTeleport plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by players.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage("Please specify the name of the world you want to teleport to.");
            return true;
        }

        String worldName = args[0];
        World targetWorld = Bukkit.getWorld(worldName);

        if (targetWorld == null) {
            player.sendMessage("The specified world does not exist.");
            return true;
        }

        player.teleport(targetWorld.getSpawnLocation());
        player.sendMessage("You have been teleported to the world: " + worldName);

        return true;
    }
}
