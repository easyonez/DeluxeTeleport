package com.pixesoj.deluxeteleport.managers.commands;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.commands.MainCmd;
import com.pixesoj.deluxeteleport.commands.home.DelHomeCmd;
import com.pixesoj.deluxeteleport.commands.home.HomeCmd;
import com.pixesoj.deluxeteleport.commands.home.HomesCmd;
import com.pixesoj.deluxeteleport.commands.home.SetHomeCmd;
import com.pixesoj.deluxeteleport.commands.lobby.DelLobbyCmd;
import com.pixesoj.deluxeteleport.commands.lobby.LobbyCmd;
import com.pixesoj.deluxeteleport.commands.lobby.SetLobbyCmd;
import com.pixesoj.deluxeteleport.commands.spawn.DelSpawnCmd;
import com.pixesoj.deluxeteleport.commands.spawn.SetSpawnCmd;
import com.pixesoj.deluxeteleport.commands.spawn.SpawnCmd;
import com.pixesoj.deluxeteleport.commands.spawn.SpawnsCmd;
import com.pixesoj.deluxeteleport.commands.tpa.*;
import com.pixesoj.deluxeteleport.commands.warps.DelWarpCmd;
import com.pixesoj.deluxeteleport.commands.warps.SetWarpCmd;
import com.pixesoj.deluxeteleport.commands.warps.WarpCmd;
import com.pixesoj.deluxeteleport.commands.warps.WarpsCmd;
import com.pixesoj.deluxeteleport.commands.world.TpWorldCMD;
import com.pixesoj.deluxeteleport.managers.TabCompleterManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.MenusFileManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public class CommandRegisterManager {

    private final DeluxeTeleport plugin;

    public CommandRegisterManager(DeluxeTeleport plugin) {
        this.plugin = plugin;
    }

    private CommandMap getCommandMap() {
        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            return (CommandMap) field.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void registerCommands() {
        registerCommandsFromConfig(plugin.getMainConfigManager().getConfig(), MainCmd.class);
        registerCommand("deluxeteleport", MainCmd.class);
        registerCommand("tpworld", TpWorldCMD.class);

        if (plugin.getMainLobbyConfigManager().isEnabled() && !plugin.getMainLobbyConfigManager().isForceDisable()){
            registerCommandsFromConfig(plugin.getMainLobbyConfigManager().getConfig(), LobbyCmd.class);

            registerCommand("lobby", LobbyCmd.class);
            registerCommand("setlobby", SetLobbyCmd.class);
            registerCommand("dellobby", DelLobbyCmd.class);
        }

        if (plugin.getMainSpawnConfigManager().isEnabled() && !plugin.getMainSpawnConfigManager().isForceDisable()){
            registerCommandsFromConfigList(plugin.getMainSpawnConfigManager().getConfig(), "commands_alias.spawn", SpawnCmd.class);
            registerCommandsFromConfigList(plugin.getMainSpawnConfigManager().getConfig(), "commands_alias.spawns", SpawnsCmd.class);

            registerCommand("spawn", SpawnCmd.class);
            registerCommand("spawns", SpawnsCmd.class);
            registerCommand("setspawn", SetSpawnCmd.class);
            registerCommand("delspawn", DelSpawnCmd.class);
        }

        if (plugin.getMainTPAConfigManager().isEnabled() && !plugin.getMainTPAConfigManager().isForceDisable()){
            registerCommandsFromConfigList(plugin.getMainTPAConfigManager().getConfig(), "commands_alias.tpa", TpaCMD.class);
            registerCommandsFromConfigList(plugin.getMainTPAConfigManager().getConfig(), "commands_alias.tpaccept", TpaCancelCMD.class);
            registerCommandsFromConfigList(plugin.getMainTPAConfigManager().getConfig(), "commands_alias.tpacancel", TpAcceptCMD.class);
            registerCommandsFromConfigList(plugin.getMainTPAConfigManager().getConfig(), "commands_alias.tpadeny", TpaDenyCMD.class);
            registerCommandsFromConfigList(plugin.getMainTPAConfigManager().getConfig(), "commands_alias.tpahere", TpaHereCMD.class);
            registerCommandsFromConfigList(plugin.getMainTPAConfigManager().getConfig(), "commands_alias.tpatoggle", TpaToggleCMD.class);

            registerCommand("tpa", TpaCMD.class);
            registerCommand("tpacancel", TpaCancelCMD.class);
            registerCommand("tpadeny", TpaDenyCMD.class);
            registerCommand("tpaccept", TpAcceptCMD.class);
            registerCommand("tpahere", TpaHereCMD.class);
            registerCommand("tpatoggle", TpaToggleCMD.class);
        }

        if (plugin.getMainHomeConfigManager().isEnabled() && !plugin.getMainHomeConfigManager().isForceDisable()){
            registerCommandsFromConfigList(plugin.getMainHomeConfigManager().getConfig(), "commands_alias.home", HomeCmd.class);
            registerCommandsFromConfigList(plugin.getMainHomeConfigManager().getConfig(), "commands_alias.homes", HomesCmd.class);
            registerCommandsFromConfigList(plugin.getMainHomeConfigManager().getConfig(), "commands_alias.sethome", SetHomeCmd.class);
            registerCommandsFromConfigList(plugin.getMainHomeConfigManager().getConfig(), "commands_alias.delhome", DelHomeCmd.class);

            registerCommand("home", HomeCmd.class);
            registerCommand("homes", HomesCmd.class);
            registerCommand("sethome", SetHomeCmd.class);
            registerCommand("delhome", DelHomeCmd.class);
        }

        if (plugin.getMainMenuConfigManager().isRegisterCommands()) {
            MenusFileManager menusFileManager = plugin.getMainMenuManager();

            Map<String, String> commandToFileMap = menusFileManager.getCommandToFileMap();

            for (Map.Entry<String, String> entry : commandToFileMap.entrySet()) {
                String command = entry.getKey();
                registerCommand(command);
            }
        }

        if (plugin.getMainWarpConfigManager().isEnabled() && !plugin.getMainWarpConfigManager().isForceDisable()){
            registerCommandsFromConfigList(plugin.getMainWarpConfigManager().getConfig(), "commands_alias.warp", WarpCmd.class);
            registerCommandsFromConfigList(plugin.getMainWarpConfigManager().getConfig(), "commands_alias.warps", WarpsCmd.class);

            registerCommand("warp", WarpCmd.class);
            registerCommand("warps", WarpsCmd.class);
            registerCommand("setwarp", SetWarpCmd.class);
            registerCommand("delwarp", DelWarpCmd.class);
        }
    }

    private void registerCommandsFromConfig(FileConfiguration config, Class<? extends CommandExecutor> commandExecutorClass) {
        if (config.contains("commands_alias")) {
            List<String> commands = config.getStringList("commands_alias");
            for (String commandName : commands) {
                registerCommand(commandName, commandExecutorClass);
            }
        }
    }

    private void registerCommandsFromConfigList(FileConfiguration config, String path, Class<? extends CommandExecutor> commandExecutorClass) {
        if (config.contains(path)) {
            List<String> commands = config.getStringList(path);
            for (String commandName : commands) {
                registerCommand(commandName, commandExecutorClass);
            }
        }
    }

    private void registerCommand(String commandName, Class<? extends CommandExecutor> commandExecutorClass) {
        CommandExecutor commandExecutor = null;
        try {
            Constructor<? extends CommandExecutor> constructor = commandExecutorClass.getConstructor(DeluxeTeleport.class);
            commandExecutor = constructor.newInstance(plugin);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

        if (commandExecutor != null) {
            CommandRegister commandRegister = new CommandRegister(commandName, commandExecutor, new TabCompleterManager(plugin));
            CommandMap commandMap = getCommandMap();
            if (commandMap != null) {
                commandMap.register("DeluxeTeleport", commandRegister);
            }
        }
    }

    public void registerCommand(String commandName) {
        CommandRegister dtCommand = new CommandRegister(commandName);
        CommandMap commandMap = getCommandMap();
        if (commandMap != null) {
            commandMap.register("DeluxeTeleport", dtCommand);
        }
    }
}