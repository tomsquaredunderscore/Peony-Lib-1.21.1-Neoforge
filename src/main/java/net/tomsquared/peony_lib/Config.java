package net.tomsquared.peony_lib;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;


public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue AREYOUGAY = BUILDER
            .comment("Whether You are gay or not")
            .define("areyougay", true);

    public static final ModConfigSpec.IntValue HOWGAY = BUILDER
            .comment("How Gay Are you?")
            .defineInRange("howgay", 42, 0, Integer.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();

    
    private static final ModConfigSpec.Builder STARTUP_BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue PLUSHIES_EXIST = STARTUP_BUILDER
            .comment("Whether plushies exist at all. Needs a restart, and server and clients must use the same value.")
            .define("plushies_exist", true);

    static final ModConfigSpec STARTUP_SPEC = STARTUP_BUILDER.build();

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

    public static boolean plushiesExist() {
        return PLUSHIES_EXIST.get();
    }
}