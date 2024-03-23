package me.dajman4.worldclear.utils;

import me.dajman4.worldclear.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Item;

import static me.dajman4.worldclear.inventory.InventoryManager.addItem;

public class ClearWorld {
    private static boolean went;
    public static void init() {

    }
    public static void clearWorld(World world) {
        world.getEntities().forEach(item -> {
            if (item instanceof Item it) {
                addItem(it);
                item.remove();
            }
        });
    }

    public static void clearWorld(World world, boolean force) {
        if (force) {
            world.getEntities().forEach(item -> {
                if (item instanceof Item it) {
                    addItem(it);
                    item.remove();
                }
            });
        } else {
            clearWorld(world);
        }
    }

    public static void clearAllWorlds(boolean force) {
        Bukkit.getScheduler().runTask(Main.instance(), () ->
                Bukkit.getWorlds().forEach(world -> world.getEntities().forEach(item -> {
                    if (item instanceof Item it) {
                        if (force) {
                            addItem(it);
                            item.remove();
                        } else {
                            if (!ConfigFile.getProhibitedItemsList().contains(((Item) item).getItemStack())) {
                                if (!ConfigFile.getDisableDisapperiengItemsList().contains(((Item) item).getItemStack())) {
                                    addItem(it);
                                    item.remove();
                                }
                            }
                        }
                        addItem(it);
                        item.remove();
                    }
                })));
    }
}
