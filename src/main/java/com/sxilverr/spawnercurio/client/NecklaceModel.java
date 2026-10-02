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
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public final class NecklaceModel {

    private static final int FLAME_FRAMES = 4;

    private final ModelPart body = createLayer().bakeRoot().getChild("body");
    private final ModelPart[] flames = new ModelPart[FLAME_FRAMES];
    private final ResourceLocation texture;

    public NecklaceModel(NecklaceTier tier) {
        //? if <1.20.5 {
        texture = new ResourceLocation(SpawnerCurio.MOD_ID, "textures/entity/" + tier.id + "_worn.png");
        //?} else {
        /*texture = ResourceLocation.fromNamespaceAndPath(SpawnerCurio.MOD_ID, "textures/entity/" + tier.id + "_worn.png");
        *///?}
        ModelPart necklace = body.getChild("necklace");
        necklace.xScale = 0.5F;
        necklace.yScale = 0.5F;
        necklace.zScale = 0.5F;
        for (int i = 0; i < FLAME_FRAMES; i++) {
            flames[i] = necklace.getChild("pendant_" + i);
        }
    }

    public void render(EntityModel<?> parent, PoseStack poseStack, MultiBufferSource buffers, int light, float ageInTicks) {
        if (parent instanceof HumanoidModel<?> humanoid) {
            body.copyFrom(humanoid.body);
        }
        int frame = (int) (ageInTicks / 3) % FLAME_FRAMES;
        for (int i = 0; i < FLAME_FRAMES; i++) {
            flames[i].visible = i == frame;
        }
        body.render(poseStack, buffers.getBuffer(RenderType.entityCutoutNoCull(texture)), light, OverlayTexture.NO_OVERLAY);
    }

    private static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition necklace = mesh.getRoot().addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO)
                .addOrReplaceChild("necklace", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-8.0F, -0.5F, -4.5F, 16.0F, 25.0F, 9.0F), PartPose.ZERO);
        for (int i = 0; i < FLAME_FRAMES; i++) {
            necklace.addOrReplaceChild("pendant_" + i, CubeListBuilder.create()
                    .texOffs(0, 34 + 7 * i).addBox(-3.0F, 3.5F, -5.5F, 6.0F, 6.0F, 1.0F), PartPose.ZERO);
        }
        return LayerDefinition.create(mesh, 64, 64);
    }
}
