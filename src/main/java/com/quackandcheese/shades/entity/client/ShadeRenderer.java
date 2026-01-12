package com.quackandcheese.shades.entity.client;

import com.quackandcheese.shades.ShadesMod;
import com.quackandcheese.shades.entity.custom.Shade;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ShadeRenderer extends MobRenderer<Shade, PlayerModel<Shade>> {

    public ShadeRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), true), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(Shade entity) {
        return ResourceLocation.fromNamespaceAndPath(ShadesMod.MOD_ID, "textures/entity/shade/skin.png");
    }
}
