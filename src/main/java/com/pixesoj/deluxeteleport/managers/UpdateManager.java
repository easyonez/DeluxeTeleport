package com.pixesoj.deluxeteleport.managers;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.filesmanager.ConfigManager;
import com.pixesoj.deluxeteleport.model.internal.UpdateCheckResult;
import com.pixesoj.deluxeteleport.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.*;

public class UpdateManager {

    private final DeluxeTeleport plugin;
    private final CommandSender sender;
    private final String currentVersion;
    private final String jarName;
    private final File pluginsFolder;
    private final MessagesManager m;
    private final UpdateCheckResult result;
    private final String latestVersion;
    private boolean ready = false;
    private final boolean isAutoUpdate;
    private UpdateCheckManager updateCheckManager;

    public UpdateManager(DeluxeTeleport plugin, CommandSender sender) {
        this.plugin = plugin;
        this.m = new MessagesManager("&8[&bDeluxeTeleport&8] ", plugin);
        this.sender = sender;
        boolean isCheckNotify = plugin.getMainConfigManager().isUpdateNotify();
        this.jarName = plugin.getName();
        this.currentVersion = plugin.version.contains("-") ? plugin.version.split("-")[0] : plugin.version;
        this.pluginsFolder = new File("plugins");
        updateCheckManager = plugin.getUpdateCheckerManager();
        this.result = updateCheckManager.check();

        this.latestVersion = result.getLatestVersion();
        ConfigManager config = plugin.getMainConfigManager();
        this.isAutoUpdate = config.isUpdateAutoUpdate();

        int checkUpdateInterval = TimeUtils.timerConverter("milliseconds", config.getUpdateCheckInterval());
        if (checkUpdateInterval > 0){
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    updateCheckManager = new UpdateCheckManager(plugin.version);
                    updateCheckManager.check();
                    startUpdate(!config.isUpdateRestartOnlyOnStart());
                    plugin.setUpdateCheckerManager(updateCheckManager);
                }
            };

            timer.scheduleAtFixedRate(task, checkUpdateInterval, checkUpdateInterval);
        }
    }

    public void startUpdate(boolean restart){
        ConsoleCommandSender s = Bukkit.getConsoleSender();

        if (result.isError()) {
            m.sendMessage(s, "&cError checking for updates", true);
        } else if (result.isUpdateAvailable()) {
            m.sendMessage(s, "&cA new version is available. &e(&7" + result.getLatestVersion() + "&e)", true);

            if (isAutoUpdate) {
                UpdateManager update = new UpdateManager(plugin, plugin.getServer().getConsoleSender());
                update.downloadUpdate(restart);
            } else {
                m.sendMessage(s, "&cUse &a/deluxeteleport update &cto update", false);
                m.sendMessage(s, "&cor you can download it at: &fhttps://modrinth.com/plugin/deluxeteleport/version/" + result.getLatestVersion(), false);
            }
        } else {
            m.sendMessage(s, "&aNo updates available.", true);
        }
    }

    public void downloadUpdate(boolean restart) {
        List<String> possibleNames = Arrays.asList(
                jarName,
                jarName + " (1)",
                jarName + " (2)",
                jarName + " (3)",
                jarName + "-" + currentVersion,
                jarName + "-" + currentVersion + " (1)",
                jarName + "-" + currentVersion + " (2)",
                jarName + "-" + currentVersion + " (3)",
                jarName.toLowerCase(Locale.ROOT) + "-" + currentVersion,
                jarName.toLowerCase(Locale.ROOT) + "-" + currentVersion + " (1)",
                jarName.toLowerCase(Locale.ROOT) + "-" + currentVersion + " (2)",
                jarName.toLowerCase(Locale.ROOT) + "-" + currentVersion + " (3)",
                jarName.toLowerCase(Locale.ROOT),
                jarName.toLowerCase(Locale.ROOT) + " (1)",
                jarName.toLowerCase(Locale.ROOT) + " (2)",
                jarName.toLowerCase(Locale.ROOT) + " (3)");

        File oldJar = null;
        for (String name : possibleNames) {
            File file = new File(pluginsFolder, name + ".jar");
            if (!file.exists()) continue;
            oldJar = file;
            break;
        }

        if (oldJar == null) {
            m.sendMessage(sender,  plugin.getMainMessagesManager().getUpdateJarNotFound()
                    .replace("%jar_name%", jarName + ".jar"), true);
            return;
        }

        try {
            URL url = new URL(result.getDownloadLink());
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setInstanceFollowRedirects(true);
            HttpURLConnection.setFollowRedirects(true);
            if (connection.getResponseCode() == 200) {
                ReadableByteChannel rbc = Channels.newChannel(connection.getInputStream());
                File out = new File(pluginsFolder, jarName + "-" + latestVersion + ".jar");
                m.sendMessage(sender, plugin.getMainMessagesManager().getUpdateDownloaded(), true);
                m.sendMessage(sender, "&6" + out.getAbsolutePath(), false);
                FileOutputStream fos = new FileOutputStream(out);
                fos.getChannel().transferFrom(rbc, 0L, Long.MAX_VALUE);
                fos.close();
                if (!oldJar.delete()) {
                    m.sendMessage(sender, plugin.getMainMessagesManager().getUpdateUnableToDeleted(), true);
                }

                if (restart){
                    m.sendMessage(Bukkit.getConsoleSender(), "&eRestarting server...", true);
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "save-all");
                    plugin.getServer().spigot().restart();
                }
            } else {
                m.sendMessage(Bukkit.getConsoleSender(), plugin.getMainMessagesManager().getUpdateResponseCode()
                        .replace("%code%", String.valueOf(connection.getResponseCode())), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}