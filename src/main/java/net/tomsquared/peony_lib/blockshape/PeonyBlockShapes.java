package net.tomsquared.peony_lib.blockshape;

import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;



/**
 * This is where the shapes are defined and how youll access them
 * 
 * supports(state): checks for the logs tag and the injected shape property.
 * getShape(state): reads the exact placed shape.
 * getShape(stack): reads the normalized item shape.
 * isShaped(state): checks whether the shape differs from FULL.
 * isSlab(shape): checks either slab half.
 *
 * setShape(serverLevel, pos, shape): changes an existing supported block,
 * recalculates connections and notifies clients/neighbors.
 * resetShape(serverLevel, pos): restores FULL without changing block identity.
 *
 * copyToStack(state, matchingStack): returns a shaped copy of the stack.
 * itemShape(shape): normalizes orientation/connections for item storage.
 *
 * Shape geometry:
 * getVoxelShape, getCollisionShape, getOcclusionShape, getSupportShape.
 *
 * Internal integration helpers:
 * placement, updateConnections, gateConnects, rotateShape, mirrorShape.
 *
 * Wiring:
 * RotatedPillarBlockMixin adds the property and physical behavior.
 * peony_lib.mixins.json enables the mixins.
 * neoforge.mods.toml loads the mixin configuration.
 * PeonyShapeModels installs the client model wrappers automatically.
 *
 * World changes belong on the logical server thread.
 * Gates support manual right-click opening/closing, but ignore redstone.
 * Trapdoors and buttons remain noninteractive and produce no redstone signals.
 * 
 * Texture-only log/wood variants:
 * PeonyLogTextures.supports(state);
 * PeonyLogTextures.isWoodTexture(state);
 * PeonyLogTextures.setWoodTexture(level, pos, true);
 * PeonyLogTextures.setWoodTexture(level, pos, false);
 *
 * Texture changes preserve block identity, shape, axis and normal drops.
 * copyToStack also preserves the selected texture variant.
 *
 * 
 * PeonyBlockShapes.resetShape(level, pos);
 * PeonyBlockShapes.setShape(level, pos, PeonyBlockShape.FULL);
 * PeonyBlockShapes.setShape(level, pos, PeonyBlockShape.SLAB_BOTTOM);
 * PeonyBlockShapes.setShape(level, pos, PeonyBlockShape.SLAB_TOP);
 * PeonyBlockShapes.setShape(level, pos, PeonyBlockShape.FENCE_NONE);
 * PeonyBlockShapes.setShape(level, pos, PeonyBlockShape.WALL_81);
 * PeonyBlockShapes.setShape(level, pos, PeonyBlockShape.GATE_NORTH_CLOSED);
 * PeonyBlockShapes.setShape(level, pos, PeonyBlockShape.BUTTON_NORTH_FLOOR);
 * PeonyBlockShapes.setShape(level, pos, PeonyBlockShape.BUTTON_EAST_WALL);
 * PeonyBlockShapes.setShape(level, pos, PeonyBlockShape.BUTTON_SOUTH_CEILING);
 * PeonyBlockShapes.setShape(level, pos, PeonyBlockShape.POLE_X);
 * PeonyLogTextures.setWoodTexture(level, pos, true/false);
 * 
 * 
 * 
 * 
 */


public final class PeonyBlockShapes {
    public static final EnumProperty<PeonyBlockShape> SHAPE =
            EnumProperty.create("peony_shape", PeonyBlockShape.class);

    private static final VoxelShape BOTTOM = Block.box(0, 0, 0, 16, 8, 16);
    private static final VoxelShape TOP = Block.box(0, 8, 0, 16, 16, 16);

    private static final VoxelShape POLE_X = Block.box(0, 6, 6, 16, 10, 10);
    private static final VoxelShape POLE_Y = Block.box(6, 0, 6, 10, 16, 10);
    private static final VoxelShape POLE_Z = Block.box(6, 6, 0, 10, 10, 16);

    private PeonyBlockShapes() {
    }

    public static boolean supports(BlockState state) {
        return state.is(BlockTags.LOGS) && state.hasProperty(SHAPE);
    }

    public static PeonyBlockShape getShape(BlockState state) {
        return state.hasProperty(SHAPE)
                ? state.getValue(SHAPE)
                : PeonyBlockShape.FULL;
    }

    public static boolean isShaped(BlockState state) {
        return getShape(state) != PeonyBlockShape.FULL;
    }

    public static boolean isSlab(PeonyBlockShape shape) {
        return shape == PeonyBlockShape.SLAB_BOTTOM
                || shape == PeonyBlockShape.SLAB_TOP;
    }

    public static VoxelShape getVoxelShape(BlockState state) {
        PeonyBlockShape shape = getShape(state);

        if (shape.hasTemplate()) {
            return shape.templateState().getShape(
                    EmptyBlockGetter.INSTANCE, BlockPos.ZERO
            );
        }

        return switch (shape) {
            case FULL -> Shapes.block();
            case SLAB_BOTTOM -> BOTTOM;
            case SLAB_TOP -> TOP;
            case POLE_X -> POLE_X;
            case POLE_Y -> POLE_Y;
            case POLE_Z -> POLE_Z;
            default -> throw new IllegalStateException("Unhandled shape: " + shape);
        };
    }

    public static VoxelShape getCollisionShape(BlockState state) {
        PeonyBlockShape shape = getShape(state);

        return shape.hasTemplate()
                ? shape.templateState().getCollisionShape(
                        EmptyBlockGetter.INSTANCE, BlockPos.ZERO
                )
                : getVoxelShape(state);
    }

    public static VoxelShape getOcclusionShape(BlockState state) {
        PeonyBlockShape shape = getShape(state);

        return shape.hasTemplate()
                ? shape.templateState().getOcclusionShape(
                        EmptyBlockGetter.INSTANCE, BlockPos.ZERO
                )
                : getVoxelShape(state);
    }

    public static VoxelShape getSupportShape(BlockState state) {
        PeonyBlockShape shape = getShape(state);

        return shape.hasTemplate()
                ? shape.templateState().getBlockSupportShape(
                        EmptyBlockGetter.INSTANCE, BlockPos.ZERO
                )
                : getVoxelShape(state);
    }

    @Nullable
    public static BlockState placement(BlockState state, BlockPlaceContext context) {
        BlockItemStateProperties properties = context.getItemInHand()
                .get(DataComponents.BLOCK_STATE);

        if (properties == null || !supports(state)) {
            return state;
        }

        state = properties.apply(state);
        PeonyBlockShape shape = getShape(state);

        if (isSlab(shape)) {
            Direction face = context.getClickedFace();
            double height = context.getClickLocation().y
                    - context.getClickedPos().getY();

            boolean top = face == Direction.DOWN
                    || (face != Direction.UP && height > 0.5D);

            state = state.setValue(
                    SHAPE,
                    top ? PeonyBlockShape.SLAB_TOP : PeonyBlockShape.SLAB_BOTTOM
            );
        } else if (shape.isStairs()) {
            state = PeonyStairs.place(state, context);
        } else if (shape.isTrapdoor()) {
            BlockState placed = Blocks.SPRUCE_TRAPDOOR.getStateForPlacement(context);
            if (placed == null) {
                return null;
            }

            state = state.setValue(SHAPE, PeonyBlockShape.fromTrapdoorState(
                    placed.setValue(TrapDoorBlock.OPEN, false)
            ));
        } else if (shape.isGate()) {
            BlockState placed = Blocks.OAK_FENCE_GATE.getStateForPlacement(context);
            if (placed == null) {
                return null;
            }

            placed = placed.setValue(FenceGateBlock.OPEN, false)
                    .setValue(FenceGateBlock.POWERED, false);

            state = state.setValue(SHAPE, PeonyBlockShape.fromGateState(placed));
        } else if (shape.isButton()) {
            BlockState placed = Blocks.OAK_BUTTON.getStateForPlacement(context);
            if (placed == null) {
                return null;
            }

            state = state.setValue(SHAPE, PeonyBlockShape.fromButtonState(placed));
        } else if (shape.isPole()) {
            Direction.Axis axis = context.getClickedFace().getAxis();

            state = state.setValue(SHAPE, PeonyBlockShape.pole(axis))
                    .setValue(RotatedPillarBlock.AXIS, axis);
        }

        return updateConnections(state, context.getLevel(), context.getClickedPos());
    }

    public static BlockState updateConnections(
            BlockState state, LevelReader level, BlockPos pos) {
        state = PeonyStairs.updateCorners(state, level, pos);
        state = PeonyFences.updateConnections(state, level, pos);
        state = PeonyWalls.updateConnections(state, level, pos);

        PeonyBlockShape shape = getShape(state);
        if (shape.isGate()) {
            BlockState gate = shape.gateState();
            Direction facing = gate.getValue(FenceGateBlock.FACING);
            Direction side = facing.getClockWise();

            boolean inWall = isWall(level.getBlockState(pos.relative(side)))
                    || isWall(level.getBlockState(pos.relative(side.getOpposite())));

            state = state.setValue(SHAPE, PeonyBlockShape.fromGateState(
                    gate.setValue(FenceGateBlock.IN_WALL, inWall)
            ));
        }

        return state;
    }

    private static boolean isWall(BlockState state) {
        return state.is(BlockTags.WALLS) || getShape(state).isWall();
    }

    public static boolean gateConnects(BlockState state, Direction direction) {
        PeonyBlockShape shape = getShape(state);
        return shape.isGate()
                && FenceGateBlock.connectsToDirection(shape.gateState(), direction);
    }

    public static BlockState rotateShape(
            BlockState original, BlockState rotated, Rotation rotation) {
        PeonyBlockShape shape = getShape(original);

        if (shape.hasTemplate()) {
            return rotated.setValue(
                    SHAPE, shape.fromTemplateState(shape.templateState().rotate(rotation))
            );
        }

        if (shape.isPole()) {
            Direction axisDirection = switch (shape.poleAxis()) {
                case X -> Direction.EAST;
                case Y -> Direction.UP;
                case Z -> Direction.SOUTH;
            };

            return rotated.setValue(
                    SHAPE, PeonyBlockShape.pole(rotation.rotate(axisDirection).getAxis())
            );
        }

        return rotated;
    }

    public static BlockState mirrorShape(BlockState state, Mirror mirror) {
        PeonyBlockShape shape = getShape(state);

        return shape.hasTemplate()
                ? state.setValue(
                        SHAPE, shape.fromTemplateState(shape.templateState().mirror(mirror))
                )
                : state;
    }

    public static boolean setShape(
            ServerLevel level, BlockPos pos, PeonyBlockShape shape) {
        Objects.requireNonNull(shape, "shape");

        if (!level.hasChunkAt(pos)) {
            throw new IllegalStateException("Cannot shape an unloaded block at " + pos);
        }

        BlockState state = level.getBlockState(pos);
        if (!supports(state)) {
            throw new IllegalArgumentException("Block does not support shaping: " + state);
        }

        BlockState changed = state.setValue(SHAPE, shape);

        if (shape.isPole()) {
            changed = changed.setValue(RotatedPillarBlock.AXIS, shape.poleAxis());
        }

        changed = updateConnections(changed, level, pos);
        return changed != state && level.setBlock(pos, changed, Block.UPDATE_ALL);
    }

    public static boolean resetShape(ServerLevel level, BlockPos pos) {
        return setShape(level, pos, PeonyBlockShape.FULL);
    }

    public static ItemStack copyToStack(BlockState state, ItemStack stack) {
        if (!supports(state)
                || !(stack.getItem() instanceof BlockItem item)
                || item.getBlock() != state.getBlock()) {
            throw new IllegalArgumentException("Stack does not match a shapeable block");
        }

        ItemStack copy = stack.copy();
        BlockItemStateProperties properties = copy.getOrDefault(
                DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY
        );

        for (var property : state.getProperties()) {
            properties = properties.with(property, state);
        }

        properties = properties.with(SHAPE, itemShape(getShape(state)));

        if (getShape(state).isPole()) {
            properties = properties.with(RotatedPillarBlock.AXIS, Direction.Axis.Y);
        }

        copy.set(DataComponents.BLOCK_STATE, properties);
        return copy;
    }

    public static PeonyBlockShape getShape(ItemStack stack) {
        BlockItemStateProperties properties = stack.get(DataComponents.BLOCK_STATE);
        PeonyBlockShape shape = properties == null ? null : properties.get(SHAPE);

        return shape == null ? PeonyBlockShape.FULL : itemShape(shape);
    }

    public static PeonyBlockShape itemShape(PeonyBlockShape shape) {
        if (isSlab(shape)) return PeonyBlockShape.SLAB_BOTTOM;
        if (shape.isStairs()) return PeonyBlockShape.STAIRS_NORTH_BOTTOM_STRAIGHT;
        if (shape.isFence()) return PeonyBlockShape.FENCE_NONE;
        if (shape.isWall()) return PeonyBlockShape.WALL_81;
        if (shape.isTrapdoor()) return PeonyBlockShape.TRAPDOOR_NORTH_BOTTOM_CLOSED;
        if (shape.isGate()) return PeonyBlockShape.GATE_NORTH_CLOSED;
        if (shape.isButton()) return PeonyBlockShape.BUTTON_NORTH_WALL;
        if (shape.isPole()) return PeonyBlockShape.POLE_Y;

        return shape;
    }
}