package com.fenixenforge.mobCoins.Handlers.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.Collections;
import java.util.List;

public abstract class BaseCommand
        extends BukkitCommand {
    private TabCompleter tabCompleter;

    public BaseCommand(String name) {
        super(name);
    }

    @Override public abstract boolean execute(CommandSender sender, String label, String[] args);

    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    public void setTabCompleter(TabCompleter tabCompleter) {
        this.tabCompleter = tabCompleter;
    }

    public TabCompleter getTabCompleter() {
        return tabCompleter;
    }

    @Override public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        if (tabCompleter != null) {
            List<String> completions = tabCompleter.onTabComplete(sender, this, alias, args);
            if (completions != null) {
                return completions;
            }
        }
        return getTabCompletions(sender, args);
    }
}
