package net.tomsqaured.peony_lib.blocks.custom;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class PeonyPlushyBlock extends BaseEntityBlock {
    public static final MapCodec<PeonyPlushyBlock> CODEC = simpleCodec(PeonyPlushyBlock::new);

    private final List<SoundEvent> sounds;

    public PeonyPlushyBlock(Properties properties) {
        this(properties, new SoundEvent[0]);
    }

    public PeonyPlushyBlock(Properties properties, SoundEvent... sounds) {
        super(properties);
        this.sounds = Arrays.stream(sounds)
                .filter(Objects::nonNull)
                .limit(10)
                .toList();
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
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
        float pitch = 0.9F + player.getRandom().nextFloat() * 0.2F;
        level.playSound(null, pos, sound, player.getSoundSource(), 1.0F, pitch);
    }
}