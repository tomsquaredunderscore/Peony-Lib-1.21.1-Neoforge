package net.tomsquared.peony_lib.items;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tomsquared.peony_lib.Peony_Lib;
import net.tomsquared.peony_lib.blockshape.PeonyBlockShape;
import net.tomsquared.peony_lib.blockshape.PeonyBlockShapes;
import net.tomsquared.peony_lib.blockshape.PeonyLogTextures;

public final class PeonyCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(
                    Registries.CREATIVE_MODE_TAB, Peony_Lib.MOD_ID
            );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> WOOD_SHAPES =
        TABS.register("wood_shapes", () -> CreativeModeTab.builder()
                .withTabFactory(PeonyCyclingCreativeTab::new)
                .title(Component.translatable(
                        "creativeTab.peony_lib.wood_shapes"
                ))
                .icon(() -> new ItemStack(Blocks.OAK_LOG))
                .displayItems(PeonyCreativeTabs::buildWoodShapes)
                .build());

    private PeonyCreativeTabs() {
    }

    public static void register(IEventBus modEventBus) {
        TABS.register(modEventBus);
    }

    private static void buildWoodShapes(
            CreativeModeTab.ItemDisplayParameters parameters,
            CreativeModeTab.Output output) {
        List<PeonyBlockShape> shapes = Arrays.stream(PeonyBlockShape.values())
                .map(PeonyBlockShapes::itemShape)
                .distinct()
                .toList();

        List<Block> materials = BuiltInRegistries.BLOCK.stream()
                .filter(block -> PeonyBlockShapes.supports(
                        block.defaultBlockState()
                ))
                .filter(block -> block.asItem() != Items.AIR)
                .filter(block -> block.isEnabled(parameters.enabledFeatures()))
                .filter(block -> block.asItem().isEnabled(
                        parameters.enabledFeatures()
                ))
                .sorted(Comparator
                        .comparing(PeonyCreativeTabs::woodType)
                        .thenComparing(block -> BuiltInRegistries.BLOCK
                                .getKey(block).toString()))
                .toList();

        for (Block block : materials) {
            BlockState base = block.defaultBlockState()
                    .setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);

            if (base.hasProperty(PeonyLogTextures.SWAPPED)) {
                base = base.setValue(PeonyLogTextures.SWAPPED, false);
            }

            boolean canSwap = PeonyLogTextures.supports(base);

            if (canSwap) {
                Block counterpart = PeonyLogTextures.textureState(
                        base.setValue(PeonyLogTextures.SWAPPED, true)
                ).getBlock();

                canSwap = counterpart.isEnabled(parameters.enabledFeatures());
            }

            for (PeonyBlockShape shape : shapes) {
                BlockState shaped = base.setValue(PeonyBlockShapes.SHAPE, shape);

                output.accept(PeonyBlockShapes.copyToStack(
                        shaped, new ItemStack(block)
                ));

                if (canSwap) {
                    output.accept(PeonyBlockShapes.copyToStack(
                            shaped.setValue(PeonyLogTextures.SWAPPED, true),
                            new ItemStack(block)
                    ));
                }
            }
        }
    }

    private static String woodType(Block block) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        String path = id.getPath();

        if (path.startsWith("stripped_")) {
            path = path.substring("stripped_".length());
        }

        for (String suffix : List.of("_log", "_wood", "_stem", "_hyphae")) {
            if (path.endsWith(suffix)) {
                path = path.substring(0, path.length() - suffix.length());
                break;
            }
        }

        return path + ":" + id.getNamespace();
    }
}