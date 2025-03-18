package com.fenixenforge.mobCoins.Handlers.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CommandLoader {

    private static final String BASE_PACKAGE = "com.fenixenforge.mobCoins.Commands";

    public static void registerAll(JavaPlugin plugin) {
        CommandMap commandMap = getCommandMap();
        if (commandMap == null) {
            return;
        }

        Reflections reflections = new Reflections(BASE_PACKAGE);
        Set<Class<? extends BaseCommand>> classes = reflections.getSubTypesOf(BaseCommand.class);

        for (Class<? extends BaseCommand> clazz : classes) {
            try {
                BaseCommand commandInstance = clazz.getDeclaredConstructor().newInstance();
                commandMap.register(plugin.getName(), commandInstance);

                commandInstance.setTabCompleter(new CustomTabCompleter(commandInstance));
            } catch (Exception e) {
            }
        }
    }

    private static CommandMap getCommandMap() {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            return (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class CustomTabCompleter
            implements TabCompleter {
        private final BaseCommand commandInstance;

        public CustomTabCompleter(BaseCommand commandInstance) {
            this.commandInstance = commandInstance;
        }

        @Override public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            List<String> completions = commandInstance.getTabCompletions(sender, args);
            return completions != null ? completions : Collections.emptyList();
        }
    }
}
