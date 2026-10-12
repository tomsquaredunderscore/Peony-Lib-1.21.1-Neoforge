package net.tomsquared.peony_lib.blocks.custom;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.tomsquared.peony_lib.blocks.PeonyBlockEntities;
import net.tomsquared.peony_lib.blocks.PeonyBlocks;

public class PeonyPlushyBlock extends HorizontalDirectionalBlock implements net.minecraft.world.level.block.EntityBlock {
    public static final MapCodec<PeonyPlushyBlock> CODEC = simpleCodec(PeonyPlushyBlock::new);

    private final List<SoundEvent> sounds;

    public PeonyPlushyBlock(Properties properties) {
        this(properties, new SoundEvent[0]);

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));

    }

    public PeonyPlushyBlock(Properties properties, SoundEvent... sounds) {
        super(properties);
        this.sounds = Arrays.stream(sounds)
                .filter(Objects::nonNull)
                .limit(10)
                .toList();

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));

    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PeonyPlushyBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
    if (level.isClientSide) {
        return InteractionResult.SUCCESS;
    }

    BlockEntity blockEntity = level.getBlockEntity(pos);
    if (blockEntity instanceof PeonyPlushyBlockEntity plushy && plushy.canSquish(level.getGameTime())) {
        plushy.startSquish(level.getGameTime());
        playRandomSound(level, pos, player);
    }

    return InteractionResult.CONSUME;
}

    private InteractionResult handleUse(Level level, BlockPos pos, Player player) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof PeonyPlushyBlockEntity plushy && plushy.canSquish(level.getGameTime())) {
            plushy.startSquish(level.getGameTime());
            playRandomSound(level, pos, player);
        }

        return InteractionResult.CONSUME;
    }

    private void playRandomSound(Level level, BlockPos pos, Player player) {
        if (sounds.isEmpty()) {
            return;
        }

        SoundEvent sound = sounds.get(player.getRandom().nextInt(sounds.size()));
        float pitch = 1.0F;
        level.playSound(null, pos, sound, player.getSoundSource(), 1.0F, pitch);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    @Override
    protected VoxelShape getShape(
            BlockState state,
            net.minecraft.world.level.BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        return Shapes.box(0.21875, 0.0, 0.21875, 0.78125, 0.9375, 0.78125);
    }

}