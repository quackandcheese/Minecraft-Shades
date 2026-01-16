package com.quackandcheese.shades.entity.client;

import com.quackandcheese.shades.Config;
import com.quackandcheese.shades.entity.custom.Shade;
import com.quackandcheese.shades.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public final class ShadeClientSounds {

    private static final Map<UUID, ShadeAmbientSound> ACTIVE = new HashMap<>();

    public static void start(Shade shade) {
        UUID id = shade.getUUID();
        if (ACTIVE.containsKey(id)) return;

        SoundManager manager = Minecraft.getInstance().getSoundManager();

        ShadeAmbientSound flying = new ShadeAmbientSound(
                shade,
                ModSounds.SHADE_FLYING.get()
        );

        manager.play(flying);
        ACTIVE.put(id, flying);

        if (Config.PLAY_SHADE_MUSIC.get()) {
            ShadeAmbientSound music = new ShadeAmbientSound(
                    shade,
                    ModSounds.SHADE_MUSIC.get()
            );
            manager.play(music);
        }
    }

    public static void stop(Shade shade) {
        ACTIVE.remove(shade.getUUID());
    }
}
