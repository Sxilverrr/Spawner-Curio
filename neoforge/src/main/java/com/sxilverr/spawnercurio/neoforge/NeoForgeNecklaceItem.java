package com.sxilverr.spawnercurio.neoforge;

import com.sxilverr.spawnercurio.config.SpawnerCurioConfig;
import com.sxilverr.spawnercurio.item.NecklaceTier;
import com.sxilverr.spawnercurio.item.SpawnerNecklaceItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public class NeoForgeNecklaceItem extends SpawnerNecklaceItem {

    public NeoForgeNecklaceItem(NecklaceTier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (SpawnerCurioConfig.lavaResistant) {
            stack.set(DataComponents.FIRE_RESISTANT, Unit.INSTANCE);
        } else {
            stack.remove(DataComponents.FIRE_RESISTANT);
        }
        return false;
    }
}
