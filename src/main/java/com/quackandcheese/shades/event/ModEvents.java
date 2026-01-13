package com.quackandcheese.shades.event;

import com.quackandcheese.shades.Config;
import com.quackandcheese.shades.ShadesMod;
import com.quackandcheese.shades.data.ModDataAttachments;
import com.quackandcheese.shades.entity.ModEntities;
import com.quackandcheese.shades.entity.custom.Shade;
import com.quackandcheese.shades.util.ShadeUtils;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;

import java.util.Optional;

@EventBusSubscriber(modid = ShadesMod.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        shadeContext(event).ifPresent(ctx -> {
            ServerPlayer player = ctx.player();
            ServerLevel level = ctx.level();

            var attachment = player.getData(ModDataAttachments.SHADE);

            attachment.shadeUuid().ifPresent(uuid -> {
                Shade oldShade = ShadeUtils.getShade(player);
                if (oldShade != null && !Config.ALLOW_MULTIPLE_SHADES.get()) {
                    if (Config.DROP_ITEMS_FROM_REPLACED_SHADE.get())
                        oldShade.dropAllItems();
                    oldShade.discard();
                }
            });

            Shade shade = ModEntities.SHADE.get().create(level);
            if (shade != null) {
                shade.moveTo(player.position().x, player.position().y + 0.5d, player.position().z);
                shade.setAssociatedPlayer(player.getUUID());
                shade.setStoredInventory(player.getInventory().save(new ListTag()));

                float playerMax = player.getMaxHealth();
                shade.getAttribute(Attributes.MAX_HEALTH)
                        .setBaseValue(playerMax);
                shade.setHealth(playerMax);

                level.addFreshEntity(shade);

                player.setData(
                        ModDataAttachments.SHADE,
                        new ModDataAttachments.ShadeAttachment(Optional.of(shade.getUUID()))
                );
            }
        });
    }

    @SubscribeEvent
    public static void onPlayerDrops(LivingDropsEvent event) {
        shadeContext(event).ifPresent(ctx -> {
            ServerPlayer player = ctx.player();
            if (ShadeUtils.getShade(player) != null) { // only cancel drops if shade is CONFIRMED to be spawned in, in case something goes awry
                event.setCanceled(true);
            }
        });
    }

    @SubscribeEvent
    public static void onPlayerDropExperience(LivingExperienceDropEvent event) {
        shadeContext(event).ifPresent(ctx -> {
            ServerPlayer player = ctx.player();
            Shade shade = ShadeUtils.getShade(player);
            if (shade != null) {
                shade.setStoredExperience(event.getOriginalExperience());
                event.setCanceled(true);
                ShadesMod.LOGGER.info("Storing experience in shade: {}", event.getOriginalExperience());
            }
        });
    }

    public record ShadeContext(ServerPlayer player, ServerLevel level) {}

    private static Optional<ShadeContext> shadeContext(EntityEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return Optional.empty();
        if (!(player.level() instanceof ServerLevel level)) return Optional.empty();
        if (level.isClientSide()) return Optional.empty();
        if (level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) return Optional.empty(); // shades don't make sense if you have keep inventory on...
        if (level.getDifficulty() == Difficulty.PEACEFUL && !Config.SPAWN_SHADES_IN_PEACEFUL.get()) return Optional.empty();

        return Optional.of(new ShadeContext(player, level));
    }
}
