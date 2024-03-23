package me.dajman4.worldclear.listeners;

import me.dajman4.worldclear.inventory.InventoryManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

public class OnInventoryClickEvent implements Listener {

    @EventHandler
    public void click(InventoryClickEvent event) { // DONE
        if (InventoryManager.clickOnInv(event.getClickedInventory(), event.getWhoClicked(), event.getSlot())) {
            event.setCancelled(true);
        }
    }
}
