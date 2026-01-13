package com.quackandcheese.shades.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.quackandcheese.shades.ShadesMod;
import com.quackandcheese.shades.entity.custom.Shade;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class ShadeEyesLayer
        extends EyesLayer<Shade, ShadePlayerModel<Shade>> {

    private static final RenderType EYES = RenderType.eyes(
            ResourceLocation.fromNamespaceAndPath(
                    ShadesMod.MOD_ID,
                    "textures/entity/shade/eyes.png"
            )
    );

    public ShadeEyesLayer(RenderLayerParent<Shade, ShadePlayerModel<Shade>> parent) {
        super(parent);
    }

    @Override
    public RenderType renderType() {
        return EYES;
    }
}
