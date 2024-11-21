package com.pixesoj.deluxeteleport.utils;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class LocationUtils {
    public static Location getLocation(DeluxeTeleport plugin, String location, String type, String multiple){
        FileConfiguration locations = plugin.getLocationsManager().getLocationsConfig();
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

    public static Location getDestinationPlace(DeluxeTeleport plugin, CommandSender sender, String destinationPlace, String destination){
        if (sender instanceof Player) {
            Player player = (Player) sender;
            destinationPlace = PlaceholderUtils.setPlaceholders(plugin, player, destinationPlace);
            destination = PlaceholderUtils.setPlaceholders(plugin, player, destination);
        }
        MessagesFileManager msg = plugin.getMainMessagesManager();
        MessagesManager m;
        FileManager fileManager;
        FileConfiguration file = null;
        UUID worldUUID;
        World world = null;

        switch (destinationPlace.toLowerCase()) {
            case "lobby":
                ConfigLobbyManager configLobby = plugin.getMainLobbyConfigManager();
                m = new MessagesManager(msg.getPrefixLobby(), plugin);
                String lobbyName = (configLobby.isMultipleLobbies() && destination != null && !destination.isEmpty()) ? destination : "general-lobby";
                fileManager = new FileManager(lobbyName + ".yml", "data/lobbies", false, plugin);
                file = fileManager.getConfig();
                if (file == null || file.getString("world") == null) return null;
                worldUUID = UUID.fromString(Objects.requireNonNull(file.getString("world")));
                world = Bukkit.getWorld(worldUUID);
                if (world == null) {
                    //m.sendMessage(sender, msg.getLobbyExeption().replace("%lobby%", lobbyName), true);
                    return null;
                }
                break;
            case "spawn":
                ConfigSpawnManager configSpawn = plugin.getMainSpawnConfigManager();
                m = new MessagesManager(msg.getPrefixSpawn(), plugin);
                String spawnName = (configSpawn.isByWorld() && destination != null && !destination.isEmpty()) ? destination : "general-spawn";
                fileManager = new FileManager(spawnName + ".yml", "data/spawns", false, plugin);
                file = fileManager.getConfig();
                if (file == null || file.getString("world") == null) return null;
                worldUUID = UUID.fromString(Objects.requireNonNull(file.getString("world")));
                world = Bukkit.getWorld(worldUUID);
                if (world == null) {
                    m.sendMessage(sender, msg.getSpawnExeption().replace("%spawn%", spawnName), true);
                    return null;
                }
                break;
            case "warp":
                m = new MessagesManager(msg.getPrefixWarp(), plugin);
                fileManager = new FileManager( destination + ".yml", "data/warps", false, plugin);
                file = fileManager.getConfig();
                if (file == null || file.getString("world") == null) return null;
                worldUUID = UUID.fromString(Objects.requireNonNull(file.getString("world")));
                world = Bukkit.getWorld(worldUUID);
                if (world == null) {
                    m.sendMessage(sender, msg.getWarpExeption().replace("%warp%", destination), true);
                    return null;
                }
                break;
        }

        if (file == null) return null;
        double x = file.getDouble("x");
        double y = file.getDouble("y");
        double z = file.getDouble("z");
        float yaw = file.getInt("yaw");
        float pitch = file.getInt("pitch");
        return new Location(world, x, y, z, yaw, pitch);
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
