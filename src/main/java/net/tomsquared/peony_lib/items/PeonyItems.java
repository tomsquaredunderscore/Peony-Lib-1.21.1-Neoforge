package net.tomsquared.peony_lib.items;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tomsquared.peony_lib.Peony_Lib;
import net.tomsquared.peony_lib.blocks.PeonyBlocks;
import net.tomsquared.peony_lib.items.custom.PeonyFoodBlockItem;
import net.tomsquared.peony_lib.items.custom.PeonyPlushies;
import net.tomsquared.peony_lib.sound.PeonySounds;
import net.tomsquared.peony_lib.Config;

public class PeonyItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, Peony_Lib.MOD_ID);

    public static final DeferredRegister<Item> PLUSHIES =
            DeferredRegister.create(Registries.ITEM, Peony_Lib.MOD_ID);

    public static final DeferredHolder<Item, PeonyPlushies> TOM_PLUSHIE = PLUSHIES.register("tom_plushie",
        () -> new PeonyPlushies(
                PeonyBlocks.TOM_PLUSHIE.get(),
                new Item.Properties().rarity(Rarity.EPIC).stacksTo(1),
                PeonySounds.TOM_PLUSHIE_VOICE_LINES_1.get(),
                PeonySounds.TOM_PLUSHIE_VOICE_LINES_2.get(),
                PeonySounds.TOM_PLUSHIE_VOICE_LINES_3.get(),
                PeonySounds.TOM_PLUSHIE_VOICE_LINES_4.get(),
                PeonySounds.TOM_PLUSHIE_VOICE_LINES_5.get(),
                PeonySounds.TOM_PLUSHIE_VOICE_LINES_6.get(),
                PeonySounds.TOM_PLUSHIE_VOICE_LINES_7.get(),
                PeonySounds.TOM_PLUSHIE_VOICE_LINES_8.get(),
                PeonySounds.TOM_PLUSHIE_VOICE_LINES_9.get(),
                PeonySounds.TOM_PLUSHIE_VOICE_LINES_10.get()
        ));

    public static final DeferredHolder<Item, PeonyPlushies> SHENDI_PLUSHIE = PLUSHIES.register("shendi_plushie",
        () -> new PeonyPlushies(
                PeonyBlocks.SHENDI_PLUSHIE.get(),
                new Item.Properties().rarity(Rarity.EPIC).stacksTo(1),
                PeonySounds.SHENDI_PLUSHIE_VOICE_LINE_1.get(),
                PeonySounds.SHENDI_PLUSHIE_VOICE_LINE_2.get(),
                PeonySounds.SHENDI_PLUSHIE_VOICE_LINE_3.get(),
                PeonySounds.SHENDI_PLUSHIE_VOICE_LINE_4.get(),
                PeonySounds.SHENDI_PLUSHIE_VOICE_LINE_5.get(),
                PeonySounds.SHENDI_PLUSHIE_VOICE_LINE_6.get(),
                PeonySounds.SHENDI_PLUSHIE_VOICE_LINE_7.get(),
                PeonySounds.SHENDI_PLUSHIE_VOICE_LINE_8.get(),
                PeonySounds.SHENDI_PLUSHIE_VOICE_LINE_9.get()
        ));

    public static final DeferredHolder<Item, PeonyPlushies> OVA_PLUSHIE = PLUSHIES.register("ova_plushie",
        () -> new PeonyPlushies(
                PeonyBlocks.OVA_PLUSHIE.get(),
                new Item.Properties().rarity(Rarity.EPIC).stacksTo(1),
                PeonySounds.PLACEHOLDER_2.get()
        ));

    public static final DeferredHolder<Item, PeonyPlushies> JACK_PLUSHIE = PLUSHIES.register("jack_plushie",
        () -> new PeonyPlushies(
                PeonyBlocks.JACK_PLUSHIE.get(),
                new Item.Properties().rarity(Rarity.EPIC).stacksTo(1),
                PeonySounds.JACK_VOICE_LINE_1.get(),
                PeonySounds.JACK_VOICE_LINE_2.get(),
                PeonySounds.JACK_VOICE_LINE_3.get(),
                PeonySounds.JACK_VOICE_LINE_4.get(),
                PeonySounds.JACK_VOICE_LINE_5.get(),
                PeonySounds.JACK_VOICE_LINE_6.get(),
                PeonySounds.JACK_VOICE_LINE_7.get(),
                PeonySounds.JACK_VOICE_LINE_8.get()
        ));

    public static final DeferredHolder<Item, PeonyPlushies> EPICCOOL_PLUSHIE = PLUSHIES.register("epiccool_plushie",
        () -> new PeonyPlushies(
                PeonyBlocks.EPICCOOL_PLUSHIE.get(),
                new Item.Properties().rarity(Rarity.EPIC).stacksTo(1),
                PeonySounds.PLACEHOLDER_2.get()
        ));

    public static final DeferredHolder<Item, PeonyPlushies> KANINSCHENSPEED_PLUSHIE = PLUSHIES.register("kaninschenspeed_plushie",
        () -> new PeonyPlushies(
                PeonyBlocks.KANINSCHENSPEED_PLUSHIE.get(),
                new Item.Properties().rarity(Rarity.EPIC).stacksTo(1),
                PeonySounds.PLACEHOLDER_2.get(),
                PeonySounds.PLACEHOLDER_1.get()
        ));

    public static final DeferredHolder<Item, PeonyPlushies> SHALAMBIAN_PLUSHIE = PLUSHIES.register("shalambian_plushie",
        () -> new PeonyPlushies(
                PeonyBlocks.SHALAMBIAN_PLUSHIE.get(),
                new Item.Properties().rarity(Rarity.EPIC).stacksTo(1),
                PeonySounds.PLACEHOLDER_2.get(),
                PeonySounds.PLACEHOLDER_1.get()
        ));

    public static final DeferredHolder<Item, PeonyPlushies> RYAN_PLUSHIE = PLUSHIES.register("ryan_plushie",
        () -> new PeonyPlushies(
                PeonyBlocks.RYAN_PLUSHIE.get(),
                new Item.Properties().rarity(Rarity.EPIC).stacksTo(1),
                PeonySounds.RYAN_PLUSHIE_VOICE_LINES_1.get()
        ));

    public static final DeferredHolder<Item, PeonyPlushies> POTATOWOLFIE_PLUSHIE = PLUSHIES.register("potatowolfie_plushie",
        () -> new PeonyPlushies(
                PeonyBlocks.POTATOWOLFIE_PLUSHIE.get(),
                new Item.Properties().rarity(Rarity.EPIC).stacksTo(1),
                PeonySounds.RYAN_PLUSHIE_VOICE_LINES_1.get()
        ));

    public static final DeferredHolder<Item, PeonyPlushies> TIE_PLUSHIE = PLUSHIES.register("tie_plushie",
        () -> new PeonyPlushies(
                PeonyBlocks.TIE_PLUSHIE.get(),
                new Item.Properties().rarity(Rarity.EPIC).stacksTo(1),
                PeonySounds.TIE_VOICE_LINE_1.get(),
                PeonySounds.TIE_VOICE_LINE_2.get(),
                PeonySounds.TIE_VOICE_LINE_3.get(),
                PeonySounds.TIE_VOICE_LINE_4.get()
        ));

    public static final DeferredHolder<Item, PeonyFoodBlockItem> CUBE =
            ITEMS.register("cube", () -> new PeonyFoodBlockItem(
                    PeonyBlocks.CUBE.get(),
                    new Item.Properties()
                            .food(new FoodProperties.Builder()
                                    .nutrition(6)
                                    .saturationModifier(0.6f)
                                    .build())
                            .stacksTo(64)
            ));

    public static final DeferredHolder<Item, Item> PIZZA_SLICE =
            ITEMS.register("portion_cube_slice",
                    () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                            .nutrition(3)
                            .saturationModifier(0.1f)
                            .build())));

    public static final DeferredHolder<Item, Item> FULL_PIZZA =
            ITEMS.register("portion_cube",
                    () -> new BlockItem(
                            PeonyBlocks.PIZZA.get(),
                            new Item.Properties()
                    ));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);

        if (Config.plushiesExist()) {
            PLUSHIES.register(eventBus);
        }
    }
}