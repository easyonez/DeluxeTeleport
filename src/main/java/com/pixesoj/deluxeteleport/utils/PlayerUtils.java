package com.pixesoj.deluxeteleport.utils;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.*;
import com.pixesoj.deluxeteleport.managers.dependencies.DependencyManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.geysermc.floodgate.api.FloodgateApi;

import java.io.File;
import java.util.Set;
import java.util.UUID;

public class PlayerUtils {
    public static boolean hasPermission(DeluxeTeleport plugin, CommandSender sender, String p, boolean d, boolean sendMessage){
        MessagesFileManager msg = plugin.getMainMessagesManager();
        MessagesManager m = new MessagesManager(msg.getPrefix(), plugin);
        if (sender instanceof Player){
            if (!(sender.hasPermission(p) || d)){
                if (sendMessage) {
                    m.sendMessage(sender, msg.getGlobalPermissionDenied()
                            .replace("%permission%", p), true);
                }
                return false;
            }
        }
        return true;
    }

    public static boolean hasPermission(CommandSender sender, String p){
        if (sender instanceof Player){
            return sender.hasPermission(p);
        }
        return true;
    }

    public static boolean isPlayer (DeluxeTeleport plugin, CommandSender sender, boolean sendMessage){
        MessagesManager m = new MessagesManager("&8[&bDeluxeTeleport&8] ", plugin);
        MessagesFileManager msg = plugin.getMainMessagesManager();
        if (!(sender instanceof Player)){
            if (sendMessage) {
                m.sendMessage(sender, msg.getGlobalConsoleDenied(), true);
            }
            return false;
        } else {
            return true;
        }
    }

    public static boolean isBedrock (Player player){
        if (!DependencyManager.isFloodgate()){
            MessagesManager m = new MessagesManager("&8[&bDeluxeTeleport&8] ", null);
            m.sendMessage(Bukkit.getConsoleSender(), "&cTrying to access the Floodgate API but the plugin does not exist on the server", true);
            return false;
        }
        FloodgateApi floodgateApi = FloodgateApi.getInstance();
        return floodgateApi.isFloodgatePlayer(player.getUniqueId());
    }

    public static int getPlayerHomeCount(DeluxeTeleport plugin, Player player) {
        UUID uuid = player.getUniqueId();
        File playerFile = plugin.getPlayerDataManager().getPlayerFile(uuid);
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
        ConfigurationSection homesSection = playerData.getConfigurationSection("homes");
        if (!playerFile.exists()) return 0;
        if (!playerData.contains("homes")) return 0;
        if (homesSection == null) return 0;
        return homesSection.getKeys(false).size();
    }

    public static int getPlayerMaxHomeCount(DeluxeTeleport plugin, Player player) {
        int maxHomes = plugin.getMainHomeConfigManager().getDefaultMaxHomes();
        ConfigurationSection ranksSection = plugin.getMainHomeConfigManager().getConfig().getConfigurationSection("sethome_multiple");
        if (ranksSection == null) {
            return maxHomes;
        }

        Set<String> permissionRanks = ranksSection.getKeys(false);
        for (String rank : permissionRanks) {

            String permission = plugin.getMainPermissionsManager().getSetHomeMultiple();
            if (PlayerUtils.hasPermission(plugin, player, permission + ".unlimited", false, false)){
                return 2147483647;
            }
            if (PlayerUtils.hasPermission(plugin, player, permission + "." + rank, false, false)) {
                int homesForRank = plugin.getMainHomeConfigManager().getConfig().getInt("sethome_multiple." + rank, maxHomes);
                maxHomes = Math.max(maxHomes, homesForRank);
            }
        }
        return maxHomes;
    }

    public static boolean hasMoney(DeluxeTeleport plugin, Player player, double money){
        if (DependencyManager.isEconomy(plugin)) {
            Economy economy = DependencyManager.getEconomy();
            double playerMoney = economy.getBalance(player);
            return playerMoney >= money;
        }
        return false;
    }

    public static boolean hasExp(Player player, int amount, boolean level) {
        if (!level) {
            return player.getTotalExperience() >= amount;
        } else {
            return player.getLevel() >= amount;
        }
    }

    public static boolean isNear(Player player, Location location, int distance) {
        if (location == null) return false;
        Location playerLocation = player.getLocation();
        double squaredDistance = playerLocation.distanceSquared(location);
        return squaredDistance <= (distance * distance);
    }

    public static boolean isOnline(DeluxeTeleport plugin, CommandSender sender, Player player, boolean sendMessage){
        if (player == null || !player.isOnline()) {
            MessagesManager m = new MessagesManager(plugin.getMainMessagesManager().getPrefix(), plugin);
            if (sendMessage) {
                String playerName = (player != null) ? player.getName() : "Desconocido";
                m.sendMessage(sender, plugin.getMainMessagesManager().getGlobalPlayerOffline()
                        .replace("%player%", playerName), true);
            }
            return false;
        }
        return player.isOnline();
    }

    public static int getCooldownTime(DeluxeTeleport plugin, Player player, String type){
        int cooldownTime = 0;
        if (type.equalsIgnoreCase("lobby")){
            ConfigLobbyManager config = plugin.getMainLobbyConfigManager();
            PermissionsManager perm = plugin.getMainPermissionsManager();
            cooldownTime = TimeUtils.timerConverter("ticks", plugin.getMainLobbyConfigManager().getCooldownTime());
            if (config.isCooldownByRankEnabled()) {
                ConfigurationSection timeRanks = config.getConfig().getConfigurationSection("cooldown.by_rank.ranks");
                if (timeRanks != null) {
                    for (String rank : timeRanks.getKeys(false)) {
                        if (config.isCooldownByRankAutoRanksEnabled()){
                            if (config.getCooldownByRankAutoRanksPermissionPlugin().equalsIgnoreCase("LuckPerms")){
                                if (DependencyManager.isLuckPerms()){
                                    RegisteredServiceProvider<LuckPerms> luckPermsProvider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
                                    if (luckPermsProvider != null) {
                                        LuckPerms luckPerms = luckPermsProvider.getProvider();
                                        User userLuckPerms = luckPerms.getUserManager().getUser(player.getUniqueId());

                                        assert userLuckPerms != null;
                                        String primaryGroup = userLuckPerms.getPrimaryGroup();
                                        if (primaryGroup.equalsIgnoreCase(rank)) cooldownTime = timeRanks.getInt(rank);
                                    }
                                }
                            }
                        } else {
                            String permission = perm.getLobbyCooldownRank() + "." + rank;
                            if (player.hasPermission(permission)) {
                                int rankCooldownTime = TimeUtils.timerConverter("ticks", timeRanks.getString(rank));

                                if (config.getCooldownByRankPrioritizeTime().equalsIgnoreCase("SHORTEST")) {
                                    if (rankCooldownTime < cooldownTime) cooldownTime = rankCooldownTime;
                                } else {
                                    if (rankCooldownTime > cooldownTime) cooldownTime = rankCooldownTime;
                                }
                            }
                        }
                    }
                }
            }
        } else if (type.equalsIgnoreCase("spawn")) {
            ConfigSpawnManager config = plugin.getMainSpawnConfigManager();
            PermissionsManager perm = plugin.getMainPermissionsManager();
            cooldownTime = TimeUtils.timerConverter("ticks", plugin.getMainSpawnConfigManager().getCooldownTime());
            if (config.isCooldownByRankEnabled()) {
                ConfigurationSection timeRanks = config.getConfig().getConfigurationSection("cooldown.by_rank.ranks");
                if (timeRanks != null) {
                    for (String rank : timeRanks.getKeys(false)) {
                        if (config.isCooldownByRankAutoRanksEnabled()){
                            if (config.getCooldownByRankAutoRanksPermissionPlugin().equalsIgnoreCase("LuckPerms")){
                                if (DependencyManager.isLuckPerms()){
                                    RegisteredServiceProvider<LuckPerms> luckPermsProvider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
                                    if (luckPermsProvider != null) {
                                        LuckPerms luckPerms = luckPermsProvider.getProvider();
                                        User userLuckPerms = luckPerms.getUserManager().getUser(player.getUniqueId());

                                        assert userLuckPerms != null;
                                        String primaryGroup = userLuckPerms.getPrimaryGroup();
                                        if (primaryGroup.equalsIgnoreCase(rank)) cooldownTime = timeRanks.getInt(rank);
                                    }
                                }
                            }
                        } else {
                            String permission = perm.getSpawnCooldownRank() + "." + rank;
                            if (player.hasPermission(permission)) {
                                int rankCooldownTime = TimeUtils.timerConverter("ticks", timeRanks.getString(rank));

                                if (config.getCooldownByRankPrioritizeTime().equalsIgnoreCase("SHORTEST")) {
                                    if (rankCooldownTime < cooldownTime) cooldownTime = rankCooldownTime;
                                } else {
                                    if (rankCooldownTime > cooldownTime) cooldownTime = rankCooldownTime;
                                }
                            }
                        }
                    }
                }
            }
        } else if (type.equalsIgnoreCase("home")) {
            ConfigHomeManager config = plugin.getMainHomeConfigManager();
            PermissionsManager perm = plugin.getMainPermissionsManager();
            cooldownTime = TimeUtils.timerConverter("ticks", plugin.getMainHomeConfigManager().getCooldownTime());
            if (config.isCooldownByRankEnabled()) {
                ConfigurationSection timeRanks = config.getConfig().getConfigurationSection("cooldown.by_rank.ranks");
                if (timeRanks != null) {
                    for (String rank : timeRanks.getKeys(false)) {
                        if (config.isCooldownByRankAutoRanksEnabled()){
                            if (config.getCooldownByRankAutoRanksPermissionPlugin().equalsIgnoreCase("LuckPerms")){
                                if (DependencyManager.isLuckPerms()){
                                    RegisteredServiceProvider<LuckPerms> luckPermsProvider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
                                    if (luckPermsProvider != null) {
                                        LuckPerms luckPerms = luckPermsProvider.getProvider();
                                        User userLuckPerms = luckPerms.getUserManager().getUser(player.getUniqueId());

                                        assert userLuckPerms != null;
                                        String primaryGroup = userLuckPerms.getPrimaryGroup();
                                        if (primaryGroup.equalsIgnoreCase(rank)) cooldownTime = timeRanks.getInt(rank);
                                    }
                                }
                            }
                        } else {
                            String permission = perm.getHomeCooldownRank() + "." + rank;
                            if (player.hasPermission(permission)) {
                                int rankCooldownTime = TimeUtils.timerConverter("ticks", timeRanks.getString(rank));

                                if (config.getCooldownByRankPrioritizeTime().equalsIgnoreCase("SHORTEST")) {
                                    if (rankCooldownTime < cooldownTime) cooldownTime = rankCooldownTime;
                                } else {
                                    if (rankCooldownTime > cooldownTime) cooldownTime = rankCooldownTime;
                                }
                            }
                        }
                    }
                }
            }
        } else if (type.equalsIgnoreCase("tpa")) {
            ConfigTPAManager config = plugin.getMainTPAConfigManager();
            PermissionsManager perm = plugin.getMainPermissionsManager();
            cooldownTime = TimeUtils.timerConverter("ticks", plugin.getMainTPAConfigManager().getCooldownTime());
            if (config.isCooldownByRankEnabled()) {
                ConfigurationSection timeRanks = config.getConfig().getConfigurationSection("cooldown.by_rank.ranks");
                if (timeRanks != null) {
                    for (String rank : timeRanks.getKeys(false)) {
                        if (config.isCooldownByRankAutoRanksEnabled()){
                            if (config.getCooldownByRankAutoRanksPermissionPlugin().equalsIgnoreCase("LuckPerms")){
                                if (DependencyManager.isLuckPerms()){
                                    RegisteredServiceProvider<LuckPerms> luckPermsProvider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
                                    if (luckPermsProvider != null) {
                                        LuckPerms luckPerms = luckPermsProvider.getProvider();
                                        User userLuckPerms = luckPerms.getUserManager().getUser(player.getUniqueId());

                                        assert userLuckPerms != null;
                                        String primaryGroup = userLuckPerms.getPrimaryGroup();
                                        if (primaryGroup.equalsIgnoreCase(rank)) cooldownTime = timeRanks.getInt(rank);
                                    }
                                }
                            }
                        } else {
                            String permission = perm.getTpaCooldownRank() + "." + rank;
                            if (player.hasPermission(permission)) {
                                int rankCooldownTime = TimeUtils.timerConverter("ticks", timeRanks.getString(rank));

                                if (config.getCooldownByRankPrioritizeTime().equalsIgnoreCase("SHORTEST")) {
                                    if (rankCooldownTime < cooldownTime) cooldownTime = rankCooldownTime;
                                } else {
                                    if (rankCooldownTime > cooldownTime) cooldownTime = rankCooldownTime;
                                }
                            }
                        }
                    }
                }
            }
        } else if (type.equalsIgnoreCase("warp")) {
            ConfigWarpsManager config = plugin.getMainWarpConfigManager();
            PermissionsManager perm = plugin.getMainPermissionsManager();
            cooldownTime = TimeUtils.timerConverter("ticks", plugin.getMainWarpConfigManager().getCooldownTime());
            if (config.isCooldownByRankEnabled()) {
                ConfigurationSection timeRanks = config.getConfig().getConfigurationSection("cooldown.by_rank.ranks");
                if (timeRanks != null) {
                    for (String rank : timeRanks.getKeys(false)) {
                        if (config.isCooldownByRankAutoRanksEnabled()){
                            if (config.getCooldownByRankAutoRanksPermissionPlugin().equalsIgnoreCase("LuckPerms")){
                                if (DependencyManager.isLuckPerms()){
                                    RegisteredServiceProvider<LuckPerms> luckPermsProvider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
                                    if (luckPermsProvider != null) {
                                        LuckPerms luckPerms = luckPermsProvider.getProvider();
                                        User userLuckPerms = luckPerms.getUserManager().getUser(player.getUniqueId());

                                        assert userLuckPerms != null;
                                        String primaryGroup = userLuckPerms.getPrimaryGroup();
                                        if (primaryGroup.equalsIgnoreCase(rank)) cooldownTime = timeRanks.getInt(rank);
                                    }
                                }
                            }
                        } else {
                            String permission = perm.getWarpsCooldownRank() + "." + rank;
                            if (player.hasPermission(permission)) {
                                int rankCooldownTime = TimeUtils.timerConverter("ticks", timeRanks.getString(rank));

                                if (config.getCooldownByRankPrioritizeTime().equalsIgnoreCase("SHORTEST")) {
                                    if (rankCooldownTime < cooldownTime) cooldownTime = rankCooldownTime;
                                } else {
                                    if (rankCooldownTime > cooldownTime) cooldownTime = rankCooldownTime;
                                }
                            }
                        }
                    }
                }
            }
        }

        return cooldownTime;
    }
}
