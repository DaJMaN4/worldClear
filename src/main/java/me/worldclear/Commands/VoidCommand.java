package me.worldclear.Commands;

import me.worldclear.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoidCommand implements CommandExecutor{

    private final Main main;

    public VoidCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("void")) {

            if (args.length != 0) {
                if (args[0].equals("clear")) {
                    if (sender.hasPermission("voidclear.clear")) {
                        main.clear();
                        return true;
                    }
                }
                if (args[0].equals("disappearing")) {
                    if (sender.hasPermission("voidClear.disappearing")) {
                        main.getConfig().options().copyDefaults();
                        if (args.length == 1) {
                            sender.sendMessage("Usage of the command: /void disappearing enable or disable");
                            return true;
                        }
                        if (args[1].equals("disable")) {
                            main.getConfig().set("disableDisappearing", false);
                            main.saveDefaultConfig();
                            main.disableDisappearing = true;
                            return true;
                        }
                        if (args[1].equals("enable")) {
                            main.getConfig().set("disableDisappearing", true);
                            main.saveDefaultConfig();
                            main.disableDisappearing = true;
                        }
                    } else {
                        sender.sendMessage("You don't have permission to use this command");
                    }
                }
            }
            if (!(sender instanceof Player)) {
                System.out.println("Command not compatible with the console");
                return true;
            }
            Player player = (Player) sender;
            if (main.oneInv) {
                player.openInventory(main.invs.get(0));
                main.invnumber.put(player, 0);
                player.sendMessage("works somehow");
                return true;
            }


            for (int i = 0 ; i != main.worlds.size() ; i++) {
                if (main.worlds.get(i).contains(player.getWorld().getName())) {
                    if (i > 1) {
                        player.openInventory(main.worldsInvs.get(i).get(0));
                        main.invnumber.put(player, 0);
                    }
                }
            }
        }
        return true;
    }
}
