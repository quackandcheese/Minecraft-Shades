package com.quackandcheese.shades.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.quackandcheese.shades.ShadesMod;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class ModDataAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ShadesMod.MOD_ID);

    public static final Supplier<AttachmentType<ShadeAttachment>> SHADE = ATTACHMENT_TYPES.register(
            "shade",
            () -> AttachmentType.builder(() -> ShadeAttachment.EMPTY)
                    .serialize(ShadeAttachment.CODEC)
                    .copyOnDeath()
                    .build()
    );

    public record ShadeAttachment(Optional<UUID> shadeUuid) {

        public static final ShadeAttachment EMPTY = new ShadeAttachment(Optional.empty());

        public static final Codec<ShadeAttachment> CODEC =
                RecordCodecBuilder.create(inst -> inst.group(
                        UUIDUtil.CODEC.optionalFieldOf("shadeUuid")
                                .forGetter(ShadeAttachment::shadeUuid)
                ).apply(inst, ShadeAttachment::new));

        public boolean hasShade() {
            return shadeUuid.isPresent();
        }
    }

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
