package net.tomsquared.peony_lib.blocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tomsquared.peony_lib.Peony_Lib;
import net.tomsquared.peony_lib.blocks.custom.PeonyPlushyBlockEntity;
import net.tomsquared.peony_lib.Config;

public class PeonyBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Peony_Lib.MOD_ID);

    public static final DeferredRegister<BlockEntityType<?>> PLUSHIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Peony_Lib.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PeonyPlushyBlockEntity>> TOM_PLUSHIE =
            PLUSHIES.register("tom_plushie",
                    () -> BlockEntityType.Builder.of(
                            PeonyPlushyBlockEntity::new,
                            PeonyBlocks.TOM_PLUSHIE.get(),
                            PeonyBlocks.SHENDI_PLUSHIE.get(),
                            PeonyBlocks.RYAN_PLUSHIE.get(),
                            PeonyBlocks.POTATOWOLFIE_PLUSHIE.get(),
                            PeonyBlocks.TIE_PLUSHIE.get(),
                            PeonyBlocks.OVA_PLUSHIE.get(),
                            PeonyBlocks.JACK_PLUSHIE.get(),
                            PeonyBlocks.KANINSCHENSPEED_PLUSHIE.get(),
                            PeonyBlocks.SHALAMBIAN_PLUSHIE.get(),
                            PeonyBlocks.EPICCOOL_PLUSHIE.get()
                    ).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);

        if (Config.plushiesExist()) {
            PLUSHIES.register(eventBus);
        }
    }
}