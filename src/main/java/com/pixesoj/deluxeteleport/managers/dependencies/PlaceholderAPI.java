package com.pixesoj.deluxeteleport.managers.dependencies;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.MessagesManager;
import com.pixesoj.deluxeteleport.utils.PlaceholderUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPI extends PlaceholderExpansion {

    // We get an instance of the plugin later.
    private DeluxeTeleport plugin;

    public PlaceholderAPI(DeluxeTeleport plugin) {
        this.plugin = plugin;
    }

    /**
     * Because this is an internal class,
     * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist(){
        return true;
    }
    /**
     * Since this expansion requires api access to the plugin "SomePlugin"
     * we must check if said plugin is on the server or not.
     *
     * @return true or false depending on if the required plugin is installed.
     */
    @Override
    public boolean canRegister(){
        return true;
    }

    /**
     * The name of the person who created this expansion should go here.
     *
     * @return The name of the author as a String.
     */
    @Override
    public @NotNull String getAuthor(){
        return "Pixesoj";
    }

    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest
     * method to obtain a value if a placeholder starts with our
     * identifier.
     * <br>This must be unique and can not contain % or _
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public @NotNull String getIdentifier(){
        return "deluxeteleport";
    }

    /**
     * This is the version of this expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * @return The version as a String.
     */
    @Override
    public @NotNull String getVersion(){
        return plugin.getDescription().getVersion();
    }

    /**
     * This is the method called when a placeholder with our identifier
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param  player
     *         A {@link org.bukkit.Player Player}.
     * @param  identifier
     *         A String containing the identifier/value.
     *
     * @return possibly-null String of the requested identifier.
     */

    // %mipluginvidas_vidas%

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        FileConfiguration config = plugin.getPlaceholdersManager().getConfig();
        String key = "placeholders." + identifier;

        Object baseObject = config.get(key);
        if (baseObject == null) {
            return "";
        }

        String base = null;

        if (baseObject instanceof String) {
            base = (String) baseObject;
        } else if (baseObject instanceof ConfigurationSection) {
            ConfigurationSection section = (ConfigurationSection) baseObject;
            String variableKey = section.getString("input");

            if (variableKey == null) {
                return "";
            }

            String variableValue = PlaceholderUtils.setPlaceholders(plugin, player, variableKey);
            ConfigurationSection conditions = section.getConfigurationSection("matchs");

            if (conditions != null) {
                for (String conditionKey : conditions.getKeys(false)) {
                    if (conditionKey.equals("else")) {
                        continue;
                    }

                    if (variableValue.equalsIgnoreCase("")) {
                        base = conditions.getString("void");
                        break;
                    }

                    if (evaluateCondition(variableValue, conditionKey)) {
                        base = conditions.getString(conditionKey);
                        break;
                    }
                }

                if (base == null && conditions.contains("else")) {
                    base = conditions.getString("else");
                }
            }

            if (base == null) {
                return "";
            }
        }

        if (player == null || base == null) {
            return "";
        }

        base = PlaceholderUtils.setPlaceholders(plugin, player, base);
        return MessagesManager.getColoredMessage(base);
    }

    private boolean evaluateCondition(String variableValue, String conditionKey) {
        try {
            double value = Double.parseDouble(variableValue);

            if (conditionKey.contains("-") && conditionKey.matches("\\d+\\.?\\d*-\\d+\\.?\\d*")) {
                String[] range = conditionKey.split("-");
                double min = Double.parseDouble(range[0]);
                double max = Double.parseDouble(range[1]);
                return value >= min && value <= max;
            } else if (conditionKey.startsWith("==")) {
                double v = Double.parseDouble(conditionKey.substring(2).trim());
                return value == v;
            } else if (conditionKey.startsWith("!=")) {
                double v = Double.parseDouble(conditionKey.substring(2).trim());
                return value != v;
            } else if (conditionKey.startsWith(">=")) {
                double v = Double.parseDouble(conditionKey.substring(2).trim());
                return value >= v;
            } else if (conditionKey.startsWith("<=")) {
                double v = Double.parseDouble(conditionKey.substring(2).trim());
                return value <= v;
            } else if (conditionKey.startsWith(">")) {
                double v = Double.parseDouble(conditionKey.substring(1).trim());
                return value > v;
            } else if (conditionKey.startsWith("<")) {
                double v = Double.parseDouble(conditionKey.substring(1).trim());
                return value < v;
            } else {
                return variableValue.equalsIgnoreCase(conditionKey);
            }
        } catch (NumberFormatException e) {
            return variableValue.equalsIgnoreCase(conditionKey);
        }
    }
}