package net.tomsquared.peony_lib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.tomsquared.peony_lib.blockshape.PeonyBlockShape;
import net.tomsquared.peony_lib.blockshape.PeonyBlockShapes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.tomsquared.peony_lib.blockshape.PeonyLogTextures;

@Mixin(RotatedPillarBlock.class)
public abstract class RotatedPillarBlockMixin extends Block {
    protected RotatedPillarBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "createBlockStateDefinition", at = @At("TAIL"))
    private void peony$addShape(
            StateDefinition.Builder<Block, BlockState> builder,
            CallbackInfo ci) {
        builder.add(PeonyBlockShapes.SHAPE, PeonyLogTextures.SWAPPED);
    }

    @Inject(method = "getStateForPlacement", at = @At("RETURN"), cancellable = true)
    private void peony$restoreShape(BlockPlaceContext context,
                                    CallbackInfoReturnable<BlockState> cir) {
        BlockState state = cir.getReturnValue();
        if (state != null && PeonyBlockShapes.supports(state)) {
            cir.setReturnValue(PeonyBlockShapes.placement(state, context));
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction,
                                      BlockState neighbor, LevelAccessor level,
                                      BlockPos pos, BlockPos neighborPos) {
        BlockState updated = super.updateShape(
                state, direction, neighbor, level, pos, neighborPos
        );

        return direction != Direction.DOWN
                ? PeonyBlockShapes.updateConnections(updated, level, pos)
                : updated;
    }

    @Inject(method = "rotate", at = @At("RETURN"), cancellable = true)
    private void peony$rotateShape(BlockState state, Rotation rotation,
                                   CallbackInfoReturnable<BlockState> cir) {
        cir.setReturnValue(
                PeonyBlockShapes.rotateShape(state, cir.getReturnValue(), rotation)
        );
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return PeonyBlockShapes.isShaped(state)
                ? PeonyBlockShapes.mirrorShape(state, mirror)
                : super.mirror(state, mirror);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level,
                                  BlockPos pos, CollisionContext context) {
        return PeonyBlockShapes.isShaped(state)
                ? PeonyBlockShapes.getVoxelShape(state)
                : super.getShape(state, level, pos, context);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level,
                                           BlockPos pos, CollisionContext context) {
        return PeonyBlockShapes.isShaped(state)
                ? PeonyBlockShapes.getCollisionShape(state)
                : super.getCollisionShape(state, level, pos, context);
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state, BlockGetter level,
                                           BlockPos pos) {
        return PeonyBlockShapes.isShaped(state)
                ? PeonyBlockShapes.getOcclusionShape(state)
                : super.getOcclusionShape(state, level, pos);
    }

    @Override
    protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level,
                                              BlockPos pos) {
        return PeonyBlockShapes.isShaped(state)
                ? PeonyBlockShapes.getSupportShape(state)
                : super.getBlockSupportShape(state, level, pos);
    }

    @Override
    protected VoxelShape getVisualShape(BlockState state, BlockGetter level,
                                        BlockPos pos, CollisionContext context) {
        return PeonyBlockShapes.isShaped(state)
                ? PeonyBlockShapes.getVoxelShape(state)
                : super.getVisualShape(state, level, pos, context);
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return PeonyBlockShapes.isShaped(state)
                || super.useShapeForLightOcclusion(state);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        PeonyBlockShape shape = PeonyBlockShapes.getShape(state);

        if (shape.isFence() || shape.isWall()) {
            return false;
        }

        if (shape.isGate()) {
            return shape.gateState().isPathfindable(type);
        }

        return super.isPathfindable(state, type);
    }

    @Override
        protected InteractionResult useWithoutItem(
                BlockState state, Level level, BlockPos pos,
                Player player, BlockHitResult hitResult) {
            PeonyBlockShape shape = PeonyBlockShapes.getShape(state);

            if (!shape.isGate()) {
                return super.useWithoutItem(state, level, pos, player, hitResult);
            }

            if (level instanceof ServerLevel serverLevel) {
                BlockState gate = shape.gateState();
                boolean opening = !gate.getValue(FenceGateBlock.OPEN);
                Direction direction = player.getDirection();

                if (opening && gate.getValue(FenceGateBlock.FACING) == direction.getOpposite()) {
                    gate = gate.setValue(FenceGateBlock.FACING, direction);
                }

                gate = gate.setValue(FenceGateBlock.OPEN, opening);

                if (PeonyBlockShapes.setShape(
                        serverLevel, pos, PeonyBlockShape.fromGateState(gate))) {
                    level.playSound(
                            null,
                            pos,
                            opening ? SoundEvents.FENCE_GATE_OPEN : SoundEvents.FENCE_GATE_CLOSE,
                            SoundSource.BLOCKS,
                            1.0F,
                            level.getRandom().nextFloat() * 0.1F + 0.9F
                    );

                    level.gameEvent(
                            player,
                            opening ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE,
                            pos
                    );
                }
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void peony$initializeShapeDefaults(
            Properties properties, CallbackInfo ci) {
        BlockState state = defaultBlockState();

        if (state.hasProperty(PeonyBlockShapes.SHAPE)
                && state.hasProperty(PeonyLogTextures.SWAPPED)) {
            registerDefaultState(state
                    .setValue(PeonyBlockShapes.SHAPE, PeonyBlockShape.FULL)
                    .setValue(PeonyLogTextures.SWAPPED, false));
        }
    }
}