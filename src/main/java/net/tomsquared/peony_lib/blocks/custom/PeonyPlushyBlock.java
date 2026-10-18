package net.tomsquared.peony_lib.blocks.custom;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.tomsquared.peony_lib.items.custom.PeonyPlushies;
import net.tomsquared.peony_lib.network.PlushySoundPayload;
import net.tomsquared.peony_lib.sound.PeonySounds;
import net.tomsquared.peony_lib.sound.PeonySoundEvents;

public class PeonyPlushyBlock extends HorizontalDirectionalBlock implements EntityBlock {
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
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }

        if (level.getBlockEntity(pos) instanceof PeonyPlushyBlockEntity plushy) {
            if (plushy.canSquish(level.getGameTime())) {
                plushy.startSquish(level.getGameTime());
            }

            int index = sounds.isEmpty() ? 0 : level.getRandom().nextInt(sounds.size());
                        if (playSound(serverLevel, pos, plushy, index)) {
                level.gameEvent(player, PeonySoundEvents.PLUSHY_SOUND, pos);
            }
        }

        return InteractionResult.CONSUME;
    }

        @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof PeonyPlushyBlockEntity plushy)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        // normal and squeaky can only be sheared, muted can only be filled
        if (itemStack.is(Items.SHEARS) && (plushy.isSqueaky() || !plushy.isMuted())) {
            if (!level.isClientSide) {
                ItemStack removed = new ItemStack(plushy.isSqueaky() ? Items.SLIME_BALL : Items.NOTE_BLOCK);
                plushy.setSqueaky(false);
                plushy.setMuted(true);

                itemStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                giveBack(level, player, removed);
                level.gameEvent(player, GameEvent.SHEAR, pos);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        boolean plainMuted = plushy.isMuted() && !plushy.isSqueaky();
        if (plainMuted && (itemStack.is(Items.NOTE_BLOCK) || itemStack.is(Items.SLIME_BALL))) {
            if (!level.isClientSide) {
                plushy.setMuted(false);
                plushy.setSqueaky(itemStack.is(Items.SLIME_BALL));

                itemStack.consume(1, player);
                level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
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
                if (playSound(level, pos, plushy, 15 - signal)) {
            level.gameEvent(PeonySoundEvents.PLUSHY_SOUND, pos, GameEvent.Context.of(state));
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (!level.isClientSide && level.getBlockEntity(pos) instanceof PeonyPlushyBlockEntity plushy) {
            plushy.setMuted(PeonyPlushies.isMuted(stack));
            plushy.setSqueaky(PeonyPlushies.isSqueaky(stack));
            plushy.updateSignal(level.getBestNeighborSignal(pos));
        }
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return List.of(stackFor(builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY)));
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return stackFor(level.getBlockEntity(pos));
    }

    private ItemStack stackFor(BlockEntity blockEntity) {
        ItemStack stack = new ItemStack(this);

        if (blockEntity instanceof PeonyPlushyBlockEntity plushy) {
            PeonyPlushies.setMuted(stack, plushy.isMuted());
            PeonyPlushies.setSqueaky(stack, plushy.isSqueaky());
        }

        return stack;
    }

        private boolean playSound(ServerLevel level, BlockPos pos, PeonyPlushyBlockEntity plushy, int index) {
        SoundEvent sound;
        if (plushy.isSqueaky()) {
            sound = PeonySounds.SQUEAK.get();
        } else if (plushy.isMuted() || sounds.isEmpty()) {
            return false;
        } else {
            sound = sounds.get(Math.min(index, sounds.size() - 1));
        }

        PlushySoundPayload.playAtBlock(level, pos, sound, BuiltInRegistries.BLOCK.getKey(this));
        return true;
    }

    private static void giveBack(Level level, Player player, ItemStack stack) {
        if (player.getInventory().add(stack)) {
            float pitch = ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F;
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, pitch);
        } else {
            player.drop(stack, false);
        }
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
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.box(0.21875, 0.0, 0.21875, 0.78125, 0.9375, 0.78125);
    }
}