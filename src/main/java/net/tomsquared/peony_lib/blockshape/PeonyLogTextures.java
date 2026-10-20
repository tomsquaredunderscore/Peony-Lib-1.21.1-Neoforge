package net.tomsquared.peony_lib.blockshape;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public final class PeonyLogTextures {
    public static final BooleanProperty SWAPPED =
            BooleanProperty.create("peony_texture_swapped");

    private PeonyLogTextures() {
    }

    public static boolean supports(BlockState state) {
        if (!PeonyBlockShapes.supports(state)) {
            return false;
        }

        TexturePair pair = findPair(state);
        return pair != null
                && PeonyBlockShapes.supports(pair.counterpart().defaultBlockState());
    }

    public static boolean hasTexturePair(BlockState state) {
        return findPair(state) != null;
    }

    public static boolean isWoodTexture(BlockState state) {
        requireSupported(state);
        TexturePair pair = requirePair(state);

        return pair.originallyWood() != state.getValue(SWAPPED);
    }

    public static boolean setWoodTexture(
            ServerLevel level, BlockPos pos, boolean woodTexture) {
        if (!level.hasChunkAt(pos)) {
            throw new IllegalStateException(
                    "Cannot change the texture of an unloaded block at " + pos
            );
        }

        BlockState state = level.getBlockState(pos);
        requireSupported(state);

        TexturePair pair = requirePair(state);
        BlockState changed = state.setValue(
                SWAPPED, woodTexture != pair.originallyWood()
        );

        return changed != state
                && level.setBlock(pos, changed, Block.UPDATE_ALL);
    }

    public static BlockState textureState(BlockState state) {
        if (!state.hasProperty(SWAPPED) || !state.getValue(SWAPPED)) {
            return state;
        }

        TexturePair pair = requirePair(state);

        return pair.counterpart().defaultBlockState()
                .setValue(
                        RotatedPillarBlock.AXIS,
                        state.getValue(RotatedPillarBlock.AXIS)
                )
                .setValue(PeonyBlockShapes.SHAPE, PeonyBlockShape.FULL)
                .setValue(SWAPPED, false);
    }

    @Nullable
    private static TexturePair findPair(BlockState state) {
        if (!hasTextureProperties(state)) {
            return null;
        }

        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        String path = id.getPath();
        String replacement;
        boolean originallyWood;

        if (path.endsWith("_log")) {
            replacement = path.substring(0, path.length() - 4) + "_wood";
            originallyWood = false;
        } else if (path.endsWith("_wood")) {
            replacement = path.substring(0, path.length() - 5) + "_log";
            originallyWood = true;
        } else if (path.endsWith("_stem")) {
            replacement = path.substring(0, path.length() - 5) + "_hyphae";
            originallyWood = false;
        } else if (path.endsWith("_hyphae")) {
            replacement = path.substring(0, path.length() - 7) + "_stem";
            originallyWood = true;
        } else {
            return null;
        }

        ResourceLocation counterpartId = ResourceLocation.fromNamespaceAndPath(
                id.getNamespace(), replacement
        );

        if (!BuiltInRegistries.BLOCK.containsKey(counterpartId)) {
            return null;
        }

        Block counterpart = BuiltInRegistries.BLOCK.get(counterpartId);

        if (!hasTextureProperties(counterpart.defaultBlockState())) {
            return null;
        }

        return new TexturePair(counterpart, originallyWood);
    }

    private static boolean hasTextureProperties(BlockState state) {
        return state.hasProperty(SWAPPED)
                && state.hasProperty(PeonyBlockShapes.SHAPE)
                && state.hasProperty(RotatedPillarBlock.AXIS);
    }

    private static TexturePair requirePair(BlockState state) {
        TexturePair pair = findPair(state);

        if (pair == null) {
            throw new IllegalArgumentException(
                    "No compatible log/wood registry counterpart exists for "
                            + BuiltInRegistries.BLOCK.getKey(state.getBlock())
            );
        }

        return pair;
    }

    private static void requireSupported(BlockState state) {
        if (!supports(state)) {
            throw new IllegalArgumentException(
                    "Texture swapping requires compatible tagged log/wood blocks: "
                            + state
            );
        }
    }

    private record TexturePair(Block counterpart, boolean originallyWood) {
    }
}