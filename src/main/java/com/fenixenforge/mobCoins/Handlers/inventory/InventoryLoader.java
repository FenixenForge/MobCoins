package com.fenixenforge.mobCoins.Handlers.inventory;

import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InventoryLoader {

    private static final String Package = "com.fenixenforge.mobCoins.Inventory";
    private static final Map<String, InventoryUI> inventories = new HashMap<>();

    public static void RegisterAll(JavaPlugin plugin) {
        Reflections reflections = new Reflections(Package);
        Set<Class<? extends InventoryUI>> classes = reflections.getSubTypesOf(InventoryUI.class);

        for (Class<? extends InventoryUI> clazz : classes) {
            try {

                InventoryUI inventoryInstance = clazz.getDeclaredConstructor().newInstance();
                inventories.put(inventoryInstance.ID(), inventoryInstance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static InventoryUI getInventory(String id) {
        return inventories.get(id);
    }
}
