package com.pixesoj.deluxeteleport.utils;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.CheckEnabledManager;
import com.pixesoj.deluxeteleport.managers.LocationsManager;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocationUtils {
    public static void setLocation(DeluxeTeleport plugin, CommandSender sender, String location, String[] args, boolean isGeneral){
        MessagesFileManager msg = plugin.getMainMessagesManager();
        MessagesManager m;
        String keyPath = "";
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        Location l = ((Player) sender).getLocation();
        if (location.equalsIgnoreCase("lobby")){
            m = new MessagesManager(plugin.getMainMessagesManager().getPrefixLobby(), plugin);
            if (CheckEnabledManager.lobbyMultiple(plugin) && args.length > 0){
                keyPath = "Lobby.Multiple." + args[0] + ".";
            } else if (isGeneral){
                keyPath = "Lobby.General.";
            }

            String world = ((Player) sender).getWorld().getName();
            String message = "";
            if (CheckEnabledManager.lobbyMultiple(plugin) && !isGeneral) {
                message = msg.getLobbyEstablished()
                        .replace("%world%", world)
                        .replace("%lobby%", String.valueOf(args[0]));
            } else {
                message = msg.getLobbyEstablished()
                        .replace("%world%", world)
                        .replace("%lobby%", "general");
            }
            m.sendMessage(sender, message, true);
        } else if (location.equalsIgnoreCase("spawn")){
            m = new MessagesManager(plugin.getMainMessagesManager().getPrefixSpawn(), plugin);
            if (args.length > 0 && CheckEnabledManager.floodgateSupport(plugin, "spawn") && plugin.getMainSpawnConfigManager().isFloodgateSplitSpawn()) {
                if (args[0].equalsIgnoreCase("java")){
                    keyPath = "Spawn.General.Java.";
                } else if (args[0].equalsIgnoreCase("bedrock")) {
                    keyPath = "Spawn.General.Bedrock.";
                }
            } else if (CheckEnabledManager.spawnByWorld(plugin)){
                Player player = (Player) sender;
                keyPath = "Spawn.ByWorld." + player.getWorld().getName() + ".";
            } else if (isGeneral){
                keyPath = "Spawn.General.Global.";
            }

            String world = ((Player) sender).getWorld().getName();
            String message = "";
            if (CheckEnabledManager.spawnByWorld(plugin)) {
                message = msg.getSpawnEstablished()
                        .replace("%world%", world);
            } else {
                message = msg.getSpawnEstablished()
                        .replace("%world%", world)
                        .replace("%spawn%", "general");
            }
            m.sendMessage(sender, message, true);
        } else if (location.equalsIgnoreCase("warp")){
            m = new MessagesManager(plugin.getMainMessagesManager().getPrefixWarp(), plugin);
            if (args.length > 0) {
                keyPath = "warps." + args[0] + ".";
            } else {
                m.sendMessage(sender, msg.getWarpSetError(), true);
                return;
            }

            m.sendMessage(sender, msg.getWarpSetSuccessfully().replace("%warp%", args[0]), true);
        }

        locations.set(keyPath + "world", Objects.requireNonNull(l.getWorld()).getName());
        locations.set(keyPath + "x", l.getX());
        locations.set(keyPath + "y", l.getY());
        locations.set(keyPath + "z", l.getZ());
        locations.set(keyPath + "yaw", l.getYaw());
        locations.set(keyPath + "pitch", l.getPitch());
        plugin.getLocationsManager().saveLocationsFile();
    }

    public static Location getLocation(DeluxeTeleport plugin, String location, String type, String multiple){
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        double x = 0;
        double y = 0;
        double z = 0;
        float yaw = 0;
        float pitch = 0;
        String world = null;
        String keyPath;
        if (location.equalsIgnoreCase("lobby")){
            if (type.equalsIgnoreCase("general")) {
                keyPath = "Lobby.General.";
            } else if (type.equalsIgnoreCase("multiple")) {
                keyPath = "Lobby.Multiple." + multiple + ".";
            } else {
                return null;
            }
            if (!locations.contains(keyPath)){
                return null;
            }
            x = locations.getDouble(keyPath + "x");
            y = locations.getDouble(keyPath + "y");
            z = locations.getDouble(keyPath + "z");
            yaw = (float) locations.getDouble(keyPath + "yaw");
            pitch = (float) locations.getDouble(keyPath + "pitch");
            world = locations.getString(keyPath + "world");
        } else if (location.equalsIgnoreCase("spawn")){
            if (type.equalsIgnoreCase("general")) {
                keyPath = "Spawn.General.Global.";
            } else if (type.equalsIgnoreCase("byworld")) {
                keyPath = "Spawn.ByWorld." + multiple + ".";
            } else {
                return null;
            }
            if (!locations.contains(keyPath)){
                return null;
            }

            x = locations.getDouble(keyPath + "x");
            y = locations.getDouble(keyPath + "y");
            z = locations.getDouble(keyPath + "z");
            yaw = (float) locations.getDouble(keyPath + "yaw");
            pitch = (float) locations.getDouble(keyPath + "pitch");
            world = locations.getString(keyPath + "world");
        }
        assert world != null;
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public static boolean isNull (DeluxeTeleport plugin, CommandSender sender, Location location, String type, String multiple, boolean senMessage){
        MessagesFileManager msg = plugin.getMainMessagesManager();
        if (location == null){
            if (senMessage) {
                switch (type) {
                    case "lobby": {
                        if (!plugin.getMainLobbyConfigManager().isMultipleLobbies()){
                            multiple = "general";
                        }
                        com.pixesoj.deluxeteleport.managers.MessagesManager m = new com.pixesoj.deluxeteleport.managers.MessagesManager(plugin.getMainMessagesManager().getPrefixLobby(), plugin);
                        m.sendMessage(sender, msg.getLobbyNotExists()
                                .replace("%lobby%", multiple), true);
                        break;
                    }
                    case "spawn": {
                        if (!plugin.getMainSpawnConfigManager().isByWorld()){
                            multiple = "general";
                        }
                        com.pixesoj.deluxeteleport.managers.MessagesManager m = new com.pixesoj.deluxeteleport.managers.MessagesManager(plugin.getMainMessagesManager().getPrefixSpawn(), plugin);
                        m.sendMessage(sender, msg.getSpawnNotExists()
                                .replace("%spawn%", multiple), true);
                        break;
                    }
                    default: {
                    }
                }
            }
            return true;
        }
        return false;
    }

    public static boolean keyPathExist(DeluxeTeleport plugin, CommandSender sender, String location, String type, String multiple, boolean sendMessage){
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        MessagesFileManager msg = plugin.getMainMessagesManager();
        MessagesManager m;
        if (location.equalsIgnoreCase("lobby")){
            m = new MessagesManager(plugin.getMainMessagesManager().getPrefixLobby(), plugin);
            if (type.equalsIgnoreCase("general")){
                if (!locations.contains("Lobby.General")){
                    if (sendMessage){
                        m.sendMessage(sender, msg.getLobbyNotExists()
                                .replace("%lobby%", "general"), true);
                    }
                    return false;
                } else {
                    return true;
                }
            } else if (type.equalsIgnoreCase("multiple")) {
                if (!locations.contains("Lobby.Multiple." + multiple)){
                    if (sendMessage){
                        m.sendMessage(sender, msg.getLobbyNotExists()
                                .replace("%lobby%", multiple), true);
                    }
                    return false;
                } else {
                    return true;
                }
            }
        } else if (location.equalsIgnoreCase("spawn")){
            m = new MessagesManager(plugin.getMainMessagesManager().getPrefixSpawn(), plugin);
            if (type.equalsIgnoreCase("general")){
                if (!locations.contains("Spawn.General.Global")){
                    if (sendMessage){
                        m.sendMessage(sender, msg.getSpawnNotExists()
                                .replace("%spawn%", "general"), true);
                    }
                    return false;
                } else {
                    return true;
                }
            } else if (type.equalsIgnoreCase("byworld")) {
                if (!locations.contains("Spawn.ByWorld." + multiple)){
                    if (sendMessage){
                        m.sendMessage(sender, msg.getSpawnNotExists()
                                .replace("%spawn%", multiple), true);
                    }
                    return false;
                } else {
                    return true;
                }
            }
        } else if (location.equalsIgnoreCase("warp")){
            m = new MessagesManager(plugin.getMainMessagesManager().getPrefixSpawn(), plugin);
            if (!locations.contains("warps." + multiple)){
                if (sendMessage){
                    m.sendMessage(sender, msg.getWarpNotExist()
                            .replace("%warp%", multiple), true);
                }
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public static Location stringToLocation(String locationString) {
        if (locationString == null || locationString.isEmpty()) {
            return null;
        }

        String[] parts = locationString.split(":");
        if (parts.length != 4) {
            return null;
        }

        String worldName = parts[0].trim();
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            return null;
        }

        try {
            double x = Double.parseDouble(parts[1].trim());
            double y = Double.parseDouble(parts[2].trim());
            double z = Double.parseDouble(parts[3].trim());

            return new Location(world, x, y, z);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
