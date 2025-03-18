package com.fenixenforge.mobCoins.Utils;

import com.fenixenforge.mobCoins.Handlers.DataBase.UserManager;
import com.fenixenforge.mobCoins.MobCoins;
import com.fenixenforge.mobCoins.ymlData.Shop;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Placeholders
        extends PlaceholderExpansion {
    private final JavaPlugin plugin;
    private final UserManager UM;

    public Placeholders(JavaPlugin plugin) {
        this.plugin = plugin;
        this.UM = new UserManager((MobCoins) plugin);
    }

    @Override public boolean canRegister() {
        return true;
    }

    @Override public boolean register() {
        boolean registered = super.register();
        plugin.getLogger().info("Placeholder expansion " + getIdentifier() + " registration result: " + registered);
        return registered;
    }

    @Override public String getIdentifier() {
        return "mobcoins";
    }

    @Override public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override public String getVersion() {
        return Variables.Version;
    }

    @Override public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }

        if (identifier.equals("balance")) {
            int balance = UM.BalanceUser(player);
            return String.valueOf(balance);
        }

        if (identifier.equals("balance_format")) {
            int balance = UM.BalanceUser(player);
            return FormatBalance(balance);
        }
        if (identifier.equals("nextshop")) {
            if (!Shop.isRandomMenu()) {
                return "";
            }
            return IntervalUtil.getFormattedTimeUntilNextShopChange();
        }
        if (identifier.equals("nextshop_seconds")) {
            if (!Shop.isRandomMenu()) {
                return "";
            }
            return String.valueOf(IntervalUtil.getSecondsUntilNextShopChange());
        }

        if (identifier.equals("nextshop_hms")) {
            if (!Shop.isRandomMenu()) {
                return "";
            }
            return IntervalUtil.getFormattedTimeHMS(false);
        }

        if (identifier.equals("nextshop_HMS")) {
            if (!Shop.isRandomMenu()) {
                return "";
            }
            return IntervalUtil.getFormattedTimeHMS(true);
        }

        if (identifier.startsWith("balancetop_user_") || identifier.startsWith("balancetop_current_")) {
            try {
                int position = Integer.parseInt(identifier.replaceAll("[^0-9]", "")) - 1;
                List<String> topUsers = UM.BalanceTop();

                if (position >= 0 && position < topUsers.size()) {
                    String[] parts = topUsers.get(position).split(" - ");
                    if (identifier.startsWith("balancetop_user_")) {
                        return parts[0].split("\\. ")[1];
                    } else {
                        return parts[1].replace(" coins", "");
                    }
                }
            } catch (NumberFormatException e) {
                return "0";
            }
        }

        return null;
    }

    private String FormatBalance(int balance) {
        if (balance >= 1_000_000_000_000L) { // Trillón
            return format(balance, 1_000_000_000_000L, "T");
        } else if (balance >= 1_000_000_000) { // Billón
            return format(balance, 1_000_000_000, "B");
        } else if (balance >= 1_000_000) { // Millón
            return format(balance, 1_000_000, "M");
        } else if (balance >= 1_000) { // Mil
            return format(balance, 1_000, "K");
        } else {
            return String.valueOf(balance);
        }
    }

    private String format(int balance, long divisor, String suffix) {
        double result = (double) balance / divisor;
        return (balance % divisor >= 100) ? String.format("%.1f%s", result, suffix) : String.format("%d%s", balance / divisor, suffix);
    }
}
