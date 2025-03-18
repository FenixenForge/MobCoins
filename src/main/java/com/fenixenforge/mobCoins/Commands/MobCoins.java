package com.fenixenforge.mobCoins.Commands;

import com.fenixenforge.mobCoins.Handlers.Commands.BaseCommand;
import com.fenixenforge.mobCoins.Handlers.DataBase.UserManager;
import com.fenixenforge.mobCoins.Handlers.Yaml.HandlerLoader;
import com.fenixenforge.mobCoins.Handlers.inventory.InventoryManager;
import com.fenixenforge.mobCoins.Inventory.ShopInventory;
import com.fenixenforge.mobCoins.Utils.Commands;
import com.fenixenforge.mobCoins.Utils.IntervalUtil;
import com.fenixenforge.mobCoins.Utils.MessageColor;
import com.fenixenforge.mobCoins.Utils.Variables;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

;

public class MobCoins
        extends BaseCommand {

    public MobCoins() {
        super("mobcoins");
    }

    @Override public boolean execute(CommandSender sender, String label, String[] args) {
        UserManager userManager = new UserManager(com.fenixenforge.mobCoins.MobCoins.getInstance());
        MessageColor.setSender(sender);

        Player player = null;

        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (args.length >= 1) {
            if (Commands.SubCommands(args, "help")) {

                Help();
            } else if (Commands.SubCommands(args, "balance")) {
                int balance = userManager.BalanceUser(player);
                MessageColor.Sender(Variables.PlName + "&r&a Cuentas con " + balance + " Coins");
            } else if (Commands.SubCommands(args, "balance-top")) {
                BalanceTop();
            } else if (Commands.SubCommands(args, "reload")) {
                if (sender.hasPermission("mobcoins.admin")) {
                    HandlerLoader.reloadAll();
                    HandlerLoader.saveAll();
                    ShopInventory.reloadInventory();
                    IntervalUtil.resetCounter();
                    MessageColor.Sender(Variables.PlName + "&a Reload Completado");
                    MessageColor.Console(Variables.PlName + "&a Reload Completado");
                    return true;
                } else {
                    MessageColor.Sender(Variables.PlName + "&cNo tienes permisos para ejecutar este comando");
                }
            } else if (Commands.SubCommands(args, "give")) {
                if (!sender.hasPermission("mobcoins.admin")) {
                    MessageColor.Sender(Variables.PlName + "&cNo tienes permisos para ejecutar este comando");
                    return true;
                }
                if (args.length == 1) {
                    MessageColor.Sender(Variables.PlName + "Usage: /mobcoins give <player> <amount>");
                    return true;
                }

                Player target = Bukkit.getPlayer(args[1]);

                if (target == null || !target.isOnline()) {
                    MessageColor.Sender(Variables.PlName + "&cEl jugador no esta en linea o no existe");
                }

                int amount;

                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    MessageColor.Sender(Variables.PlName + "&cLa cantidad debe ser un numero valido");
                    return true;
                }

                if (amount <= 0) {
                    MessageColor.Sender(Variables.PlName + "&cLa cantidad debe ser mayor a 0");
                    return true;
                }

                userManager.AddBalance(target, amount);

                MessageColor.Sender(Variables.PlName + "&aHas dado &e" + amount + " MobCoins &aa " + target.getName() + ".");
                target.sendMessage(MessageColor.MessageColor(Variables.PlName + "&aHas recibido &e" + amount + " MobCoins &ade un administrador."));
                return true;
            } else if (Commands.SubCommands(args, "remove")) {
                if (!sender.hasPermission("mobcoins.admin")) {
                    MessageColor.Sender(Variables.PlName + "&cNo tienes permisos para ejecutar este comando");
                    return true;
                }
                if (args.length == 1) {
                    MessageColor.Sender(Variables.PlName + "Usage: /mobcoins remove <player> <amount>");
                    return true;
                }

                Player target = Bukkit.getPlayer(args[1]);

                if (target == null || !target.isOnline()) {
                    MessageColor.Sender(Variables.PlName + "&cEl jugador no esta en linea o no existe");
                }

                int amount;

                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    MessageColor.Sender(Variables.PlName + "&cLa cantidad debe ser un numero valido");
                    return true;
                }

                if (amount <= 0) {
                    MessageColor.Sender(Variables.PlName + "&cLa cantidad debe ser mayor a 0");
                    return true;
                }

                userManager.RemoveBalance(target, amount);

                MessageColor.Sender(Variables.PlName + "&cHas quitado &e" + amount + " MobCoins &ca " + target.getName() + ".");
                target.sendMessage(MessageColor.MessageColor("&cTe ha quitado &e" + amount + " MobCoins &cun administrador."));
                return true;
            } else if (Commands.SubCommands(args, "pay")) {
                if (!(sender instanceof Player)) {
                    MessageColor.Sender(Variables.PlName + "&cEste comando solo puede ser usado por jugadores.");
                    return true;
                }

                if (args.length < 3) {
                    MessageColor.Sender(Variables.PlName + "&cUso: /mobcoins pay <jugador> <cantidad>");
                    return true;
                }

                Player senderPlayer = (Player) sender;
                Player target = Bukkit.getPlayer(args[1]);

                if (target == null || !target.isOnline()) {
                    MessageColor.Sender(Variables.PlName + "&cEl jugador no está en línea o no existe.");
                    return true;
                }

                if (senderPlayer.equals(target)) {
                    MessageColor.Sender(Variables.PlName + "&cNo puedes enviarte MobCoins a ti mismo.");
                    return true;
                }

                int amount;
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    MessageColor.Sender(Variables.PlName + "&cLa cantidad debe ser un número válido.");
                    return true;
                }

                if (amount <= 0) {
                    MessageColor.Sender(Variables.PlName + "&cLa cantidad debe ser mayor a 0.");
                    return true;
                }
                int senderBalance = userManager.BalanceUser(senderPlayer);

                if (senderBalance < amount) {
                    MessageColor.Sender(Variables.PlName + "&cNo tienes suficientes MobCoins para realizar esta transacción.");
                    return true;
                }

                userManager.RemoveBalance(senderPlayer, amount);
                userManager.AddBalance(target, amount);

                MessageColor.Sender(Variables.PlName + "&aHas enviado &e" + FormatBalance(amount) + " MobCoins &aa " + target.getName() + ".");
                target.sendMessage(MessageColor.MessageColor(Variables.PlName + "&aHas recibido &e" + FormatBalance(amount) + " MobCoins &ade " + senderPlayer.getName() + "."));

                return true;
            } else if (Commands.SubCommands(args, "shop")) {
                InventoryManager.OpenInventory(player, "shop");
            }
        } else if (sender.hasPermission("mobcoins.admin")) {
            HelpAdmin();
        } else if (!(sender instanceof Player)) {
            HelpConsole();
        } else {
            Help();
        }
        return true;
    }

    @Override public List<String> getTabCompletions(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 0 || (args.length == 1 && args[0].isEmpty())) {
            completions.addAll(List.of("help", "balance", "balancetop", "reload", "give", "remove", "pay", "shop"));
            return completions;
        }

        if (args.length == 1) {
            String prefix = args[0].toLowerCase();
            completions.addAll(List.of("help", "balance", "balancetop", "reload", "give", "remove", "pay", "shop").stream().filter(s -> s.startsWith(prefix)).toList());
            return completions;
        }

        if (args.length == 2) {
            String subCommand = args[0].toLowerCase();
            if (subCommand.equals("give") || subCommand.equals("remove") || subCommand.equals("pay")) {
                String playerPrefix = args[1].toLowerCase();
                completions.addAll(Bukkit.getOnlinePlayers().stream().map(player -> player.getName()).filter(name -> name.toLowerCase().startsWith(playerPrefix)).toList());
            }
            return completions;
        }

        if (args.length == 3) {
            String subCommand = args[0].toLowerCase();
            if (subCommand.equals("give") || subCommand.equals("remove") || subCommand.equals("pay")) {
                completions.add("<amount>");
            }
            return completions;
        }

        return completions;
    }

    public void Help() {
        MessageColor.Sender("#fb301f&l&m----------&r " + Variables.PlName + " Help " + "&m#fb301f&l&m----------");
        MessageColor.Sender("&f- &a/mobcoins Shop");
        MessageColor.Sender("&f- &a/mobcoins pay <player> <amount>");
        MessageColor.Sender("&f- &a/mobcoins balance");
        MessageColor.Sender("&f- &a/mobcoins balance-top");
        MessageColor.Sender("&f- &a/mobcoins help");
        MessageColor.Sender("#fb301f&l&m----------&r " + Variables.PlName + " Help " + "#fb301f&l&m----------");
    }

    public void HelpAdmin() {
        MessageColor.Sender("#fb301f&l&m----------&r " + Variables.PlName + " Help " + "#fb301f&l&m----------");
        MessageColor.Sender("&f- &a/mobcoins Shop");
        MessageColor.Sender("&f- &a/mobcoins pay <player> <amount>");
        MessageColor.Sender("&f- &a/mobcoins give <player> <amount>");
        MessageColor.Sender("&f- &a/mobcoins remove <player> <amount>");
        MessageColor.Sender("&f- &a/mobcoins balance");
        MessageColor.Sender("&f- &a/mobcoins balance-top");
        MessageColor.Sender("&f- &a/mobcoins reload");
        MessageColor.Sender("&f- &a/mobcoins help");
        MessageColor.Sender("#fb301f&l&m&m----------&r " + Variables.PlName + " Help " + "#fb301f&l&m----------");
    }

    public void HelpConsole() {
        MessageColor.Console("#fb301f&l&m----------&r " + Variables.PlName + " Help " + "#fb301f&l&m----------");
        MessageColor.Console("&f- &a/mobcoins Shop");
        MessageColor.Console("&f- &a/mobcoins pay <player> <amount>");
        MessageColor.Console("&f- &a/mobcoins give <player> <amount>");
        MessageColor.Console("&f- &a/mobcoins remove <player> <amount>");
        MessageColor.Console("&f- &a/mobcoins balance");
        MessageColor.Console("&f- &a/mobcoins balance-top");
        MessageColor.Console("&f- &a/mobcoins reload");
        MessageColor.Console("&f- &a/mobcoins help");
        MessageColor.Console("#fb301f&l&m----------&r&l " + Variables.PlName + " Help " + "#fb301f&l&m----------");
    }

    public void BalanceTop() {
        UserManager userManager = new UserManager(com.fenixenforge.mobCoins.MobCoins.getInstance());
        List<String> topUsers = userManager.BalanceTop();

        MessageColor.Sender("&a&l&m----------&r " + "#ffff67&l Balance Top " + "&a&l&m----------");

        int maxTop = Math.min(topUsers.size(), 10);

        for (int i = 0; i < maxTop; i++) {
            String playerData = topUsers.get(i);

            String[] splitData = playerData.split("\\. ", 2);
            if (splitData.length < 2) {
                continue;
            }

            String playerInfo = splitData[1];
            String[] playerSplit = playerInfo.split(" - ");

            if (playerSplit.length < 2) {
                continue;
            }

            String playerName = playerSplit[0];
            String balance = playerSplit[1];
            String rankColor = getRankColor(i + 1);

            String message = "&f- " + rankColor + "Top " + (i + 1) + " &l" + playerName + " &7- &e" + balance;
            MessageColor.Sender(message);
        }
    }

    private String getRankColor(int position) {
        if (position == 1) {
            return "&a";
        }
        if (position == 2) {
            return "&e";
        }
        if (position == 3) {
            return "&c";
        }
        if (position <= 5) {
            return "&4";
        }
        if (position <= 8) {
            return "&7";
        }
        return "&8";
    }

    private String FormatBalance(int balance) {
        if (balance >= 1_000_000_000_000L) { // Trillón
            return format(balance, 1_000_000_000_000L, "T");
        } else if (balance >= 1_000_000_000) { // Billón
            return format(balance, 1_000_000_000, "B");
        } else if (balance >= 1_000_000) { // Millón
            return format(balance, 1_000_000, "M");
        } else if (balance >= 1_000) { // Mil
            return format(balance, 1_000, "K");
        } else {
            return String.valueOf(balance);
        }
    }

    private String format(int balance, long divisor, String suffix) {
        double result = (double) balance / divisor;
        return (balance % divisor >= 100) ? String.format("%.1f%s", result, suffix) : String.format("%d%s", balance / divisor, suffix);
    }
}
