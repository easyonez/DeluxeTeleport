package com.pixesoj.deluxeteleport.managers;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.libs.cenderedmessage.DefaultFontInfo;
import com.pixesoj.deluxeteleport.managers.dependencies.DependencyManager;
import com.pixesoj.deluxeteleport.utils.OtherUtils;
import com.pixesoj.deluxeteleport.utils.PlaceholderUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessagesManager {

    private final String prefix;
    private final DeluxeTeleport plugin;

    public MessagesManager(String prefix, DeluxeTeleport plugin) {
        this.prefix = prefix;
        this.plugin = plugin;
    }

    public void sendMessage(CommandSender sender, String message, boolean usePrefix) {
        String coloredMessage = usePrefix ? prefix + message : message;
        if (message != null && !message.isEmpty()) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (DependencyManager.isPlaceholderAPI()) {
                    coloredMessage = PlaceholderUtils.setPlaceholders(plugin, player, coloredMessage);
                } else {
                    coloredMessage = PlaceholderUtils.setLocalPlaceholders(plugin, player, coloredMessage);
                }
            }
            sender.sendMessage(getColoredMessage(coloredMessage));
        }
    }

    public static String getColoredMessage(String message) {
        if (OtherUtils.isNew()) {
            Pattern pattern = Pattern.compile("&?#([a-fA-F0-9]{6})");
            Matcher matcher = pattern.matcher(message);

            while (matcher.find()) {
                String color = matcher.group(1);
                String fullMatch = matcher.group(0);

                message = message.replace(fullMatch, ChatColor.of("#" + color).toString());
                matcher = pattern.matcher(message);
            }
        }

        if (message == null){
            message = "";
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public void sendComponent(Player player, BaseComponent message) {
        if (message != null) {
            player.spigot().sendMessage(message);
        } else {
            player.sendMessage("Null");
        }
    }

    public static String getCenteredMessage(String message){
        int CENTER_PX = 154;
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()){
            if(c == '§'){
                previousCode = true;
                continue;
            }else if(previousCode == true){
                previousCode = false;
                if(c == 'l' || c == 'L'){
                    isBold = true;
                    continue;
                }else isBold = false;
            }else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        return getColoredMessage((sb.toString() + message));
    }

    public TextComponent getCenteredComponent(TextComponent message) {
        final int CENTER_PX = 154;

        int messagePxSize = getMessagePixelLength(message);

        int toCompensate = CENTER_PX - (messagePxSize / 2);
        if (toCompensate > 0) {
            TextComponent prefix = new TextComponent("");

            int spaceWidth = DefaultFontInfo.SPACE.getLength();
            int spaceCount = toCompensate / spaceWidth;

            for (int i = 0; i < spaceCount; i++) {
                prefix.addExtra(" ");
            }

            prefix.addExtra(message);
            return prefix;
        } else {
            return message;
        }
    }

    private int getMessagePixelLength(TextComponent component) {
        String plainText = ChatColor.stripColor(component.toPlainText());
        int messagePxSize = 0;
        boolean isBold = false;

        boolean previousCode = false;
        for (char c : plainText.toCharArray()) {
            if (c == '§') {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else {
                DefaultFontInfo dfi = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dfi.getBoldLength() : dfi.getLength();
            }
        }

        if (component.isBold()) {
            isBold = true;
        }

        if (component.getExtra() != null) {
            for (BaseComponent extra : component.getExtra()) {
                if (extra instanceof TextComponent) {
                    messagePxSize += getMessagePixelLength((TextComponent) extra);
                }
            }
        }

        return messagePxSize;
    }

    public static String formatCoordinate(double coordinate) {
        return String.format("%.2f", coordinate);
    }

    public static String capitalizeText(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public static String progressBar(int percentage, int totalBlocks) {
        int progressBlocks = (int) ((percentage / 100.0) * totalBlocks);
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < totalBlocks; i++) {
            bar.append(i < progressBlocks ? "&a■" : "&7■");
        }
        return MessagesManager.getColoredMessage(bar.toString());
    }
}
