package com.quackandcheese.shades;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    // Server config

    private static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ALLOW_MULTIPLE_SHADES = SERVER_BUILDER
            .comment("If enabled, instead of a new shade replacing the old, Shades will not disappear until slain.")
            .define("allowMultipleShades", false);

    public static final ModConfigSpec.BooleanValue DROP_ITEMS_FROM_REPLACED_SHADE = SERVER_BUILDER
            .comment("If enabled, when a shade is replaced with a new shade (the player dies before killing their shade), the items are dropped at the location of the old shade instead of getting permanently destroyed.")
            .define("dropItemsFromReplacedShade", true);

    public static final ModConfigSpec.BooleanValue ONLY_OWNER_CAN_DAMAGE_SHADE = SERVER_BUILDER
            .comment("If enabled, the 'owner', a.k.a. whoever died, are the only thing that can damage their shade. If disabled, anything and anyone can damage a shade.")
            .define("onlyOwnerCanDamageShade", true);

    public static final ModConfigSpec.BooleanValue SPAWN_SHADES_IN_PEACEFUL = SERVER_BUILDER
            .comment("If enabled, shades will be enabled in peaceful mode.")
            .define("spawnShadesInPeaceful", false);

    public static final ModConfigSpec.BooleanValue SHOW_SHADE_OWNER_NAMES = SERVER_BUILDER
            .comment("If enabled, Shades will display who it belongs to above its head.")
            .define("showShadeOwnerNames", false);

    static final ModConfigSpec SERVER_SPEC = SERVER_BUILDER.build();

    // Client config

    private static final ModConfigSpec.Builder CLIENT_BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue PLAY_SHADE_MUSIC = CLIENT_BUILDER
            .comment("If enabled, Shades will ambiently play an off-key music box melody.")
            .define("playShadeMusic", true);

    static final ModConfigSpec CLIENT_SPEC = CLIENT_BUILDER.build();
}
