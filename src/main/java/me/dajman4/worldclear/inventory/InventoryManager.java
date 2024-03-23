package me.dajman4.worldclear.inventory;

import lombok.Getter;
import me.dajman4.worldclear.utils.ConfigFile;
import me.dajman4.worldclear.utils.Constants;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class InventoryManager {
    private static boolean b;
    private static SingleInventory singleInventory;

    @Getter private static HashMap<SingleInventory, List<World>> MultipleInventories;

    public InventoryManager() {
        List<World> workWorlds = null;
        if (ConfigFile.isCombiningEnabled()) {
            Bukkit.getConsoleSender().sendMessage(" §7Combining enabled.");
        } else {
            if (ConfigFile.isWorldSeparateInventories()) {
                Bukkit.getConsoleSender().sendMessage(" §7World separate inventories enabled.");
                Bukkit.getServer().getWorlds().forEach(world -> {
                    MultipleInventories.put(new SingleInventory(0, world.getName()), List.of(world));
                });
            } else {
                Bukkit.getConsoleSender().sendMessage(" §7One inventory enabled.");
                singleInventory = new SingleInventory();
            }
        }
    }

    public static void addItem(Item item) {
        singleInventory.addItem(item.getItemStack());
        return;
        /*
        if (ConfigFile.isOneInv()) {
            singleInventory.addItem(item.getItemStack());
        } else {
            MultipleInventories.forEach((singleInventory, worlds) -> {
                if (worlds.contains(item.getWorld())) {
                    singleInventory.addItem(item.getItemStack());
                }
            });
        }

         */
    }

    static public void openInv(HumanEntity player) {
        if (!player.hasPermission(Constants.VOID_CMD_PERMISSION)) {
            return;
        }
        if (!ConfigFile.isWorldSeparateInventories()) {
            singleInventory.openInv(player);
            return;
        }
        MultipleInventories.forEach((singleInventory, worlds) -> {
            if (worlds.contains(player.getWorld())) {
                singleInventory.openInv(player);
            }
        });
    }

    static public Boolean clickOnInv(Inventory inv, HumanEntity player, Integer slot) {
        return switching(slot, player);
        /*
        if (ConfigFile.isOneInv()) {
            if (singleInventory.getInvs().contains(inv)) {
                return switching(slot, player);
            }
        } else {
            b = false;
            MultipleInventories.forEach((singleInventory, worlds) -> {
                if (singleInventory.getInvs().contains(inv)) {
                    b = switching(slot, player, singleInventory);
                }
            });
            return b;
        }
        return false;

         */
    }
    private static Boolean switching(Integer slot, HumanEntity player) {
        if (slot == ConfigFile.getSlotPreviousPage()) {
            singleInventory.previousPage(player);
        } else if (slot == ConfigFile.getSlotExit()) {
            player.closeInventory();
        } else if (slot == ConfigFile.getSlotNextPage()) {
            singleInventory.nextPage(player);
        }
        return slot > 44;
    }
    private static Boolean switching(Integer slot, HumanEntity player, SingleInventory localSingleInventory) {
        if (slot == ConfigFile.getSlotPreviousPage()) {
            localSingleInventory.previousPage(player);
        } else if (slot == ConfigFile.getSlotExit()) {
            player.closeInventory();
        } else if (slot == ConfigFile.getSlotNextPage()) {
            localSingleInventory.nextPage(player);
        }
        return slot > 44;
    }

    public static boolean checkIfInside(Inventory inv) {
        if (ConfigFile.isOneInv()) {
            return singleInventory.getInvs().contains(inv);
        } else {
            b = false;
            MultipleInventories.forEach((singleInventory, worlds) -> {
                if (singleInventory.getInvs().contains(inv)) {
                    //if (singleInventory.getVoidGroup().getPutIn()) {
                    //    b = true;
                    //}
                }
            });
            return b;
        }
    }
}
