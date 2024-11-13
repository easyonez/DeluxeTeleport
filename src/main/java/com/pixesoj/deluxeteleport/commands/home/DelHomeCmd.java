package com.pixesoj.deluxeteleport.commands.home;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.ConfigHomeManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.PermissionsManager;
import com.pixesoj.deluxeteleport.managers.CheckEnabledManager;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.UUID;

public class DelHomeCmd implements CommandExecutor {

    private final DeluxeTeleport plugin;

    public DelHomeCmd(DeluxeTeleport plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        mainCommand(sender, args);
        return true;
    }

    public void mainCommand(@NotNull CommandSender sender, String[] args) {
        MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefixHome(), plugin);
        ConfigHomeManager config = plugin.getMainHomeConfigManager();
        PermissionsManager perm = plugin.getMainPermissionsManager();
        MessagesFileManager msg = plugin.getMainMessagesManager();
        if (!CheckEnabledManager.home(plugin, sender, true)) return;
        if (!PlayerUtils.isPlayer(plugin, sender, true)) return;
        if (!PlayerUtils.hasPermission(plugin, sender, perm.getDelHome(), perm.isDelHomeDefault(), true)) return;

        String home;
        if (args.length == 0){
            if (config.getDefaultName().equalsIgnoreCase("none")) {
                m.sendMessage(sender, msg.getHomeDeletedError(), true);
                return;
            } else {
                home = config.getDefaultName();
            }
        } else {
            home = args[0];
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        File playerFile = plugin.getPlayerDataManager().getPlayerFile(uuid);
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
        if (!playerData.contains("homes." + home)){
            m.sendMessage(sender, msg.getHomeNotExist()
                    .replace("%home%", home), true);
            return;
        }

        plugin.getPlayerDataManager().delPlayerHome(player, home);
        m.sendMessage(sender, msg.getHomeDeletedSuccessfully()
                .replace("%home%", home), true);
        return;
    }
}
