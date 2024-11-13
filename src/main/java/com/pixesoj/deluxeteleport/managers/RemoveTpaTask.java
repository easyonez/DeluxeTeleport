package com.pixesoj.deluxeteleport.managers;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.utils.TimeUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RemoveTpaTask {
    private final DeluxeTeleport plugin;
    private final Player player;
    private final Player targetPlayer;

    public RemoveTpaTask(DeluxeTeleport plugin, Player player, Player targetPlayer) {
        this.plugin = plugin;
        this.player = player;
        this.targetPlayer = targetPlayer;
    }

    public void runWithDelay() {
        int delayInTicks = TimeUtils.timerConverter("seconds", plugin.getMainTPAConfigManager().getExpirationTime());
        new BukkitRunnable() {
            @Override
            public void run() {
                if (ListManager.isPlayerInTpaList(player, targetPlayer.getName())) {
                    ListManager.removeTpa(player, targetPlayer.getName());
                    MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefixTPA(), plugin);
                    m.sendMessage(player, plugin.getMainMessagesManager().getTPATeleportDefeated()
                            .replace("%player%", targetPlayer.getName()), true);
                    m.sendMessage(targetPlayer, plugin.getMainMessagesManager().getTPATeleportDefeatedTargetPlayer(), true);
                }
            }
        }.runTaskLater(plugin, delayInTicks);
    }
}