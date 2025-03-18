package com.fenixenforge.mobCoins.Inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ShopInventoryHolder
        implements InventoryHolder {
    private Inventory inventory;

    @Override public Inventory getInventory() {
        return null; // No es necesario retornar el inventario aquí; se usa solo para identificar el holder.
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
