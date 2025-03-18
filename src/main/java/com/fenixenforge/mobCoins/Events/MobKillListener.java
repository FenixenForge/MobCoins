package com.fenixenforge.mobCoins.Events;

import com.fenixenforge.mobCoins.Handlers.DataBase.UserManager;
import com.fenixenforge.mobCoins.Handlers.Events.AutoListener;
import com.fenixenforge.mobCoins.MobCoins;
import com.fenixenforge.mobCoins.Utils.MessageColor;
import com.fenixenforge.mobCoins.Utils.MobReward;
import com.fenixenforge.mobCoins.Utils.Variables;
import com.fenixenforge.mobCoins.ymlData.Config;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;

public class MobKillListener
        implements AutoListener {

    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        // Verifica que el mob fue asesinado por un jugador
        if (entity.getKiller() != null) {
            Player player = entity.getKiller();
            EntityType killedType = entity.getType();

            // Obtén la lista de recompensas configuradas
            List<MobReward> rewards = Config.getMobRewards();
            for (MobReward reward : rewards) {
                if (reward.getMobType() == killedType) {
                    double randomValue = Math.random() * 100;
                    if (randomValue < reward.getChance()) {
                        int coins = reward.getCoins();
                        UserManager userManager = new UserManager(MobCoins.getInstance());
                        userManager.AddBalance(player, coins);

                        // Recupera el mensaje configurable y reemplaza los placeholders
                        String message = Config.getMobKillMessage();
                        message = message.replace("%coins%", String.valueOf(coins)).replace("%mob%", killedType.name());
                        player.sendMessage(MessageColor.MessageColor(Variables.PlName + " " + message));
                    }
                }
            }
        }
    }
}
