package me.dajman4.worldclear.inventory;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class Inventories {

    @Getter
    @Accessors(fluent = true)
    private static Inventories instance;
    private static ChestGui gui;
    public void init() {
        createGui();
        instance = this;
    }

    private void createGui(){
        gui = new ChestGui(6, "Test");


        PaginatedPane pages = new PaginatedPane(0, 0, 9, 5);

        pages.populateWithItemStacks(Arrays.asList(
                new ItemStack(Material.GOLDEN_SWORD),
                new ItemStack(Material.LIGHT_GRAY_GLAZED_TERRACOTTA, 16),
                new ItemStack(Material.COOKED_COD, 64)
        ));
        pages.setOnClick(event -> {
            //buy item
        });

        gui.addPane(pages);
        OutlinePane background = new OutlinePane(0, 5, 9, 1);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        background.setRepeat(true);
        background.setPriority(Pane.Priority.LOWEST);

        gui.addPane(background);

        StaticPane navigation = new StaticPane(0, 5, 9, 1);
        navigation.addItem(new GuiItem(new ItemStack(Material.RED_WOOL), event -> {
            if (pages.getPage() > 0) {
                pages.setPage(pages.getPage() - 1);


            }
            event.setCancelled(true);
        }), 0, 0);
        navigation.addItem(new GuiItem(new ItemStack(Material.GREEN_WOOL), event -> {
            if (pages.getPage() < pages.getPages() - 1) {
                pages.setPage(pages.getPage() + 1);


            }
            event.setCancelled(true);
        }), 8, 0);

        navigation.addItem(new GuiItem(new ItemStack(Material.BARRIER), event -> {
            event.getWhoClicked().closeInventory();
            event.setCancelled(true);

        }), 4, 0);;
        gui.addPane(navigation);
    }

    public static void openInv(Player player){
        gui.show(player);
    }

}
