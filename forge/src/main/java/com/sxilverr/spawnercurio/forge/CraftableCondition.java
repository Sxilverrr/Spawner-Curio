package com.sxilverr.spawnercurio.forge;

import com.google.gson.JsonObject;
import com.sxilverr.spawnercurio.SpawnerCurio;
import com.sxilverr.spawnercurio.item.NecklaceTier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public record CraftableCondition(NecklaceTier tier) implements ICondition {

    private static final ResourceLocation ID = new ResourceLocation(SpawnerCurio.MOD_ID, "craftable");

    public static final IConditionSerializer<CraftableCondition> SERIALIZER = new IConditionSerializer<>() {

        @Override
        public void write(JsonObject json, CraftableCondition value) {
            json.addProperty("necklace", value.tier.id);
        }

        @Override
        public CraftableCondition read(JsonObject json) {
            return new CraftableCondition(NecklaceTier.byId(GsonHelper.getAsString(json, "necklace")));
        }

        @Override
        public ResourceLocation getID() {
            return ID;
        }
    };

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public boolean test(IContext context) {
        return tier.enabled && tier.craftable;
    }
}
