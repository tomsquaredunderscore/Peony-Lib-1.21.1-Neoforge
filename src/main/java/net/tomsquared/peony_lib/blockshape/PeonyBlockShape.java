package net.tomsquared.peony_lib.blockshape;

import java.util.Locale;

import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.block.state.properties.WallSide;

public enum PeonyBlockShape implements StringRepresentable {
    FULL,
    SLAB_BOTTOM,
    SLAB_TOP,

    STAIRS_NORTH_BOTTOM_STRAIGHT,
    STAIRS_NORTH_BOTTOM_INNER_LEFT,
    STAIRS_NORTH_BOTTOM_INNER_RIGHT,
    STAIRS_NORTH_BOTTOM_OUTER_LEFT,
    STAIRS_NORTH_BOTTOM_OUTER_RIGHT,
    STAIRS_NORTH_TOP_STRAIGHT,
    STAIRS_NORTH_TOP_INNER_LEFT,
    STAIRS_NORTH_TOP_INNER_RIGHT,
    STAIRS_NORTH_TOP_OUTER_LEFT,
    STAIRS_NORTH_TOP_OUTER_RIGHT,

    STAIRS_EAST_BOTTOM_STRAIGHT,
    STAIRS_EAST_BOTTOM_INNER_LEFT,
    STAIRS_EAST_BOTTOM_INNER_RIGHT,
    STAIRS_EAST_BOTTOM_OUTER_LEFT,
    STAIRS_EAST_BOTTOM_OUTER_RIGHT,
    STAIRS_EAST_TOP_STRAIGHT,
    STAIRS_EAST_TOP_INNER_LEFT,
    STAIRS_EAST_TOP_INNER_RIGHT,
    STAIRS_EAST_TOP_OUTER_LEFT,
    STAIRS_EAST_TOP_OUTER_RIGHT,

    STAIRS_SOUTH_BOTTOM_STRAIGHT,
    STAIRS_SOUTH_BOTTOM_INNER_LEFT,
    STAIRS_SOUTH_BOTTOM_INNER_RIGHT,
    STAIRS_SOUTH_BOTTOM_OUTER_LEFT,
    STAIRS_SOUTH_BOTTOM_OUTER_RIGHT,
    STAIRS_SOUTH_TOP_STRAIGHT,
    STAIRS_SOUTH_TOP_INNER_LEFT,
    STAIRS_SOUTH_TOP_INNER_RIGHT,
    STAIRS_SOUTH_TOP_OUTER_LEFT,
    STAIRS_SOUTH_TOP_OUTER_RIGHT,

    STAIRS_WEST_BOTTOM_STRAIGHT,
    STAIRS_WEST_BOTTOM_INNER_LEFT,
    STAIRS_WEST_BOTTOM_INNER_RIGHT,
    STAIRS_WEST_BOTTOM_OUTER_LEFT,
    STAIRS_WEST_BOTTOM_OUTER_RIGHT,
    STAIRS_WEST_TOP_STRAIGHT,
    STAIRS_WEST_TOP_INNER_LEFT,
    STAIRS_WEST_TOP_INNER_RIGHT,
    STAIRS_WEST_TOP_OUTER_LEFT,
    STAIRS_WEST_TOP_OUTER_RIGHT,

    FENCE_NONE, FENCE_N, FENCE_E, FENCE_NE,
    FENCE_S, FENCE_NS, FENCE_ES, FENCE_NES,
    FENCE_W, FENCE_NW, FENCE_EW, FENCE_NEW,
    FENCE_SW, FENCE_NSW, FENCE_ESW, FENCE_NESW,

    //Thank God for predictive text fuck most of this shit

    WALL_0, WALL_1, WALL_2, WALL_3, WALL_4, WALL_5,
    WALL_6, WALL_7, WALL_8, WALL_9, WALL_10, WALL_11,
    WALL_12, WALL_13, WALL_14, WALL_15, WALL_16, WALL_17,
    WALL_18, WALL_19, WALL_20, WALL_21, WALL_22, WALL_23,
    WALL_24, WALL_25, WALL_26, WALL_27, WALL_28, WALL_29,
    WALL_30, WALL_31, WALL_32, WALL_33, WALL_34, WALL_35,
    WALL_36, WALL_37, WALL_38, WALL_39, WALL_40, WALL_41,
    WALL_42, WALL_43, WALL_44, WALL_45, WALL_46, WALL_47,
    WALL_48, WALL_49, WALL_50, WALL_51, WALL_52, WALL_53,
    WALL_54, WALL_55, WALL_56, WALL_57, WALL_58, WALL_59,
    WALL_60, WALL_61, WALL_62, WALL_63, WALL_64, WALL_65,
    WALL_66, WALL_67, WALL_68, WALL_69, WALL_70, WALL_71,
    WALL_72, WALL_73, WALL_74, WALL_75, WALL_76, WALL_77,
    WALL_78, WALL_79, WALL_80, WALL_81, WALL_82, WALL_83,
    WALL_84, WALL_85, WALL_86, WALL_87, WALL_88, WALL_89,
    WALL_90, WALL_91, WALL_92, WALL_93, WALL_94, WALL_95,
    WALL_96, WALL_97, WALL_98, WALL_99, WALL_100, WALL_101,
    WALL_102, WALL_103, WALL_104, WALL_105, WALL_106, WALL_107,
    WALL_108, WALL_109, WALL_110, WALL_111, WALL_112, WALL_113,
    WALL_114, WALL_115, WALL_116, WALL_117, WALL_118, WALL_119,
    WALL_120, WALL_121, WALL_122, WALL_123, WALL_124, WALL_125,
    WALL_126, WALL_127, WALL_128, WALL_129, WALL_130, WALL_131,
    WALL_132, WALL_133, WALL_134, WALL_135, WALL_136, WALL_137,
    WALL_138, WALL_139, WALL_140, WALL_141, WALL_142, WALL_143,
    WALL_144, WALL_145, WALL_146, WALL_147, WALL_148, WALL_149,
    WALL_150, WALL_151, WALL_152, WALL_153, WALL_154, WALL_155,
    WALL_156, WALL_157, WALL_158, WALL_159, WALL_160, WALL_161,

    TRAPDOOR_NORTH_BOTTOM_CLOSED,
    TRAPDOOR_NORTH_BOTTOM_OPEN,
    TRAPDOOR_NORTH_TOP_CLOSED,
    TRAPDOOR_NORTH_TOP_OPEN,
    TRAPDOOR_EAST_BOTTOM_CLOSED,
    TRAPDOOR_EAST_BOTTOM_OPEN,
    TRAPDOOR_EAST_TOP_CLOSED,
    TRAPDOOR_EAST_TOP_OPEN,
    TRAPDOOR_SOUTH_BOTTOM_CLOSED,
    TRAPDOOR_SOUTH_BOTTOM_OPEN,
    TRAPDOOR_SOUTH_TOP_CLOSED,
    TRAPDOOR_SOUTH_TOP_OPEN,
    TRAPDOOR_WEST_BOTTOM_CLOSED,
    TRAPDOOR_WEST_BOTTOM_OPEN,
    TRAPDOOR_WEST_TOP_CLOSED,
    TRAPDOOR_WEST_TOP_OPEN,

    GATE_NORTH_CLOSED,
    GATE_NORTH_OPEN,
    GATE_NORTH_WALL_CLOSED,
    GATE_NORTH_WALL_OPEN,
    GATE_EAST_CLOSED,
    GATE_EAST_OPEN,
    GATE_EAST_WALL_CLOSED,
    GATE_EAST_WALL_OPEN,
    GATE_SOUTH_CLOSED,
    GATE_SOUTH_OPEN,
    GATE_SOUTH_WALL_CLOSED,
    GATE_SOUTH_WALL_OPEN,
    GATE_WEST_CLOSED,
    GATE_WEST_OPEN,
    GATE_WEST_WALL_CLOSED,
    GATE_WEST_WALL_OPEN,

    BUTTON_NORTH_FLOOR,
    BUTTON_NORTH_WALL,
    BUTTON_NORTH_CEILING,
    BUTTON_EAST_FLOOR,
    BUTTON_EAST_WALL,
    BUTTON_EAST_CEILING,
    BUTTON_SOUTH_FLOOR,
    BUTTON_SOUTH_WALL,
    BUTTON_SOUTH_CEILING,
    BUTTON_WEST_FLOOR,
    BUTTON_WEST_WALL,
    BUTTON_WEST_CEILING,

    POLE_X,
    POLE_Y,
    POLE_Z;

    public boolean isStairs() {
        return between(STAIRS_NORTH_BOTTOM_STRAIGHT, STAIRS_WEST_TOP_OUTER_RIGHT);
    }

    public boolean isFence() {
        return between(FENCE_NONE, FENCE_NESW);
    }

    public boolean isWall() {
        return between(WALL_0, WALL_161);
    }

    public boolean isTrapdoor() {
        return between(TRAPDOOR_NORTH_BOTTOM_CLOSED, TRAPDOOR_WEST_TOP_OPEN);
    }

    public boolean isGate() {
        return between(GATE_NORTH_CLOSED, GATE_WEST_WALL_OPEN);
    }

    public boolean isButton() {
        return between(BUTTON_NORTH_FLOOR, BUTTON_WEST_CEILING);
    }

    public boolean isPole() {
        return this == POLE_X || this == POLE_Y || this == POLE_Z;
    }

    public boolean hasTemplate() {
        return isStairs() || isFence() || isWall()
                || isTrapdoor() || isGate() || isButton();
    }

    private boolean between(PeonyBlockShape first, PeonyBlockShape last) {
        return ordinal() >= first.ordinal() && ordinal() <= last.ordinal();
    }

    public BlockState stairState() {
        require(isStairs());
        int code = ordinal() - STAIRS_NORTH_BOTTOM_STRAIGHT.ordinal();

        return Blocks.OAK_STAIRS.defaultBlockState()
                .setValue(StairBlock.FACING, direction(code / 10))
                .setValue(StairBlock.HALF, code % 10 >= 5 ? Half.TOP : Half.BOTTOM)
                .setValue(StairBlock.SHAPE, StairsShape.values()[code % 5]);
    }

    public static PeonyBlockShape stairs(Direction facing, Half half,
                                         StairsShape corner) {
        int code = directionCode(facing) * 10
                + (half == Half.TOP ? 5 : 0)
                + corner.ordinal();

        return values()[STAIRS_NORTH_BOTTOM_STRAIGHT.ordinal() + code];
    }

    public static PeonyBlockShape fromStairState(BlockState state) {
        return stairs(
                state.getValue(StairBlock.FACING),
                state.getValue(StairBlock.HALF),
                state.getValue(StairBlock.SHAPE)
        );
    }

    public BlockState fenceState() {
        require(isFence());
        int mask = ordinal() - FENCE_NONE.ordinal();

        return Blocks.OAK_FENCE.defaultBlockState()
                .setValue(BlockStateProperties.NORTH, (mask & 1) != 0)
                .setValue(BlockStateProperties.EAST, (mask & 2) != 0)
                .setValue(BlockStateProperties.SOUTH, (mask & 4) != 0)
                .setValue(BlockStateProperties.WEST, (mask & 8) != 0);
    }

    public static PeonyBlockShape fromFenceState(BlockState state) {
        int mask = (state.getValue(BlockStateProperties.NORTH) ? 1 : 0)
                | (state.getValue(BlockStateProperties.EAST) ? 2 : 0)
                | (state.getValue(BlockStateProperties.SOUTH) ? 4 : 0)
                | (state.getValue(BlockStateProperties.WEST) ? 8 : 0);

        return values()[FENCE_NONE.ordinal() + mask];
    }

    public BlockState wallState() {
        require(isWall());
        int code = ordinal() - WALL_0.ordinal();

        return Blocks.COBBLESTONE_WALL.defaultBlockState()
                .setValue(WallBlock.NORTH_WALL, wallSide(code % 3))
                .setValue(WallBlock.EAST_WALL, wallSide(code / 3 % 3))
                .setValue(WallBlock.SOUTH_WALL, wallSide(code / 9 % 3))
                .setValue(WallBlock.WEST_WALL, wallSide(code / 27 % 3))
                .setValue(WallBlock.UP, code >= 81);
    }

    public static PeonyBlockShape fromWallState(BlockState state) {
        int code = wallCode(state.getValue(WallBlock.NORTH_WALL))
                + 3 * wallCode(state.getValue(WallBlock.EAST_WALL))
                + 9 * wallCode(state.getValue(WallBlock.SOUTH_WALL))
                + 27 * wallCode(state.getValue(WallBlock.WEST_WALL))
                + (state.getValue(WallBlock.UP) ? 81 : 0);

        return values()[WALL_0.ordinal() + code];
    }

    public BlockState trapdoorState() {
        require(isTrapdoor());
        int code = ordinal() - TRAPDOOR_NORTH_BOTTOM_CLOSED.ordinal();

        return Blocks.SPRUCE_TRAPDOOR.defaultBlockState()
                .setValue(TrapDoorBlock.FACING, direction(code / 4))
                .setValue(TrapDoorBlock.HALF, (code & 2) != 0 ? Half.TOP : Half.BOTTOM)
                .setValue(TrapDoorBlock.OPEN, (code & 1) != 0);
    }

    public static PeonyBlockShape fromTrapdoorState(BlockState state) {
        int code = directionCode(state.getValue(TrapDoorBlock.FACING)) * 4
                + (state.getValue(TrapDoorBlock.HALF) == Half.TOP ? 2 : 0)
                + (state.getValue(TrapDoorBlock.OPEN) ? 1 : 0);

        return values()[TRAPDOOR_NORTH_BOTTOM_CLOSED.ordinal() + code];
    }

    public BlockState gateState() {
        require(isGate());
        int code = ordinal() - GATE_NORTH_CLOSED.ordinal();

        return Blocks.OAK_FENCE_GATE.defaultBlockState()
                .setValue(FenceGateBlock.FACING, direction(code / 4))
                .setValue(FenceGateBlock.IN_WALL, (code & 2) != 0)
                .setValue(FenceGateBlock.OPEN, (code & 1) != 0);
    }

    public static PeonyBlockShape fromGateState(BlockState state) {
        int code = directionCode(state.getValue(FenceGateBlock.FACING)) * 4
                + (state.getValue(FenceGateBlock.IN_WALL) ? 2 : 0)
                + (state.getValue(FenceGateBlock.OPEN) ? 1 : 0);

        return values()[GATE_NORTH_CLOSED.ordinal() + code];
    }

    public BlockState buttonState() {
        require(isButton());
        int code = ordinal() - BUTTON_NORTH_FLOOR.ordinal();

        AttachFace face = switch (code % 3) {
            case 0 -> AttachFace.FLOOR;
            case 1 -> AttachFace.WALL;
            case 2 -> AttachFace.CEILING;
            default -> throw new IllegalStateException("Invalid button code");
        };

        return Blocks.OAK_BUTTON.defaultBlockState()
                .setValue(ButtonBlock.FACING, direction(code / 3))
                .setValue(ButtonBlock.FACE, face)
                .setValue(ButtonBlock.POWERED, false);
    }

    public static PeonyBlockShape fromButtonState(BlockState state) {
        int face = switch (state.getValue(ButtonBlock.FACE)) {
            case FLOOR -> 0;
            case WALL -> 1;
            case CEILING -> 2;
        };

        int code = directionCode(state.getValue(ButtonBlock.FACING)) * 3 + face;
        return values()[BUTTON_NORTH_FLOOR.ordinal() + code];
    }

    public Direction.Axis poleAxis() {
        return switch (this) {
            case POLE_X -> Direction.Axis.X;
            case POLE_Y -> Direction.Axis.Y;
            case POLE_Z -> Direction.Axis.Z;
            default -> throw new IllegalStateException("Not a pole: " + this);
        };
    }

    public static PeonyBlockShape pole(Direction.Axis axis) {
        return switch (axis) {
            case X -> POLE_X;
            case Y -> POLE_Y;
            case Z -> POLE_Z;
        };
    }

    public BlockState templateState() {
        if (isStairs()) return stairState();
        if (isFence()) return fenceState();
        if (isWall()) return wallState();
        if (isTrapdoor()) return trapdoorState();
        if (isGate()) return gateState();
        if (isButton()) return buttonState();

        throw new IllegalStateException("No template for " + this);
    }

    public PeonyBlockShape fromTemplateState(BlockState state) {
        if (isStairs()) return fromStairState(state);
        if (isFence()) return fromFenceState(state);
        if (isWall()) return fromWallState(state);
        if (isTrapdoor()) return fromTrapdoorState(state);
        if (isGate()) return fromGateState(state);
        if (isButton()) return fromButtonState(state);

        throw new IllegalStateException("No template for " + this);
    }

    private void require(boolean valid) {
        if (!valid) {
            throw new IllegalStateException("Wrong shape family: " + this);
        }
    }

    private static Direction direction(int code) {
        return switch (code) {
            case 0 -> Direction.NORTH;
            case 1 -> Direction.EAST;
            case 2 -> Direction.SOUTH;
            case 3 -> Direction.WEST;
            default -> throw new IllegalArgumentException("Invalid direction: " + code);
        };
    }

    private static int directionCode(Direction direction) {
        return switch (direction) {
            case NORTH -> 0;
            case EAST -> 1;
            case SOUTH -> 2;
            case WEST -> 3;
            default -> throw new IllegalArgumentException("Expected horizontal direction");
        };
    }

    private static WallSide wallSide(int code) {
        return switch (code) {
            case 0 -> WallSide.NONE;
            case 1 -> WallSide.LOW;
            case 2 -> WallSide.TALL;
            default -> throw new IllegalArgumentException("Invalid wall side: " + code);
        };
    }

    private static int wallCode(WallSide side) {
        return switch (side) {
            case NONE -> 0;
            case LOW -> 1;
            case TALL -> 2;
        };
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}