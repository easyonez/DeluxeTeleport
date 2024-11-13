package com.pixesoj.deluxeteleport.commands.home;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.handlers.CooldownHandlers;
import com.pixesoj.deluxeteleport.managers.*;
import com.pixesoj.deluxeteleport.managers.filesmanager.ConfigHomeManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.PermissionsManager;
import com.pixesoj.deluxeteleport.handlers.DelayHandler;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import com.pixesoj.deluxeteleport.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.UUID;

public class HomeCmd implements CommandExecutor {

    private final DeluxeTeleport plugin;
    private final  ActionsManager actionsManager;
    private final ConditionsManager conditionsManager;
    private final MessagesManager m;
    private final ConfigHomeManager config;

    public HomeCmd(DeluxeTeleport plugin) {
        this.plugin = plugin;
        this.config = plugin.getMainHomeConfigManager();
        this.m = new MessagesManager(plugin.getMainMessagesManager().getPrefixHome(), plugin);

        this.conditionsManager = new ConditionsManager(plugin, config.getConfig(), "teleport_conditions");
        this.actionsManager = new ActionsManager(plugin, config.getConfig(), "teleport_actions");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        mainCommand(sender, args);
        return true;
    }

    public void mainCommand(@NotNull CommandSender sender, String[] args) {
        MessagesFileManager msg = plugin.getMainMessagesManager();
        PermissionsManager perm = plugin.getMainPermissionsManager();
        if (!CheckEnabledManager.home(plugin, sender, true)) return;
        if (!PlayerUtils.isPlayer(plugin, sender, true)) return;
        if (!PlayerUtils.hasPermission(plugin, sender, perm.getHome(), perm.isHomeDefault(), true)) return;

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        File playerFile = plugin.getPlayerDataManager().getPlayerFile(uuid);
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
        String home;

        if (config.isCooldownEnabled()) {
            if (plugin.playerHomeInCooldown((Player) sender) && PlayerUtils.isPlayer(plugin, sender, false)) {
                if (!PlayerUtils.hasPermission(plugin, sender, perm.getHomeBypassCooldown(), perm.isHomeBypassCooldownDefault(), false)) {
                    int time = CooldownManager.getRemainingHomeTime(player);
                    m.sendMessage(sender, msg.getHomeInCooldown()
                            .replace("%time%", String.valueOf(time)), true);
                    return;
                }
            }
        }

        if (args.length == 0){
            if (config.getDefaultName().equalsIgnoreCase("none")) {
                m.sendMessage(sender, msg.getHomeNotSpecify(), true);
                return;
            } else {
                home = config.getDefaultName();
            }
        } else {
            home = args[0];
        }

        if (!playerData.contains("homes." + home)){
            m.sendMessage(sender, msg.getHomeNotExist()
                    .replace("%home%", home), true);
            return;
        }

        if (!conditionsManager.isCondition(player)) return;

        String keyPath = "homes." + home + ".";
        double x = playerData.getDouble(keyPath + "x");
        double y = playerData.getDouble(keyPath + "y");
        double z = playerData.getDouble(keyPath + "z");
        float yaw = (float) playerData.getDouble(keyPath + "yaw");
        float pitch = (float) playerData.getDouble(keyPath + "pitch");
        String world = playerData.getString(keyPath + "world");
        assert world != null;
        Location location = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);

        boolean defaultMessage = config.getConfig().getBoolean("actions.default_messages", true);
        if (config.isTeleportDelayEnabled() && !PlayerUtils.hasPermission(plugin, player, perm.getHomeBypassDelay(), perm.isHomeBypassDelayDefault(), false)){
            int delay = TimeUtils.timerConverter("ticks", config.getTeleportDelay());
            DelayManager delayManager = new DelayManager(plugin, delay, (Player) sender, location);
            DelayHandler.home(plugin, player, delayManager, home);
            if (defaultMessage)  m.sendMessage(player, msg.getHomeDelayInTeleport()
                    .replace("%time%", String.valueOf(plugin.getMainHomeConfigManager().getTeleportDelay())), true);
            actionsManager.general("before_delay", player);
        } else {
            if (config.isCooldownEnabled()) {
                CooldownHandlers.home(plugin, player);
            }

            player.teleport(location);
            if (defaultMessage) m.sendMessage(sender, msg.getHomeTeleporting()
                    .replace("%home%", home), true);

            actionsManager.general("none", player);
        }
        return;
    }
}
