package com.fenixenforge.mobCoins.Handlers.Events;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.util.Set;

public class EventLoader {

    private static final String BASE_PACKAGE = "com.fenixenforge.mobCoins.Events";

    public static void registerAll(JavaPlugin plugin) {
        Reflections reflections = new Reflections(BASE_PACKAGE);
        Set<Class<? extends AutoListener>> classes = reflections.getSubTypesOf(AutoListener.class);

        for (Class<? extends AutoListener> clazz : classes) {
            try {
                AutoListener listenerInstance = clazz.getDeclaredConstructor().newInstance();
                Bukkit.getPluginManager().registerEvents((Listener) listenerInstance, plugin);
            } catch (Exception e) {

            }
        }
    }
}
