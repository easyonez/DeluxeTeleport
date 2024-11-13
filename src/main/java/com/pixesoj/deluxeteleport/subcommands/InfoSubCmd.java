package com.pixesoj.deluxeteleport.subcommands;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.managers.UpdateCheckManager;
import com.pixesoj.deluxeteleport.model.internal.UpdateCheckResult;
import com.pixesoj.deluxeteleport.utils.OtherUtils;
import com.pixesoj.deluxeteleport.utils.SubCommand;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import org.bukkit.command.CommandSender;

public class InfoSubCmd implements SubCommand {

    private final DeluxeTeleport plugin;

    public InfoSubCmd(DeluxeTeleport plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        MessagesManager m = new MessagesManager("&7[&b&lDT&7] ", plugin);
        if (!PlayerUtils.hasPermission(plugin, sender, plugin.getMainPermissionsManager().getInfo(),
                plugin.getMainPermissionsManager().isInfoDefault(), true)) return true;

        UpdateCheckManager updateCheckManager = new UpdateCheckManager(plugin.version);
        UpdateCheckResult result = updateCheckManager.check();

        m.sendMessage(sender, "&aRunning &bDeluxeTeleport v%plugin_version% &aby &bPixesoj.".replace("%plugin_version%", plugin.version), true);
        m.sendMessage(sender, "&7-  &bUpdated: " + (result.isUpdateAvailable() ? "&c✘" : "&a✔"), true);
        m.sendMessage(sender, "&7-  &bPlatform: &a%server_platform%".replace("%server_platform%", OtherUtils.getServerSoftwareName(plugin)), true);
        m.sendMessage(sender, "&7-  &bServer Version: &a%server_version%".replace("%server_version%", plugin.getServer().getVersion()), true);
        m.sendMessage(sender, "&7-  &bEnabled Functions:", true);
        m.sendMessage(sender, "     &bLobby: " + (plugin.getMainLobbyConfigManager().isEnabled() ? "&a✔" : "&c✘"), true);
        m.sendMessage(sender, "     &bSpawn: " + (plugin.getMainSpawnConfigManager().isEnabled() ? "&a✔" : "&c✘"), true);
        m.sendMessage(sender, "     &bHomes: " + (plugin.getMainHomeConfigManager().isEnabled() ? "&a✔" : "&c✘"), true);
        m.sendMessage(sender, "     &bWarps: " + (plugin.getMainWarpConfigManager().isEnabled() ? "&a✔" : "&c✘"), true);
        m.sendMessage(sender, "     &bTpa: " + (plugin.getMainTPAConfigManager().isEnabled() ? "&a✔" : "&c✘"), true);
        m.sendMessage(sender, "", true);
        return false;
    }
}
