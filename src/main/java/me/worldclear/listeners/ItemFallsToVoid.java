package me.worldclear.listeners;

import me.worldclear.Main;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.ArrayList;
import java.util.List;

public class ItemFallsToVoid implements Listener {

    private final Main main;
    private boolean oneInv;
    public List<List<String>> worlds = new ArrayList<List<String>>();

    public ItemFallsToVoid(Main main) {
        this.main = main;
    }

    @EventHandler
    public void fall(EntityDeathEvent event) {
        if (event.getEntityType() == EntityType.DROPPED_ITEM) {
            if (event.getEntity().getLocation().getY() < -127) {
                oneInv = main.oneInv;
                if (main.oneInv) {
                    main.addItem(event.getEntity(), main.invs);
                    return;
                }
                worlds = main.worlds;
                for (List<String> x : worlds) {
                    for (String y : x) {
                        if (event.getEntity().getWorld().getName().equals(y)) {
                            main.addItem(event.getEntity(), main.worldsInvs.get(x.indexOf(y)));
                        }
                    }
                }
            }
        }
    }
}
