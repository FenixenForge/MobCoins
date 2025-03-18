package com.fenixenforge.mobCoins.Utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.List;
import java.util.Map;

public class ShopItem {
    private final String id;
    private final List<Integer> slots; // Cambiado a List<Integer>
    private final Material material;
    private final String name;
    private final List<String> lore;
    private final List<String> flags;
    private final Map<Enchantment, Integer> enchantments;
    private final int price;
    private final List<String> commands;

    public ShopItem(String id, List<Integer> slots, Material material, String name, List<String> lore, List<String> flags, Map<Enchantment, Integer> enchantments, int price, List<String> commands) {
        this.id = id;
        this.slots = slots;
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.flags = flags;
        this.enchantments = enchantments;
        this.price = price;
        this.commands = commands;
    }

    public String getId() {
        return id;
    }

    public List<Integer> getSlots() { // Retorna la lista de slots
        return slots;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    public int getPrice() {
        return price;
    }

    public List<String> getFlags() {
        return flags;
    }

    public List<String> getCommands() {
        return commands;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }
}