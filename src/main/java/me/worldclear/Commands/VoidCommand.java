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
            if (!(sender instanceof Player)) {
                System.out.println("Command not compatible with the console");
                return true;
            }
            Player player = (Player) sender;

            player.openInventory(main.invs.get(0));
            main.invnumber.put(player, 0);

            if (args.length != 0) {
                if (player.isOp()) {
                    if (args[0].equals("clear"))
                        main.clear();
                }
            }
        }
        return true;
    }
}
