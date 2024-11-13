package com.pixesoj.deluxeteleport.managers.database;

import com.pixesoj.deluxeteleport.DeluxeTeleport;

import java.io.File;
import java.sql.*;

public class DataDatabase {

    private final String urlData = "jdbc:sqlite:plugins/DeluxeTeleport/data/sql/data.db";
    private DeluxeTeleport plugin;

    public DataDatabase(DeluxeTeleport plugin) {
        this.plugin = plugin;
        createDataTable();
    }

    private void ensureDatabaseDirectoryExists() {
        File databaseDir = new File("plugins/DeluxeTeleport/data/sql");
        if (!databaseDir.exists()) {
            databaseDir.mkdirs();
        }
    }

    public Connection connectData() {
        Connection conn = null;
        try {
            ensureDatabaseDirectoryExists();
            conn = DriverManager.getConnection(urlData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void createDataTable() {
        String sql = "CREATE TABLE IF NOT EXISTS notify_changelog ("
                + "playerName TEXT,"
                + "version TEXT,"
                + "notify BOOLEAN,"
                + "PRIMARY KEY(playerName, version)"
                + ");";

        try (Connection conn = this.connectData();
             Statement stmt = conn.createStatement()) {
            if (conn != null) {
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveData(String playerName, String version, boolean notify) {
        String sql = "INSERT OR REPLACE INTO notify_changelog(playerName, version, notify) VALUES(?, ?, ?)";

        try (Connection conn = connectData();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            pstmt.setString(2, version);
            pstmt.setBoolean(3, notify);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getNotificationStatus(String playerName, String version) {
        String sql = "SELECT notify FROM notify_changelog WHERE playerName = ? AND version = ?";

        try (Connection conn = connectData();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            pstmt.setString(2, version);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("notify");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}