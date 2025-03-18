package com.fenixenforge.mobCoins.Utils;

import org.bukkit.entity.EntityType;

public class MobReward {
    private final EntityType mobType;
    private final double chance;
    private final int coins;

    public MobReward(EntityType mobType, double chance, int coins) {
        this.mobType = mobType;
        this.chance = chance;
        this.coins = coins;
    }

    public EntityType getMobType() {
        return mobType;
    }

    public double getChance() {
        return chance;
    }

    public int getCoins() {
        return coins;
    }
}
