package com.pixesoj.deluxeteleport.managers.dependencies;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.utils.OtherUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Objects;

public class DependencyManager {
    private final boolean paper;
    private static Economy econ = null;

    public DependencyManager() {
        this.paper = checkPaper();
    }

    private static boolean checkFloodgate() {
        try {
            Class.forName("org.geysermc.floodgate.api.FloodgateApi");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static boolean checkNLogin() {
        try {
            Class.forName("com.nickuc.login.api");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static boolean checkPlaceholderAPI() {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null &&
                Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("PlaceholderAPI")).isEnabled();
    }

    private static boolean checkLuckPerms() {
        return Bukkit.getPluginManager().getPlugin("LuckPerms") != null &&
                Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("LuckPerms")).isEnabled();
    }

    private boolean checkPaper() {
        try {
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean isPlaceholderAPI() {
        return checkPlaceholderAPI();
    }

    public static boolean isLuckPerms() {
        return checkLuckPerms();
    }

    public boolean isPaper() {
        return paper;
    }

    public static boolean isEconomy(DeluxeTeleport plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static boolean isNLogin() {
        return checkNLogin();
    }

    public static String getNLoginSoftware(DeluxeTeleport plugin) {
        if (isNLogin()) {
            if (OtherUtils.isRunningOnProxy(plugin)) {
                return "Proxy";
            } else {
                return "Bukkit";
            }
        }
        return null;
    }

    public static boolean isFloodgate() {
        return checkFloodgate();
    }
}
