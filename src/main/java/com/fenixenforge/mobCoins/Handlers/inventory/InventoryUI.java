package com.fenixenforge.mobCoins.Handlers.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public interface InventoryUI {

    String ID();

    Inventory CreateInv(JavaPlugin plugin);

    default void Open(Player player, JavaPlugin plugin) {
        player.openInventory(CreateInv(plugin));
    }
}
