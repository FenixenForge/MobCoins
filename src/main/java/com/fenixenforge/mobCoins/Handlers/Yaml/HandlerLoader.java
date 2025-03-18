package com.fenixenforge.mobCoins.Handlers.Yaml;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HandlerLoader {
    private static final String BASE_PACKAGE = "com.fenixenforge.mobCoins.ymlData";
    private static JavaPlugin plugin;
    private static final Map<Class<? extends YamlFile>, YamlData> loadedYamlFiles = new HashMap<>();

    public static void registerAll(JavaPlugin pluginInstance) {
        plugin = pluginInstance;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        Reflections reflections = new Reflections(BASE_PACKAGE);
        Set<Class<? extends YamlFile>> classes = reflections.getSubTypesOf(YamlFile.class);

        for (Class<? extends YamlFile> clazz : classes) {
            try {
                String simpleName = clazz.getSimpleName();
                String fileName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1) + ".yml";

                String packageName = clazz.getPackage().getName();
                String relativePath = "";
                if (packageName.length() > BASE_PACKAGE.length()) {
                    relativePath = packageName.substring(BASE_PACKAGE.length() + 1).replace('.', File.separatorChar);
                }

                File file;
                if (!relativePath.isEmpty()) {
                    file = new File(plugin.getDataFolder(), relativePath + File.separator + fileName);
                } else {
                    file = new File(plugin.getDataFolder(), fileName);
                }

                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                if (!file.exists()) {
                    String resourcePath = (!relativePath.isEmpty() ? relativePath.replace(File.separatorChar, '/') + "/" : "") + fileName;
                    plugin.saveResource(resourcePath, false);
                }

                FileConfiguration config = YamlConfiguration.loadConfiguration(file);

                Method initMethod = clazz.getMethod("init", FileConfiguration.class);
                initMethod.invoke(null, config);

                loadedYamlFiles.put(clazz, new YamlData(file, config));
            } catch (NoSuchMethodException ignored) {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveAll() {
        for (Map.Entry<Class<? extends YamlFile>, YamlData> entry : loadedYamlFiles.entrySet()) {
            YamlData data = entry.getValue();
            try {
                ((YamlConfiguration) data.getConfig()).save(data.getFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void reloadAll() {
        for (Map.Entry<Class<? extends YamlFile>, YamlData> entry : loadedYamlFiles.entrySet()) {
            YamlData data = entry.getValue();
            try {
                FileConfiguration newConfig = YamlConfiguration.loadConfiguration(data.getFile());
                Method initMethod = entry.getKey().getMethod("init", FileConfiguration.class);
                initMethod.invoke(null, newConfig);
                data.setConfig(newConfig);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class YamlData {
        private final File file;
        private FileConfiguration config;

        public YamlData(File file, FileConfiguration config) {
            this.file = file;
            this.config = config;
        }

        public File getFile() {
            return file;
        }

        public FileConfiguration getConfig() {
            return config;
        }

        public void setConfig(FileConfiguration config) {
            this.config = config;
        }
    }

    public static String processPlaceholder(Player player, String text) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return PlaceholderAPI.setPlaceholders(player, text);
        }
        return text;
    }
}
