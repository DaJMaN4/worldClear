package me.worldclear.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class ItemFallsToVoid implements Listener {

    @EventHandler
    public void fall(EntityDeathEvent event) {
        if (event.getEntityType() == EntityType.DROPPED_ITEM) {
            if (event.getEntity().getLocation().getY() < -128) {

            }
        }
    }
}
