package com.quackandcheese.shades.event;

import com.quackandcheese.shades.ShadesMod;
import com.quackandcheese.shades.data.ModDataAttachments;
import com.quackandcheese.shades.entity.ModEntities;
import com.quackandcheese.shades.entity.custom.Shade;
import com.quackandcheese.shades.util.ShadeUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.Optional;
import java.util.UUID;

@EventBusSubscriber(modid = ShadesMod.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!(player.level() instanceof ServerLevel level)) return;
        if (level.isClientSide()) return;
        if (level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) return;

        var attachment = player.getData(ModDataAttachments.SHADE);

        // If a shade already exists, remove or replace it
        attachment.shadeUuid().ifPresent(uuid -> {
            Entity e = level.getEntity(uuid);
            if (e != null) {
                e.discard();
            }
        });

        // died, summon shade.
        Shade shade = ModEntities.SHADE.get().create(level);
        if (shade != null) {
            shade.moveTo(player.getEyePosition());
            shade.setAssociatedPlayer(player.getUUID());
            shade.setStoredInventory(player.getInventory().save(new ListTag()));
            level.addFreshEntity(shade);

            player.setData(
                    ModDataAttachments.SHADE,
                    new ModDataAttachments.ShadeAttachment(Optional.of(shade.getUUID()))
            );
        }
    }

    @SubscribeEvent
    public static void onPlayerDrops(LivingDropsEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!(player.level() instanceof ServerLevel level)) return;
        if (level.isClientSide()) return;
        if (level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) return;

        if (ShadeUtils.getShade(player) != null) { // only cancel drops if shade is CONFIRMED to be spawned in, in case something goes awry
            event.setCanceled(true);
        }
    }
}
