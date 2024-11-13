package com.pixesoj.deluxeteleport.handlers;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.filesmanager.*;
import com.pixesoj.deluxeteleport.managers.CooldownManager;
import com.pixesoj.deluxeteleport.managers.dependencies.DependencyManager;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class CooldownHandlers {
    public static void Lobby(DeluxeTeleport plugin, Player player) {
        int cooldownTime = PlayerUtils.getCooldownTime(plugin, player, "lobby");
        CooldownManager c = new CooldownManager(plugin, cooldownTime, player.getName());
        c.cooldown("lobby");
        plugin.addLobbyCooldown(player.getName(), cooldownTime);
    }

    public static void Spawn(DeluxeTeleport plugin, Player player) {
        int cooldownTime = PlayerUtils.getCooldownTime(plugin, player, "spawn");
        CooldownManager c = new CooldownManager(plugin, cooldownTime, player.getName());
        c.cooldown("spawn");
        plugin.addSpawnCooldown(player.getName(), cooldownTime);
    }

    public static void home(DeluxeTeleport plugin, Player player) {
        int cooldownTime = PlayerUtils.getCooldownTime(plugin, player, "home");
        CooldownManager c = new CooldownManager(plugin, cooldownTime, player.getName());
        c.cooldown("home");
        plugin.addHomeCooldown(player.getName(), cooldownTime);
    }

    public static void tpa(DeluxeTeleport plugin, Player player) {
        int cooldownTime = PlayerUtils.getCooldownTime(plugin, player, "tpa");
        CooldownManager c = new CooldownManager(plugin, cooldownTime, player.getName());
        c.cooldown("tpa");
        plugin.addTpaCooldown(player.getName(), cooldownTime);
    }

    public static void warp(DeluxeTeleport plugin, Player player) {
        int cooldownTime = PlayerUtils.getCooldownTime(plugin, player, "warp");
        CooldownManager c = new CooldownManager(plugin, cooldownTime, player.getName());
        c.cooldown("warp");
        plugin.addWarpCooldown(player.getName(), cooldownTime);
    }
}
