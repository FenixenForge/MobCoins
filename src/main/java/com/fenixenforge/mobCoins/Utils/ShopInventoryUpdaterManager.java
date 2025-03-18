package com.fenixenforge.mobCoins.Utils;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ShopInventoryUpdaterManager {
    private static final Map<String, RealTimePlaceholderUpdater> updaters = new HashMap<>();

    public static void addUpdater(Player player, RealTimePlaceholderUpdater updater) {
        updaters.put(player.getUniqueId().toString(), updater);
    }

    public static RealTimePlaceholderUpdater getUpdater(Player player) {
        return updaters.get(player.getUniqueId().toString());
    }

    public static void removeUpdater(Player player) {
        updaters.remove(player.getUniqueId().toString());
    }
}