package me.worldclear;

import me.worldclear.Commands.VoidCommand;
import me.worldclear.listeners.ItemFallsToVoid;
import me.worldclear.listeners.MoveToInv;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Main extends JavaPlugin implements Listener {

    public List<Inventory> invs = new ArrayList<Inventory>();
    public List<Integer> nums = getConfig().getIntegerList("void inv.combine");
    public List<List<String>> worlds = new ArrayList<List<String>>();
    public Map<Player, Integer> invnumber = new HashMap<Player, Integer>();
    public boolean went;

    public String name = ChatColor.translateAlternateColorCodes('&',
            getConfig().getString("messages.name"));
    public String previous = ChatColor.translateAlternateColorCodes('&',
            getConfig().getString("texts.previous"));
    public String next = ChatColor.translateAlternateColorCodes('&',
            getConfig().getString("texts.next"));


    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();
        if (getConfig().getBoolean("void inv.voidfall"))
            this.getServer().getPluginManager().registerEvents(new ItemFallsToVoid(), this);
        if (getConfig().getBoolean("void inv.putIn"))
            this.getServer().getPluginManager().registerEvents(new MoveToInv(this), this);
        getCommand("void").setExecutor(new VoidCommand(this));

    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public void reloadConfig() {
        String name = ChatColor.translateAlternateColorCodes('&',
                getConfig().getString("messages.name"));
        String previous = ChatColor.translateAlternateColorCodes('&',
                getConfig().getString("texts.previous"));
        String next = ChatColor.translateAlternateColorCodes('&',
                getConfig().getString("texts.next"));

        List<Integer> nums = getConfig().getIntegerList("void inv.combine");


        worlds = worldsConf();
    }


    public List<List<String>> worldsConf() {
        List<List<String>> listOfLists = new ArrayList<List<String>>();
        List<String> worldsAppeard = new ArrayList<String>();
        List<String> commands = new ArrayList<String>();
        for (int x : nums) {
            List<String> list = new ArrayList<String>();
            list.add(getConfig().getString("void inv.combine" + x + "command"));
            list.add(getConfig().getString("void inv.combine" + x + "works"));
            if (list.get(1) == "true")
                commands.add(list.get(0));

            for (String y : getConfig().getStringList(x + "worlds")) {
                if (worldsAppeard.contains(y)) {
                    //make exception
                    return null;
                }
                    list.add(getConfig().getString("void inv.combine" + x + y));
                    worldsAppeard.add(y);
            }
            listOfLists.add(list);
        }
        for (String x : commands) {
            for (String y : commands) {
                if (x == y) {
                    //Raise exception
                    return null;
                }
            }
        }
        return listOfLists;
    }



    public void addItem(Entity item, List<Inventory> inven) {
        Item it = (Item) item;
        for (int i = 0; i != inven.size(); i++) {
            if (!isfull(inven.get(i))) {
                inven.get(i).addItem(it.getItemStack());
                return;
            }
        }
        if (inven.isEmpty() || isfull(inven.get(inven.size() - 1))) {
            inven.add(createInv());
        }
        inven.get(inven.size() - 1).addItem(it.getItemStack());
    }


    public boolean isfull(Inventory inv) {
        for (int i = 45; i != 0; i--) {
            if (inv.getItem(i) == null)
                return false;
        }
        return true;
    }


    public void invlist() {
        for (List<String> x : worlds) {
            for (String y : x) {

            }
        }
    }


    public Inventory createInv() {
        Inventory inv = Bukkit.createInventory(null, 54,
                ChatColor.DARK_PURPLE + name + (invs.size() + 1) + "/" + invs.size());

        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + previous);
        item.setItemMeta(meta);
        inv.setItem(45, item);

        meta.setDisplayName(ChatColor.GOLD + next);
        item.setItemMeta(meta);
        inv.setItem(53, item);

        item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        meta = item.getItemMeta();
        meta.setDisplayName(" ");
        inv.setItem(52, item);
        inv.setItem(51, item);
        inv.setItem(50, item);
        inv.setItem(49, item);
        inv.setItem(48, item);
        inv.setItem(47, item);
        inv.setItem(46, item);
        return inv;
    }


    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (invs.contains(event.getInventory()))
            if (event.getSlot() > 44) {
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();
                if (event.getSlot() == 45 && invnumber.get(player) - 1 != -1) {
                    invnumber.put(player, invnumber.get(player) - 1);
                    event.getWhoClicked().closeInventory();
                    event.getWhoClicked().openInventory(invs.get(invnumber.get(player)));

                } else if (event.getSlot() == 53 && invnumber.get(player) + 1 <= invs.size() - 1) {
                    invnumber.put(player, invnumber.get(player) + 1);
                    event.getWhoClicked().closeInventory();
                    event.getWhoClicked().openInventory(invs.get(invnumber.get(player)));
                }
            }
    }


    @EventHandler
    public void dis(ItemDespawnEvent event) {
        //addItem(event.getEntity());
    }


    public void clear() {
        went = false;
        getServer().getWorlds().forEach(world -> {
            world.getEntities().forEach(item -> {
                if (item instanceof Item) {
                    //addItem(item);
                    item.remove();
                    went = true;
                }
            });
        });
    }
}

