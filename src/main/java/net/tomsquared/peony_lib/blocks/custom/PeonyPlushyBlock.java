package net.tomsquared.peony_lib.blocks.custom;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.tomsquared.peony_lib.items.custom.PeonyPlushies;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.tomsquared.peony_lib.network.PlushySoundPayload;
import net.minecraft.util.RandomSource;

public class PeonyPlushyBlock extends HorizontalDirectionalBlock implements net.minecraft.world.level.block.EntityBlock {
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
        if (blockEntity instanceof PeonyPlushyBlockEntity plushy) {
            if (plushy.canSquish(level.getGameTime())) {
                plushy.startSquish(level.getGameTime());
            }

            if (!plushy.isMuted()) {
                playRandomSound(level, pos, player);
            }
        }

        return InteractionResult.CONSUME;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof PeonyPlushyBlockEntity plushy)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (itemStack.is(Items.SHEARS) && !plushy.isMuted()) {
            if (!level.isClientSide) {
                plushy.setMuted(true);
                itemStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));

                ItemStack noteBlock = new ItemStack(Items.NOTE_BLOCK);
                if (!player.getInventory().add(noteBlock)) {
                    player.drop(noteBlock, false);
                }
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (itemStack.is(Items.NOTE_BLOCK) && plushy.isMuted()) {
            if (!level.isClientSide) {
                plushy.setMuted(false);
                itemStack.consume(1, player);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

        @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);

        if (!level.isClientSide) {
            level.scheduleTick(pos, this, 1);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!(level.getBlockEntity(pos) instanceof PeonyPlushyBlockEntity plushy)) {
            return;
        }

        int signal = level.getBestNeighborSignal(pos);
        if (!plushy.updateSignal(signal) || signal == 0) {
            return;
        }

        plushy.startSquish(level.getGameTime());

        if (!plushy.isMuted() && !sounds.isEmpty()) {
            // strongest signal = first sound, anything past the end reuses the last one
            int index = Math.min(15 - signal, sounds.size() - 1);
            playSound(level, pos, sounds.get(index));
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (!level.isClientSide && level.getBlockEntity(pos) instanceof PeonyPlushyBlockEntity plushy) {
            plushy.setMuted(PeonyPlushies.isMuted(stack));
            plushy.updateSignal(level.getBestNeighborSignal(pos));
        }
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        ItemStack stack = new ItemStack(this);

        if (builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof PeonyPlushyBlockEntity plushy && plushy.isMuted()) {
            PeonyPlushies.setMuted(stack, true);
        }

        return List.of(stack);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        ItemStack stack = new ItemStack(this);

        if (level.getBlockEntity(pos) instanceof PeonyPlushyBlockEntity plushy && plushy.isMuted()) {
            PeonyPlushies.setMuted(stack, true);
        }

        return stack;
    }

    private void playRandomSound(Level level, BlockPos pos, Player player) {
        if (level instanceof ServerLevel serverLevel && !sounds.isEmpty()) {
            playSound(serverLevel, pos, sounds.get(level.getRandom().nextInt(sounds.size())));
        }
    }

    private void playSound(ServerLevel level, BlockPos pos, SoundEvent sound) {
            PlushySoundPayload.playAtBlock(level, pos, sound, BuiltInRegistries.BLOCK.getKey(this));
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