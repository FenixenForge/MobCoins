package com.fenixenforge.mobCoins.Utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemsBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemsBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemsBuilder setName(String name) {
        itemMeta.setDisplayName(MessageColor.MessageColor(name));
        return this;
    }

    public ItemsBuilder setLore(List<String> lore) {
        List<String> coloredLore = new ArrayList<>();
        for (String line : lore) {
            coloredLore.add(MessageColor.MessageColor(line));
        }

        itemMeta.setLore(coloredLore);
        return this;
    }

    public ItemsBuilder addLore(String line) {
        List<String> lore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();
        lore.add(MessageColor.MessageColor(line));
        itemMeta.setLore(lore);
        return this;
    }

    public ItemsBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemsBuilder addEnchantmentsFromMap(Map<Enchantment, Integer> enchantments) {
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            itemMeta.addEnchant(entry.getKey(), entry.getValue(), true);
        }
        return this;
    }

    public ItemsBuilder setUnbreakable(boolean unbreakable) {
        itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    public ItemsBuilder addFlag(String flagName) {
        try {
            ItmesFlags flagType = ItmesFlags.valueOf(flagName.toUpperCase());
            switch (flagType) {
                case ENCHANT:
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    break;
                case POTIONS:
                    itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                    break;
                case HIDE_ATTRIBUTES:
                    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    break;
                case HIDE_DESTROYS:
                    itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
                    break;
                case HIDE_ENCHANTS:
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    break;
                case HIDE_PLACED_ON:
                    itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
                    break;
                case HIDE_POTION_EFFECTS:
                    itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                    break;
                case HIDE_ARMOR_TRIM:
                    itemMeta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
                    break;
                case HIDE_UNBREAKABLE:
                    itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                    break;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("⚠ Error: Flag '" + flagName + "' no es válido.");
        }
        return this;
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}


