package me.worldclear.listeners;

import me.worldclear.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClick implements Listener {

    private final Main main;

    public InventoryClick(Main main) {
        this.main = main;
    }

    @EventHandler
    public void click(InventoryClickEvent event) {
        for (int i = 0 ; i != main.worldsInvs.size() ; i++) {
            if (main.worldsInvs.get(i).contains(event.getInventory())) {
                if (event.getSlot() > 44)
                    event.setCancelled(true);
            }
        }
    }

}
