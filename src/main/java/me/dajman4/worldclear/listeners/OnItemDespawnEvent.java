package me.dajman4.worldclear.listeners;

import me.dajman4.worldclear.utils.ConfigFile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;

public class OnItemDespawnEvent implements Listener {

    @EventHandler
    public void dis(ItemDespawnEvent event) {
        if (ConfigFile.isDisableDisappearing())
            event.setCancelled(true);
        if (ConfigFile.getItemsLiveTime() != 300) {
            event.getEntity().setTicksLived(0);
        }
    }
}
