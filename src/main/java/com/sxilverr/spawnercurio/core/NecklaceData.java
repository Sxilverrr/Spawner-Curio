package com.sxilverr.spawnercurio.core;

import com.sxilverr.spawnercurio.config.SpawnerCurioConfig;
import com.sxilverr.spawnercurio.item.SpawnerNecklaceItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
//? if >=1.20.5 {
/*import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
*///?}

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class NecklaceData {

    private static final String ROOT = "SpawnerCurio";
    private static final String ON = "On";
    private static final String KILLS = "Kills";
    private static final String SPAWNERS = "Spawners";
    private static final String XP = "Xp";

    public static Function<Player, List<ItemStack>> curios = player -> List.of();

    private NecklaceData() {
    }

    public static List<ItemStack> active(Player player) {
        List<ItemStack> found = new ArrayList<>();
        if (SpawnerCurioConfig.worksInCurioSlot) {
            found.addAll(curios.apply(player));
        }
        Inventory inventory = player.getInventory();
        if (SpawnerCurioConfig.worksInInventory) {
            //? if >=1.21.5 {
            /*found.addAll(inventory.getNonEquipmentItems());
            found.add(player.getOffhandItem());
            *///?} else {
            found.addAll(inventory.items);
            found.addAll(inventory.offhand);
            //?}
        } else if (SpawnerCurioConfig.worksInHand) {
            found.add(player.getMainHandItem());
            found.add(player.getOffhandItem());
        }
        found.removeIf(stack -> !(stack.getItem() instanceof SpawnerNecklaceItem item) || !item.tier.enabled || !isOn(stack));
        return found;
    }

    public static boolean isOn(ItemStack stack) {
        CompoundTag root = root(stack);
        return Nbt.getBoolean(root, ON, SpawnerCurioConfig.defaultOn);
    }

    public static void setOn(ItemStack stack, boolean on) {
        CompoundTag root = root(stack);
        root.putBoolean(ON, on);
        store(stack, root);
    }

    public static int kills(ItemStack stack) {
        return Nbt.getInt(root(stack), KILLS);
    }

    public static int spawners(ItemStack stack) {
        return Nbt.getInt(root(stack), SPAWNERS);
    }

    public static int xp(ItemStack stack) {
        return Nbt.getInt(root(stack), XP);
    }

    public static void addXp(ItemStack stack, int amount) {
        increment(stack, XP, amount);
    }

    public static void addKill(ItemStack stack) {
        increment(stack, KILLS, 1);
    }

    public static void addSpawner(ItemStack stack) {
        increment(stack, SPAWNERS, 1);
    }

    private static void increment(ItemStack stack, String key, int amount) {
        CompoundTag root = root(stack);
        root.putInt(key, Nbt.getInt(root, key) + amount);
        store(stack, root);
    }

    private static CompoundTag root(ItemStack stack) {
        return Nbt.getCompound(read(stack), ROOT);
    }

    private static void store(ItemStack stack, CompoundTag root) {
        CompoundTag tag = read(stack);
        tag.put(ROOT, root);
        write(stack, tag);
    }

    private static CompoundTag read(ItemStack stack) {
        //? if <1.20.5 {
        CompoundTag tag = stack.getTag();
        return tag == null ? new CompoundTag() : tag.copy();
        //?} else {
        /*return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        *///?}
    }

    private static void write(ItemStack stack, CompoundTag tag) {
        //? if <1.20.5 {
        stack.setTag(tag);
        //?} else {
        /*stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        *///?}
    }
}
