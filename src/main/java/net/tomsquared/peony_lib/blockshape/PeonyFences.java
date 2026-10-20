package net.tomsquared.peony_lib.blockshape;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class PeonyFences {
    private PeonyFences() {
    }

    public static BlockState updateConnections(BlockState state,
                                                BlockGetter level, BlockPos pos) {
        if (!PeonyBlockShapes.getShape(state).isFence()) {
            return state;
        }

        FenceBlock fence = (FenceBlock) Blocks.OAK_FENCE;
        BlockState connected = Blocks.OAK_FENCE.defaultBlockState();

        connected = connected
                .setValue(FenceBlock.NORTH, connects(fence, level, pos, Direction.NORTH))
                .setValue(FenceBlock.EAST, connects(fence, level, pos, Direction.EAST))
                .setValue(FenceBlock.SOUTH, connects(fence, level, pos, Direction.SOUTH))
                .setValue(FenceBlock.WEST, connects(fence, level, pos, Direction.WEST));

        return state.setValue(
                PeonyBlockShapes.SHAPE,
                PeonyBlockShape.fromFenceState(connected)
        );
    }

    private static boolean connects(FenceBlock fence, BlockGetter level,
                                      BlockPos pos, Direction direction) {
        BlockPos neighborPos = pos.relative(direction);
        BlockState neighbor = level.getBlockState(neighborPos);

        return fence.connectsTo(
                neighbor,
                neighbor.isFaceSturdy(level, neighborPos, direction.getOpposite()),
                direction.getOpposite()
        );
    }
}