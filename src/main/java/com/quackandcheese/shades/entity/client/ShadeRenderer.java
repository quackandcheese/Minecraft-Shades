package com.quackandcheese.shades.entity.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.quackandcheese.shades.ShadesMod;
import com.quackandcheese.shades.entity.custom.Shade;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class ShadeRenderer extends MobRenderer<Shade, ShadePlayerModel<Shade>> {
    private static final ResourceLocation DEFAULT_SHADE_TEXTURE = ResourceLocation.fromNamespaceAndPath(ShadesMod.MOD_ID, "textures/entity/shade/skin.png");

    public ShadeRenderer(EntityRendererProvider.Context context) {
        super(context, new ShadePlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), true), 0.5f);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new ShadeEyesLayer(this));
    }

    public ResourceLocation getTextureLocation(Shade entity) {
        Entity owner = entity.getOwner();
        if (owner instanceof AbstractClientPlayer player) {
            return ShadeSkinCache.getOrCreate(player.getUUID(), player);
        }

        return DEFAULT_SHADE_TEXTURE;
    }

    @Override
    public void render(
            Shade entity,
            float entityYaw,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight
    ) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    public class ShadeSkinCache {
        private static final Map<UUID, ResourceLocation> CACHE = new HashMap<>();

        public static ResourceLocation getOrCreate(UUID playerId, AbstractClientPlayer player) {
            return CACHE.computeIfAbsent(playerId, id -> createShadeSkin(player));
        }

        private static ResourceLocation createShadeSkin(AbstractClientPlayer player) {
            return player.getSkin().texture();
        }
    }
}
