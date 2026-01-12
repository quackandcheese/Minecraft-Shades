package com.quackandcheese.shades;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ALLOW_MULTIPLE_SHADES = BUILDER
            .comment("If enabled, instead of a new shade replacing the old, Shades will not disappear until slain.")
            .define("allowMultipleShades", false);

    public static final ModConfigSpec.BooleanValue DROP_ITEMS_FROM_REPLACED_SHADE = BUILDER
            .comment("If enabled, when a shade is replaced with a new shade (the player dies before killing their shade), the items are dropped at the location of the old shade instead of getting permanently destroyed.")
            .define("dropItemsFromReplacedShade", true);

    public static final ModConfigSpec.BooleanValue ONLY_OWNER_CAN_DAMAGE_SHADE = BUILDER
            .comment("If enabled, the 'owner', a.k.a. whoever died, are the only thing that can damage their shade. If disabled, anything and anyone can damage a shade.")
            .define("onlyOwnerCanDamageShade", true);

    static final ModConfigSpec SPEC = BUILDER.build();
}
