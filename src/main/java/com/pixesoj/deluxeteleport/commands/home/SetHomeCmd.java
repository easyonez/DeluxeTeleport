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

public class SetHomeCmd implements CommandExecutor {

    private final DeluxeTeleport plugin;

    public SetHomeCmd(DeluxeTeleport plugin) {
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
        MessagesFileManager msg = plugin.getMainMessagesManager();
        PermissionsManager perm = plugin.getMainPermissionsManager();
        if (!CheckEnabledManager.home(plugin, sender, true)) return;
        if (!PlayerUtils.isPlayer(plugin, sender, true)) return;
        if (!PlayerUtils.hasPermission(plugin, sender, perm.getSetHome(), perm.isSetHomeDefault(), true)) return;

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        File playerFile = plugin.getPlayerDataManager().getPlayerFile(uuid);
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
        String home;
        if (args.length == 0){
            if (config.getDefaultName().equalsIgnoreCase("none")) {
                m.sendMessage(sender, msg.getHomeSetError(), true);
                return;
            } else {
                home = config.getDefaultName();
            }
        } else {
            home = args[0];
        }

        if (playerData.contains("homes." + home) && !config.isOverwriteExistingHome()){
            m.sendMessage(sender, msg.getHomeExists(), true);
            return;
        }

        int maxHomes = PlayerUtils.getPlayerMaxHomeCount(plugin, player);
        int playerHomeCount = PlayerUtils.getPlayerHomeCount(plugin, player);

        if (playerHomeCount >= maxHomes) {
            m.sendMessage(sender, msg.getHomeMaxCount(), true);
            return;
        }

        plugin.getPlayerDataManager().savePlayerHome(player, home);
        m.sendMessage(sender, msg.getHomeSetSuccessfully()
                .replace("%home%", home), true);

        return;
    }
}
