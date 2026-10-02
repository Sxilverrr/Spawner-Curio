package com.sxilverr.spawnercurio.neoforge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sxilverr.spawnercurio.SpawnerCurio;
import com.sxilverr.spawnercurio.client.NecklaceModel;
import com.sxilverr.spawnercurio.item.NecklaceTier;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;

@EventBusSubscriber(modid = SpawnerCurio.MOD_ID, value = Dist.CLIENT)
public final class NeoForgeClient {

    private NeoForgeClient() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        for (NecklaceTier tier : NecklaceTier.values()) {
            CuriosRendererRegistry.register(tier.item.get(), () -> new Renderer(tier));
        }
    }

    private static final class Renderer implements ICurioRenderer {

        private final NecklaceModel model;

        private Renderer(NecklaceTier tier) {
            model = new NecklaceModel(tier);
        }

        @Override
        public <T extends LivingEntity, M extends EntityModel<T>> void render(
                ItemStack stack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<T, M> parent,
                MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks,
                float ageInTicks, float netHeadYaw, float headPitch) {
            model.render(parent.getModel(), poseStack, buffers, light, ageInTicks);
        }
    }
}
