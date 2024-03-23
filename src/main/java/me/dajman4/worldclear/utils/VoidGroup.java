package me.dajman4.worldclear.utils;

import lombok.Getter;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class VoidGroup {
    @Getter private List<String> worlds;
    @Getter private boolean permission;
    @Getter private boolean works;
    @Getter private String commandArgument;
    @Getter private boolean putIn;
    @Getter private boolean voidFall;
    @Getter private boolean disableDisappearing;
    @Getter private boolean disableVoidInventory;
    @Getter private List<ItemStack> prohibitedItems;
    @Getter private List<ItemStack> antydespawnItems;
    @Getter private int num;

    @SuppressWarnings("unchecked") public VoidGroup(int num) {
        HashMap<String, Object> map = ConfigFile.getVariablesForVoidGroup(num);
        permission = (Boolean) map.get("permission");
        works = (Boolean) map.get("works");
        commandArgument = (String) map.get("commandArgument");
        putIn = (Boolean) map.get("works");
        voidFall = (Boolean) map.get("works");
        prohibitedItems = (List<ItemStack>) map.get("prohibitedItems");
        antydespawnItems = (List<ItemStack>) map.get("works");
        worlds = (List<String>) map.get("worlds");
        disableVoidInventory = (Boolean) map.get("disableVoidInventory");
        disableDisappearing = (Boolean) map.get("disableDisappearing");
        this.num = num;

    }
    @SuppressWarnings("unchecked") public VoidGroup() {
        HashMap<String, Object> map = ConfigFile.getVariablesForVoidGroupDefault();
        permission = (Boolean) map.get("permission");
        works = (Boolean) map.get("works");
        commandArgument = (String) map.get("commandArgument");
        putIn = (Boolean) map.get("putIn");
        voidFall = (Boolean) map.get("voidFall");
        prohibitedItems = (List<ItemStack>) map.get("prohibitedItems");
        antydespawnItems = (List<ItemStack>) map.get("antiDespawnItems");
        worlds = (List<String>) map.get("worlds");
        disableVoidInventory = (Boolean) map.get("disableVoidInventory");
        disableDisappearing = (Boolean) map.get("disableDisappearing");


    }
    public VoidGroup(int num, String world) {
        HashMap<String, Object> map = ConfigFile.getVariablesForVoidGroup(num);
        permission = (Boolean) map.get("permission");
        works = (Boolean) map.get("works");
        commandArgument = (String) map.get("commandArgument");
        putIn = (Boolean) map.get("works");
        voidFall = (Boolean) map.get("works");
        prohibitedItems = (List<ItemStack>) map.get("prohibitedItems");
        antydespawnItems = (List<ItemStack>) map.get("works");
        worlds.add(world);
        disableVoidInventory = (Boolean) map.get("disableVoidInventory");
        disableDisappearing = (Boolean) map.get("disableDisappearing");
        this.num = num;
    }
    /*
    public VoidGroup(int num, List<World> world) {
        permission = ConfigFileUtil.isPermissionDefault();
        works = ConfigFileUtil.isOpenJustLocalInventories();
        commandArgument = null;
        putIn = ConfigFileUtil.isPlayerCanPutItemsInInventories();
        voidFall = ConfigFileUtil.isItemsFallToVoid();
        prohibitedItems = ConfigFileUtil.getProhibitedItemsList();
        antydespawnItems = ConfigFileUtil.getDisableDisapperiengItemsList();
        worlds = world;
        disableVoidInventory = ConfigFileUtil.combineDisableVoidInventory(num);
        disableDisapperieng = ConfigFileUtil.combineDisableDisappearing(num);
        this.num = num;
    }
    */
}