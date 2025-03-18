package com.fenixenforge.mobCoins.Utils;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Variables {

    public static String Name;
    public static String Version;
    public static String Main;
    public static List<String> Authors;
    public static String PlName;

    public static void init(JavaPlugin plugin) {
        Name = plugin.getName();
        Version = plugin.getDescription().getVersion();
        Main = plugin.getDescription().getMain();
        Authors = plugin.getDescription().getAuthors();
    }
}
