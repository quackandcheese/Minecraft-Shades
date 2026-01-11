package com.quackandcheese.shades;

import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ALLOW_MULTIPLE_SHADES = BUILDER
            .comment("If enabled, instead of a new shade replacing the old, Shades will not disappear until slain.")
            .define("allowMultipleShades", false);

    static final ModConfigSpec SPEC = BUILDER.build();
}
