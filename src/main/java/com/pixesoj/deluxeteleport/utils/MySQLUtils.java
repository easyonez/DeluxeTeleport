package com.pixesoj.deluxeteleport.utils;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import org.bukkit.Bukkit;

import java.sql.*;

public class MySQLUtils {

    private DeluxeTeleport plugin;
    private Connection connection;
    com.pixesoj.deluxeteleport.managers.MessagesManager m;
    MessagesFileManager msg = plugin.getMainMessagesManager();

    public void coloredMessage(String text) {
        Bukkit.getConsoleSender().sendMessage(MessagesManager.getColoredMessage(text));
    }

    public MySQLUtils(DeluxeTeleport plugin) {
        this.plugin = plugin;
        this.m = new com.pixesoj.deluxeteleport.managers.MessagesManager("&8[&bDeluxeTeleport&8] ", plugin);
        String dataType = plugin.getMainConfigManager().getDataType();
        if (!dataType.equals("MySQL")){
            return;
        }
        connectToDatabase();
        createTablesIfNotExists();
    }

    private void connectToDatabase() {
        try {
            if (connection != null && !connection.isClosed()) {
                m.sendMessage(Bukkit.getConsoleSender(), msg.getMySQLErrorConnecting(), true);
                return;
            }

            String address = plugin.getMainConfigManager().getDataAddress();
            int port = plugin.getMainConfigManager().getDataPort();
            String database = plugin.getMainConfigManager().getDatabase();
            String user = plugin.getMainConfigManager().getDataUserName();
            String password = plugin.getMainConfigManager().getDataPassword();

            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://" + address + ":" + port + "/" + database, user, password);

            m.sendMessage(Bukkit.getConsoleSender(), msg.getMySQLConnecting(),true);
        } catch (SQLException | ClassNotFoundException e) {
            m.sendMessage(Bukkit.getConsoleSender(), msg.getMySQLError(),true);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private void createTablesIfNotExists() {
        try (Statement statement = connection.createStatement()) {
            String tableName = plugin.getMainConfigManager().getDataTableName();
            ResultSet resultSet = connection.getMetaData().getTables(null, null, tableName, null);
            if (!resultSet.next()) {
                //createTables();
            }
        } catch (SQLException e) {
            m.sendMessage(Bukkit.getConsoleSender(), msg.getMySQLErrorTables(),true);
        }
    }

    private void createTables(String location) {
        try (Statement statement = connection.createStatement()) {
            String tableName = plugin.getMainConfigManager().getDataTableName();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                    "UUID VARCHAR(36) PRIMARY KEY," +
                    "PlayerName VARCHAR(255)," +
                    "Homes JSON)");

        } catch (SQLException e) {
            m.sendMessage(Bukkit.getConsoleSender(), msg.getMySQLErrorTables(),true);
        }
    }

    private void handleException(Exception e, String message) {
        e.printStackTrace();
        m.sendMessage(Bukkit.getConsoleSender(), "&c" + message + ": " + e.getMessage(),true);
    }
}