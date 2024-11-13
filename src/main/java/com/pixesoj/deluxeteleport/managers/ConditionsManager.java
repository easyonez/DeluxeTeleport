package com.pixesoj.deluxeteleport.managers;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.dependencies.DependencyManager;
import com.pixesoj.deluxeteleport.utils.PlaceholderUtils;
import com.pixesoj.deluxeteleport.utils.LocationUtils;
import com.pixesoj.deluxeteleport.utils.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ConditionsManager {

    private final DeluxeTeleport plugin;
    private final FileConfiguration config;
    private final String conditionsPath;

    private ConfigurationSection conditionsSection;

    public ConditionsManager(DeluxeTeleport plugin, FileConfiguration config, String conditionsPath) {
        this.plugin = plugin;
        this.config = config;
        this.conditionsPath = conditionsPath;

        this.conditionsSection = config.getConfigurationSection(conditionsPath + ".conditions");
    }

    public boolean isCondition(Player player) {
        int optionalConditionsMet = 0;
        int optionalConditions = 0;

        int minimumRequirements = config.getInt(conditionsPath + ".minimum_requirements", -1);
        if (conditionsSection != null) {
            for (String key : conditionsSection.getKeys(false)) {
                ConfigurationSection conditionSection = conditionsSection.getConfigurationSection(key);
                if (conditionSection != null) {
                    String type = conditionSection.getString("type", "");
                    boolean conditionEnabled = conditionSection.getBoolean("enabled", true);
                    boolean isOptional = conditionSection.getBoolean("optional", false);

                    if (!conditionEnabled) {
                        continue;
                    }

                    String permission = conditionSection.getString("permission");
                    if (permission != null) permission = PlaceholderUtils.setPlaceholders(plugin, player, permission);
                    String stringAmount = conditionSection.getString("amount", "0");
                    stringAmount = PlaceholderUtils.setPlaceholders(plugin, player, stringAmount);
                    boolean level = conditionSection.getBoolean("level", true);
                    String locationString = conditionSection.getString("location");
                    if (locationString != null) locationString = PlaceholderUtils.setPlaceholders(plugin, player, locationString);
                    Location location = LocationUtils.stringToLocation(locationString);
                    int distance = conditionSection.getInt("distance");
                    String input = conditionSection.getString("input", "");
                    input = PlaceholderUtils.setPlaceholders(plugin, player, input);
                    String output = conditionSection.getString("output", "");
                    output = PlaceholderUtils.setPlaceholders(plugin, player, output);

                    int inputInt;
                    int outputInt;
                    try {
                        assert input != null;
                        inputInt = Integer.parseInt(input);
                    } catch (NumberFormatException e) {
                        inputInt = 0;
                    }
                    try {
                        assert output != null;
                        outputInt = Integer.parseInt(output);
                    } catch (NumberFormatException e) {
                        outputInt = 0;
                    }

                    double amount;
                    try {
                        amount = Double.parseDouble(stringAmount);
                    } catch (NumberFormatException e) {
                        amount = 0;
                    }

                    boolean conditionMet = false;
                    switch (type) {
                        case "has permission":
                            if (permission != null) {
                                conditionMet = PlayerUtils.hasPermission(player, permission);
                            }
                            break;
                        case "!has permission":
                            if (permission != null) {
                                conditionMet = !PlayerUtils.hasPermission(player, permission);
                            }
                            break;
                        case "has money":
                            if (DependencyManager.isEconomy(plugin)) {
                                conditionMet = PlayerUtils.hasMoney(plugin, player, amount);
                            }
                            break;
                        case "!has money":
                            if (DependencyManager.isEconomy(plugin)) {
                                conditionMet = !PlayerUtils.hasMoney(plugin, player, amount);
                            }
                            break;
                        case "has exp":
                            conditionMet = PlayerUtils.hasExp(player, (int) amount, level);
                            break;
                        case "!has exp":
                            conditionMet = !PlayerUtils.hasExp(player, (int) amount, level);
                            break;
                        case "is near":
                            if (locationString != null) {
                                conditionMet = PlayerUtils.isNear(player, location, distance);
                            }
                            break;
                        case "!is near":
                            if (locationString != null) {
                                conditionMet = !PlayerUtils.isNear(player, location, distance);
                            }
                            break;
                        case "string equals":
                            if (input != null && output != null) {
                                conditionMet = input.equals(output);
                            }
                            break;
                        case "!string equals":
                            if (input != null && output != null) {
                                conditionMet = !input.equals(output);
                            }
                            break;
                        case "string equals ignorecase":
                            if (input != null && output != null) {
                                conditionMet = input.equalsIgnoreCase(output);
                            }
                            break;
                        case "!string equals ignorecase":
                            if (input != null && output != null) {
                                conditionMet = !input.equalsIgnoreCase(output);
                            }
                            break;
                        case "string contains":
                            if (input != null && output != null) {
                                conditionMet = input.contains(output);
                            }
                            break;
                        case "!string contains":
                            if (input != null && output != null) {
                                conditionMet = !input.contains(output);
                            }
                            break;
                        case "==":
                            conditionMet = inputInt == outputInt;
                            break;
                        case ">=":
                            conditionMet = inputInt >= outputInt;
                            break;
                        case "<=":
                            conditionMet = inputInt <= outputInt;
                            break;
                        case "!=":
                            conditionMet = inputInt != outputInt;
                            break;
                        case ">":
                            conditionMet = inputInt > outputInt;
                            break;
                        case "<":
                            conditionMet = inputInt < outputInt;
                            break;
                    }


                    ActionsManager actionsManager;
                    if (conditionMet) {
                        actionsManager = new ActionsManager(plugin, config, conditionsPath + ".conditions." + key + ".succes_actions");
                    } else {
                        actionsManager = new ActionsManager(plugin, config, conditionsPath + ".conditions." + key + ".deny_actions");
                    }
                    actionsManager.general("none", player);

                    if (isOptional) {
                        optionalConditions++;
                        if (conditionMet) {
                            optionalConditionsMet++;
                        }
                    } else if (!conditionMet) {
                        return false;
                    }

                    if (minimumRequirements > 0 && optionalConditionsMet >= minimumRequirements) {
                        return true;
                    }
                }
            }
        }

        if (minimumRequirements == -1) {
            return optionalConditionsMet == optionalConditions;
        }

        return optionalConditionsMet >= minimumRequirements;
    }
}
