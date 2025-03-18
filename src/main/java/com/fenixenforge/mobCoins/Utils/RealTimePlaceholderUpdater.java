package com.fenixenforge.mobCoins.Utils;

import com.fenixenforge.mobCoins.Inventory.ShopInventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class RealTimePlaceholderUpdater {

    private BukkitTask task;
    private final Player player;
    private final Inventory inventory;
    private final JavaPlugin plugin;
    private final String dynamicPlaceholder = "%nextshop%"; // Placeholder dinámico

    public RealTimePlaceholderUpdater(Player player, Inventory inventory, JavaPlugin plugin) {
        this.player = player;
        this.inventory = inventory;
        this.plugin = plugin;
    }

    public void start() {
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            // Comprobar que el inventario abierto tenga nuestro ShopInventoryHolder
            if (!(player.getOpenInventory().getTopInventory().getHolder() instanceof ShopInventoryHolder)) {
                cancel();
                return;
            }
            // Recorre cada slot y actualiza los ítems con el placeholder dinámico
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack item = inventory.getItem(i);
                if (item != null && item.hasItemMeta()) {
                    ItemMeta meta = item.getItemMeta();
                    boolean updated = false;
                    if (meta.hasDisplayName()) {
                        String display = meta.getDisplayName();
                        if (display.contains(dynamicPlaceholder)) {
                            String newDisplay = display.replace(dynamicPlaceholder, IntervalUtil.getFormattedTimeUntilNextShopChange());
                            meta.setDisplayName(newDisplay);
                            updated = true;
                        }
                    }
                    if (meta.hasLore()) {
                        List<String> lore = meta.getLore();
                        List<String> newLore = new ArrayList<>();
                        for (String line : lore) {
                            if (line.contains(dynamicPlaceholder)) {
                                line = line.replace(dynamicPlaceholder, IntervalUtil.getFormattedTimeUntilNextShopChange());
                                updated = true;
                            }
                            newLore.add(line);
                        }
                        meta.setLore(newLore);
                    }
                    if (updated) {
                        item.setItemMeta(meta);
                        inventory.setItem(i, item);
                    }
                }
            }
        }, 20L, 20L);
    }

    public void cancel() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }
}
