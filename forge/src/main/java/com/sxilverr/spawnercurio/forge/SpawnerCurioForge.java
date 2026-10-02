package com.sxilverr.spawnercurio.forge;

import com.sxilverr.spawnercurio.SpawnerCurio;
import com.sxilverr.spawnercurio.config.SpawnerCurioConfig;
import com.sxilverr.spawnercurio.core.NecklaceData;
import com.sxilverr.spawnercurio.item.NecklaceTier;
import com.sxilverr.spawnercurio.item.SpawnerNecklaceItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

@Mod(SpawnerCurio.MOD_ID)
public final class SpawnerCurioForge {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SpawnerCurio.MOD_ID);

    static {
        for (NecklaceTier tier : NecklaceTier.values()) {
            tier.item = ITEMS.register(tier.id, () -> new SpawnerNecklaceItem(tier, new Item.Properties().stacksTo(1)));
        }
    }

    public SpawnerCurioForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
        bus.addListener(SpawnerCurioForge::onCreativeTab);
        ForgeConfig.register();
        CraftingHelper.register(CraftableCondition.SERIALIZER);
        MinecraftForge.EVENT_BUS.register(new ForgeEvents());
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
