package net.tomsquared.peony_lib.blocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tomsquared.peony_lib.Peony_Lib;
import net.tomsquared.peony_lib.blocks.custom.PeonyPlushyBlock;
import net.tomsquared.peony_lib.sound.PeonySounds;
import net.tomsquared.peony_lib.blocks.PeonyBlocks;

public class PeonyBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, Peony_Lib.MOD_ID);

    public static final DeferredHolder<Block, PeonyPlushyBlock> TOM_PLUSHIE = BLOCKS.register("tom_plushie",
            () -> new PeonyPlushyBlock(
                    BlockBehaviour.Properties.of()
                            .sound(net.minecraft.world.level.block.SoundType.WOOL)
                            .strength(0.8F)
                            .destroyTime(0.2f)
                            .noOcclusion(),

                    PeonySounds.TOM_PLUSHIE_VOICE_LINES_3.get()

            ));

    public static final DeferredHolder<Block, PeonyPlushyBlock> SHENDI_PLUSHIE = BLOCKS.register("shendi_plushie",
            () -> new PeonyPlushyBlock(
                    BlockBehaviour.Properties.of()
                            .sound(net.minecraft.world.level.block.SoundType.WOOL)
                            .strength(0.8F)
                            .destroyTime(0.2f)
                            .noOcclusion(),

                    PeonySounds.PLACEHOLDER_1.get()
            ));

    public static final DeferredHolder<Block, PeonyPlushyBlock> RYAN_PLUSHIE = BLOCKS.register("ryan_plushie",
            () -> new PeonyPlushyBlock(
                    BlockBehaviour.Properties.of()
                            .sound(net.minecraft.world.level.block.SoundType.WOOL)
                            .strength(0.8F)
                            .destroyTime(0.2f)
                            .noOcclusion(),
                    PeonySounds.RYAN_PLUSHIE_VOICE_LINES_1.get()

            ));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}