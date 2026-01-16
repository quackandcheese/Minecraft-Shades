package com.quackandcheese.shades.event;

import com.quackandcheese.shades.ShadesMod;
import com.quackandcheese.shades.entity.ModEntities;
import com.quackandcheese.shades.entity.client.ShadeClientSounds;
import com.quackandcheese.shades.entity.client.ShadeRenderer;
import com.quackandcheese.shades.entity.custom.Shade;
import com.quackandcheese.shades.particle.ModParticles;
import com.quackandcheese.shades.particle.ShadeParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = ShadesMod.MOD_ID, value = Dist.CLIENT)
public class ModClientEvents {

//    @SubscribeEvent
//    public static void onClientSetup(FMLClientSetupEvent event) {
//        EntityRenderers.register(ModEntities.SHADE.get(), ShadeRenderer::new);
//    }


    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!(event.getLevel() instanceof ClientLevel)) return;
        if (!(event.getEntity() instanceof Shade shade)) return;

        ShadeClientSounds.start(shade);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.SHADE.get(), ShadeRenderer::new);
    }

    @SubscribeEvent // on the mod event bus only on the physical client
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        // #registerSpriteSet MUST be used when dealing with particle descriptions.
        event.registerSpriteSet(ModParticles.SHADE_PARTICLE.get(), ShadeParticle.Provider::new);
    }
}
