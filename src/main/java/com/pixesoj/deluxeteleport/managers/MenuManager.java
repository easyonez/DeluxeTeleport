package com.pixesoj.deluxeteleport.managers;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.filesmanager.MenusFileManager;
import com.pixesoj.deluxeteleport.model.item.ItemSkullData;
import com.pixesoj.deluxeteleport.utils.HomeUtils;
import com.pixesoj.deluxeteleport.utils.ItemUtils;
import com.pixesoj.deluxeteleport.utils.PlaceholderUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class MenuManager {

    private final String fileName;
    private final Player player;
    private final DeluxeTeleport plugin;

    private static Map<Player, MenuManager> openMenus;

    private final FileConfiguration menu;
    private Inventory inventory;

    public MenuManager(DeluxeTeleport plugin, Player player, String fileName) {
        this.fileName = fileName;
        this.player = player;
        this.plugin = plugin;

        MenusFileManager menuFileManager = new MenusFileManager(plugin);
        menu = menuFileManager.getConfig(fileName);
        openMenus = plugin.getOpenMenus();
    }

    public void openMenu() {
        int size = menu.getInt("size", 27);
        if (size < 9) {
            size = 9;
        } else if (size > 54) {
            size = 54;
        } else if (size % 9 != 0) {
            int remainder = size % 9;
            if (remainder < 5) {
                size -= remainder;
            } else {
                size += (9 - remainder);
            }
        }
        String title = fixedText(menu.getString("menu_title"));

        inventory = Bukkit.createInventory(null, size, title);

        if (menu.isConfigurationSection("items")) {
            for (String itemKey : menu.getConfigurationSection("items").getKeys(false)) {
                String materialName = fixedText(menu.getString("items." + itemKey + ".material"));
                if (materialName.isEmpty()) {
                    continue;
                }

                Material material;
                if (materialName.startsWith("basehead-") || materialName.startsWith("head-")) {
                    material = Material.PLAYER_HEAD;
                } else {
                    material = Material.getMaterial(materialName.toUpperCase());
                }

                if (material == null) {
                    continue;
                }

                ItemSkullData skullData = null;
                if (materialName.startsWith("basehead-")){
                    String texture = materialName.replace("basehead-", "");
                    texture = PlaceholderUtils.setPlaceholders(plugin, player, texture);
                    skullData = new ItemSkullData(null, texture, null);
                } else if (materialName.startsWith("head-")) {
                    String owner = materialName.replace("head-", "");
                    owner = PlaceholderUtils.setPlaceholders(plugin, player, owner);
                    skullData = new ItemSkullData(owner, null, null);
                }

                int amount = menu.getInt("items." + itemKey + ".amount", 1);
                ItemStack item = new ItemStack(material, amount);
                item = ItemUtils.setSkullData(item, skullData, player);

                String type = menu.getString("items." + itemKey + ".type");
                if (type == null || type.equalsIgnoreCase("none")) {
                    if (menu.isSet("items." + itemKey + ".name")) {
                        String name = fixedText(menu.getString("items." + itemKey + ".name", materialName));
                        ItemMeta meta = item.getItemMeta();
                        if (meta != null) {
                            meta.setDisplayName(name);
                            item.setItemMeta(meta);
                        }
                    }

                    if (menu.isSet("items." + itemKey + ".lore")) {
                        List<String> lore = menu.getStringList("items." + itemKey + ".lore");
                        ItemMeta meta = item.getItemMeta();
                        if (meta != null) {
                            List<String> formattedLore = new ArrayList<>();
                            for (String line : lore) {
                                formattedLore.add(fixedText(line));
                            }
                            meta.setLore(formattedLore);
                            item.setItemMeta(meta);
                        }
                    }

                    item = itemSetMeta(item, itemKey);
                    handleSlots(menu, itemKey, item);
                } else if (type.equalsIgnoreCase("home")) {
                    List<Map<String, Object>> homes = HomeUtils.getHomes(plugin, player);
                    List<Integer> availableSlots = getAvailableSlots(menu, itemKey);
                    Set<String> addedHomes = new HashSet<>();

                    for (int i = 0; i < homes.size() && i < availableSlots.size(); i++) {
                        Map<String, Object> home = homes.get(i);
                        String homeName = (String) home.get("name");

                        if (addedHomes.contains(homeName)) {
                            continue;
                        }

                        Location location = (Location) home.get("location");

                        String itemName = fixedText(menu.getString("items." + itemKey + ".name", materialName));
                        itemName = PlaceholderUtils.setHomePlaceholders(plugin, player, itemName, homeName, location);

                        ItemMeta meta = item.getItemMeta();
                        if (meta != null) {
                            meta.setDisplayName(itemName);

                            if (menu.isSet("items." + itemKey + ".lore")) {
                                List<String> lore = menu.getStringList("items." + itemKey + ".lore");
                                List<String> formattedLore = new ArrayList<>();
                                for (String line : lore) {
                                    line = fixedText(line);
                                    line = PlaceholderUtils.setHomePlaceholders(plugin, player, line, homeName, location);
                                    formattedLore.add(line);
                                }
                                meta.setLore(formattedLore);
                            }
                            item.setItemMeta(meta);
                        }

                        item = itemSetMeta(item, itemKey);
                        int slot = availableSlots.get(i);
                        inventory.setItem(slot, item);
                        addedHomes.add(homeName);
                    }
                }
            }
        }

        player.openInventory(inventory);
        openMenus.put(player, this);
    }

    private ItemStack itemSetMeta(ItemStack itemStack, String itemKey){
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            if (menu.isSet("items." + itemKey + ".item_flags")) {
                List<String> flags = menu.getStringList("items." + itemKey + ".item_flags");
                for (String line : flags) {
                    line = fixedText(line);
                    try {
                        meta.addItemFlags(ItemFlag.valueOf(line.toUpperCase()));
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            }

            if (menu.isSet("items." + itemKey + ".enchantments")) {
                List<String> enchantments = menu.getStringList("items." + itemKey + ".enchantments");
                for (String line : enchantments) {
                    line = fixedText(line);
                    String[] parts = line.split(":");

                    try {
                        Enchantment enchantment = Enchantment.getByName(parts[0].toUpperCase());
                        if (enchantment != null) {
                            int level = (parts.length > 1) ? Integer.parseInt(parts[1]) : 1;
                            meta.addEnchant(enchantment, level, true);
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    private void handleSlots(FileConfiguration menu, String itemKey, ItemStack item) {
        if (menu.isSet("items." + itemKey + ".slot")) {
            int slot = menu.getInt("items." + itemKey + ".slot", 0);
            inventory.setItem(slot, item);
        }

        if (menu.isSet("items." + itemKey + ".slots")) {
            List<String> slotsList = menu.getStringList("items." + itemKey + ".slots");
            for (String slotRange : slotsList) {
                if (slotRange.contains("-")) {
                    String[] parts = slotRange.split("-");
                    try {
                        int startSlot = Integer.parseInt(parts[0].trim());
                        int endSlot = Integer.parseInt(parts[1].trim());
                        for (int i = startSlot; i <= endSlot; i++) {
                            inventory.setItem(i, item);
                        }
                    } catch (NumberFormatException ignored) { }
                } else {
                    try {
                        int slot = Integer.parseInt(slotRange.trim());
                        inventory.setItem(slot, item);
                    } catch (NumberFormatException ignored) { }
                }
            }
        }
    }

    private List<Integer> getAvailableSlots(FileConfiguration menu, String itemKey) {
        List<Integer> availableSlots = new ArrayList<>();
        if (menu.isSet("items." + itemKey + ".slot")) {
            availableSlots.add(menu.getInt("items." + itemKey + ".slot"));
        }

        if (menu.isSet("items." + itemKey + ".slots")) {
            List<String> slotsList = menu.getStringList("items." + itemKey + ".slots");
            for (String slotRange : slotsList) {
                if (slotRange.contains("-")) {
                    String[] parts = slotRange.split("-");
                    try {
                        int startSlot = Integer.parseInt(parts[0].trim());
                        int endSlot = Integer.parseInt(parts[1].trim());
                        for (int i = startSlot; i <= endSlot; i++) {
                            availableSlots.add(i);
                        }
                    } catch (NumberFormatException ignored) { }
                } else {
                    try {
                        int slot = Integer.parseInt(slotRange.trim());
                        availableSlots.add(slot);
                    } catch (NumberFormatException ignored) { }
                }
            }
        }
        return availableSlots;
    }

    public void clickMenu(int targetSlot, String clickType) {
        int highestPriority = Integer.MIN_VALUE;

        if (menu.isConfigurationSection("items")) {
            for (String itemKey : menu.getConfigurationSection("items").getKeys(false)) {
                List<Integer> availableSlots = getAvailableSlots(menu, itemKey);

                if (availableSlots.contains(targetSlot)) {
                    int priority = menu.getInt("items." + itemKey + ".priority", 0);

                    if (priority > highestPriority) {
                        highestPriority = priority;
                        String materialName = fixedText(menu.getString("items." + itemKey + ".material"));
                        Material material = Material.getMaterial(materialName.toUpperCase());

                        if (material != null) {
                            String type = menu.getString("items." + itemKey + ".type");

                            if (type != null && type.equalsIgnoreCase("home")) {
                                List<Map<String, Object>> homes = HomeUtils.getHomes(plugin, player);
                                int index = availableSlots.indexOf(targetSlot);
                                if (index >= 0 && index < homes.size()) {
                                    Map<String, Object> home = homes.get(index);
                                    String homeName = (String) home.get("name");
                                    Location homeLocation = (Location) home.get("location");

                                    ActionsManager actionsManager = new ActionsManager(plugin, plugin.getMainMenuManager().getConfig(fileName), "items." + itemKey + "." + clickType);
                                    actionsManager.itemMenuHome(player, homeName, homeLocation);
                                }
                            } else {
                                ActionsManager actionsManager = new ActionsManager(plugin, plugin.getMainMenuManager().getConfig(fileName), "items." + itemKey + "." + clickType);
                                actionsManager.itemMenu(player);
                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean isMenuOpen(Player player) {
        return openMenus != null && openMenus.containsKey(player);
    }

    public static void closeMenu(Player player) {
        openMenus.remove(player);
    }

    public static MenuManager getMenu(Player player) {
        return openMenus.get(player);
    }

    public Inventory getInventory() {
        return inventory;
    }

    private String fixedText(String text){
        text = PlaceholderUtils.setPlaceholders(plugin, player, text);
        return MessagesManager.getColoredMessage(text);
    }
}
