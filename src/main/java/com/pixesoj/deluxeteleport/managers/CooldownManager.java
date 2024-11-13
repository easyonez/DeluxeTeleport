package com.pixesoj.deluxeteleport.managers;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.database.CooldownDatabase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Map;


public class CooldownManager {
    private int TaskID;
    private final DeluxeTeleport plugin;
    private final String player;
    private int time;

    public CooldownManager(DeluxeTeleport plugin, int time, String player) {
        this.plugin = plugin;
        this.player = player;
        this.time = time;
    }

    private static final Map<String, CooldownManager> cooldownLobbyMap = new HashMap<>();
    private static final Map<String, CooldownManager> cooldownSpawnMap = new HashMap<>();
    private static final Map<String, CooldownManager> cooldownHomenMap = new HashMap<>();
    private static final Map<String, CooldownManager> cooldownTpaMap = new HashMap<>();
    private static final Map<String, CooldownManager> cooldownWarpMap = new HashMap<>();

    public void cooldown(String type) {
        CooldownDatabase cooldownDatabase = new CooldownDatabase(plugin);
        if (type.equalsIgnoreCase("lobby")) {
            cooldownLobbyMap.put(player, this);
            BukkitScheduler sh = Bukkit.getServer().getScheduler();
            TaskID = sh.runTaskTimerAsynchronously(plugin, () -> {
                if (time == 0) {
                    Bukkit.getScheduler().cancelTask(TaskID);
                    plugin.removeLobbyCooldown(player);
                    cooldownLobbyMap.remove(player);
                    cooldownDatabase.deleteCooldown(player, "lobby");
                } else {
                    if (plugin.getMainLobbyConfigManager().isCooldownSavePlayerData()) {
                        plugin.addLobbyCooldown(player, time);
                        cooldownDatabase.saveCooldown(player, "lobby", time);
                    }
                    time--;
                }
            }, 0L, 20L).getTaskId();
        } else if (type.equalsIgnoreCase("spawn")) {
            cooldownSpawnMap.put(player, this);
            BukkitScheduler sh = Bukkit.getServer().getScheduler();
            TaskID = sh.runTaskTimerAsynchronously(plugin, () -> {
                if (time == 0) {
                    Bukkit.getScheduler().cancelTask(TaskID);
                    plugin.removeSpawnCooldown(player);
                    cooldownSpawnMap.remove(player);
                    cooldownDatabase.deleteCooldown(player, "spawn");
                } else {
                    if (plugin.getMainSpawnConfigManager().isCooldownSavePlayerData()) {
                        plugin.addSpawnCooldown(player, time);
                        cooldownDatabase.saveCooldown(player, "spawn", time);
                    }
                    time--;
                }
            }, 0L, 20L).getTaskId();
        } else if (type.equalsIgnoreCase("home")) {
            cooldownHomenMap.put(player, this);
            BukkitScheduler sh = Bukkit.getServer().getScheduler();
            TaskID = sh.runTaskTimerAsynchronously(plugin, () -> {
                if (time == 0) {
                    Bukkit.getScheduler().cancelTask(TaskID);
                    plugin.removeHomeCooldown(player);
                    cooldownHomenMap.remove(player);
                    cooldownDatabase.deleteCooldown(player, "home");
                } else {
                    if (plugin.getMainHomeConfigManager().isCooldownSavePlayerData()) {
                        plugin.addHomeCooldown(player, time);
                        cooldownDatabase.saveCooldown(player, "home", time);
                    }
                    time--;
                }
            }, 0L, 20L).getTaskId();
        } else if (type.equalsIgnoreCase("tpa")) {
            cooldownTpaMap.put(player, this);
            BukkitScheduler sh = Bukkit.getServer().getScheduler();
            TaskID = sh.runTaskTimerAsynchronously(plugin, () -> {
                if (time == 0) {
                    Bukkit.getScheduler().cancelTask(TaskID);
                    plugin.removeTpaCooldown(player);
                    cooldownTpaMap.remove(player);
                    cooldownDatabase.deleteCooldown(player, "tpa");
                } else {
                    if (plugin.getMainTPAConfigManager().isCooldownSavePlayerData()) {
                        plugin.addTpaCooldown(player, time);
                        cooldownDatabase.saveCooldown(player, "tpa", time);
                    }
                    time--;
                }
            }, 0L, 20L).getTaskId();
        } else if (type.equalsIgnoreCase("warp")) {
            cooldownWarpMap.put(player, this);
            BukkitScheduler sh = Bukkit.getServer().getScheduler();
            TaskID = sh.runTaskTimerAsynchronously(plugin, () -> {
                if (time == 0) {
                    Bukkit.getScheduler().cancelTask(TaskID);
                    plugin.removeWarpCooldown(player);
                    cooldownWarpMap.remove(player);
                    cooldownDatabase.deleteCooldown(player, "warp");
                } else {
                    if (plugin.getMainWarpConfigManager().isCooldownSavePlayerData()) {
                        plugin.addWarpCooldown(player, time);
                        cooldownDatabase.saveCooldown(player, "warp", time);
                    }
                    time--;
                }
            }, 0L, 20L).getTaskId();
        }
    }

    public int getRemainingTime() {
        return time;
    }

    public static int getRemainingLobbyTime(Player player) {
        CooldownManager cooldownLobby = cooldownLobbyMap.get(player.getName());
        return (cooldownLobby != null) ? cooldownLobby.getRemainingTime() : 0;
    }

    public static int getRemainingSpawnTime(Player player) {
        CooldownManager cooldownSpawn = cooldownSpawnMap.get(player.getName());
        return (cooldownSpawn != null) ? cooldownSpawn.getRemainingTime() : 0;
    }

    public static int getRemainingHomeTime(Player player) {
        CooldownManager cooldownHome = cooldownHomenMap.get(player.getName());
        return (cooldownHome != null) ? cooldownHome.getRemainingTime() : 0;
    }

    public static int getRemainingTpaTime(Player player) {
        CooldownManager cooldownTpa = cooldownTpaMap.get(player.getName());
        return (cooldownTpa != null) ? cooldownTpa.getRemainingTime() : 0;
    }

    public static int getRemainingWarpTime(Player player) {
        CooldownManager cooldownWarp = cooldownWarpMap.get(player.getName());
        return (cooldownWarp != null) ? cooldownWarp.getRemainingTime() : 0;
    }

    public static void stopCooldown(String type, String playerName) {
        CooldownManager cooldownManager;
        if (type.equalsIgnoreCase("lobby")) {
            cooldownManager = cooldownLobbyMap.get(playerName);
            if (cooldownManager != null) {
                Bukkit.getScheduler().cancelTask(cooldownManager.TaskID);
                cooldownLobbyMap.remove(playerName);
            }
        } else if (type.equalsIgnoreCase("spawn")) {
            cooldownManager = cooldownSpawnMap.get(playerName);
            if (cooldownManager != null) {
                Bukkit.getScheduler().cancelTask(cooldownManager.TaskID);
                cooldownSpawnMap.remove(playerName);
            }
        } else if (type.equalsIgnoreCase("home")) {
            cooldownManager = cooldownHomenMap.get(playerName);
            if (cooldownManager != null) {
                Bukkit.getScheduler().cancelTask(cooldownManager.TaskID);
                cooldownHomenMap.remove(playerName);
            }
        } else if (type.equalsIgnoreCase("tpa")) {
            cooldownManager = cooldownTpaMap.get(playerName);
            if (cooldownManager != null) {
                Bukkit.getScheduler().cancelTask(cooldownManager.TaskID);
                cooldownTpaMap.remove(playerName);
            }
        } else if (type.equalsIgnoreCase("warp")) {
            cooldownManager = cooldownWarpMap.get(playerName);
            if (cooldownManager != null) {
                Bukkit.getScheduler().cancelTask(cooldownManager.TaskID);
                cooldownWarpMap.remove(playerName);
            }
        }
    }
}
