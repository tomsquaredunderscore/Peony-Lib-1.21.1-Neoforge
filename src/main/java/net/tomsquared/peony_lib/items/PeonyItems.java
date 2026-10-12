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
                PeonySounds.TOM_PLUSHIE_VOICE_LINES_3.get()

        ));
    public static final DeferredHolder<Item, PeonyPlushies> SHENDI_PLUSHIE = ITEMS.register("shendi_plushie",
        () -> new PeonyPlushies(
                PeonyBlocks.SHENDI_PLUSHIE.get(),
                new Item.Properties().rarity(Rarity.EPIC).stacksTo(1),

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