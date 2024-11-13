package com.pixesoj.deluxeteleport.managers.filesmanager;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenusFileManager {

    private final DeluxeTeleport plugin;
    private List<String> allCommands;
    private Map<String, String> commandToFileMap = new HashMap<>();

    public MenusFileManager(DeluxeTeleport plugin) {
        this.plugin = plugin;
        this.allCommands = new ArrayList<>();
        loadAllCommands();
    }

    public void loadAllCommands() {
        File folder = new File(plugin.getDataFolder(), "gui_menus");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".yml")) {
                FileManager configFile = new FileManager(file.getName(), "gui_menus", plugin);
                configFile.registerFile();
                FileConfiguration config = configFile.getConfig();

                if (config.getBoolean("enabled", true)) {
                    if (config.isList("open_commands")) {
                        List<String> commands = config.getStringList("open_commands");
                        for (String command : commands) {
                            commandToFileMap.put(command.toLowerCase(), file.getName());
                        }
                    } else if (config.isString("open_commands")) {
                        String command = config.getString("open_commands");
                        commandToFileMap.put(command.toLowerCase(), file.getName());
                    }
                }
            }
        }
    }

    public boolean isCommandInMenus(String command) {
        return allCommands.contains(command);
    }

    public void reloadMenus() {
        allCommands.clear();
        commandToFileMap.clear();

        loadAllCommands();
    }

    public Map<String, String> getCommandToFileMap(){
        return commandToFileMap;
    }

    public FileConfiguration getConfig(String fileName) {
        File folder = new File(plugin.getDataFolder(), "gui_menus");


        if (!folder.exists()) {
            folder.mkdirs();
        }

        File[] files = folder.listFiles();
        if (files == null) {
            return null;
        }

        for (File file : files) {
            if (file.isFile() && file.getName().equals(fileName) && file.getName().endsWith(".yml")) {
                FileManager configFile = new FileManager(file.getName(), "gui_menus", plugin);
                configFile.registerFile();

                return configFile.getConfig();
            }
        }

        return null;
    }
}