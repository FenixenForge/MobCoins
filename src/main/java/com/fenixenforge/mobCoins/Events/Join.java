package com.fenixenforge.mobCoins.Events;

import com.fenixenforge.mobCoins.Handlers.DataBase.UserManager;
import com.fenixenforge.mobCoins.Handlers.Events.AutoListener;
import com.fenixenforge.mobCoins.MobCoins;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join
        implements AutoListener {

    @EventHandler public void onPlayerJoin(PlayerJoinEvent event) {
        UserManager userManager = new UserManager(MobCoins.getInstance());
        Player player = (Player) event.getPlayer();
        userManager.CreateUser(player);
    }
}
