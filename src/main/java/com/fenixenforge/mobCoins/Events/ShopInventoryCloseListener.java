package com.fenixenforge.mobCoins.Events;

import com.fenixenforge.mobCoins.Utils.ShopInventoryUpdaterManager;
import com.fenixenforge.mobCoins.ymlData.Shop;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class ShopInventoryCloseListener
        implements Listener {
    @EventHandler public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(Shop.getTitle())) {
            Player player = (Player) event.getPlayer();
            if (ShopInventoryUpdaterManager.getUpdater(player) != null) {
                ShopInventoryUpdaterManager.getUpdater(player).cancel();
                ShopInventoryUpdaterManager.removeUpdater(player);
            }
        }
    }
}
