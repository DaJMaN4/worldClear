package me.worldclear;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Main extends JavaPlugin implements Listener {

    public List<Inventory> invs = new ArrayList<Inventory>();
    public Map<Player, Integer> invnumber = new HashMap<Player, Integer>();
    public boolean went;
    public boolean on = this.getConfig().getBoolean("on");
    public String broadcast = ChatColor.translateAlternateColorCodes('&',
            this.getConfig().getString("messages.messege on clearing world to players"));
    public String clearmsg = ChatColor.translateAlternateColorCodes('&',
            this.getConfig().getString("messages.on empty inv"));
    public Integer time = this.getConfig().getInt("reminder before kick");


    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(this, this);
        run();
        on = false;
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equalsIgnoreCase("otchlan")) {
            if (!(sender instanceof Player)) {
                System.out.println("Command not compatible with console");
                return true;
            }
            Player player = (Player) sender;

            if (!invs.isEmpty() && args.length == 0) {
                player.openInventory(invs.get(0));
                invnumber.put(player, 0);
            } else {
                if (args.length == 1) {
                    if (player.isOp()) {
                        if (player.isOp() && args[0].equals("on"))
                            on = true;
                        else if (player.isOp() && args[0].equals("off"))
                            on = false;
                    }
                }
                else
                    getServer().broadcastMessage(clearmsg);
            }
        }
        return false;
    }


    public void addItem(Entity item) {
        if (invs.isEmpty() || isfull(invs.get(invs.size() - 1))) {
            createInv();
        }
        Item it = (Item) item;
        it.getItemStack();
        invs.get(invs.size() - 1).addItem(it.getItemStack());
    }


    public boolean isfull(Inventory inv) {
        int size = 0;
        for (int i = 45; i != 0; i--) {
             if (inv.getItem(i) == null)
                 return false;
        }
        return true;
    }


    public void createInv() {
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.DARK_PURPLE + "Otchłan strona" + (invs.size() - 1));

        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Poprzednia strona");
        item.setItemMeta(meta);
        inv.setItem(45, item);

        meta.setDisplayName(ChatColor.GOLD + "Następna strona");
        item.setItemMeta(meta);
        inv.setItem(53, item);

        invs.add(inv);
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


    public void run() {
        new BukkitRunnable() {
            public void run() {
                if (on) {
                    clear();
                }
            }
        }.runTaskTimer(this, 0, time * 20);
    }

    public void clear() {
        invs.clear();
        getServer().getWorlds().forEach(world -> {
            world.getEntities().forEach(item -> {
                if (item instanceof Item) {
                    addItem(item);
                    item.remove();

                }
            });
        });
        getServer().broadcastMessage(broadcast);
    }
}

