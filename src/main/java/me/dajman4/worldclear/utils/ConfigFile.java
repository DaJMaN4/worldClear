package me.dajman4.worldclear.utils;

import de.leonhard.storage.Config;
import de.leonhard.storage.SimplixBuilder;
import de.leonhard.storage.Yaml;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.ReloadSettings;
import lombok.Getter;
import me.dajman4.worldclear.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigFile {

    @Getter private static boolean oneInv, disableDisappearing, itemsFallToVoid, playerCanPutItemsInInventories,
            combiningEnabled, willMiddleItemExists, openJustLocalInventories, worldSeparateInventories,
            permissionDefault, disableVoidInventory;

    @Getter private static Component inventoryName, doNotHavePermissionMessage;

    @Getter private static List<String> previousPageItemLoreList, nextPageItemLoreList, middleItemLoreList, fillItemsLoreList;
    @Getter private static Integer itemsLiveTime, numberOfCombiningGroups, slotNextPage, slotPreviousPage, slotExit;
    @Getter private static List<ItemStack> prohibitedItemsList, disableDisapperiengItemsList;
    @Getter private static Inventory inventoryTemplate;
    @Getter private static HashMap<Integer, HashMap<String, Object>> worldsCombined = new HashMap<>();
    @Getter private static HashMap<String, Object> voidGroupDefault = new HashMap<>();
    private Config config;
    private static final MiniMessage mm = MiniMessage.miniMessage();

    public void load() {
        //this.config = new Config("config.yml", "plugins/WorldClear/");

        config = SimplixBuilder
                .fromFile(new File("plugins/WorldClear/config.yml"))
                .setReloadSettings(ReloadSettings.MANUALLY)
                .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
                .setDataType(DataType.SORTED)
                .createConfig();
        Bukkit.getConsoleSender().sendMessage("tththt  " + config.getString("version"));

        reload_config();
        combining();
        createInventory();
    }

    public void createInventory() {
        Inventory inventory = Bukkit.createInventory(null, 54, mm.deserialize(config.getString(ConfigConstants.GUI_NAME)));
        int num = 0;
        for (int i = 45; i < 54; i++) {
            Bukkit.getConsoleSender().sendMessage(ConfigConstants.GUI_ITEMS_MATERIAL[num] + " " + config.getString(ConfigConstants.GUI_ITEMS_MATERIAL[num]));
            ItemStack item = new ItemStack(Material.getMaterial(config.getString(ConfigConstants.GUI_ITEMS_MATERIAL[num])));
            ItemMeta meta = item.getItemMeta();
            meta.displayName(mm.deserialize(config.getString(ConfigConstants.GUI_ITEMS_NAME[num])));
            List<Component> lore = new ArrayList<>();
            for (String s : config.getStringList(ConfigConstants.GUI_ITEMS_LORE[num])) {lore.add(mm.deserialize(s));}
            meta.lore(lore);
            item.setItemMeta(meta);
            inventory.setItem(i, item);
            if (config.getString(ConfigConstants.GUI_ITEMS_ACTIONS[num]) != null) {
                switch (config.getString(ConfigConstants.GUI_ITEMS_ACTIONS[num])) {
                    case "nextPage" -> slotNextPage = i;
                    case "previousPage" -> slotPreviousPage = i;
                    case "exit" -> slotExit = i;
                }
            }
            num++;
        }
        inventoryTemplate = inventory;
    }

    public void createItemStacks() {
        for (String s : config.getStringList(ConfigConstants.PROHIBITED_ITEMS)) {
            ItemStack item = new ItemStack(Material.valueOf(s));
            prohibitedItemsList.add(item);
        }
        for (String s : config.getStringList(ConfigConstants.DISABLE_DISAPPEARING_OF_LIST_ITEMS)) {
            ItemStack item = new ItemStack(Material.valueOf(s));
            disableDisapperiengItemsList.add(item);
        }
    }


    public void reload_config() {
        playerCanPutItemsInInventories = config.getBoolean(ConfigConstants.PLAYERS_CAN_PUT_ITEMS_INSIDE_INVENTORIES);
        itemsLiveTime = config.getInt(ConfigConstants.TIME_ALIVE_OF_ITEMS);
        permissionDefault = config.getBoolean(ConfigConstants.PERMISSION);
        itemsFallToVoid = config.getBoolean(ConfigConstants.VOID_FALL);
        disableDisappearing = config.getBoolean(ConfigConstants.DISABLE_DISAPPEARING_OF_ALL);
        disableVoidInventory = config.getBoolean(ConfigConstants.DISABLE_VOID_INVENTORY);
        worldSeparateInventories = config.getBoolean(ConfigConstants.WORLD_SEPARATE_INVENTORIES);
        combiningEnabled = config.getBoolean(ConfigConstants.COMBINING_ENABLED);
        openJustLocalInventories = config.getBoolean(ConfigConstants.OPEN_LOCAL_GROUP_INVENTORY);

        inventoryName = mm.deserialize(config.getString(ConfigConstants.GUI_NAME));

        doNotHavePermissionMessage = mm.deserialize(config.getString("texts.doNotHavePermissionMessage"));
        numberOfCombiningGroups = 0;
        while (config.getInt("combine" + numberOfCombiningGroups) != 0) {
            numberOfCombiningGroups++;
        }
        //for (int x : main.getConfig().getIntegerList("voidinv.combine")) {
        //    voidGroups.add(new VoidGroup(x));
        //}
    }

    public List<VoidGroup> createVoidGroups () {
        List<VoidGroup> voidGroups = new ArrayList<>();
        for (int i = 0; i < numberOfCombiningGroups; i++) {
            if (config.getStringList(ConfigConstants.COMBINING_GROUPS + "." + i + ".worlds").isEmpty())
                Bukkit.getConsoleSender().sendMessage(Component.text(
                        "WorldClear: Error in config.yml. Combining group " + i + " has no worlds.")
                        .color(NamedTextColor.RED));
            else {
                //voidGroups.add(new VoidGroup(i, config.getStringList(ConfigConstants.COMBINING_GROUPS + "." + i + ".worlds")
                //voidGroups.add(new VoidGroup(i));
            }
        }
        return voidGroups;
    }
    private void combining() {
        if (combiningEnabled) {
            for (int i = 0; i < numberOfCombiningGroups; i++) {
                HashMap<String, Object> group = new HashMap<>();
                group.put("permission", config.getBoolean(ConfigConstants.COMBINING_GROUPS + "." + i + "." + ConfigConstants.PERMISSION));
                group.put("works", config.getBoolean(ConfigConstants.COMBINING_GROUPS + "." + i + ".works"));
                group.put("commandArgument", config.getString(ConfigConstants.COMBINING_GROUPS + "." + i + ".commandArgument"));
                group.put("putIn", config.getBoolean(ConfigConstants.COMBINING_GROUPS + "." + i + ".putIn"));
                group.put("voidFall", config.getBoolean(ConfigConstants.COMBINING_GROUPS + "." + i + ".voidFall"));
                group.put("prohibitedItems", config.getStringList(ConfigConstants.COMBINING_GROUPS + "." + i + ".prohibitedItems"));
                group.put("antiDespawnItems", config.getStringList(ConfigConstants.COMBINING_GROUPS + "." + i + ".antydespawnItems"));
                group.put("disableVoidInventory", config.getBoolean(ConfigConstants.COMBINING_GROUPS + "." + i + ".disableVoidInventory"));
                group.put("disableDisappearing", config.getBoolean(ConfigConstants.COMBINING_GROUPS + "." + i + ".disableDisappearing"));
                worldsCombined.put(i, group);
            }
        }


        voidGroupDefault.put("permission", config.getBoolean(ConfigConstants.PERMISSION));
        Bukkit.getConsoleSender().sendMessage(Component.text("Permission: " + config.getBoolean(ConfigConstants.PERMISSION)));
        voidGroupDefault.put("works", false);
        voidGroupDefault.put("commandArgument", "o");
        voidGroupDefault.put("putIn", config.getBoolean(ConfigConstants.PLAYERS_CAN_PUT_ITEMS_INSIDE_INVENTORIES));
        Bukkit.getConsoleSender().sendMessage(Component.text("PutIn: " + config.getBoolean(ConfigConstants.PLAYERS_CAN_PUT_ITEMS_INSIDE_INVENTORIES)));
        voidGroupDefault.put("voidFall", config.getBoolean(ConfigConstants.VOID_FALL));
        voidGroupDefault.put("prohibitedItems", config.getStringList(ConfigConstants.PROHIBITED_ITEMS));
        voidGroupDefault.put("antiDespawnItems", config.getStringList(ConfigConstants.DISABLE_DISAPPEARING_OF_LIST_ITEMS));
        voidGroupDefault.put("disableVoidInventory", config.getBoolean(ConfigConstants.DISABLE_VOID_INVENTORY));
        voidGroupDefault.put("disableDisappearing", config.getBoolean(ConfigConstants.COMBINING_GROUPS));

    }
    public static HashMap<String, Object> getVariablesForVoidGroup(Integer i) {
        return worldsCombined.get(i);
    }
    public static HashMap<String, Object> getVariablesForVoidGroupDefault() {
        return voidGroupDefault;
    }


}
