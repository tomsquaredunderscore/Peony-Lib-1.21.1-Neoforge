package net.tomsquared.peony_lib.items;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tomsquared.peony_lib.Peony_Lib;
import net.tomsquared.peony_lib.blocks.PeonyBlocks;
import net.tomsquared.peony_lib.items.custom.PeonyPlushies;
import net.tomsquared.peony_lib.sound.PeonySounds;

public class PeonyItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, Peony_Lib.MOD_ID);

    public static final DeferredHolder<Item, PeonyPlushies> TOM_PLUSHIE = ITEMS.register("tom_plushie",
        () -> new PeonyPlushies(
                PeonyBlocks.TOM_PLUSHIE.get(),
                new Item.Properties().rarity(Rarity.EPIC).stacksTo(1),
                PeonySounds.TOM_PLUSHIE_VOICE_LINES_1.get(),
                PeonySounds.TOM_PLUSHIE_VOICE_LINES_3.get()

        ));
    public static final DeferredHolder<Item, PeonyPlushies> SHENDI_PLUSHIE = ITEMS.register("shendi_plushie",
        () -> new PeonyPlushies(
                PeonyBlocks.SHENDI_PLUSHIE.get(),
                new Item.Properties().rarity(Rarity.EPIC).stacksTo(1),

                PeonySounds.PLACEHOLDER_1.get()
        ));
    public static final DeferredHolder<Item, PeonyPlushies> OVA_PLUSHIE = ITEMS.register("ova_plushie",
        () -> new PeonyPlushies(
                PeonyBlocks.OVA_PLUSHIE.get(),
                new Item.Properties().rarity(Rarity.EPIC).stacksTo(1),

                PeonySounds.PLACEHOLDER_2.get()
        ));
    public static final DeferredHolder<Item, PeonyPlushies> JACK_PLUSHIE = ITEMS.register("jack_plushie",
        () -> new PeonyPlushies(
                PeonyBlocks.JACK_PLUSHIE.get(),
                new Item.Properties().rarity(Rarity.EPIC).stacksTo(1),

                PeonySounds.PLACEHOLDER_2.get()
        ));
    public static final DeferredHolder<Item, PeonyPlushies> KANINSCHENSPEED_PLUSHIE = ITEMS.register("kaninschenspeed_plushie",
        () -> new PeonyPlushies(
                PeonyBlocks.KANINSCHENSPEED_PLUSHIE.get(),
                new Item.Properties().rarity(Rarity.EPIC).stacksTo(1),

                PeonySounds.PLACEHOLDER_2.get(),
                PeonySounds.PLACEHOLDER_1.get()

        ));
    public static final DeferredHolder<Item, PeonyPlushies> SHALAMBIAN_PLUSHIE = ITEMS.register("shalambian_plushie",
        () -> new PeonyPlushies(
                PeonyBlocks.SHALAMBIAN_PLUSHIE.get(),
                new Item.Properties().rarity(Rarity.EPIC).stacksTo(1),

                PeonySounds.PLACEHOLDER_2.get(),
                PeonySounds.PLACEHOLDER_1.get()

        ));
    public static final DeferredHolder<Item, PeonyPlushies> RYAN_PLUSHIE = ITEMS.register("ryan_plushie",
        () -> new PeonyPlushies(
                PeonyBlocks.RYAN_PLUSHIE.get(),
                new Item.Properties().rarity(Rarity.EPIC).stacksTo(1),
                PeonySounds.RYAN_PLUSHIE_VOICE_LINES_1.get()

        ));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}