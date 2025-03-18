package com.fenixenforge.mobCoins.Utils;

import com.fenixenforge.mobCoins.Inventory.ShopInventory;
import com.fenixenforge.mobCoins.ymlData.Shop;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class IntervalUtil {

    private static int intervalTicks = 0;
    private static long intervalMillis = 0;
    private static long nextShopChangeMillis = 0;

    public static int parseTimeToTicks(String timeStr) {
        if (timeStr == null || timeStr.isEmpty()) {
            return 0;
        }
        timeStr = timeStr.trim().toLowerCase();
        char unit = timeStr.charAt(timeStr.length() - 1);
        String numberPart = timeStr.substring(0, timeStr.length() - 1);
        int value;
        try {
            value = Integer.parseInt(numberPart);
        } catch (NumberFormatException e) {
            return 0;
        }
        switch (unit) {
            case 's':
                return value * 20;
            case 'm':
                return value * 60 * 20;
            case 'h':
                return value * 60 * 60 * 20;
            case 'd':
                return value * 24 * 60 * 60 * 20;
            default:
                return value * 20;
        }
    }

    public static String getFormattedTimeUntilNextShopChange() {
        long remaining = nextShopChangeMillis - System.currentTimeMillis();
        if (remaining < 0) {
            remaining = 0;
        }

        long seconds = (remaining / 1000) % 60;
        long minutes = (remaining / (1000 * 60)) % 60;
        long hours = (remaining / (1000 * 60 * 60));

        return hours + "h " + minutes + "m " + seconds + "s";
    }

    public static void start(JavaPlugin plugin) {
        intervalTicks = parseTimeToTicks(Shop.getIntervalo());
        intervalMillis = (long) intervalTicks * 50;
        nextShopChangeMillis = System.currentTimeMillis() + intervalMillis;
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Shop.reloadShop();
            ShopInventory.reloadInventory();
            nextShopChangeMillis = System.currentTimeMillis() + intervalMillis;
        }, intervalTicks, intervalTicks);
    }

    public static long getSecondsUntilNextShopChange() {
        long remaining = nextShopChangeMillis - System.currentTimeMillis();
        return Math.max(remaining / 1000, 0);
    }

    public static String getFormattedTimeHMS(boolean uppercase) {
        long seconds = getSecondsUntilNextShopChange();
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long sec = seconds % 60;

        String format = uppercase ? "%dH %dM %dS" : "%dh %dm %ds";
        return String.format(format, hours, minutes, sec);
    }

    public static void resetCounter() {
        intervalTicks = parseTimeToTicks(Shop.getIntervalo());
        intervalMillis = intervalTicks * 50L;
        nextShopChangeMillis = System.currentTimeMillis() + intervalMillis;
    }
}
