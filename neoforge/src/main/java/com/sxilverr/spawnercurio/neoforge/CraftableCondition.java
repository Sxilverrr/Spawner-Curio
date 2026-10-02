package com.sxilverr.spawnercurio.neoforge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.sxilverr.spawnercurio.item.NecklaceTier;
import net.neoforged.neoforge.common.conditions.ICondition;

public record CraftableCondition(NecklaceTier tier) implements ICondition {

    public static final MapCodec<CraftableCondition> CODEC = Codec.STRING.fieldOf("necklace")
            .xmap(id -> new CraftableCondition(NecklaceTier.byId(id)), condition -> condition.tier.id);

    @Override
    public boolean test(IContext context) {
        return tier.enabled && tier.craftable;
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
