package com.pixesoj.deluxeteleport.utils;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.CooldownManager;
import com.pixesoj.deluxeteleport.managers.DelayManager;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.managers.UpdateCheckManager;
import com.pixesoj.deluxeteleport.managers.dependencies.DependencyManager;
import com.pixesoj.deluxeteleport.model.internal.UpdateCheckResult;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Objects;

public class PlaceholderUtils {
    public static String setPlaceholders(DeluxeTeleport plugin, Player player, String text){
        text = setLocalPlaceholders(plugin, player, text);
        if (DependencyManager.isPlaceholderAPI()) text = PlaceholderAPI.setPlaceholders(player, text);
        return text;
    }

    public static String setLocalPlaceholders(DeluxeTeleport plugin, Player player, String text){
        int remaningSpawnDelayTime = DelayManager.getRemainingSpawnTime(player);
        int remaningLobbyDelayTime = DelayManager.getRemainingLobbyTime(player);
        int remaningHomeDelayTime = DelayManager.getRemainingHomeTime(player);
        int remaningTPADelayTime = DelayManager.getRemainingTPATime(player);
        int remaningWarpDelayTime = DelayManager.getRemainingWarpTime(player);

        int spawnDelayTime = TimeUtils.timerConverter("ticks", plugin.getMainSpawnConfigManager().getTeleportDelay());
        int lobbyDelayTime = TimeUtils.timerConverter("ticks", plugin.getMainLobbyConfigManager().getTeleportDelay());
        int homeDelayTime = TimeUtils.timerConverter("ticks", plugin.getMainHomeConfigManager().getTeleportDelay());
        int tpaDelayTime = TimeUtils.timerConverter("ticks", plugin.getMainTPAConfigManager().getDelayTime());
        int warpDelayTime = TimeUtils.timerConverter("ticks", plugin.getMainWarpConfigManager().getTeleportDelay());

        World world = player.getWorld();
        World.Environment worldEnvironment = world.getEnvironment();

        text = text
                .replace("%player%", player.getName())
                .replace("%player_name%", player.getDisplayName())
                .replace("%player_displayname%", player.getDisplayName())
                .replace("%player_world%", Objects.requireNonNull(player.getLocation().getWorld()).getName())
                .replace("%player_world_uuid%", player.getLocation().getWorld().getUID().toString())
                .replace("%player_world_type%", worldEnvironment.name())
                .replace("%player_x%", String.valueOf(player.getLocation().getX()))
                .replace("%player_y%", String.valueOf(player.getLocation().getY()))
                .replace("%player_z%", String.valueOf(player.getLocation().getZ()))
                .replace("%player_yaw%", String.valueOf(player.getLocation().getYaw()))
                .replace("%player_pitch%", String.valueOf(player.getLocation().getPitch()))

                .replace("%prefix%", plugin.getMainMessagesManager().getPrefix())
                .replace("%prefix_global%", plugin.getMainMessagesManager().getPrefix())
                .replace("%prefix_spawn%", plugin.getMainMessagesManager().getPrefixSpawn())
                .replace("%prefix_lobby%", plugin.getMainMessagesManager().getPrefixLobby())
                .replace("%prefix_home%", plugin.getMainMessagesManager().getPrefixHome())
                .replace("%prefix_tpa%", plugin.getMainMessagesManager().getPrefixTPA())


                .replace("%spawn_delay%", String.valueOf(remaningSpawnDelayTime))
                .replace("%spawn_delay_formatted%", TimeUtils.formatTime(plugin, remaningSpawnDelayTime))
                .replace("%spawn_delay_time%", String.valueOf(spawnDelayTime))
                .replace("%spawn_delay_time_formatted%", TimeUtils.formatTime(plugin, spawnDelayTime))

                .replace("%spawn_cooldown%", String.valueOf(CooldownManager.getRemainingSpawnTime(player)))
                .replace("%spawn_cooldown_formatted%", TimeUtils.formatTime(plugin, CooldownManager.getRemainingSpawnTime(player)))
                .replace("%spawn_cooldown_time%", String.valueOf(PlayerUtils.getCooldownTime(plugin, player, "spawn")))
                .replace("%spawn_cooldown_time_formatted%", TimeUtils.formatTime(plugin, PlayerUtils.getCooldownTime(plugin, player, "spawn")))


                .replace("%lobby_delay%", String.valueOf(remaningLobbyDelayTime))
                .replace("%lobby_delay_formatted%", TimeUtils.formatTime(plugin, remaningLobbyDelayTime))
                .replace("%lobby_delay_time%", String.valueOf(lobbyDelayTime))
                .replace("%lobby_delay_time_formatted%", TimeUtils.formatTime(plugin, lobbyDelayTime))

                .replace("%lobby_cooldown%", String.valueOf(CooldownManager.getRemainingLobbyTime(player)))
                .replace("%lobby_cooldown_formatted%", TimeUtils.formatTime(plugin, CooldownManager.getRemainingLobbyTime(player)))
                .replace("%lobby_cooldown_time%", String.valueOf(PlayerUtils.getCooldownTime(plugin, player, "lobby")))
                .replace("%lobby_cooldown_time_formatted%", TimeUtils.formatTime(plugin, PlayerUtils.getCooldownTime(plugin, player, "lobby")))


                .replace("%home_delay%", String.valueOf(remaningHomeDelayTime))
                .replace("%home_delay_formatted%", TimeUtils.formatTime(plugin, remaningHomeDelayTime))
                .replace("%home_delay_time%", String.valueOf(homeDelayTime))
                .replace("%home_delay_time_formatted%", TimeUtils.formatTime(plugin, lobbyDelayTime))

                .replace("%home_cooldown%", String.valueOf(CooldownManager.getRemainingHomeTime(player)))
                .replace("%home_cooldown_formatted%", TimeUtils.formatTime(plugin, CooldownManager.getRemainingHomeTime(player)))
                .replace("%home_cooldown_time%", String.valueOf(PlayerUtils.getCooldownTime(plugin, player, "home")))
                .replace("%home_cooldown_time_formatted%", TimeUtils.formatTime(plugin, PlayerUtils.getCooldownTime(plugin, player, "home")))

                .replace("%homes_set%", String.valueOf(PlayerUtils.getPlayerHomeCount(plugin, player)))
                .replace("%homes_max%", String.valueOf(PlayerUtils.getPlayerMaxHomeCount(plugin, player)))
                .replace("%homes_max_formatted%", PlayerUtils.getPlayerMaxHomeCount(plugin, player) == 2147483647 ? plugin.getMainMessagesManager().getVariablesUnlimited() : String.valueOf(PlayerUtils.getPlayerMaxHomeCount(plugin, player)))


                .replace("%tpa_delay%", String.valueOf(remaningTPADelayTime))
                .replace("%tpa_delay_formatted%", TimeUtils.formatTime(plugin, remaningTPADelayTime))
                .replace("%tpa_delay_time%", String.valueOf(tpaDelayTime))
                .replace("%tpa_delay_time_formatted%", TimeUtils.formatTime(plugin, tpaDelayTime))

                .replace("%tpa_cooldown%", String.valueOf(CooldownManager.getRemainingTpaTime(player)))
                .replace("%tpa_cooldown_formatted%", TimeUtils.formatTime(plugin, CooldownManager.getRemainingTpaTime(player)))
                .replace("%tpa_cooldown_time%", String.valueOf(PlayerUtils.getCooldownTime(plugin, player, "tpa")))
                .replace("%tpa_cooldown_time_formatted%", TimeUtils.formatTime(plugin, PlayerUtils.getCooldownTime(plugin, player, "tpa")))


                .replace("%warp_delay%", String.valueOf(remaningWarpDelayTime))
                .replace("%warp_delay_formatted%", TimeUtils.formatTime(plugin, remaningWarpDelayTime))
                .replace("%warp_delay_time%", String.valueOf(warpDelayTime))
                .replace("%warp_delay_time_formatted%", TimeUtils.formatTime(plugin, warpDelayTime))

                .replace("%warp_cooldown%", String.valueOf(CooldownManager.getRemainingWarpTime(player)))
                .replace("%warp_cooldown_formatted%", TimeUtils.formatTime(plugin, CooldownManager.getRemainingWarpTime(player)))
                .replace("%warp_cooldown_time%", String.valueOf(PlayerUtils.getCooldownTime(plugin, player, "warp")))
                .replace("%warp_cooldown_time_formatted%", TimeUtils.formatTime(plugin, PlayerUtils.getCooldownTime(plugin, player, "warp")));
        return text;
    }

    public static String setHomePlaceholders(DeluxeTeleport plugin, Player player, String text, String homeName, Location location) {
        text = text.replace("%home_name%", homeName)
                .replace("%home_world%", Objects.requireNonNull(location.getWorld()).getName())
                .replace("%home_x%", String.valueOf(location.getX()))
                .replace("%home_y%", String.valueOf(location.getY()))
                .replace("%home_z%", String.valueOf(location.getZ()))
                .replace("%home_yaw%", String.valueOf(location.getYaw()))
                .replace("%home_pitch%", String.valueOf(location.getPitch()))
                .replace("%home_name_formatted%", MessagesManager.capitalizeText(homeName))
                .replace("%home_world_formatted%", MessagesManager.capitalizeText(location.getWorld().getName()))
                .replace("%home_x_formatted%", MessagesManager.formatCoordinate(location.getX()))
                .replace("%home_y_formatted%", MessagesManager.formatCoordinate(location.getY()))
                .replace("%home_z_formatted%", MessagesManager.formatCoordinate(location.getZ()))
                .replace("%home_yaw_formatted%", MessagesManager.formatCoordinate(location.getYaw()))
                .replace("%home_pitch_formatted%", MessagesManager.formatCoordinate(location.getPitch()));

        text = setPlaceholders(plugin, player, text);
        return text;
    }
}
