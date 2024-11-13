package com.pixesoj.deluxeteleport.subcommands;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import com.pixesoj.deluxeteleport.managers.CooldownManager;
import com.pixesoj.deluxeteleport.managers.database.CooldownDatabase;
import com.pixesoj.deluxeteleport.utils.SubCommand;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetSubCmd implements SubCommand {

    private final DeluxeTeleport plugin;

    public ResetSubCmd(DeluxeTeleport plugin) {
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String[] args) {
        MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefix(), plugin);
        MessagesFileManager msg = plugin.getMainMessagesManager();

        if (!PlayerUtils.hasPermission(plugin, sender, plugin.getMainPermissionsManager().getReset(), plugin.getMainPermissionsManager().isResetDefault(), true)) {
            return true;
        }

        if (args.length < 1) {
            m.sendMessage(sender, msg.getResetErrorUnspecifiedValue()
                    .replace("%available_values%", "Cooldown"), true);
            return true;
        }

        if (!args[0].equalsIgnoreCase("cooldown")) {
            m.sendMessage(sender, msg.getResetErrorInvalidValue()
                            .replace("%value%", args[0])
                            .replace("%available_values%", "Cooldown"), true);
            return true;
        }

        if (args.length < 2) {
            m.sendMessage(sender, msg.getResetUnspecifiedPlayer()
                    .replace("%value%", args[0]), true);
            return true;
        }

        String playerName = args[1];
        Player player = Bukkit.getPlayer(playerName);
        if (player == null || !player.isOnline()) {
            m.sendMessage(sender, msg.getGlobalPlayerOffline()
                    .replace("%player%", playerName), true);
            return true;
        }

        playerName = player.getName();
        m.sendMessage(sender, msg.getResetSuccessfully()
                .replace("%player%", playerName)
                .replace("%value%", args[0]), true);

        CooldownDatabase cooldownDatabase = new CooldownDatabase(plugin);
        plugin.removeTpaCooldown(playerName);
        plugin.removeHomeCooldown(playerName);
        plugin.removeSpawnCooldown(playerName);
        plugin.removeLobbyCooldown(playerName);
        cooldownDatabase.deleteCooldown(playerName, "tpa");
        cooldownDatabase.deleteCooldown(playerName, "spawn");
        cooldownDatabase.deleteCooldown(playerName, "lobby");
        cooldownDatabase.deleteCooldown(playerName, "home");
        CooldownManager.stopCooldown("tpa", playerName);
        CooldownManager.stopCooldown("spawn", playerName);
        CooldownManager.stopCooldown("lobby", playerName);
        CooldownManager.stopCooldown("home", playerName);
        return true;
    }
}