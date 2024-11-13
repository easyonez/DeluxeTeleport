package com.pixesoj.deluxeteleport.commands.tpa;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.PermissionsManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.ConfigTPAManager;
import com.pixesoj.deluxeteleport.managers.ListManager;
import com.pixesoj.deluxeteleport.managers.CheckEnabledManager;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpaCancelCMD implements CommandExecutor {
    private final DeluxeTeleport plugin;

    public TpaCancelCMD(DeluxeTeleport plugin) {
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
        if (!PlayerUtils.hasPermission(plugin, sender, perm.getTpaCancel(), perm.isTpaCancelDefault(), true)) return;

        Player player = (Player) sender;
        String targetPlayerName = null;

        if (config.getListPosition().equalsIgnoreCase("first")) {
            targetPlayerName = ListManager.getFirstMyTpaPlayer(player);
        } else if (config.getListPosition().equalsIgnoreCase("none")) {
            if (args.length == 0){
                m.sendMessage(sender, msg.getTPASpecifyPlayer(), true);
                return;
            }
        } else {
            targetPlayerName = ListManager.getMyLastTpaPlayer(player);
        }

        if (!(args.length == 0)){
            targetPlayerName = args[0];
        }


        if (args.length == 0 && targetPlayerName == null){
            m.sendMessage(player, msg.getTPANoRequestSent(), true);
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        if (targetPlayer == null || !targetPlayer.isOnline()) {
            m.sendMessage(sender, msg.getGlobalPlayerOffline()
                    .replace("%player%", targetPlayerName), true);
            return;
        }

        if (!ListManager.isPlayerInMyTpaList(player, targetPlayerName)){
            m.sendMessage(player, msg.getTPANoRequestSentPlayer()
                    .replace("%player%", targetPlayer.getName()), true);
            return;
        }

        ListManager.removeTpaTeleport(player);
        ListManager.removeTpa(targetPlayer, player.getName());
        ListManager.removeMyTpa(player, targetPlayer.getName());

        m.sendMessage(player, msg.getTPACancel()
                .replace("%player%", targetPlayer.getName()), true);
        m.sendMessage(targetPlayer, msg.getTPACancelTargetPlayer()
                .replace("%player%", player.getName()), true);
        return;
    }
}
