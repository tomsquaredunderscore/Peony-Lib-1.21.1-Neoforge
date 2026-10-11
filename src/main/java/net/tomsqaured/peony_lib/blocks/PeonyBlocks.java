package net.tomsqaured.peony_lib.blocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tomsqaured.peony_lib.Peony_Lib;
import net.tomsqaured.peony_lib.blocks.custom.PeonyPlushyBlock;
import net.tomsqaured.peony_lib.sound.PeonySounds;

public class PeonyBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, Peony_Lib.MOD_ID);

    public static final DeferredHolder<Block, PeonyPlushyBlock> TOM_PLUSHIE = BLOCKS.register("tom_plushie",
            () -> new PeonyPlushyBlock(
                    BlockBehaviour.Properties.of()
                            .strength(0.8F)
                            .noOcclusion(),
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
        BLOCKS.register(eventBus);
    }
}