package com.fenixenforge.mobCoins.Inventory;

import com.fenixenforge.mobCoins.Handlers.Yaml.HandlerLoader;
import com.fenixenforge.mobCoins.Handlers.inventory.InventoryUI;
import com.fenixenforge.mobCoins.Utils.ItemsBuilder;
import com.fenixenforge.mobCoins.Utils.RealTimePlaceholderUpdater;
import com.fenixenforge.mobCoins.Utils.ShopInventoryUpdaterManager;
import com.fenixenforge.mobCoins.Utils.ShopItem;
import com.fenixenforge.mobCoins.ymlData.Shop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopInventory
        implements InventoryUI {

    private static Inventory inventory;
    private static Map<Integer, ShopItem> slotItemMap = new HashMap<>();
    private static ShopInventoryHolder inventoryHolder = new ShopInventoryHolder();

    @Override public String ID() {
        return "shop";
    }

    @Override public Inventory CreateInv(JavaPlugin plugin) {
        if (inventory == null) {
            inventory = Bukkit.createInventory(inventoryHolder, Shop.getSlot(), Shop.getTitle());
            inventoryHolder.setInventory(inventory);
            slotItemMap.clear();
            for (ShopItem item : Shop.getCurrentShop().getItems().values()) {
                ItemsBuilder builder = new ItemsBuilder(item.getMaterial()).setName(item.getName()).setLore(item.getLore()).addEnchantmentsFromMap(item.getEnchantments());
                for (String flag : item.getFlags()) {
                    builder.addFlag(flag);
                }
                ItemStack itemStack = builder.build();
                for (int slot : item.getSlots()) { // Iterar sobre la lista de slots
                    if (slot >= 0 && slot < inventory.getSize()) {
                        inventory.setItem(slot, itemStack);
                        slotItemMap.put(slot, item);
                    } else {
                        System.err.println("Slot inválido (" + slot + ") para el ítem: " + item.getId());
                    }
                }
            }
        }
        return inventory;
    }

    @Override public void Open(Player player, JavaPlugin plugin) {
        Inventory template = CreateInv(plugin);
        Inventory cloned = Bukkit.createInventory(inventoryHolder, template.getSize(), Shop.getTitle());

        for (int i = 0; i < template.getSize(); i++) {
            ItemStack item = template.getItem(i);
            if (item != null) {
                ItemStack clonedItem = item.clone();
                if (clonedItem.hasItemMeta()) {
                    ItemMeta meta = clonedItem.getItemMeta();
                    if (meta.hasDisplayName()) {
                        String name = meta.getDisplayName();
                        name = HandlerLoader.processPlaceholder(player, name);
                        meta.setDisplayName(name);
                    }
                    if (meta.hasLore()) {
                        List<String> lore = meta.getLore();
                        List<String> newLore = new ArrayList<>();
                        for (String line : lore) {
                            newLore.add(HandlerLoader.processPlaceholder(player, line));
                        }
                        meta.setLore(newLore);
                    }
                    clonedItem.setItemMeta(meta);
                }
                cloned.setItem(i, clonedItem);
            }
        }

        player.openInventory(cloned);
        RealTimePlaceholderUpdater updater = new RealTimePlaceholderUpdater(player, cloned, plugin);
        updater.start();
        ShopInventoryUpdaterManager.addUpdater(player, updater);
    }

    public static ShopItem getShopItemBySlot(int slot) {
        return slotItemMap.get(slot);
    }

    public static void reloadInventory() {
        inventory = null;
        slotItemMap.clear();
    }
}