package com.sxilverr.spawnercurio.core;

import com.sxilverr.spawnercurio.config.SpawnerCurioConfig;
import com.sxilverr.spawnercurio.item.NecklaceTier;
import com.sxilverr.spawnercurio.item.SpawnerNecklaceItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class Progression {

    private Progression() {
    }

    public static int level(ItemStack stack) {
        if (!SpawnerCurioConfig.progressionEnabled || !tier(stack).progresses()) {
            return SpawnerCurioConfig.maxLevel;
        }
        int level = 0;
        if (SpawnerCurioConfig.progressionFromKills) {
            level += NecklaceData.kills(stack) / SpawnerCurioConfig.killsPerLevel;
        }
        if (SpawnerCurioConfig.progressionFromSpawners) {
            level += NecklaceData.spawners(stack) / SpawnerCurioConfig.spawnersPerLevel;
        }
        if (SpawnerCurioConfig.progressionFromXp) {
            level += NecklaceData.xp(stack) / SpawnerCurioConfig.xpPerLevel;
        }
        return Math.min(level, SpawnerCurioConfig.maxLevel);
    }

    public static double speed(ItemStack stack) {
        return SpawnerCurioConfig.speedFor(level(stack), tier(stack).speed);
    }

    public static double radius(ItemStack stack) {
        return SpawnerCurioConfig.radiusFor(level(stack), tier(stack).range);
    }

    public static int absorbXp(Player player, int amount) {
        if (!SpawnerCurioConfig.progressionEnabled || !SpawnerCurioConfig.progressionFromXp || amount <= 0
                || player.level().isClientSide) {
            return amount;
        }
        for (ItemStack stack : progressing(player)) {
            if (level(stack) < SpawnerCurioConfig.maxLevel) {
                double share = amount * SpawnerCurioConfig.xpShare;
                int taken = (int) share + (player.getRandom().nextDouble() < share % 1 ? 1 : 0);
                NecklaceData.addXp(stack, taken);
                return amount - taken;
            }
        }
        return amount;
    }

    public static void onKill(Player player) {
        if (SpawnerCurioConfig.progressionEnabled && SpawnerCurioConfig.progressionFromKills
                && !player.level().isClientSide) {
            progressing(player).forEach(NecklaceData::addKill);
        }
    }

    public static void onSpawnerBroken(Player player) {
        if (SpawnerCurioConfig.progressionEnabled && SpawnerCurioConfig.progressionFromSpawners
                && !player.level().isClientSide) {
            progressing(player).forEach(NecklaceData::addSpawner);
        }
    }

    private static NecklaceTier tier(ItemStack stack) {
        return ((SpawnerNecklaceItem) stack.getItem()).tier;
    }

    private static List<ItemStack> progressing(Player player) {
        return NecklaceData.active(player).stream().filter(stack -> tier(stack).progresses()).toList();
    }
}
