package com.sxilverr.spawnercurio.neoforge;

import com.sxilverr.spawnercurio.config.SpawnerCurioConfig;
import com.sxilverr.spawnercurio.item.NecklaceTier;
import com.sxilverr.spawnercurio.item.SpawnerNecklaceItem;
import net.minecraft.core.component.DataComponents;
//? if >=1.21.2 {
/*import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.item.component.DamageResistant;
*///?} else {
import net.minecraft.util.Unit;
//?}
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public class NeoForgeNecklaceItem extends SpawnerNecklaceItem {

    public NeoForgeNecklaceItem(NecklaceTier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        //? if >=1.21.2 {
        /*if (SpawnerCurioConfig.lavaResistant) {
            stack.set(DataComponents.DAMAGE_RESISTANT, new DamageResistant(DamageTypeTags.IS_FIRE));
        } else {
            stack.remove(DataComponents.DAMAGE_RESISTANT);
        }
        *///?} else {
        if (SpawnerCurioConfig.lavaResistant) {
            stack.set(DataComponents.FIRE_RESISTANT, Unit.INSTANCE);
        } else {
            stack.remove(DataComponents.FIRE_RESISTANT);
        }
        //?}
        return false;
    }
}
