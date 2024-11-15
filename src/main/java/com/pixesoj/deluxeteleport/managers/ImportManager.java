package com.pixesoj.deluxeteleport.managers;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.libs.actionbar.ActionBarAPI;
import com.pixesoj.deluxeteleport.managers.filesmanager.FileManager;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;

public class ImportManager {
    public static void importEssentialsAll(DeluxeTeleport plugin, CommandSender sender) {
        MessagesFileManager msg = plugin.getMainMessagesManager();
        MessagesManager m = new MessagesManager(msg.getPrefix(), plugin);
        m.sendMessage(sender, "&aStarting import of Essentials homes...", true);
        importEssentialsHomes(plugin, sender);

        new BukkitRunnable() {
            @Override
            public void run() {
                m.sendMessage(sender, "&aStarting import of Essentials warps...", true);
                importEssentialsWarps(plugin, sender);
            }
        }.runTaskLater(plugin, 60L);
    }

    public static void importEssentialsHomes(DeluxeTeleport plugin, CommandSender sender) {
        MessagesFileManager msg = plugin.getMainMessagesManager();
        MessagesManager m = new MessagesManager(msg.getPrefix(), plugin);
        File userdataFolder = new File("plugins/Essentials/userdata");

        if (!userdataFolder.exists() || !userdataFolder.isDirectory()) {
            m.sendMessage(sender, "No data to import.", true);
            return;
        }

        long startTime = System.currentTimeMillis();
        int fileCount = 0;
        int totalHomes = 0;
        int importedHomes = 0;

        File[] userFiles = userdataFolder.listFiles();
        int totalFiles = (userFiles != null) ? userFiles.length : 0;

        if (userFiles != null) {
            for (File userFile : userFiles) {
                if (userFile.isFile() && userFile.getName().endsWith(".yml")) {
                    fileCount++;
                    try {
                        YamlConfiguration userConfig = YamlConfiguration.loadConfiguration(userFile);

                        String uuidString = userFile.getName().replace(".yml", "");

                        if (userConfig.isConfigurationSection("homes")) {
                            ConfigurationSection homesSection = userConfig.getConfigurationSection("homes");
                            int playerHomeCount = homesSection.getKeys(false).size();
                            totalHomes += playerHomeCount;

                            for (String homeName : homesSection.getKeys(false)) {
                                ConfigurationSection homeData = homesSection.getConfigurationSection(homeName);

                                String worldName = homeData.getString("world-name");
                                double x = homeData.getDouble("x");
                                double y = homeData.getDouble("y");
                                double z = homeData.getDouble("z");
                                float yaw = (float) homeData.getDouble("yaw");
                                float pitch = (float) homeData.getDouble("pitch");

                                World world = plugin.getServer().getWorld(worldName);
                                if (world != null) {
                                    Location homeLocation = new Location(world, x, y, z, yaw, pitch);

                                    plugin.getPlayerDataManager().savePlayerHome(uuidString, homeName, homeLocation);
                                    importedHomes++;

                                    double progress = (double) fileCount / totalFiles;
                                    int percentage = (int) (progress * 100);
                                    String progressBar = m.progressBar(percentage, 12);
                                    String format = String.format(
                                            "&8[&aHomes&8] &bEssentials &8▶ &bDeluxeTeleport &7• &f%s%% &8(&7%s&8) &7• &f%d/%d processed.",
                                            percentage, progressBar, fileCount, totalFiles);
                                    if (sender instanceof Player) {
                                        Player player = (Player) sender;

                                        ActionBarAPI.sendActionBar(player, format);
                                    } else {
                                        m.sendMessage(sender, format, false);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        m.sendMessage(sender, "Error importing homes from file: " + userFile.getName(), true);
                        e.printStackTrace();
                    }
                }
            }
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        String format = String.format(
                "&8[&aHomes&8] &bEssentials &8▶ &bDeluxeTeleport &7• &aImport completed in %sms &8(&7%s&8) &7• &f%d/%d imported.",
                elapsedTime, fileCount, importedHomes, totalHomes
        );
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ActionBarAPI.sendActionBar(plugin, player, format, 100);
        } else {
            m.sendMessage(sender, format, false);
        }
    }

    public static void importEssentialsWarps(DeluxeTeleport plugin, CommandSender sender) {
        MessagesFileManager msg = plugin.getMainMessagesManager();
        MessagesManager m = new MessagesManager(msg.getPrefix(), plugin);
        File warpsFolder = new File("plugins/Essentials/warps");

        if (!warpsFolder.exists() || !warpsFolder.isDirectory()) {
            m.sendMessage(sender, "No data to import.", true);
            return;
        }

        long startTime = System.currentTimeMillis();
        int importedWarps = 0;

        File[] warpFiles = warpsFolder.listFiles();
        int totalFiles = (warpFiles != null) ? warpFiles.length : 0;

        if (warpFiles != null) {
            for (File warpFile : warpFiles) {
                if (warpFile.isFile() && warpFile.getName().endsWith(".yml")) {
                    String warpName = warpFile.getName().replace(".yml", "");
                    try {
                        YamlConfiguration warpConfig = YamlConfiguration.loadConfiguration(warpFile);

                        FileManager fileManager = new FileManager(warpName + ".yml", "data/warps", plugin);
                        FileConfiguration warp = fileManager.getConfig();

                        warp.set("world", warpConfig.getString("world"));
                        warp.set("world_name", warpConfig.getString("world_name"));
                        warp.set("x", warpConfig.getDouble("x"));
                        warp.set("y", warpConfig.getDouble("y"));
                        warp.set("z", warpConfig.getDouble("z"));
                        warp.set("yaw", warpConfig.getDouble("yaw"));
                        warp.set("pitch", warpConfig.getDouble("pitch"));
                        warp.set("name", warpName);
                        warp.set("lastowner", warpConfig.getString("lastowner"));
                        warp.set("teleport_actions.actions", new ArrayList<>());
                        warp.set("teleport_conditions.conditions", new ArrayList<>());
                        fileManager.saveConfig();

                        importedWarps++;

                        double progress = (double) importedWarps / totalFiles;
                        int percentage = (int) (progress * 100);
                        String progressBar = m.progressBar(percentage, 12);
                        String format = String.format(
                                "&8[&aWarps&8] &bEssentials &8▶ &bDeluxeTeleport &7• &f%s%% &8(&7%s&8) &7• &f%d/%d processed.",
                                percentage, progressBar, importedWarps, totalFiles);
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            ActionBarAPI.sendActionBar(player, format);
                        } else {
                            m.sendMessage(sender, format, false);
                        }
                    } catch (Exception e) {
                        m.sendMessage(sender, "Error importing warp from file: " + warpFile.getName(), true);
                        e.printStackTrace();
                    }
                }
            }
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        String finalMessage = String.format(
                "&8[&aWarps&8] &bEssentials &8▶ &bDeluxeTeleport &7• &aImport completed in %d ms &8(&7%s&8) &7• &f%d/%d imported.",
                elapsedTime, m.progressBar(100, 12), importedWarps, totalFiles
        );
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ActionBarAPI.sendActionBar(plugin, player, finalMessage, 100);
        } else {
            m.sendMessage(sender, finalMessage, false);
        }
    }
}
