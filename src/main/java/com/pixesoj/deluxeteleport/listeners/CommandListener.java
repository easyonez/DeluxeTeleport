package com.pixesoj.deluxeteleport.listeners;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
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
import com.pixesoj.deluxeteleport.commands.tpa.*;
import com.pixesoj.deluxeteleport.commands.warps.DelWarpCmd;
import com.pixesoj.deluxeteleport.commands.warps.SetWarpCmd;
import com.pixesoj.deluxeteleport.commands.warps.WarpCmd;
import com.pixesoj.deluxeteleport.commands.warps.WarpsCmd;
import com.pixesoj.deluxeteleport.managers.ActionsManager;
import com.pixesoj.deluxeteleport.managers.ConditionsManager;
import com.pixesoj.deluxeteleport.managers.MenuManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CommandListener implements Listener {

    private final MenusFileManager menuFileManager;
    private final DeluxeTeleport plugin;

    public CommandListener(DeluxeTeleport plugin) {
        this.plugin = plugin;
        this.menuFileManager = plugin.getMainMenuManager();
    }

    @EventHandler
    public void onMenu(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();

        if (message.length() <= 1) return;
        String command = message.substring(1).toLowerCase();

        Map<String, String> commandToFileMap = menuFileManager.getCommandToFileMap();

        if (commandToFileMap.containsKey(command)) {
            String fileName = commandToFileMap.get(command);
            MenusFileManager menusFileManager = plugin.getMainMenuManager();
            FileConfiguration menuConfig = menusFileManager.getConfig(fileName);

            if (!menuConfig.getBoolean("enabled", true)) return;
            if (!menuConfig.getBoolean("default_actions", false)) event.setCancelled(true);

            ConditionsManager conditionsManager = new ConditionsManager(plugin, menuConfig, "open_conditions");
            if (!conditionsManager.isCondition(player)) return;

            ActionsManager actionsManager = new ActionsManager(plugin, menuConfig, "open_actions");
            actionsManager.menu(player);

            MenuManager menuManager = new MenuManager(plugin, player, fileName);
            menuManager.openMenu();
            return;
        }

        String[] parts = message.substring(1).split(" ");
        String commandBase = parts[0].toLowerCase();
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        ConfigLobbyManager configLobbyManager = plugin.getMainLobbyConfigManager();
        List<String> lobbyCommands = configLobbyManager.getConfig().getStringList("commands_alias");
        lobbyCommands.add("lobby");

        if (configLobbyManager.isHighPriority()) {

            if (lobbyCommands.contains(commandBase)) {
                event.setCancelled(true);
                LobbyCmd lobbyCMD = new LobbyCmd(plugin);
                lobbyCMD.mainCommand(player, args);
            } else if (commandBase.equalsIgnoreCase("setlobby")) {
                event.setCancelled(true);
                SetLobbyCmd setLobbyCMD = new SetLobbyCmd(plugin);
                setLobbyCMD.mainCommand(player, args);
            } else if (commandBase.equalsIgnoreCase("dellobby")) {
                event.setCancelled(true);
                DelLobbyCmd delLobbyCMD = new DelLobbyCmd(plugin);
                delLobbyCMD.mainCommand(player, args);
            }
        }

        ConfigSpawnManager configSpawnManager = plugin.getMainSpawnConfigManager();
        List<String> spawnCommands = configSpawnManager.getConfig().getStringList("commands_alias");
        spawnCommands.add("spawn");

        if (configSpawnManager.isHighPriority()) {

            if (spawnCommands.contains(commandBase)) {
                event.setCancelled(true);
                SpawnCmd spawnCMD = new SpawnCmd(plugin);
                spawnCMD.mainCommand(player, args);
            } else if (commandBase.equalsIgnoreCase("setspawn")) {
                event.setCancelled(true);
                SetSpawnCmd setSpawnCMD = new SetSpawnCmd(plugin);
                setSpawnCMD.mainCommand(player, args);
            } else if (commandBase.equalsIgnoreCase("delspawn")) {
                event.setCancelled(true);
                DelSpawnCmd delSpawnCMD = new DelSpawnCmd(plugin);
                delSpawnCMD.mainCommand(player, args);
            }
        }

        ConfigHomeManager configHomeManager = plugin.getMainHomeConfigManager();
        List<String> homeCommands = configHomeManager.getConfig().getStringList("commands_alias.home");
        List<String> homesCommands = configHomeManager.getConfig().getStringList("commands_alias.homes");
        List<String> delHomeCommands = configHomeManager.getConfig().getStringList("commands_alias.delhome");
        List<String> setHomeCommands = configHomeManager.getConfig().getStringList("commands_alias.sethome");

        homeCommands.add("home");
        homesCommands.add("homes");
        setHomeCommands.add("sethome");
        delHomeCommands.add("delhome");

        if (configHomeManager.isHighPriority()) {

            if (homeCommands.contains(commandBase)) {
                event.setCancelled(true);
                HomeCmd homeCMD = new HomeCmd(plugin);
                homeCMD.mainCommand(player, args);
            } else if (homesCommands.contains(commandBase)) {
                event.setCancelled(true);
                HomesCmd homesCMD = new HomesCmd(plugin);
                homesCMD.mainCommand(player, args);
            } else if (setHomeCommands.contains(commandBase)) {
                event.setCancelled(true);
                SetHomeCmd setHomeCMD = new SetHomeCmd(plugin);
                setHomeCMD.mainCommand(player, args);
            } else if (delHomeCommands.contains(commandBase)) {
                event.setCancelled(true);
                DelHomeCmd delHomeCMD = new DelHomeCmd(plugin);
                delHomeCMD.mainCommand(player, args);
            }
        }

        ConfigTPAManager configTpaManager = plugin.getMainTPAConfigManager();
        List<String> tpaCommands = configTpaManager.getConfig().getStringList("commands_alias.tpa");
        List<String> tpaCceptCommands = configTpaManager.getConfig().getStringList("commands_alias.tpaccept");
        List<String> tpaCancelCommands = configTpaManager.getConfig().getStringList("commands_alias.tpacancel");
        List<String> tpaDenyCommands = configTpaManager.getConfig().getStringList("commands_alias.tpadeny");
        List<String> tpaHereCommands = configTpaManager.getConfig().getStringList("commands_alias.tpahere");
        List<String> tpaToggleCommands = configTpaManager.getConfig().getStringList("commands_alias.tpatoggle");

        tpaCommands.add("tpa");
        tpaCceptCommands.add("tpaccept");
        tpaCancelCommands.add("tpacancel");
        tpaDenyCommands.add("tpadeny");
        tpaHereCommands.add("tpahere");
        tpaToggleCommands.add("tpatoggle");


        if (configTpaManager.isHighPriority()) {
            if (tpaCommands.contains(commandBase)) {
                event.setCancelled(true);
                TpaCMD TpaCMD = new TpaCMD(plugin);
                TpaCMD.mainCommand(player, args);
            } else if (tpaCceptCommands.contains(commandBase)) {
                event.setCancelled(true);
                TpAcceptCMD tpAcceptCMD = new TpAcceptCMD(plugin);
                tpAcceptCMD.mainCommand(player, args);
            } else if (tpaCancelCommands.contains(commandBase)) {
                event.setCancelled(true);
                TpaCancelCMD tpaCancelCMD = new TpaCancelCMD(plugin);
                tpaCancelCMD.mainCommand(player, args);
            } else if (tpaDenyCommands.contains(commandBase)) {
                event.setCancelled(true);
                TpaDenyCMD tpaDenyCMD = new TpaDenyCMD(plugin);
                tpaDenyCMD.mainCommand(player, args);
            } else if (tpaHereCommands.contains(commandBase)){
                event.setCancelled(true);
                TpaHereCMD tpaHereCMD = new TpaHereCMD(plugin);
                tpaHereCMD.mainCommand(player, args);
            } else if (tpaToggleCommands.contains(commandBase)) {
                event.setCancelled(true);
                TpaToggleCMD tpaToggleCMD = new TpaToggleCMD(plugin);
                tpaToggleCMD.mainCommand(player, args);
            }
        }

        ConfigWarpsManager configWarpManager = plugin.getMainWarpConfigManager();
        List<String> warpCommands = configWarpManager.getConfig().getStringList("commands_alias.warp");
        List<String> warpsCommands = configWarpManager.getConfig().getStringList("commands_alias.warps");

        warpCommands.add("warp");
        warpsCommands.add("warps");

        if (configWarpManager.isHighPriority()) {

            if (warpCommands.contains(commandBase)) {
                event.setCancelled(true);
                WarpCmd warpCmd = new WarpCmd(plugin);
                warpCmd.mainCommand(player, args);
            } else if (warpsCommands.contains(commandBase)) {
                event.setCancelled(true);
                WarpsCmd warpsCmd = new WarpsCmd(plugin);
                warpsCmd.mainCommand(player);
            } else if (commandBase.equalsIgnoreCase("setwarp")) {
                event.setCancelled(true);
                SetWarpCmd setWarpCmd = new SetWarpCmd(plugin);
                setWarpCmd.mainCommand(player, args);
            } else if (commandBase.equalsIgnoreCase("delwarp")) {
                event.setCancelled(true);
                DelWarpCmd delWarpCmd = new DelWarpCmd(plugin);
                delWarpCmd.mainCommand(player, args);
            }
        }
    }
}
