package com.pixesoj.deluxeteleport.managers;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.libs.actionbar.ActionBarAPI;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class ImportManager {
    public static void importEssentialsHomes(DeluxeTeleport plugin, CommandSender sender) {
        MessagesFileManager msg = plugin.getMainMessagesManager();
        MessagesManager m = new MessagesManager(msg.getPrefix(), plugin);
        File userdataFolder = new File("plugins/Essentials/userdata");

        if (!userdataFolder.exists() || !userdataFolder.isDirectory()) {
            m.sendMessage(sender, "La carpeta userdata de Essentials no existe.", true);
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
}
