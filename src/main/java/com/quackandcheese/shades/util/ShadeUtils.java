package com.quackandcheese.shades.util;

import com.quackandcheese.shades.data.ModDataAttachments;
import com.quackandcheese.shades.entity.custom.Shade;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class ShadeUtils {
    public static @Nullable Shade getShade(ServerPlayer player) {
        var data = player.getData(ModDataAttachments.SHADE);
        if (!data.hasShade()) return null;

        Entity e = player.serverLevel().getEntity(data.shadeUuid().get());

        if (e instanceof Shade shade && shade.isAlive()) {
            return shade;
        }

        // Shade no longer exists → clean up attachment
        player.setData(ModDataAttachments.SHADE, ModDataAttachments.ShadeAttachment.EMPTY);
        return null;
    }

    public static @Nullable boolean hasShade(ServerPlayer player) {
        var data = player.getData(ModDataAttachments.SHADE);
        return data.hasShade();
    }
}
