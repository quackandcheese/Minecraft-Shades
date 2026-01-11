package com.quackandcheese.shades.event;

import com.quackandcheese.shades.ShadesMod;
import com.quackandcheese.shades.entity.ModEntities;
import com.quackandcheese.shades.entity.custom.Shade;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.UUID;

@EventBusSubscriber(modid = ShadesMod.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(player.level() instanceof ServerLevel level)) return;
        if (level.isClientSide()) return;

        ShadesMod.LOGGER.info("PLAYER DIED: {}", player.getName().getString());

        // died, summon shade.
        Shade shade = ModEntities.SHADE.get().create(level);
        if (shade != null) {
            shade.moveTo(player.getEyePosition());
            shade.setAssociatedPlayer(player.getUUID());
            level.addFreshEntity(shade);
        }
    }
}
