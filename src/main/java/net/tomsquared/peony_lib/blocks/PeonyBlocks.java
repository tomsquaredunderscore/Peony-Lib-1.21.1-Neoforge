package net.tomsquared.peony_lib.blocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.Shapes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tomsquared.peony_lib.Peony_Lib;
import net.tomsquared.peony_lib.blocks.custom.PeonyFoodBlock;
import net.tomsquared.peony_lib.blocks.custom.PeonyPlushyBlock;
import net.tomsquared.peony_lib.items.PeonyItems;
import net.tomsquared.peony_lib.sound.PeonySounds;

public class PeonyBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, Peony_Lib.MOD_ID);

    public static final DeferredHolder<Block, PeonyPlushyBlock> TOM_PLUSHIE = BLOCKS.register("tom_plushie",
            () -> new PeonyPlushyBlock(
                    BlockBehaviour.Properties.of()
                            .sound(SoundType.WOOL)
                            .strength(0.8F)
                            .destroyTime(0.2f)
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

    public static final DeferredHolder<Block, PeonyPlushyBlock> SHENDI_PLUSHIE = BLOCKS.register("shendi_plushie",
            () -> new PeonyPlushyBlock(
                    BlockBehaviour.Properties.of()
                            .sound(SoundType.WOOL)
                            .strength(0.8F)
                            .destroyTime(0.2f)
                            .noOcclusion(),

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

    public static final DeferredHolder<Block, PeonyPlushyBlock> OVA_PLUSHIE = BLOCKS.register("ova_plushie",
            () -> new PeonyPlushyBlock(
                    BlockBehaviour.Properties.of()
                            .sound(SoundType.WOOL)
                            .strength(0.8F)
                            .destroyTime(0.2f)
                            .noOcclusion(),

                    PeonySounds.PLACEHOLDER_2.get()
            ));
    public static final DeferredHolder<Block, PeonyPlushyBlock> JACK_PLUSHIE = BLOCKS.register("jack_plushie",
            () -> new PeonyPlushyBlock(
                    BlockBehaviour.Properties.of()
                            .sound(SoundType.WOOL)
                            .strength(0.8F)
                            .destroyTime(0.2f)
                            .noOcclusion(),

                    PeonySounds.JACK_VOICE_LINE_1.get(),
                    PeonySounds.JACK_VOICE_LINE_2.get(),
                    PeonySounds.JACK_VOICE_LINE_3.get(),
                    PeonySounds.JACK_VOICE_LINE_4.get(),
                    PeonySounds.JACK_VOICE_LINE_5.get(),
                    PeonySounds.JACK_VOICE_LINE_6.get(),
                    PeonySounds.JACK_VOICE_LINE_7.get(),
                    PeonySounds.JACK_VOICE_LINE_8.get()
            ));

    public static final DeferredHolder<Block, PeonyPlushyBlock> RYAN_PLUSHIE = BLOCKS.register("ryan_plushie",
            () -> new PeonyPlushyBlock(
                    BlockBehaviour.Properties.of()
                            .sound(SoundType.WOOL)
                            .strength(0.8F)
                            .destroyTime(0.2f)
                            .noOcclusion(),
                    PeonySounds.RYAN_PLUSHIE_VOICE_LINES_1.get()

            ));
    public static final DeferredHolder<Block, PeonyPlushyBlock> POTATOWOLFIE_PLUSHIE = BLOCKS.register("potatowolfie_plushie",
            () -> new PeonyPlushyBlock(
                    BlockBehaviour.Properties.of()
                            .sound(SoundType.WOOL)
                            .strength(0.8F)
                            .destroyTime(0.2f)
                            .noOcclusion(),
                    PeonySounds.PLACEHOLDER_2.get()

            ));
    public static final DeferredHolder<Block, PeonyPlushyBlock> TIE_PLUSHIE = BLOCKS.register("tie_plushie",
            () -> new PeonyPlushyBlock(
                    BlockBehaviour.Properties.of()
                            .sound(SoundType.WOOL)
                            .strength(0.8F)
                            .destroyTime(0.2f)
                            .noOcclusion(),
                    PeonySounds.TIE_VOICE_LINE_1.get(),
                    PeonySounds.TIE_VOICE_LINE_2.get(),
                    PeonySounds.TIE_VOICE_LINE_3.get(),
                    PeonySounds.TIE_VOICE_LINE_4.get()

            ));

    public static final DeferredHolder<Block, PeonyPlushyBlock> KANINSCHENSPEED_PLUSHIE = BLOCKS.register("kaninschenspeed_plushie",
            () -> new PeonyPlushyBlock(
                    BlockBehaviour.Properties.of()
                            .sound(SoundType.WOOL)
                            .strength(0.8F)
                            .destroyTime(0.2f)
                            .noOcclusion(),
                    PeonySounds.PLACEHOLDER_2.get(),
                    PeonySounds.PLACEHOLDER_1.get()

            ));
    public static final DeferredHolder<Block, PeonyPlushyBlock> EPICCOOL_PLUSHIE = BLOCKS.register("epiccool_plushie",
            () -> new PeonyPlushyBlock(
                    BlockBehaviour.Properties.of()
                            .sound(SoundType.WOOL)
                            .strength(0.8F)
                            .destroyTime(0.2f)
                            .noOcclusion(),
                    PeonySounds.PLACEHOLDER_2.get()

            ));
    public static final DeferredHolder<Block, PeonyPlushyBlock> SHALAMBIAN_PLUSHIE = BLOCKS.register("shalambian_plushie",
            () -> new PeonyPlushyBlock(
                    BlockBehaviour.Properties.of()
                            .sound(SoundType.WOOL)
                            .strength(0.8F)
                            .destroyTime(0.2f)
                            .noOcclusion(),
                    PeonySounds.PLACEHOLDER_2.get(),
                    PeonySounds.PLACEHOLDER_1.get()

            ));

    public static final DeferredHolder<Block, PeonyFoodBlock> CUBE =
            BLOCKS.register("cube",
                    () -> new PeonyFoodBlock(
                            BlockBehaviour.Properties.of()
                                    .sound(SoundType.WOOL)
                                    .strength(0.8F)
                                    .destroyTime(0.2f)
                                    .noOcclusion()
                    ));


    public static final DeferredHolder<Block, FoodPortionBlock> PIZZA =
            BLOCKS.register("portion_cube", () -> new FoodPortionBlock(
                    BlockBehaviour.Properties.of()
                            .sound(SoundType.WART_BLOCK)
                            .strength(0.5F)
                            .noOcclusion(),
                    () -> PeonyItems.PIZZA_SLICE.get(),
                    4,
                    Shapes.box(
                            0.0, 0.0, 0.0,
                            1.0, 0.1, 1.0
                    )
            ));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}