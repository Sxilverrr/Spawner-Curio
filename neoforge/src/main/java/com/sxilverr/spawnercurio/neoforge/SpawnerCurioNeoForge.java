package com.sxilverr.spawnercurio.neoforge;

import com.mojang.serialization.MapCodec;
import com.sxilverr.spawnercurio.SpawnerCurio;
import com.sxilverr.spawnercurio.config.SpawnerCurioConfig;
import com.sxilverr.spawnercurio.core.NecklaceData;
import com.sxilverr.spawnercurio.item.NecklaceTier;
import com.sxilverr.spawnercurio.item.SpawnerNecklaceItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

@Mod(SpawnerCurio.MOD_ID)
public final class SpawnerCurioNeoForge {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(BuiltInRegistries.ITEM, SpawnerCurio.MOD_ID);

    static {
        for (NecklaceTier tier : NecklaceTier.values()) {
            tier.item = ITEMS.register(tier.id, () -> new NeoForgeNecklaceItem(tier, new Item.Properties().stacksTo(1)));
        }
    }

    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITIONS =
            DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, SpawnerCurio.MOD_ID);

    public SpawnerCurioNeoForge(IEventBus bus, ModContainer container) {
        CONDITIONS.register("craftable", () -> CraftableCondition.CODEC);
        ITEMS.register(bus);
        CONDITIONS.register(bus);
        bus.addListener(SpawnerCurioNeoForge::onCreativeTab);
        NeoForgeConfig.register(container);
        NeoForge.EVENT_BUS.register(new NeoForgeEvents());
        NecklaceData.curios = player -> CuriosApi.getCuriosInventory(player)
                .<List<ItemStack>>map(inventory -> inventory.findCurios(stack -> stack.getItem() instanceof SpawnerNecklaceItem).stream()
                        .filter(result -> SpawnerCurioConfig.slotAllowed(result.slotContext().identifier()))
                        .map(SlotResult::stack)
                        .toList())
                .orElse(List.of());
    }

    private static void onCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            for (NecklaceTier tier : NecklaceTier.values()) {
                if (tier.enabled) {
                    event.accept(tier.item.get());
                }
            }
        }
    }
}
