package com.quackandcheese.shades.entity;

import com.quackandcheese.shades.ShadesMod;
import com.quackandcheese.shades.entity.custom.Shade;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(
        BuiltInRegistries.ENTITY_TYPE,
        ShadesMod.MOD_ID
    );

    public static final Supplier<EntityType<Shade>> SHADE = ENTITY_TYPES.register(Shade.NAME, () -> EntityType.Builder.of(Shade::new, MobCategory.MONSTER)
            .sized(0.6F, 1.8F)
            .eyeHeight(1.62F)
            .clientTrackingRange(32)
            .build(Shade.NAME));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
