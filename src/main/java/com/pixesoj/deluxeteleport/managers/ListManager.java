package com.pixesoj.deluxeteleport.managers;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListManager {
    private DeluxeTeleport plugin;

    private ArrayList<String> delayPlayers;
    private ArrayList<String> cooldownLobbyPlayers;
    private ArrayList<String> cooldownSpawnPlayers;
    private ArrayList<String> lastLocationOneTime;
    private static final Map<String, List<String>> tpaPlayersMap = new HashMap<>();
    private static final Map<String, List<String>> myTpaPlayersMap = new HashMap<>();
    private static final Map<String, List<String>> homePlayersMap = new HashMap<>();
    private static final List<String> tpaTeleportPlayers = new ArrayList<>();


    public ListManager (DeluxeTeleport plugin){
        this.plugin = plugin;
    }

    public static void addTpa(Player player, String targetPlayer) {
        List<String> tpaList = tpaPlayersMap.getOrDefault(player.getName(), new ArrayList<>());
        tpaList.add(targetPlayer);
        tpaPlayersMap.put(player.getName(), tpaList);
    }

    public static void addMyTpa(Player player, String targetPlayer) {
        List<String> tpaList = myTpaPlayersMap.getOrDefault(player.getName(), new ArrayList<>());
        tpaList.add(targetPlayer);
        myTpaPlayersMap.put(player.getName(), tpaList);
    }

    public static void removeTpa(Player player, String targetPlayer) {
        List<String> tpaList = tpaPlayersMap.getOrDefault(player.getName(), new ArrayList<>());
        tpaList.remove(targetPlayer);
        tpaPlayersMap.put(player.getName(), tpaList);
    }

    public static void removeMyTpa(Player player, String targetPlayer) {
        List<String> tpaList = myTpaPlayersMap.getOrDefault(player.getName(), new ArrayList<>());
        tpaList.remove(targetPlayer);
        myTpaPlayersMap.put(player.getName(), tpaList);
    }

    public static boolean isTpa(Player player, String targetPlayer) {
        List<String> tpaList = tpaPlayersMap.getOrDefault(player.getName(), new ArrayList<>());
        return tpaList.contains(targetPlayer);
    }

    public static boolean isTpaEmpty(Player player) {
        List<String> tpaList = tpaPlayersMap.getOrDefault(player.getName(), new ArrayList<>());
        return tpaList.isEmpty();
    }

    public static String getLastTpaPlayer(Player player) {
        List<String> tpaList = tpaPlayersMap.getOrDefault(player.getName(), new ArrayList<>());
        if (!tpaList.isEmpty()) {
            return tpaList.get(tpaList.size() - 1);
        } else {
            return null;
        }
    }

    public static String getMyLastTpaPlayer(Player player) {
        List<String> tpaList = myTpaPlayersMap.getOrDefault(player.getName(), new ArrayList<>());
        if (!tpaList.isEmpty()) {
            return tpaList.get(tpaList.size() - 1);
        } else {
            return null;
        }
    }

    public static String getFirstTpaPlayer(Player player) {
        List<String> tpaList = tpaPlayersMap.getOrDefault(player.getName(), new ArrayList<>());
        if (!tpaList.isEmpty()) {
            return tpaList.get(0);
        } else {
            return null;
        }
    }

    public static String getFirstMyTpaPlayer(Player player) {
        List<String> tpaList = myTpaPlayersMap.getOrDefault(player.getName(), new ArrayList<>());
        if (!tpaList.isEmpty()) {
            return tpaList.get(0);
        } else {
            return null;
        }
    }

    public static boolean isPlayerInTpaList(Player player, String targetPlayer) {
        List<String> tpaList = tpaPlayersMap.getOrDefault(player.getName(), new ArrayList<>());
        return tpaList.contains(targetPlayer);
    }

    public static boolean isPlayerInMyTpaList(Player player, String targetPlayer) {
        List<String> tpaList = myTpaPlayersMap.getOrDefault(player.getName(), new ArrayList<>());
        return tpaList.contains(targetPlayer);
    }

    public static void addTpaTeleport(Player player) {
        if (!tpaTeleportPlayers.contains(player.getName())) {
            tpaTeleportPlayers.add(player.getName());
        }
    }

    public static void removeTpaTeleport(Player player) {
        tpaTeleportPlayers.remove(player.getName());
    }

    public static boolean isInTpaTeleport(Player player) {
        return tpaTeleportPlayers.contains(player.getName());
    }
}
