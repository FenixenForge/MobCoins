package com.fenixenforge.mobCoins.Utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageColor {

    private static CommandSender sender;

    public static final Pattern HEX_PATTERN = Pattern.compile("#([A-Fa-f0-9]{6})");

    public static String MessageColor(String m) {
        Matcher matcher = HEX_PATTERN.matcher(m);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(buffer, ChatColor.of("#" + matcher.group(1)).toString());
        }

        matcher.appendTail(buffer);
        m = buffer.toString();

        return org.bukkit.ChatColor.translateAlternateColorCodes('&', m);
    }

    public static void Console(String m) {
        Bukkit.getConsoleSender().sendMessage(MessageColor(m));
    }

    public static void setSender(CommandSender sender) {
        MessageColor.sender = sender;
    }

    public static void Sender(String m) {
        sender.sendMessage(MessageColor(m));
    }
}
