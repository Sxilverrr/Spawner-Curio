package com.sxilverr.spawnercurio.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sxilverr.spawnercurio.SpawnerCurio;
import com.sxilverr.spawnercurio.item.NecklaceTier;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.texture.OverlayTexture;
//? if >=1.21.11 {
/*import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
*///?} else {
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
//?}

public final class NecklaceModel {

    private static final int FLAME_FRAMES = 4;

    private final ModelPart[] frames = new ModelPart[FLAME_FRAMES];
    //? if >=1.21.11 {
    /*private final Identifier texture;
    *///?} else {
    private final ResourceLocation texture;
    //?}

    public NecklaceModel(NecklaceTier tier) {
        String path = "textures/entity/" + tier.id + "_worn.png";
        //? if >=1.21.11 {
        /*texture = Identifier.fromNamespaceAndPath(SpawnerCurio.MOD_ID, path);
        *///?} else if >=1.20.5 {
        /*texture = ResourceLocation.fromNamespaceAndPath(SpawnerCurio.MOD_ID, path);
        *///?} else {
        texture = new ResourceLocation(SpawnerCurio.MOD_ID, path);
        //?}
        for (int i = 0; i < FLAME_FRAMES; i++) {
            ModelPart necklace = createLayer(i).bakeRoot().getChild("necklace");
            necklace.xScale = 0.5F;
            necklace.yScale = 0.5F;
            necklace.zScale = 0.5F;
            frames[i] = necklace;
        }
    }

    //? if >=1.21.11 {
    /*public void submit(EntityModel<?> parent, PoseStack poseStack, SubmitNodeCollector collector, int light, float ageInTicks) {
        poseStack.pushPose();
        followBody(parent, poseStack);
        collector.submitModelPart(frame(ageInTicks), poseStack, RenderTypes.entityCutoutNoCull(texture), light,
                OverlayTexture.NO_OVERLAY, null);
        poseStack.popPose();
    }
    *///?} else {
    public void render(EntityModel<?> parent, PoseStack poseStack, MultiBufferSource buffers, int light, float ageInTicks) {
        poseStack.pushPose();
        followBody(parent, poseStack);
        frame(ageInTicks).render(poseStack, buffers.getBuffer(RenderType.entityCutoutNoCull(texture)), light,
                OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }
    //?}

    private static void followBody(EntityModel<?> parent, PoseStack poseStack) {
        if (parent instanceof HumanoidModel<?> humanoid) {
            humanoid.body.translateAndRotate(poseStack);
        }
    }

    private ModelPart frame(float ageInTicks) {
        return frames[(int) (ageInTicks / 3) % FLAME_FRAMES];
    }

    private static LayerDefinition createLayer(int frame) {
        MeshDefinition mesh = new MeshDefinition();
        mesh.getRoot().addOrReplaceChild("necklace", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-8.0F, -0.5F, -4.5F, 16.0F, 25.0F, 9.0F)
                .texOffs(0, 34 + 7 * frame).addBox(-3.0F, 3.5F, -5.5F, 6.0F, 6.0F, 1.0F), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 64);
    }
}
