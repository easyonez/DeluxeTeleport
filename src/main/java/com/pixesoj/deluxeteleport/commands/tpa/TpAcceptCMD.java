package com.pixesoj.deluxeteleport.commands.tpa;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.handlers.CooldownHandlers;
import com.pixesoj.deluxeteleport.managers.*;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.PermissionsManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.ConfigTPAManager;
import com.pixesoj.deluxeteleport.handlers.DelayHandler;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import com.pixesoj.deluxeteleport.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpAcceptCMD implements CommandExecutor {
    private final DeluxeTeleport plugin;
    private final ActionsManager actionsManager;

    public TpAcceptCMD(DeluxeTeleport plugin) {
        this.plugin = plugin;
        this.actionsManager = new ActionsManager(plugin, plugin.getMainTPAConfigManager().getConfig(), "teleport_actions");
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
        if (!PlayerUtils.hasPermission(plugin, sender, perm.getTpAccept(), perm.isTpAcceptDefault(), true)) return;

        Player player = (Player) sender;
        if (ListManager.isTpaEmpty(player)){
            m.sendMessage(player, msg.getTPANoRequest(), true);
            return;
        }

        String targetPlayerName = null;
        if (config.getListPosition().equalsIgnoreCase("first")) {
            targetPlayerName = ListManager.getFirstTpaPlayer(player);
        } else if (config.getListPosition().equalsIgnoreCase("none")) {
            if (args.length == 0){
                m.sendMessage(sender, msg.getTPASpecifyPlayer(), true);
                return;
            }
        } else {
            targetPlayerName = ListManager.getLastTpaPlayer(player);
        }

        if (!(args.length == 0)){
            targetPlayerName = args[0];
        }

        assert targetPlayerName != null;
        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        if (targetPlayer == null || !targetPlayer.isOnline()) {
            m.sendMessage(sender, msg.getGlobalPlayerOffline()
                    .replace("%player%", targetPlayerName), true);
            return;
        }

        if (!ListManager.isPlayerInTpaList(player, targetPlayerName)){
            m.sendMessage(player, msg.getTPANoRequestPlayer()
                    .replace("%player%", targetPlayer.getName()), true);
            return;
        }

        if (ListManager.isInTpaTeleport(player)){
            m.sendMessage(player, msg.getTPAPlayerInTeleport()
                    .replace("%player%", player.getName()), true);
            return;
        }

        if (ListManager.isInTpaTeleport(targetPlayer)){
            m.sendMessage(player, msg.getTPATargetPlayerInTeleport()
                    .replace("%player%", targetPlayer.getName()), true);
            return;
        }

        if (config.isDelayEnabled()){
            ListManager.addTpaTeleport(player);
            ListManager.addTpaTeleport(targetPlayer);
            int delay = TimeUtils.timerConverter("ticks", config.getDelayTime());
            DelayManager delayManager = new DelayManager(plugin, delay, player, null);
            DelayHandler.tpa(plugin, player, targetPlayer, delayManager);

            actionsManager.tpa("before_delay", player, targetPlayer);
        } else {
            if (config.isCooldownEnabled()){
                String cooldownTo = plugin.getMainTPAConfigManager().getCooldownTo();
                switch (cooldownTo.toLowerCase()) {
                    case "player":
                        CooldownHandlers.tpa(plugin, player);
                        break;

                    case "targetplayer":
                        CooldownHandlers.tpa(plugin, targetPlayer);
                        break;

                    default:
                        CooldownHandlers.tpa(plugin, targetPlayer);
                        CooldownHandlers.tpa(plugin, player);
                        break;
                }
            }

            boolean defaultMessage = config.getConfig().getBoolean("actions.default_messages", true);
            targetPlayer.teleport(player);
            ListManager.removeTpa(player, targetPlayer.getName());
            ListManager.removeTpaTeleport(targetPlayer);
            ListManager.removeTpaTeleport(player);
            if (defaultMessage) m.sendMessage(targetPlayer, msg.getTPATeleporting()
                    .replace("%player%", player.getName()), true);
            if (defaultMessage) m.sendMessage(player, msg.getTPATeleportTargetPlayer()
                    .replace("%player%", targetPlayer.getName()), true);

            actionsManager.tpa("none", player, targetPlayer);
        }
    }
}
