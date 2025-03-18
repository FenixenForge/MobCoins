package com.fenixenforge.mobCoins.Events;

import com.fenixenforge.mobCoins.Handlers.DataBase.UserManager;
import com.fenixenforge.mobCoins.Handlers.Events.AutoListener;
import com.fenixenforge.mobCoins.Handlers.Yaml.HandlerLoader;
import com.fenixenforge.mobCoins.Inventory.ShopInventory;
import com.fenixenforge.mobCoins.Inventory.ShopInventoryHolder;
import com.fenixenforge.mobCoins.MobCoins;
import com.fenixenforge.mobCoins.Utils.MessageColor;
import com.fenixenforge.mobCoins.Utils.ShopItem;
import com.fenixenforge.mobCoins.Utils.Variables;
import com.fenixenforge.mobCoins.ymlData.Config;
import com.fenixenforge.mobCoins.ymlData.Shop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ShopInventoryClickListener
        implements AutoListener {
    @EventHandler public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof ShopInventoryHolder) {
            event.setCancelled(true);
        }
        if (event.getView().getTitle().equals(Shop.getTitle())) {
            if (event.getCurrentItem() == null) {
                return;
            }

            int slot = event.getRawSlot();
            ShopItem shopItem = ShopInventory.getShopItemBySlot(slot);
            if (shopItem == null) {
                return;
            }

            Player player = (Player) event.getWhoClicked();
            UserManager userManager = new UserManager(MobCoins.getInstance());
            int balance = userManager.BalanceUser(player);

            String itemName = HandlerLoader.processPlaceholder(player, shopItem.getName());

            if (balance < shopItem.getPrice()) {
                player.closeInventory();
                String insufficientMsg = Config.getInsufficientFundsMessage();
                insufficientMsg = insufficientMsg.replace("%item%", itemName).replace("%price%", String.valueOf(shopItem.getPrice()));
                player.sendMessage(MessageColor.MessageColor(insufficientMsg));
                return;
            }

            if (shopItem.getPrice() == 0) {
                return;
            }

            userManager.RemoveBalance(player, shopItem.getPrice());

            for (String cmd : shopItem.getCommands()) {
                String[] parts = cmd.split(",");  // Aquí declaras y asignas la variable 'parts'
                for (String part : parts) {  // Usas 'parts' correctamente
                    String commandToExecute = part.trim().replace("%player%", player.getName());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandToExecute);
                }
            }

            String successMsg = Config.getPurchaseSuccessMessage();
            successMsg = successMsg.replace("%item%", itemName).replace("%price%", String.valueOf(shopItem.getPrice()));
            player.sendMessage(MessageColor.MessageColor(Variables.PlName + " " + successMsg));
        }
    }
}