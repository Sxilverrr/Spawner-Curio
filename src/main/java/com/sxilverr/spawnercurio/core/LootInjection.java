package com.sxilverr.spawnercurio.core;

import com.sxilverr.spawnercurio.config.SpawnerCurioConfig;
import com.sxilverr.spawnercurio.item.NecklaceTier;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public final class LootInjection {

    private LootInjection() {
    }

    public static LootPool poolFor(String table, NecklaceTier tier) {
        if (!tier.enabled || !tier.inLoot || !SpawnerCurioConfig.lootTableAllowed(table)) {
            return null;
        }
        return LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(tier.item.get()))
                .when(LootItemRandomChanceCondition.randomChance((float) SpawnerCurioConfig.lootChance))
                .build();
    }
}
