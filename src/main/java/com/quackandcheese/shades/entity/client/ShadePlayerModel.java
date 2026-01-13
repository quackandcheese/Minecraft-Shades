package com.quackandcheese.shades.entity.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.LivingEntity;

public class ShadePlayerModel<T extends LivingEntity> extends PlayerModel<T> {

    public ShadePlayerModel(ModelPart root, boolean slim) {
        super(root, slim);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        super.renderToBuffer(poseStack, buffer, 0, packedOverlay, color);
    }
}
