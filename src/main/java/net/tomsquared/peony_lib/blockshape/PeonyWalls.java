package net.tomsquared.peony_lib.blockshape;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.tomsquared.peony_lib.mixin.WallBlockAccess;

public final class PeonyWalls {
    private PeonyWalls() {
    }

    public static BlockState updateConnections(BlockState state,
                                                LevelReader level, BlockPos pos) {
        if (!PeonyBlockShapes.getShape(state).isWall()) {
            return state;
        }

        WallBlockAccess wall = (WallBlockAccess) Blocks.COBBLESTONE_WALL;
        BlockPos abovePos = pos.above();

        BlockState calculated = wall.peony$calculateShape(
                level,
                Blocks.COBBLESTONE_WALL.defaultBlockState(),
                abovePos,
                level.getBlockState(abovePos),
                connects(wall, level, pos, Direction.NORTH),
                connects(wall, level, pos, Direction.EAST),
                connects(wall, level, pos, Direction.SOUTH),
                connects(wall, level, pos, Direction.WEST)
        );

        return state.setValue(
                PeonyBlockShapes.SHAPE,
                PeonyBlockShape.fromWallState(calculated)
        );
    }

    private static boolean connects(WallBlockAccess wall, LevelReader level,
                                      BlockPos pos, Direction direction) {
        BlockPos neighborPos = pos.relative(direction);
        BlockState neighbor = level.getBlockState(neighborPos);

        return wall.peony$connectsTo(
                neighbor,
                neighbor.isFaceSturdy(level, neighborPos, direction.getOpposite()),
                direction.getOpposite()
        );
    }
}