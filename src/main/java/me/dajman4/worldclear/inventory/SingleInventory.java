package me.dajman4.worldclear.inventory;

import me.dajman4.worldclear.utils.ConfigFile;
import me.dajman4.worldclear.utils.VoidGroup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SingleInventory {
    private List<Inventory> inven = new ArrayList<>();
    private ItemStack glass, itemPreviousPage, itemNextPage;
    private VoidGroup voidGroup;

    public SingleInventory(int num) {
        createInventory();
        voidGroup = new VoidGroup(num);
    }
    public SingleInventory(int num, String world) {
        createInventory();
        voidGroup = new VoidGroup(num, world);
    }
    //public SingleInventory(int num, List<World> worlds) {
    //    createInventory();
    //    voidGroup = new VoidGroup(num, worlds);
    //}
    public SingleInventory() {
        createInventory();
        voidGroup = new VoidGroup();
    }

    public void createInventory() {
        Inventory original = ConfigFile.getInventoryTemplate();
        Inventory copy = Bukkit.createInventory(null, original.getSize());
        for (int i = 0; i < original.getSize(); i++) {
            ItemStack originalItem = original.getItem(i);
            if (originalItem != null) {
                copy.setItem(i, originalItem.clone());
            }
        }
        inven.add(copy);
    }

    private boolean isfull(Inventory inv) {
        for (int i = 45; i != 0; i--) {
            if (inv.getItem(i) == null)
                return false;
        }
        return true;
    }

    public void openInv(HumanEntity player) {
        if (voidGroup.isPermission()) {
            if (player.hasPermission("worldclear.allowopen." + voidGroup.getNum())) {
                player.openInventory(inven.get(0));
                return;
            }
            player.sendMessage(ConfigFile.getDoNotHavePermissionMessage());
        }
        player.openInventory(inven.get(0));
    }

    public void nextPage(HumanEntity player) {
        for (int i = 0; i != inven.size(); i++) {
            if (inven.get(i).equals(player.getOpenInventory().getTopInventory())) {
                if (i + 1 < inven.size()) {
                    player.openInventory(inven.get(i + 1));
                    return;
                }
            }
        }
    }

    public void previousPage(HumanEntity player) {
        for (int i = 0; i != inven.size(); i++) {
            if (inven.get(i).equals(player.getOpenInventory().getTopInventory())) {
                if (i != 0) {
                    player.openInventory(inven.get(i - 1));
                    return;
                }
            }
        }
    }

    public void addItem(ItemStack item) {
        for (int i = 0; i != inven.size(); i++) {
            if (!isfull(inven.get(i))) {
                Bukkit.getConsoleSender().sendMessage("§cAdding item to inventory");
                inven.get(i).addItem(item);
                return;
            }
        }
        Bukkit.getConsoleSender().sendMessage("§cCreating new inventory");
        if (inven.isEmpty() || isfull(inven.get(inven.size() - 1))) {
            createInventory();
        }
        inven.get(inven.size() - 1).addItem(item);
    }


    public List<Inventory> getInvs() {return inven;}
    public VoidGroup getVoidGroup() {return voidGroup;}

}
