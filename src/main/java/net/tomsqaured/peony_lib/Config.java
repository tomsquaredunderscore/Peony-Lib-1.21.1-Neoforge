package net.tomsqaured.peony_lib;

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

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }
}
