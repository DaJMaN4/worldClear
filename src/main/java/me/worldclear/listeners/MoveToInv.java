package me.worldclear.listeners;

import me.worldclear.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MoveToInv implements Listener {

    private final Main main;
    public Map<Player, ItemStack> lastItem = new HashMap<Player, ItemStack>();

    public MoveToInv(Main main) {
        this.main = main;
    }

    @EventHandler
    public void move(InventoryClickEvent event) {

        if (event.getWhoClicked().hasPermission("worldClear.moveTo"))
            return;

        if(main.invs.contains(event.getWhoClicked().getOpenInventory().getTopInventory())) {
            Player p = (Player) event.getWhoClicked();
            //if (lastItem.get(p) == )
            lastItem.put(p, event.getCurrentItem());
        }

    }

    @EventHandler
    public void close(InventoryCloseEvent event) {

    }
}
