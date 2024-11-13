package com.pixesoj.deluxeteleport.managers;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.pixesoj.deluxeteleport.DeluxeTeleport;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;


public class BungeeMessagingManager implements PluginMessageListener {

    private final DeluxeTeleport plugin;

    public BungeeMessagingManager(DeluxeTeleport plugin) {
        this.plugin = plugin;
        registerChannels();
    }

    private void registerChannels() {
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", this);
    }

    public void sendToServer(CommandSender sender, Player player, String serverName, boolean sendSender) {
        boolean defaultMessage = plugin.getMainLobbyConfigManager().getConfig().getBoolean("actions.default_messages", true);
        MessagesManager m = new MessagesManager("&8[&bDeluxeTeleport&8] ", plugin);
        MessagesManager lobbyM = new MessagesManager(plugin.getMainMessagesManager().getPrefixLobby(), plugin);
        if (serverName == null || serverName.isEmpty()) {
            assert serverName != null;
            m.sendMessage(Bukkit.getConsoleSender(), plugin.getMainMessagesManager().getGlobalServerNotExists()
                    .replace("%server%", serverName), true);
            return;
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);
        if (defaultMessage) lobbyM.sendMessage(player, plugin.getMainMessagesManager().getLobbyTeleporting()
                .replace("%server%", serverName), true);
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
        if (sendSender) {
            m.sendMessage(sender, plugin.getMainMessagesManager().getLobbyOtherTeleported()
                    .replace("%player%", player.getName())
                    .replace("%server%", serverName), true);
        }
    }

    @Override
    public void onPluginMessageReceived(@NotNull String s, @NotNull Player player, @NotNull byte[] bytes) {
    }

}