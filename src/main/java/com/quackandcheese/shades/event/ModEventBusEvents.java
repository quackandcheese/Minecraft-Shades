package com.quackandcheese.shades.event;

import com.quackandcheese.shades.ShadesMod;
import com.quackandcheese.shades.entity.ModEntities;
import com.quackandcheese.shades.entity.custom.Shade;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = ShadesMod.MOD_ID)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.SHADE.get(), Shade.createAttributes().build());
    }
}
