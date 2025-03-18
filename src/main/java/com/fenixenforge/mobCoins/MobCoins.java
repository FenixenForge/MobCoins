package com.fenixenforge.mobCoins;

import com.fenixenforge.mobCoins.Handlers.Commands.CommandLoader;
import com.fenixenforge.mobCoins.Handlers.DataBase.DataBaseManager;
import com.fenixenforge.mobCoins.Handlers.Events.EventLoader;
import com.fenixenforge.mobCoins.Handlers.Yaml.HandlerLoader;
import com.fenixenforge.mobCoins.Handlers.inventory.InventoryLoader;
import com.fenixenforge.mobCoins.Inventory.ShopInventory;
import com.fenixenforge.mobCoins.Utils.IntervalUtil;
import com.fenixenforge.mobCoins.Utils.MessageColor;
import com.fenixenforge.mobCoins.Utils.Placeholders;
import com.fenixenforge.mobCoins.Utils.Variables;
import com.fenixenforge.mobCoins.ymlData.Config;
import org.bukkit.plugin.java.JavaPlugin;

public final class MobCoins
        extends JavaPlugin {
    public static MobCoins instance;
    private DataBaseManager dbManager;

    @Override public void onEnable() {
        instance = this;
        HandlerLoader.registerAll(this);
        Variables.init(this);
        Variables.PlName = Config.getServerName();
        HandlerLoader.reloadAll();
        HandlerLoader.saveAll();
        CommandLoader.registerAll(this);
        EventLoader.registerAll(this);
        InventoryLoader.RegisterAll(this);
        ShopInventory.reloadInventory();

        dbManager = new DataBaseManager(this);
        dbManager.connect();

        IntervalUtil.start(this);

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholders(this).register();
            MessageColor.Console(Variables.PlName + "&a PlaceholderAPI expansion for MobCoins registered!");
        } else {
            MessageColor.Console(Variables.PlName + "&c PlaceholderAPI not found! Placeholders will not be available.");
        }

        MessageColor.Console(Variables.PlName + "&a - Enabled&b " + Variables.Version + "ᵛ");
    }

    @Override public void onDisable() {
        if (dbManager != null) {
            dbManager.close();
        }
        HandlerLoader.reloadAll();
        HandlerLoader.saveAll();
        ShopInventory.reloadInventory();
        MessageColor.Console(Variables.PlName + "&c - Disabled&b " + Variables.Version + "ᵛ");
    }

    public DataBaseManager getDbManager() {
        return dbManager;
    }

    public static MobCoins getInstance() {
        return instance;
    }
}
