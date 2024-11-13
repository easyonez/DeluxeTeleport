package com.pixesoj.deluxeteleport.managers;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class MySQL {

    public static boolean playerExists (Connection connection, UUID uuid, DeluxeTeleport plugin){
        try {
            String tableName = plugin.getMainConfigManager().getDataTableName();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE (UUID=?)");
            statement.setString(1, uuid.toString());
            ResultSet result = statement.executeQuery();
            if (result.next()){
                return true;
            }
        }

        catch (SQLException ignored){ }
        return false;
    }

    public static void playerCreate(Connection connection, UUID uuid, Player player, DeluxeTeleport plugin) {
        try {
            if (playerExists(connection, uuid, plugin)) {
                String tableName = plugin.getMainConfigManager().getDataTableName();
                PreparedStatement updateStatement = connection.prepareStatement("UPDATE " + tableName + " SET X=?, Y=?, Z=?, Yaw=?, Pitch=?, World=? WHERE UUID=?");

                double x = player.getLocation().getX();
                double y = player.getLocation().getY();
                double z = player.getLocation().getZ();
                float yaw = player.getLocation().getYaw();
                float pitch = player.getLocation().getPitch();
                String world = player.getWorld().getName();

                updateStatement.setDouble(1, x);
                updateStatement.setDouble(2, y);
                updateStatement.setDouble(3, z);
                updateStatement.setFloat(4, yaw);
                updateStatement.setFloat(5, pitch);
                updateStatement.setString(6, world);
                updateStatement.setString(7, uuid.toString());

                updateStatement.executeUpdate();
            } else {
                String tableName = plugin.getMainConfigManager().getDataTableName();
                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO " + tableName + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

                double x = player.getLocation().getX();
                double y = player.getLocation().getY();
                double z = player.getLocation().getZ();
                float yaw = player.getLocation().getYaw();
                float pitch = player.getLocation().getPitch();
                String world = player.getWorld().getName();

                insertStatement.setString(1, uuid.toString());
                insertStatement.setString(2, player.getName());
                insertStatement.setString(3, world);
                insertStatement.setDouble(4, x);
                insertStatement.setDouble(5, y);
                insertStatement.setDouble(6, z);
                insertStatement.setFloat(7, yaw);
                insertStatement.setFloat(8, pitch);

                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getLastLocation(Connection connection, UUID uuid, Player player, DeluxeTeleport plugin) {
        try {
            if (playerExists(connection, uuid, plugin)) {
                String tableName = plugin.getMainConfigManager().getDataTableName();
                PreparedStatement getStatement = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE (UUID = ?)");
                getStatement.setString(1, uuid.toString());
                ResultSet result = getStatement.executeQuery();

                if (result.next()) {
                    String world = result.getString("world");
                    double x = result.getDouble("x");
                    double y = result.getDouble("y");
                    double z = result.getDouble("z");
                    float yaw = result.getFloat("yaw");
                    float pitch = result.getFloat("pitch");

                    Location location = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
                    player.teleport(location);
                }
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
