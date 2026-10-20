package net.tomsquared.peony_lib.blockshape;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;

public final class PeonyStairs {
    private PeonyStairs() {
    }

    public static BlockState place(BlockState state, BlockPlaceContext context) {
        Direction face = context.getClickedFace();
        double height = context.getClickLocation().y - context.getClickedPos().getY();

        Half half = face == Direction.DOWN
                || (face != Direction.UP && height > 0.5D)
                ? Half.TOP
                : Half.BOTTOM;

        PeonyBlockShape shape = PeonyBlockShape.stairs(
                context.getHorizontalDirection(),
                half,
                StairsShape.STRAIGHT
        );

        return updateCorners(
                state.setValue(PeonyBlockShapes.SHAPE, shape),
                context.getLevel(),
                context.getClickedPos()
        );
    }

    public static BlockState updateCorners(BlockState state, BlockGetter level,
                                            BlockPos pos) {
        PeonyBlockShape shape = PeonyBlockShapes.getShape(state);
        if (!shape.isStairs()) {
            return state;
        }

        BlockState stairs = shape.stairState();
        Direction facing = stairs.getValue(StairBlock.FACING);
        Half half = stairs.getValue(StairBlock.HALF);

        StairsShape corner = StairsShape.STRAIGHT;

        PeonyBlockShape front = PeonyBlockShapes.getShape(
                level.getBlockState(pos.relative(facing))
        );

        if (front.isStairs()) {
            BlockState neighbor = front.stairState();
            Direction next = neighbor.getValue(StairBlock.FACING);

            if (neighbor.getValue(StairBlock.HALF) == half
                    && next.getAxis() != facing.getAxis()
                    && canTurn(level, pos, next.getOpposite(), facing, half)) {
                corner = next == facing.getCounterClockWise()
                        ? StairsShape.OUTER_LEFT
                        : StairsShape.OUTER_RIGHT;
            }
        }

        if (corner == StairsShape.STRAIGHT) {
            PeonyBlockShape back = PeonyBlockShapes.getShape(
                    level.getBlockState(pos.relative(facing.getOpposite()))
            );

            if (back.isStairs()) {
                BlockState neighbor = back.stairState();
                Direction next = neighbor.getValue(StairBlock.FACING);

                if (neighbor.getValue(StairBlock.HALF) == half
                        && next.getAxis() != facing.getAxis()
                        && canTurn(level, pos, next, facing, half)) {
                    corner = next == facing.getCounterClockWise()
                            ? StairsShape.INNER_LEFT
                            : StairsShape.INNER_RIGHT;
                }
            }
        }

        return state.setValue(
                PeonyBlockShapes.SHAPE,
                PeonyBlockShape.stairs(facing, half, corner)
        );
    }

    private static boolean canTurn(BlockGetter level, BlockPos pos,
                                    Direction side, Direction facing, Half half) {
        PeonyBlockShape shape = PeonyBlockShapes.getShape(
                level.getBlockState(pos.relative(side))
        );

        if (!shape.isStairs()) {
            return true;
        }

        BlockState neighbor = shape.stairState();
        return neighbor.getValue(StairBlock.FACING) != facing
                || neighbor.getValue(StairBlock.HALF) != half;
    }
}