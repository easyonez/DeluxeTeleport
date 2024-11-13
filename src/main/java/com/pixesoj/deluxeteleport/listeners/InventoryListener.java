package com.pixesoj.deluxeteleport.listeners;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.MenuManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryListener implements Listener {

    private final DeluxeTeleport plugin;

    public InventoryListener(DeluxeTeleport plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void clickInventory(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (MenuManager.isMenuOpen(player)) {
            MenuManager menuManager = MenuManager.getMenu(player);

            if (event.getInventory().equals(menuManager.getInventory())) {
                event.setCancelled(true);

                menuManager.clickMenu(event.getSlot(), "click_actions");

                ClickType clickType = event.getClick();
                switch (clickType) {
                    case LEFT:
                        menuManager.clickMenu(event.getSlot(), "left_click_actions");
                        break;
                    case RIGHT:
                        menuManager.clickMenu(event.getSlot(), "right_click_actions");
                        break;
                    case MIDDLE:
                        menuManager.clickMenu(event.getSlot(), "middle_click_actions");
                        break;
                    case SHIFT_LEFT:
                        menuManager.clickMenu(event.getSlot(), "shift_left_click_actions");
                        break;
                    case SHIFT_RIGHT:
                        menuManager.clickMenu(event.getSlot(), "shift_right_click_actions");
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @EventHandler
    public void closeInventory(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();
        if (MenuManager.isMenuOpen(player)) {
            MenuManager.closeMenu(player);
        }
    }
}
