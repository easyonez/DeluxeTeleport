package com.pixesoj.deluxeteleport.managers.database;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.*;

public class CooldownDatabase {

    private final String urlCooldown = "jdbc:sqlite:plugins/DeluxeTeleport/data/sql/cooldowns.db";
    private DeluxeTeleport plugin;

    public CooldownDatabase (DeluxeTeleport plugin) {
        this.plugin = plugin;
    }

    private void ensureDatabaseDirectoryExists() {
        File databaseDir = new File("plugins/DeluxeTeleport/data/sql");
        if (!databaseDir.exists()) {
            databaseDir.mkdirs();
        }
    }

    public Connection connectCooldown() {
        Connection conn = null;
        try {
            ensureDatabaseDirectoryExists();
            conn = DriverManager.getConnection(urlCooldown);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void createCooldownTable(String cooldownType) {
        String sql = "CREATE TABLE IF NOT EXISTS " + cooldownType + " ("
                + "player TEXT UNIQUE,"
                + "type TEXT,"
                + "time INTEGER"
                + ");";

        try (Connection conn = this.connectCooldown();
             Statement stmt = conn.createStatement()) {
            if (conn != null) {
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadCooldowns(String cooldownType) {
        String sql = "SELECT player, type, time FROM " + cooldownType;

        createCooldownTable(cooldownType);
        try (Connection conn = connectCooldown();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String playerName = rs.getString("player");
                String type = rs.getString("type");
                int remainingTime = rs.getInt("time");

                if (type.equalsIgnoreCase(cooldownType)) {
                    plugin.startCooldown(playerName, cooldownType, remainingTime);
                    switch (cooldownType){
                        case "tpa":
                            plugin.addTpaCooldown(playerName, remainingTime);
                        case "lobby":
                            plugin.addLobbyCooldown(playerName, remainingTime);
                        case "spawn":
                            plugin.addSpawnCooldown(playerName, remainingTime);
                        case "home":
                            plugin.addHomeCooldown(playerName, remainingTime);
                        case "warp":
                            plugin.addHomeCooldown(playerName, remainingTime);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCooldown(String playerName, String cooldownType) {
        String sqlCheckTable = "SELECT 1 FROM " + cooldownType + " LIMIT 1";
        String sqlDelete = "DELETE FROM " + cooldownType + " WHERE player = ?";

        try (Connection conn = connectCooldown()) {
            try (PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheckTable)) {
                pstmtCheck.executeQuery();
            } catch (SQLException e) {
                return;
            }

            try (PreparedStatement pstmtDelete = conn.prepareStatement(sqlDelete)) {
                pstmtDelete.setString(1, playerName);
                pstmtDelete.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveCooldown(String playerName, String type, int remainingTime) {
        createCooldownTable(type);
        String sql = "INSERT OR REPLACE INTO " + type + "(player, type, time) VALUES(?, ?, ?)";

        try (Connection conn = connectCooldown();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            pstmt.setString(2, type);
            pstmt.setInt(3, remainingTime);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}