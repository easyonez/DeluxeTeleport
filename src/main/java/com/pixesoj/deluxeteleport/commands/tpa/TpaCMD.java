package com.pixesoj.deluxeteleport.commands.tpa;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.*;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.PermissionsManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.ConfigTPAManager;
import com.pixesoj.deluxeteleport.handlers.CooldownHandlers;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import com.pixesoj.deluxeteleport.utils.TimeUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TpaCMD implements CommandExecutor {
    private final DeluxeTeleport plugin;

    public TpaCMD(DeluxeTeleport plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        mainCommand(sender, args);
        return true;
    }

    public void mainCommand(@NotNull CommandSender sender, String[] args) {
        MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefixTPA(), plugin);
        ConfigTPAManager config = plugin.getMainTPAConfigManager();
        MessagesFileManager msg = plugin.getMainMessagesManager();
        PermissionsManager perm = plugin.getMainPermissionsManager();
        if (!CheckEnabledManager.tpa(plugin, sender, true)) return;
        if (!PlayerUtils.isPlayer(plugin, sender, true)) return;
        if (!PlayerUtils.hasPermission(plugin, sender, perm.getTpa(), perm.isTpaDefault(), true)) return;

        Player player = (Player) sender;
        if (config.isCooldownEnabled() && plugin.playerTpaInCooldown(player) && !PlayerUtils.hasPermission(plugin, sender, perm.getTpaBypassCooldown(), perm.isTpaBypassCooldownDefault(), false)) {
            m.sendMessage(sender, msg.getTPAInCooldown(), true);
            return;
        }

        String targetPlayerName;
        if (args.length == 0){
            m.sendMessage(sender, msg.getTPASpecifyPlayer(), true);
            return;
        }

        targetPlayerName = args[0];

        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        if (targetPlayer == null || !targetPlayer.isOnline()) {
            m.sendMessage(sender, msg.getGlobalPlayerOffline()
                    .replace("%player%", targetPlayerName), true);
            return;
        }

        if (targetPlayerName.equalsIgnoreCase(player.getName()) && !config.isTpaHimself()){
            m.sendMessage(sender, msg.getTPAHimself(), true);
            return;
        }

        UUID uuid = targetPlayer.getUniqueId();
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(plugin.getPlayerDataManager().getPlayerFile(uuid));
        boolean tpaRequests = playerData.getBoolean("settings.tpa-requests", config.isStatusDefault());
        if (!tpaRequests && !config.isIgnoreTpaStatus()){
            m.sendMessage(player, msg.getTPABlocked(), true);
            return;
        }

        if (ListManager.isTpa(targetPlayer, player.getName())){
            m.sendMessage(player, msg.getTPAPending(), true);
            return;
        }

        if (!ListManager.isTpaEmpty(targetPlayer) && !config.isMultipleTPA()){
            m.sendMessage(sender, msg.getTPAPendingRequest(), true);
            return;
        }

        ListManager.addTpa(targetPlayer, player.getName());
        ListManager.addMyTpa(player, targetPlayer.getName());
        m.sendMessage(player, msg.getTPASend()
                .replace("%player%", targetPlayer.getName()), true);

        if (!config.isGeyserSuport()) {
            sendMessageJava(player, targetPlayer);
        } else {
            if (PlayerUtils.isBedrock(targetPlayer) && config.isGeyserDiferentMessage()) {
                for (String message : msg.getTPAGeyserRequest()){
                    m.sendMessage(targetPlayer, message
                            .replace("%player%", player.getName()), false);
                }
            } else {
                sendMessageJava(player, targetPlayer);
            }
        }

        if (config.isExpirationEnabled()) {
            new RemoveTpaTask(plugin, player, targetPlayer).runWithDelay();
        }

        if (config.isCooldownEnabled() && !config.isCooldownCountWhenTeleporting()){
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
    }

    public void sendMessageJava(Player player, Player targetPlayer){
        MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefixTPA(), plugin);
        ConfigTPAManager config = plugin.getMainTPAConfigManager();
        MessagesFileManager msg = plugin.getMainMessagesManager();
        for (String message : msg.getTPARequest()){
            if (config.isCenteredRequestMessage()){
                m.sendMessage(targetPlayer, m.getCenteredMessage(message)
                        .replace("%player%", player.getName()), false);
            } else {
                m.sendMessage(targetPlayer, message
                        .replace("%player%", player.getName()), false);
            }
        }

        TextComponent accept = new TextComponent();
        accept.setText(m.getColoredMessage(msg.getTPAClickAccept()));
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept " + player.getName()));
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(m.getColoredMessage(msg.getTPAClickAcceptDescription())).create()));

        TextComponent cancel = new TextComponent();
        cancel.setText(m.getColoredMessage(msg.getTPAClickCancel()));
        cancel.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadeny " + player.getName()));
        cancel.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(m.getColoredMessage(msg.getTPAClickCancelDescription())).create()));

        if (config.isGeyserSuport() && !config.isGeyserClickTpa()){
            if (PlayerUtils.isBedrock(targetPlayer)){
                return;
            }
        }

        if (config.isClickTpa()) {
            TextComponent combined = new TextComponent(accept);
            combined.addExtra("                 ");
            combined.addExtra(cancel);
            if (config.isCenteredRequestMessage()) {
                m.sendComponent(targetPlayer, m.getCenteredComponent(combined));
            } else {
                m.sendComponent(targetPlayer, combined);
            }
        }
    }
}
