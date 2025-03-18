package com.fenixenforge.mobCoins.ymlData;

import com.fenixenforge.mobCoins.Handlers.Yaml.YamlFile;
import com.fenixenforge.mobCoins.Utils.ShopItem;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Shop
        implements YamlFile {

    private static FileConfiguration config;
    private static Shop instance;

    // Configuración global
    private static String titleMenu;
    private static int slots;
    private static boolean randomMenu;
    private static String intervalo; // Lo puedes convertir a ticks si lo requieres

    // Mapa de shops (cada clave corresponde a "shop1", "shop2", etc.)
    private static Map<String, ShopData> shops = new HashMap<>();
    // Shop actualmente activo
    private static ShopData currentShop;

    private Shop() {}

    public static void init(FileConfiguration fileConfiguration) {
        config = fileConfiguration;
        instance = new Shop();
        loadGlobalConfig();
        loadShops();
    }

    private static void loadGlobalConfig() {
        titleMenu = config.getString("title_menu", "Shop");
        slots = config.getInt("slots", 54);
        randomMenu = config.getBoolean("ramdon_menu", false);
        intervalo = config.getString("intervalo", "60m");
    }

    private static void loadShops() {
        shops.clear();
        Set<String> keys = config.getKeys(false);

        for (String key : keys) {
            if (key.equalsIgnoreCase("title_menu") || key.equalsIgnoreCase("slots") || key.equalsIgnoreCase("ramdon_menu") || key.equalsIgnoreCase("intervalo")) {
                continue;
            }

            Map<String, ShopItem> shopItems = loadShopItems(key);
            shops.put(key, new ShopData(shopItems));
        }

        // Selecciona la tienda actual
        if (!shops.isEmpty()) {
            if (randomMenu) {
                selectRandomShop();
            } else {
                // Selecciona la primera tienda en el orden definido en el YAML
                String firstKey = keys.stream().filter(k -> !k.equalsIgnoreCase("title_menu") && !k.equalsIgnoreCase("slots") && !k.equalsIgnoreCase("ramdon_menu") && !k.equalsIgnoreCase("intervalo")).findFirst().orElse(null);

                if (firstKey != null) {
                    currentShop = shops.get(firstKey);
                }
            }
        }
    }

    private static Map<String, ShopItem> loadShopItems(String shopKey) {
        Map<String, ShopItem> shopItems = new HashMap<>();
        if (config.contains(shopKey)) {
            for (String key : config.getConfigurationSection(shopKey).getKeys(false)) {
                String path = shopKey + "." + key;
                List<Integer> slots = config.getIntegerList(path + ".slots");  // Leer la lista de slots
                if (slots.isEmpty()) {
                    // Si no se define una lista de slots, intenta leer un único slot (para compatibilidad hacia atrás)
                    int singleSlot = config.getInt(path + ".slot", -1);
                    if (singleSlot != -1) {
                        slots = List.of(singleSlot);
                    } else {
                        System.err.println("No se definieron slots para el ítem: " + key + " en la sección: " + shopKey);
                        continue; // Saltar este ítem si no tiene slots definidos
                    }
                }

                String materialStr = config.getString(path + ".material", "STONE");
                Material material = Material.matchMaterial(materialStr);
                if (material == null) {
                    material = Material.STONE;
                }
                String name = config.getString(path + ".name", key);
                List<String> lore = config.getStringList(path + ".lore");
                List<String> flags = config.getStringList(path + ".flags");
                List<String> enchantStrings = config.getStringList(path + ".enchantments");
                Map<Enchantment, Integer> enchantments = new HashMap<>();
                for (String enchantStr : enchantStrings) {
                    String[] parts = enchantStr.split(":");
                    if (parts.length == 2) {
                        Enchantment enchant = Enchantment.getByName(parts[0].toUpperCase());
                        if (enchant != null) {
                            try {
                                int level = Integer.parseInt(parts[1]);
                                enchantments.put(enchant, level);
                            } catch (NumberFormatException e) {
                                System.out.println("Nivel de encantamiento inválido en " + path + ": " + enchantStr);
                            }
                        } else {
                            System.out.println("Encantamiento inválido en " + path + ": " + parts[0]);
                        }
                    }
                }

                int price = config.getInt(path + ".price", 0);
                List<String> commands = config.getStringList(path + ".commands");
                if (commands.isEmpty()) {
                    String singleCommand = config.getString(path + ".command", "");
                    if (!singleCommand.isEmpty()) {
                        commands.add(singleCommand);
                    }
                }
                ShopItem item = new ShopItem(key, slots, material, name, lore, flags, enchantments, price, commands);
                shopItems.put(key, item);
            }
        }
        return shopItems;
    }

    public static void selectRandomShop() {
        List<ShopData> list = new ArrayList<>(shops.values());

        if (list.isEmpty()) {
            currentShop = null;
            return;
        }

        if (config.getBoolean("ramdon_menu")) {
            Random random = new Random();
            currentShop = list.get(random.nextInt(list.size()));
        } else {
            currentShop = list.get(0);
        }
    }

    public static ShopData getCurrentShop() {
        return currentShop;
    }

    // Getters para la configuración global
    public static int getSlot() {
        return slots;
    }

    public static String getTitle() {
        return titleMenu;
    }

    public static boolean isRandomMenu() {
        return randomMenu;
    }

    public static String getIntervalo() {
        return intervalo;
    }

    // Método para recargar las shops (por ejemplo, en un scheduler)
    public static void reloadShop() {
        loadShops();
    }

    // Clase interna para almacenar los items de cada shop
    public static class ShopData {
        private Map<String, ShopItem> items;

        public ShopData(Map<String, ShopItem> items) {
            this.items = items;
        }

        public Map<String, ShopItem> getItems() {
            return items;
        }
    }
}