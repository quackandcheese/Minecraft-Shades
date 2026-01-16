package com.quackandcheese.shades.entity.client;

import com.quackandcheese.shades.entity.custom.Shade;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShadeAmbientSound extends AbstractTickableSoundInstance {

    private final Shade shade;

    public ShadeAmbientSound(Shade shade, SoundEvent soundEvent) {
        super(soundEvent, SoundSource.HOSTILE, SoundInstance.createUnseededRandom());
        this.shade = shade;

        this.looping = true;
        this.volume = 1.0f;
        this.attenuation = Attenuation.LINEAR;

        this.x = shade.getX();
        this.y = shade.getY();
        this.z = shade.getZ();
    }

    @Override
    public void tick() {
        if (shade.isRemoved() || !shade.isAlive()) {
            stop();
            ShadeClientSounds.stop(shade);
            return;
        }

        if (shade.isRemoved() || !shade.isAlive()) {
            this.volume -= 0.1f;
            if (this.volume <= 0f) stop();
            return;
        }

        this.x = shade.getX();
        this.y = shade.getY();
        this.z = shade.getZ();
    }
}
