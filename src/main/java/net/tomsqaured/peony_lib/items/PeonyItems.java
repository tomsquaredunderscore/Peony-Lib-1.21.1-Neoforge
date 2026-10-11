package net.tomsqaured.peony_lib.items;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tomsqaured.peony_lib.Peony_Lib;
import net.tomsqaured.peony_lib.blocks.PeonyBlocks;
import net.tomsqaured.peony_lib.items.custom.PeonyPlushies;
import net.tomsqaured.peony_lib.sound.PeonySounds;

public class PeonyItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, Peony_Lib.MOD_ID);

    public static final DeferredHolder<Item, PeonyPlushies> TOM_PLUSHIE = ITEMS.register("tom_plushie",
        () -> new PeonyPlushies(
                PeonyBlocks.TOM_PLUSHIE.get(),
                new Item.Properties().stacksTo(1),
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

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}