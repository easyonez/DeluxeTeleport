package com.pixesoj.deluxeteleport.managers;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.libs.actionbar.ActionBarAPI;
import com.pixesoj.deluxeteleport.utils.PlaceholderUtils;
import com.pixesoj.deluxeteleport.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ActionsManager {

    private final DeluxeTeleport plugin;
    private final FileConfiguration config;
    private final String actionsPath;

    private ConfigurationSection actionsSecction;
    private String placeholdersType;
    private String homeName;
    private Location homeLocation;

    public ActionsManager (DeluxeTeleport plugin, FileConfiguration config, String actionsPath){
        this.plugin = plugin;
        this.config = config;
        this.actionsPath = actionsPath;

        this.actionsSecction = config.getConfigurationSection(actionsPath + ".actions");
    }

    public void general(String whenAction, Player player) {
        this.placeholdersType = "general";
        if (actionsSecction != null) {
            for (String key : actionsSecction.getKeys(false)) {
                ConfigurationSection actionSection = actionsSecction.getConfigurationSection(key);
                if (actionSection != null) {
                    String when = actionSection.getString("when", "none");
                    String[] whenActions = when.split("\\s*,\\s*");

                    boolean matchFound = false;
                    for (String action : whenActions) {
                        if ((action.equalsIgnoreCase("none") && "none".equalsIgnoreCase(whenAction)) ||
                                action.equalsIgnoreCase(whenAction)) {
                            matchFound = true;
                            break;
                        }
                    }

                    ConfigurationSection conditionsSection = actionSection.getConfigurationSection("conditions");
                    if (matchFound) {
                        if (conditionsSection != null && !conditionsSection.getKeys(false).isEmpty()) {
                            ConditionsManager conditionsManager = new ConditionsManager(plugin, config, conditionsSection.getCurrentPath());
                            if (conditionsManager.isCondition(player)) {
                                actions(player, actionSection);
                            }
                            return;
                        }
                        actions(player, actionSection);
                    }
                }
            }
        }
    }

    public void tpa(String whenAction, Player player, Player targetPlayer) {
        this.placeholdersType = "general";
        if (actionsSecction != null) {
            for (String key : actionsSecction.getKeys(false)) {
                ConfigurationSection actionSection = actionsSecction.getConfigurationSection(key);
                if (actionSection != null) {
                    String when = actionSection.getString("when", "none");
                    String[] whenActions = when.split("\\s*,\\s*");

                    boolean matchFound = false;
                    for (String action : whenActions) {
                        if ((action.equalsIgnoreCase("none") && "none".equalsIgnoreCase(whenAction)) ||
                                action.equalsIgnoreCase(whenAction)) {
                            matchFound = true;
                            break;
                        }
                    }

                    if (matchFound) {
                        String whom = actionSection.getString("whom", "player");
                        String[] whomActions = whom.split("\\s*,\\s*");

                        for (String whomAction : whomActions) {
                            if (whomAction.equalsIgnoreCase("player")) {
                                actions(player, actionSection);
                            } else if (whomAction.equalsIgnoreCase("target_player")) {
                                actions(targetPlayer, actionSection);
                            } else if (whomAction.equalsIgnoreCase("both")) {
                                actions(player, actionSection);
                                actions(targetPlayer, actionSection);
                            }
                        }
                    }
                }
            }
        }
    }

    public void menu(Player player) {
        this.placeholdersType = "general";
        if (actionsSecction != null) {
            for (String key : actionsSecction.getKeys(false)) {
                ConfigurationSection actionSection = actionsSecction.getConfigurationSection(key);
                if (actionSection != null) {
                    ConfigurationSection conditionsSecction = actionSection.getConfigurationSection("conditions");
                    if (conditionsSecction != null && !conditionsSecction.getKeys(false).isEmpty()) {
                        ConditionsManager conditionsManager = new ConditionsManager(plugin, config, conditionsSecction.getCurrentPath());
                        if (!conditionsManager.isCondition(player)) return;
                    }

                    actions(player, actionSection);
                }
            }
        }
    }

    public void itemMenu(Player player) {
        this.placeholdersType = "general";
        if (actionsSecction != null) {
            for (String key : actionsSecction.getKeys(false)) {
                ConfigurationSection actionSection = actionsSecction.getConfigurationSection(key);
                if (actionSection != null) {
                    ConfigurationSection conditionsSecction = actionSection.getConfigurationSection("conditions");
                    if (conditionsSecction != null && !conditionsSecction.getKeys(false).isEmpty()) {
                        ConditionsManager conditionsManager = new ConditionsManager(plugin, config, conditionsSecction.getCurrentPath());
                        if (!conditionsManager.isCondition(player)) return;
                    }

                    actions(player, actionSection);
                }
            }
        }
    }

    public void itemMenuHome(Player player, String homeName, Location homeLocation) {
        this.placeholdersType = "home";
        this.homeName = homeName;
        this.homeLocation = homeLocation;
        if (actionsSecction != null) {
            for (String key : actionsSecction.getKeys(false)) {
                ConfigurationSection actionSection = actionsSecction.getConfigurationSection(key);
                if (actionSection != null) {
                    ConfigurationSection conditionsSecction = actionSection.getConfigurationSection("conditions");
                    if (conditionsSecction != null && !conditionsSecction.getKeys(false).isEmpty()) {
                        ConditionsManager conditionsManager = new ConditionsManager(plugin, config, conditionsSecction.getCurrentPath());
                        if (!conditionsManager.isCondition(player)) return;
                    }

                    actions(player, actionSection);
                }
            }
        }
    }

    public void actions(Player player, ConfigurationSection action){
        boolean actionEnabled = action.getBoolean("enabled", true);
        String type = action.getString("type", "");
        switch (type) {
            case "console":
                String consoleCommand = (String) action.get("command");
                if (consoleCommand == null || !actionEnabled) break;
                if (placeholdersType != null && placeholdersType.equals("home")){
                    consoleCommand = PlaceholderUtils.setHomePlaceholders(plugin, player, consoleCommand, homeName, homeLocation);
                } else {
                    consoleCommand = PlaceholderUtils.setPlaceholders(plugin, player, consoleCommand);
                }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), consoleCommand);
                break;
            case "command":
                String playerCommand = (String) action.get("command");
                if (playerCommand == null || !actionEnabled) break;
                if (placeholdersType != null && placeholdersType.equals("home")){
                    playerCommand = PlaceholderUtils.setHomePlaceholders(plugin, player, playerCommand, homeName, homeLocation);
                } else {
                    playerCommand = PlaceholderUtils.setPlaceholders(plugin, player, playerCommand);
                }
                player.performCommand(playerCommand);
                break;
            case "sound":
                String sound = (String) action.get("sound");
                float volume = (float) action.getDouble("volume", 1.0);
                float pitch = (float) action.getDouble("pitch", 1.0);
                if (sound == null || !actionEnabled) break;
                try {
                    if (placeholdersType != null && placeholdersType.equals("home")){
                        sound = PlaceholderUtils.setHomePlaceholders(plugin, player, sound, homeName, homeLocation);
                    } else {
                        sound = PlaceholderUtils.setPlaceholders(plugin, player, sound);
                    }
                    player.playSound(player.getLocation(), Sound.valueOf(sound), volume, pitch);
                } catch (IllegalArgumentException ignored) { }
                break;
            case "message":
                String message = (String) action.get("message");
                if (message == null || !actionEnabled) break;
                if (placeholdersType != null && placeholdersType.equals("home")){
                    message = PlaceholderUtils.setHomePlaceholders(plugin, player, message, homeName, homeLocation);
                } else {
                    message = PlaceholderUtils.setPlaceholders(plugin, player, message);
                }
                player.sendMessage(MessagesManager.getColoredMessage(message));
                break;
            case "centeredmessage":
                String centeredMessage = (String) action.get("message");
                if (centeredMessage == null || !actionEnabled) break;
                if (placeholdersType != null && placeholdersType.equals("home")){
                    centeredMessage = PlaceholderUtils.setHomePlaceholders(plugin, player, centeredMessage, homeName, homeLocation);
                } else {
                    centeredMessage = PlaceholderUtils.setPlaceholders(plugin, player, centeredMessage);
                }
                player.sendMessage(MessagesManager.getCenteredMessage(centeredMessage));
                break;
            case "broadcast":
                String broadcastMessage = (String) action.get("message");
                if (broadcastMessage == null || !actionEnabled) break;
                if (placeholdersType != null && placeholdersType.equals("home")){
                    broadcastMessage = PlaceholderUtils.setHomePlaceholders(plugin, player, broadcastMessage, homeName, homeLocation);
                } else {
                    broadcastMessage = PlaceholderUtils.setPlaceholders(plugin, player, broadcastMessage);
                }
                Bukkit.broadcastMessage(MessagesManager.getColoredMessage(broadcastMessage));
                break;
            case "title":
                String title = (String) action.get("title");
                if (title == null || !actionEnabled) break;
                if (placeholdersType != null && placeholdersType.equals("home")){
                    title = PlaceholderUtils.setHomePlaceholders(plugin, player, title, homeName, homeLocation);
                } else {
                    title = PlaceholderUtils.setPlaceholders(plugin, player, title);
                }
                String subtitle = action.getString("subtitle", "");
                if (placeholdersType != null && placeholdersType.equals("home")){
                    subtitle = PlaceholderUtils.setHomePlaceholders(plugin, player, subtitle, homeName, homeLocation);
                } else {
                    subtitle = PlaceholderUtils.setPlaceholders(plugin, player, subtitle);
                }
                int fadeIn = TimeUtils.timerConverter("ticks", action.getString("fadeIn", "10t"));
                int stay = TimeUtils.timerConverter("ticks", action.getString("stay", "3s 10t"));
                int fadeOut = TimeUtils.timerConverter("ticks", action.getString("fadeOut", "1s"));
                player.sendTitle(MessagesManager.getColoredMessage(title), MessagesManager.getColoredMessage(subtitle), fadeIn, stay, fadeOut);
                break;
            case "gamemode":
                String gamemode = (String) action.get("gamemode");
                if (gamemode == null || !actionEnabled) break;
                try {
                    GameMode mode = GameMode.valueOf(gamemode.toUpperCase());
                    player.setGameMode(mode);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid gamemode specified: " + gamemode);
                }
                break;
            case "potioneffect":
                String effectName = (String) action.get("effect");
                int effectDuration = TimeUtils.timerConverter("ticks", action.getString("duration", "5s"));
                int effectAmplifier = action.getInt("amplifier", 0);
                if (effectName == null || !actionEnabled) break;

                try {
                    PotionEffectType potionEffectType = PotionEffectType.getByName(effectName.toUpperCase());
                    if (potionEffectType != null) {
                        player.addPotionEffect(new PotionEffect(potionEffectType, effectDuration, effectAmplifier));
                    } else {
                        plugin.getLogger().warning("Invalid potion effect specified: " + effectName);
                    }
                } catch (Exception e) {
                    plugin.getLogger().warning("An error occurred while applying potion effect: " + effectName);
                }
                break;
            case "actionbar":
                String actionBar = (String) action.get("message");
                int actionBarDuration = TimeUtils.timerConverter("ticks", action.getString("duration", "3s"));
                if (actionBar == null || !actionEnabled) break;
                if (placeholdersType != null && placeholdersType.equals("home")){
                    actionBar = PlaceholderUtils.setHomePlaceholders(plugin, player, actionBar, homeName, homeLocation);
                } else {
                    actionBar = PlaceholderUtils.setPlaceholders(plugin, player, actionBar);
                }
                ActionBarAPI.sendActionBar(plugin, player, actionBar, actionBarDuration);
                break;
            default:
                plugin.getLogger().warning("Action type not recognized: " + type);
        }
    }
}
