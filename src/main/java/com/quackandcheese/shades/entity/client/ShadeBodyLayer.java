package com.quackandcheese.shades.entity.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.quackandcheese.shades.entity.custom.Shade;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class ShadeBodyLayer extends RenderLayer<Shade, PlayerModel<Shade>> {

    public ShadeBodyLayer(RenderLayerParent<Shade, PlayerModel<Shade>> parent) {
        super(parent);
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            Shade entity,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        RenderSystem.setShaderColor(0f, 0f, 0f, 1f);
        this.getParentModel().renderToBuffer(
                poseStack,
                buffer.getBuffer(RenderType.eyes(this.getTextureLocation(entity))),
                packedLight,
                OverlayTexture.NO_OVERLAY
        );
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }
}

