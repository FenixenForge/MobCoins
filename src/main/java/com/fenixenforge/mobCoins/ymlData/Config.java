package com.fenixenforge.mobCoins.ymlData;

import com.fenixenforge.mobCoins.Handlers.Yaml.YamlFile;
import com.fenixenforge.mobCoins.Utils.MobReward;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class Config
        implements YamlFile {

    private static FileConfiguration config;
    private static Config instance;

    private Config() {}

    public static void init(FileConfiguration fileConfiguration) {
        config = fileConfiguration;
        instance = new Config();
    }

    public static Config getInstance() {
        return instance;
    }

    public static String getServerName() {

        return config.getString("prefix", "#ffff6d&lMobCoins");
    }

    public static String getMobKillMessage() {
        return config.getString("messages.mob_kill.success", "&aHas recibido %coins% coins por matar un %mob%.");
    }

    public static boolean useMySQL() {
        return config.getBoolean("use-mysql", false);
    }

    public static String getMySQLHost() {
        return config.getString("mysql.host", "localhost");
    }

    public static int getMySQLPort() {
        return config.getInt("mysql.port", 3306);
    }

    public static String getMySQLDatabase() {
        return config.getString("mysql.database", "mobcoins");
    }

    public static String getMySQLUsername() {
        return config.getString("mysql.username", "user");
    }

    public static String getMySQLPassword() {
        return config.getString("mysql.password", "password");
    }

    public static int getCoinsDefault() {
        return config.getInt("coins_default");
    }

    public static String getPurchaseSuccessMessage() {
        return config.getString("messages.purchase_success_message", "&aHas comprado %item% por %price% coins.");
    }

    public static String getInsufficientFundsMessage() {
        return config.getString("messages.insufficient_funds", "&cDinero insuficiente para comprar %item% (Precio: %price% coins).");
    }

    public static List<MobReward> getMobRewards() {
        List<MobReward> rewards = new ArrayList<>();
        if (config.contains("mobs")) {
            for (String key : config.getConfigurationSection("mobs").getKeys(false)) {
                String base = "mobs." + key + ".";
                String mobName = config.getString(base + "mob", "");
                double chance = config.getDouble(base + "chance", 0);
                int coins = config.getInt(base + "coins", 0);
                try {
                    EntityType type = EntityType.valueOf(mobName.toUpperCase());
                    rewards.add(new MobReward(type, chance, coins));
                } catch (Exception e) {
                    Bukkit.getLogger().warning("[MobCoins] Error en configuración de mob '" + key + "': " + mobName + " no es un EntityType válido.");
                }
            }
        }
        return rewards;
    }
}
