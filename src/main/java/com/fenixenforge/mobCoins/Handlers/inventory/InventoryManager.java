package com.fenixenforge.mobCoins.Handlers.inventory;

import com.fenixenforge.mobCoins.MobCoins;
import org.bukkit.entity.Player;

public class InventoryManager {

    public static void OpenInventory(Player player, String Id) {
        MobCoins plugin = MobCoins.getInstance();
        InventoryUI inventory = InventoryLoader.getInventory(Id);
        if (inventory != null) {
            inventory.Open(player, plugin);
        } else {
            player.sendMessage("Inventory not found: " + Id);
        }
    }
}
