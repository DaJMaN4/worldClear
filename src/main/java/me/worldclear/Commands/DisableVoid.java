package me.worldclear.Commands;

import me.worldclear.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class DisableVoid implements CommandExecutor {

    private final Main main;

    public DisableVoid(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("disablevoid")) {
            if (sender.hasPermission("voidClear.disableDisappearing"))
                main.getConfig().options().copyDefaults();
                main.getConfig().set("Foody", "ananas");
                main.saveDefaultConfig();
                main.disableDisappearing = true;
        }
        return true;
    }
}
