package com.quackandcheese.shades.sound;

import com.quackandcheese.shades.ShadesMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, ShadesMod.MOD_ID);

    public static Supplier<SoundEvent> SHADE_HURT = registerSoundEvent("shade_hurt");
    public static Supplier<SoundEvent> SHADE_IDLE = registerSoundEvent("shade_idle");
    public static Supplier<SoundEvent> SHADE_ATTACK = registerSoundEvent("shade_attack");
    public static Supplier<SoundEvent> SHADE_FLYING = registerSoundEvent("shade_flying");
    public static Supplier<SoundEvent> SHADE_MUSIC = registerSoundEvent("shade_music");

    public static Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ShadesMod.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
