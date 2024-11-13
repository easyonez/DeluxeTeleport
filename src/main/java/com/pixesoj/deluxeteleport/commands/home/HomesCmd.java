package com.pixesoj.deluxeteleport.commands.home;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.PermissionsManager;
import com.pixesoj.deluxeteleport.managers.CheckEnabledManager;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Set;

public class HomesCmd implements CommandExecutor {

    private final DeluxeTeleport plugin;

    public HomesCmd(DeluxeTeleport plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        mainCommand(sender, args);
        return true;
    }

    public void mainCommand(@NotNull CommandSender sender, String[] args) {
        MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefixHome(), plugin);
        MessagesFileManager msg = plugin.getMainMessagesManager();
        PermissionsManager perm = plugin.getMainPermissionsManager();

        if (!CheckEnabledManager.home(plugin, sender, true)) return;
        if (!PlayerUtils.isPlayer(plugin, sender, true)) return;
        if (!PlayerUtils.hasPermission(plugin, sender, perm.getHomes(), perm.isHomesDefault(), true)) return;

        Player player = (Player) sender;

        String targetPlayerName;
        boolean isOther;

        if (args.length >= 1 && PlayerUtils.hasPermission(plugin, sender, perm.getHomesOther(), perm.isHomesOtherDefault(), false)) {
            targetPlayerName = args[0];
            isOther = true;
        } else {
            targetPlayerName = player.getName();
            isOther = false;
        }

        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        if (targetPlayer == null || !targetPlayer.isOnline()) {
            m.sendMessage(sender, msg.getGlobalPlayerOffline().replace("%player%", targetPlayerName), true);
            return;
        }

        File playerFile = plugin.getPlayerDataManager().getPlayerFile(targetPlayer.getUniqueId());
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

        if (!playerData.contains("homes")) {
            if (isOther) {
                m.sendMessage(player, msg.getHomeHasNoHomes().replace("%player%", targetPlayer.getName()), true);
            } else {
                m.sendMessage(player, msg.getHomeNoHomes(), true);
            }
            return;
        }

        ConfigurationSection homesSection = playerData.getConfigurationSection("homes");
        if (homesSection == null || homesSection.getKeys(false).isEmpty()) {
            if (isOther) {
                m.sendMessage(player, msg.getHomeHasNoHomes().replace("%player%", targetPlayer.getName()), true);
            } else {
                m.sendMessage(player, msg.getHomeNoHomes(), true);
            }
        } else {
            Set<String> homes = homesSection.getKeys(false);
            if (homes.isEmpty()) {
                if (isOther) {
                    m.sendMessage(player, msg.getHomeHasNoHomes().replace("%player%", targetPlayer.getName()), true);
                } else {
                    m.sendMessage(player, msg.getHomeNoHomes(), true);
                }
            } else {
                StringBuilder homeList = new StringBuilder();
                if (isOther) {
                    homeList.append(msg.getHomeHomesOf().replace("%player%", targetPlayer.getName()));
                } else {
                    homeList.append(msg.getHomeYourHomes());
                }
                for (String home : homes) {
                    homeList.append("&7").append(home).append("&8, ");
                }
                if (homeList.length() > 0) {
                    homeList.setLength(homeList.length() - 2);
                }
                m.sendMessage(player, homeList.toString(), true);
            }
        }

        return;
    }
}
