package com.pixesoj.deluxeteleport.commands.tpa;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.PermissionsManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.ConfigTPAManager;
import com.pixesoj.deluxeteleport.managers.CheckEnabledManager;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TpaToggleCMD implements CommandExecutor {
    private final DeluxeTeleport plugin;

    public TpaToggleCMD(DeluxeTeleport plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        mainCommand(sender, args);
        return true;
    }

    public void mainCommand(@NotNull CommandSender sender, String[] args) {
        MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefixTPA(), plugin);
        MessagesFileManager msg = plugin.getMainMessagesManager();
        ConfigTPAManager config = plugin.getMainTPAConfigManager();
        PermissionsManager perm = plugin.getMainPermissionsManager();
        if (!CheckEnabledManager.tpa(plugin, sender, true)) return;
        if (!PlayerUtils.isPlayer(plugin, sender, true)) return;
        if (!PlayerUtils.hasPermission(plugin, sender, perm.getTpaToggle(), perm.isTpaToggleDefault(), true)) return;

        Player targetPlayer = (Player) sender;
        boolean isOther = false;
        if (args.length >= 1 && PlayerUtils.hasPermission(plugin, sender, perm.getTpaToggleOther(), perm.isTpaToggleOtherDefault(), false)){
            targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer == null) {
                m.sendMessage(sender, msg.getGlobalPlayerOffline()
                        .replace("%player%", args[0]), true);
                return;
            }
            isOther = true;
        }

        UUID uuid = targetPlayer.getUniqueId();
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(plugin.getPlayerDataManager().getPlayerFile(uuid));
        if (!playerData.contains("settings.tpa-requests")) {
            plugin.getPlayerDataManager().saveTpaToggle(targetPlayer, !config.isStatusDefault());
            boolean tpaRequests = playerData.getBoolean("settings.tpa-requests", config.isStatusDefault());
            if (isOther){
                String status = msg.getVariablesEnabled();
                if (tpaRequests){
                    status = msg.getVariablesDisabled();
                }
                m.sendMessage(sender, msg.getTPAToggleOther()
                        .replace("%player%", targetPlayer.getName())
                        .replace("%status%", status), true);
                if (targetPlayer.isOnline()) {
                    m.sendMessage(targetPlayer, msg.getTPAToggleOtherTargetPlayer()
                            .replace("%player%", sender.getName())
                            .replace("%status%", status), true);
                    return;
                }
            } else {
                if (tpaRequests) {
                    m.sendMessage(targetPlayer, msg.getTPAToggleNo(), true);
                } else {
                    m.sendMessage(targetPlayer, msg.getTPAToggleYes(), true);
                }
            }
            return;
        }

        boolean tpaRequests = playerData.getBoolean("settings.tpa-requests");

        plugin.getPlayerDataManager().saveTpaToggle(targetPlayer, !tpaRequests);

        if (isOther){
            String status = msg.getVariablesEnabled();
            if (tpaRequests){
                status = msg.getVariablesDisabled();
            }
            m.sendMessage(sender, msg.getTPAToggleOther()
                    .replace("%player%", targetPlayer.getName())
                    .replace("%status%", status), true);
            if (targetPlayer.isOnline()) {
                m.sendMessage(targetPlayer, msg.getTPAToggleOtherTargetPlayer()
                        .replace("%player%", sender.getName())
                        .replace("%status%", status), true);
                return;
            }
        } else {
            if (tpaRequests) {
                m.sendMessage(targetPlayer, msg.getTPAToggleNo(), true);
            } else {
                m.sendMessage(targetPlayer, msg.getTPAToggleYes(), true);
            }
        }
        return;
    }
}
