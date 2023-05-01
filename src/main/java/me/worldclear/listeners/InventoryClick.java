package me.worldclear.listeners;

import me.worldclear.Main;
import org.bukkit.entity.Player;
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
        if (main.oneInv) {
            if (main.invs.contains(event.getInventory())){
                if (event.getSlot() > 44) {
                    event.setCancelled(true);
                    Player player = (Player) event.getWhoClicked();
                    if (event.getSlot() == 45 && main.invnumber.get(player) - 1 != -1) {
                        main.invnumber.put(player, main.invnumber.get(player) - 1);
                        event.getWhoClicked().closeInventory();
                        event.getWhoClicked().openInventory(main.invs.get(main.invnumber.get(player)));

                    } else if (event.getSlot() == 53 && main.invnumber.get(player) + 1 <= main.invs.size() - 1) {
                        main.invnumber.put(player, main.invnumber.get(player) + 1);
                        event.getWhoClicked().closeInventory();
                        event.getWhoClicked().openInventory(main.invs.get(main.invnumber.get(player)));
                    }
                }
            }
            return;
        }
        for (int i = 0 ; i != main.worldsInvs.size() ; i++) {
            if (main.worldsInvs.get(i).contains(event.getInventory())) {
                if (event.getSlot() > 44) {
                    event.setCancelled(true);
                    Player player = (Player) event.getWhoClicked();
                    if (event.getSlot() == 45 && main.invnumber.get(player) - 1 != -1) {
                        main.invnumber.put(player, main.invnumber.get(player) - 1);
                        event.getWhoClicked().closeInventory();
                        event.getWhoClicked().openInventory(main.worldsInvs.get(i).get(main.invnumber.get(player)));

                    } else if (event.getSlot() == 53 && main.invnumber.get(player) + 1 <= main.invs.size() - 1) {
                        main.invnumber.put(player, main.invnumber.get(player) + 1);
                        event.getWhoClicked().closeInventory();
                        event.getWhoClicked().openInventory(main.worldsInvs.get(i).get(main.invnumber.get(player)));
                    }
                }
            }
        }
    }
}
