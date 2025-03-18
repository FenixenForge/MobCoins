package com.fenixenforge.mobCoins.Utils;

import org.bukkit.command.CommandSender;

public class Commands {

    public static boolean SubCommands(String[] args, String m) {
        return args[0].equalsIgnoreCase(m);
    }

    public static boolean SubCommands2(String[] args, String m, int i) {
        return args[i].equalsIgnoreCase(m);
    }

    public static boolean Permissions(CommandSender sender, String m) {
        sender.hasPermission(m);
        return false;
    }
}
