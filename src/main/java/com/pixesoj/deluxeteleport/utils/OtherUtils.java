package com.pixesoj.deluxeteleport.utils;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.dependencies.DependencyManager;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.yaml.snakeyaml.Yaml;


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class OtherUtils {

    public static boolean isNew() {
        ServerVersion serverVersion = ServerInfo.getServerVersion();
        return ServerVersion.serverVersionGreaterEqualThan(serverVersion, ServerVersion.v1_16_1);
    }

    public static boolean isLegacy() {
        ServerVersion serverVersion = ServerInfo.getServerVersion();
        return !ServerVersion.serverVersionGreaterEqualThan(serverVersion, ServerVersion.v1_13_1);
    }

    public static boolean isTrimNew() {
        ServerVersion serverVersion = ServerInfo.getServerVersion();
        return ServerVersion.serverVersionGreaterEqualThan(serverVersion, ServerVersion.v1_20_1);
    }

    public static void sendConsoleMessagesOnEnabled(DeluxeTeleport plugin){
        MessagesManager m = new MessagesManager("&8[&bDeluxeTeleport&8] ", plugin);
        ConsoleCommandSender s = Bukkit.getConsoleSender();

        m.sendMessage(s, "&b ____  ____ ", false);
        m.sendMessage(s, "&b(  _ \\(_  _)  " + "&8By &bPixesoj &av" + plugin.version, false);
        m.sendMessage(s, "&b )(_) ) )(    " + "&8Running on " + getServerSoftwareName(plugin), false);
        m.sendMessage(s, "&b(____/ (__)", false);
        m.sendMessage(s, " ", false);
        dependencyMessage(plugin);
        dependencyAlert(plugin);
    }

    public static void dependencyAlert(DeluxeTeleport plugin) {
        MessagesManager m = new MessagesManager("&8[&bDeluxeTeleport&8] ", plugin);
        ConsoleCommandSender s = Bukkit.getConsoleSender();
        int errors = 0;
        if (OtherUtils.isRunningOnProxy(plugin)) {
            if (DependencyManager.isFloodgate()) {
                boolean sendFloodgateData = Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Floodgate")).getConfig().getBoolean("send-floodgate-data");
                if (!sendFloodgateData) {
                    m.sendMessage(s, "&bfloodgate_support &cesta activado pero &bsend-floodgate-data &8(&aConfiguracion de Floodgate&8) " +
                            "&ces false, debes activar &csend-floodgate-data &cpara que funcione correctamente", true);
                    errors = errors + 1;
                }
            }
        }
        if (errors > 0) {
            m.sendMessage(s, "&bSe encontraron &c%errors% &berrores"
                    .replace("%errors%", String.valueOf(errors)), true);
        }
    }

    public static void dependencyMessage(DeluxeTeleport plugin) {
        MessagesManager m = new MessagesManager("&8[&bDeluxeTeleport&8] ", plugin);
        ConsoleCommandSender s = Bukkit.getConsoleSender();
        String messageTemplate = plugin.getMainMessagesManager().getGlobalDependencyFound() != null
                ? plugin.getMainMessagesManager().getGlobalDependencyFound()
                : "&aDependency found: &b%dependency%";

        Map<String, Boolean> dependencies = new HashMap<>();
        dependencies.put("Vault", DependencyManager.isEconomy(plugin));
        dependencies.put("PlaceholderAPI", DependencyManager.isPlaceholderAPI());
        dependencies.put("nLogin - " + DependencyManager.getNLoginSoftware(plugin), DependencyManager.isNLogin());
        dependencies.put("Floodgate", DependencyManager.isFloodgate());
        dependencies.put("LuckPerms", DependencyManager.isLuckPerms());

        for (Map.Entry<String, Boolean> entry : dependencies.entrySet()) {
            if (entry.getValue()) {
                String message = messageTemplate.replace("%dependency%", entry.getKey());
                m.sendMessage(s, message, true);
            }
        }
    }

    public static void sendConsoleMessagesOnDisabled(DeluxeTeleport plugin){
        MessagesManager m = new MessagesManager("&8[&bDeluxeTeleport&8] ", plugin);
        m.sendMessage(Bukkit.getConsoleSender(), "&7Version: &a" + plugin.version, true);
        m.sendMessage(Bukkit.getConsoleSender(), "&7Author: &bPixesoj", true);
    }

    public static String getServerSoftwareName(DeluxeTeleport plugin) {
        String serverName = Bukkit.getName();
        switch (serverName.toLowerCase()) {
            case "craftbukkit":
                return ChatColor.GREEN + "CraftBukkit";
            case "spigot":
                return ChatColor.AQUA + "Spigot";
            case "paper":
                return ChatColor.YELLOW + "Paper";
            case "purpur":
                return ChatColor.LIGHT_PURPLE + "Purpur";
            default:
                return ChatColor.AQUA + serverName;
        }
    }

    public static boolean isRunningOnProxy(DeluxeTeleport plugin) {
        return isRunningOnBungeeCord(plugin) || isRunningOnVelocity();
    }

    public static boolean isRunningOnBungeeCord(DeluxeTeleport plugin) {
        if (plugin.getServer().spigot().getConfig().getBoolean("settings.bungeecord")){
            return true;
        }

        DependencyManager dependencyManager = new DependencyManager();
        if (dependencyManager.isPaper()){
            try {
                InputStream inputStream = Files.newInputStream(Paths.get("config/paper-global.yml"));
                Yaml yaml = new Yaml();
                Map<String, Object> config = yaml.load(inputStream);

                Map<String, Object> proxies = (Map<String, Object>) config.get("proxies");
                if (proxies != null) {
                    Map<String, Object> velocity = (Map<String, Object>) proxies.get("velocity");
                    if (velocity != null) {
                        return (boolean) velocity.get("enabled");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return false;
    }

    public static boolean isRunningOnVelocity() {
        try {
            Class.forName("com.velocitypowered.api.server.VelocityServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static void updateConfig (DeluxeTeleport plugin, String config){
        MessagesManager m = new MessagesManager("&8[&bDeluxeTeleport&8] ", plugin);
        String messageTemplate = plugin.getMainMessagesManager().getGlobalUpdatingConfig() != null ?
                plugin.getMainMessagesManager().getGlobalUpdatingConfig() :
                "&aUpdating &b%config% &ato the latest version...";
        m.sendMessage(Bukkit.getConsoleSender(), messageTemplate
                .replace("%config%", config), true);
    }
}
