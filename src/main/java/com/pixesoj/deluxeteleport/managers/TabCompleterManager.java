package com.pixesoj.deluxeteleport.managers;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.filesmanager.PermissionsManager;
import com.pixesoj.deluxeteleport.utils.FileUtils;
import com.pixesoj.deluxeteleport.utils.LocationUtils;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TabCompleterManager implements TabCompleter {
    private final DeluxeTeleport plugin;

    public TabCompleterManager(DeluxeTeleport deluxeteleport) {
        this.plugin = deluxeteleport;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        String preset = plugin.getMainPermissionsManager().getReset();
        boolean dreset = plugin.getMainPermissionsManager().isResetDefault();

        String pi = plugin.getMainPermissionsManager().getImport();
        boolean di = plugin.getMainPermissionsManager().isImportDefault();

        String pc = plugin.getMainPermissionsManager().getChangelog();
        boolean dc = plugin.getMainPermissionsManager().isChangelogDefault();

        String pInfo = plugin.getMainPermissionsManager().getInfo();
        boolean dInfo = plugin.getMainPermissionsManager().isInfoDefault();

        String pdw = plugin.getMainPermissionsManager().getDelWarps();
        boolean ddw = plugin.getMainPermissionsManager().isDelWarpsDefault();

        String pw = plugin.getMainPermissionsManager().getWarps();
        boolean dw = plugin.getMainPermissionsManager().isWarpsDefault();

        boolean resetHasPermission = PlayerUtils.hasPermission(plugin, sender, preset, dreset, false);
        boolean importHasPermission = PlayerUtils.hasPermission(plugin, sender, pi, di, false);
        boolean changelogHasPermission = PlayerUtils.hasPermission(plugin, sender, pc, dc, false);
        boolean infoHasPermission = PlayerUtils.hasPermission(plugin, sender, pInfo, dInfo, false);
        boolean delWarpsHasPermission = PlayerUtils.hasPermission(plugin, sender, pdw, ddw, false);
        boolean warpsHasPermission = PlayerUtils.hasPermission(plugin, sender, pw, dw, false);

        List<String> mainCommands = new ArrayList<>();
        if (plugin.getMainHomeConfigManager().getConfig().contains("commands_alias.home")) {
            List<String> mainCommandAlias = plugin.getMainConfigManager().getConfig().getStringList("commands_alias");
            mainCommands.addAll(mainCommandAlias);
            mainCommands.add("deluxeteleport");
        }

        List<String> homeCommands = new ArrayList<>();
        if (plugin.getMainHomeConfigManager().getConfig().contains("commands_alias.home")) {
            List<String> homeCommandAlias = plugin.getMainHomeConfigManager().getConfig().getStringList("commands_alias.home");
            homeCommands.addAll(homeCommandAlias);
            homeCommands.add("home");
        }

        if (plugin.getMainHomeConfigManager().getConfig().contains("commands_alias.delhome")) {
            List<String> homeCommandAlias = plugin.getMainHomeConfigManager().getConfig().getStringList("commands_alias.delhome");
            homeCommands.addAll(homeCommandAlias);
            homeCommands.add("delhome");
        }

        if (mainCommands.contains(command.getName().toLowerCase())) {
            List<String> completions = new ArrayList<>();

            if (args.length == 1) {
                String pr = plugin.getMainPermissionsManager().getReload();
                boolean dr = plugin.getMainPermissionsManager().isReloadDefault();
                if (PlayerUtils.hasPermission(plugin, sender, pr, dr, false)) {
                    completions.add("reload");
                }

                if (importHasPermission) {
                    completions.add("import");
                }

                String ph = plugin.getMainPermissionsManager().getHelp();
                boolean dh = plugin.getMainPermissionsManager().isHelpDefault();
                if (PlayerUtils.hasPermission(plugin, sender, ph, dh, false)) {
                    completions.add("help");
                }

                String pu = plugin.getMainPermissionsManager().getUpdate();
                boolean du = plugin.getMainPermissionsManager().isUpdateDefault();
                if (PlayerUtils.hasPermission(plugin, sender, pu, du, false)) {
                    completions.add("update");
                }

                if (resetHasPermission) {
                    completions.add("reset");
                }


                if (changelogHasPermission) {
                    completions.add("changelog");
                }

                if (infoHasPermission){
                    completions.add("info");
                }

            } else if (args.length == 2) {
                if (resetHasPermission && args[0].equalsIgnoreCase("reset")) {
                    completions.add("cooldown");
                }

                if (importHasPermission && args[0].equalsIgnoreCase("import")) {
                    completions.add("Essentials");
                }

                if (changelogHasPermission && args[0].equalsIgnoreCase("changelog")){
                    if (plugin.getAvaibleVersions() != null) {
                        completions.addAll(plugin.getAvaibleVersions());
                    }
                }
            } else if (args.length == 3) {
                if (changelogHasPermission && args[0].equalsIgnoreCase("changelog")){
                    completions.add("true");
                    completions.add("false");
                }

                if (resetHasPermission && args[0].equalsIgnoreCase("reset") && args[1].equalsIgnoreCase("cooldown")) {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        completions.add(onlinePlayer.getName());
                    }
                }

                if (importHasPermission && args[0].equalsIgnoreCase("import") && args[1].equalsIgnoreCase("Essentials")) {
                    completions.add("homes");
                }

                String partialInput = args[2].toLowerCase();
                completions.removeIf(name -> !name.toLowerCase().startsWith(partialInput));
            }
            return completions;
        }

        if (homeCommands.contains(command.getName().toLowerCase())) {
            if (args.length == 1) {
                Player player = (Player) sender;
                UUID uuid = player.getUniqueId();
                File playerFile = plugin.getPlayerDataManager().getPlayerFile(uuid);
                FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
                ConfigurationSection homesSection = playerData.getConfigurationSection("homes");

                if (!playerFile.exists() || !playerData.contains("homes") || homesSection == null) {
                    return Collections.emptyList();
                }

                List<String> completions = new ArrayList<>(homesSection.getKeys(false));

                return completions;
            }
        }

        List<String> mainCommandAlias = plugin.getMainConfigManager().getConfig().getStringList("commands_alias");
        PermissionsManager perm = plugin.getMainPermissionsManager();
        if ((command.getName().equalsIgnoreCase("spawn") || mainCommandAlias.contains(command))
                && plugin.getMainSpawnConfigManager().isMenuEnabled() && PlayerUtils.hasPermission(plugin, sender, perm.getMenuSpawnAdmin(), perm.isMenuSpawnAdminDefault(), false)){
            if (args.length == 1){
                List<String> completions = new ArrayList<>();
                completions.add("menu");
                return completions;
            }
        }

        if (plugin.getMainTPAConfigManager().isAutocompleteNames()) {
            List<String> tpaCommands = new ArrayList<>();
            if (plugin.getMainTPAConfigManager().getConfig().contains("commands_alias.tpa")) {
                List<String> tpaCommandAlias = plugin.getMainTPAConfigManager().getConfig().getStringList("commands_alias.tpa");
                tpaCommands.addAll(tpaCommandAlias);
                tpaCommands.add("tpa");
            }

            if (plugin.getMainTPAConfigManager().getConfig().contains("commands_alias.tpaccept")) {
                List<String> tpaCommandAlias = plugin.getMainTPAConfigManager().getConfig().getStringList("commands_alias.tpaccept");
                tpaCommands.addAll(tpaCommandAlias);
                tpaCommands.add("tpaccept");
            }

            if (plugin.getMainTPAConfigManager().getConfig().contains("commands_alias.tpacancel")) {
                List<String> tpaCommandAlias = plugin.getMainTPAConfigManager().getConfig().getStringList("commands_alias.tpacancel");
                tpaCommands.addAll(tpaCommandAlias);
                tpaCommands.add("tpacancel");
            }

            if (plugin.getMainTPAConfigManager().getConfig().contains("commands_alias.tpadeny")) {
                List<String> tpaCommandAlias = plugin.getMainTPAConfigManager().getConfig().getStringList("commands_alias.tpadeny");
                tpaCommands.addAll(tpaCommandAlias);
                tpaCommands.add("tpadeny");
            }

            if (plugin.getMainTPAConfigManager().getConfig().contains("commands_alias.tpahere")) {
                List<String> tpaCommandAlias = plugin.getMainTPAConfigManager().getConfig().getStringList("commands_alias.tpahere");
                tpaCommands.addAll(tpaCommandAlias);
                tpaCommands.add("tpahere");
            }

            if (plugin.getMainTPAConfigManager().getConfig().contains("commands_alias.tpatoggle")) {
                List<String> tpaCommandAlias = plugin.getMainTPAConfigManager().getConfig().getStringList("commands_alias.tpatoggle");
                tpaCommands.addAll(tpaCommandAlias);
                tpaCommands.add("tpatoggle");
            }

            if (tpaCommands.contains(command.getName().toLowerCase())) {
                if (args.length == 1) {
                    List<String> completions = new ArrayList<>();

                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        completions.add(onlinePlayer.getName());
                    }

                    String partialInput = args[0].toLowerCase();
                    completions.removeIf(name -> !name.toLowerCase().startsWith(partialInput));

                    return completions;
                }
            }
        }

        if ((command.getName().equalsIgnoreCase("delwarp") && delWarpsHasPermission && args.length == 1)
                || (command.getName().equalsIgnoreCase("warp") && warpsHasPermission && args.length == 1)){
            return new ArrayList<>(FileUtils.getFiles(plugin, "warps"));
        }

        return Collections.emptyList();
    }
}
