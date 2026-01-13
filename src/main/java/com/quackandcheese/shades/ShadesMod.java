package com.quackandcheese.shades;

import com.quackandcheese.shades.data.ModDataAttachments;
import com.quackandcheese.shades.entity.ModEntities;
import com.quackandcheese.shades.particle.ModParticles;
import com.quackandcheese.shades.sound.ModSounds;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;

@Mod(ShadesMod.MOD_ID)
public class ShadesMod {
    public static final String MOD_ID = "shades";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ShadesMod(IEventBus modEventBus, ModContainer modContainer) {
        ModEntities.register(modEventBus);
        ModDataAttachments.register(modEventBus);
        ModParticles.register(modEventBus);
        ModSounds.register(modEventBus);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
    }
}
