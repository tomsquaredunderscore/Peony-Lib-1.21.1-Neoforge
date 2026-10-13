
package net.tomsquared.peony_lib.blocks;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FoodPortionBlock extends Block {

    public static final IntegerProperty PORTIONS_TAKEN =
            IntegerProperty.create("portions_taken", 0, 15);

    private final Supplier<? extends Item> portionItem;
    private final int totalPortions;
    private final VoxelShape shape;

    public FoodPortionBlock(
            Properties properties,
            Supplier<? extends Item> portionItem,
            int totalPortions,
            VoxelShape shape) {

        super(properties);

        if (totalPortions < 1 || totalPortions > 16) {
            throw new IllegalArgumentException(
                    "Portions must be between 1 and 16"
            );
        }

        this.portionItem = portionItem;
        this.totalPortions = totalPortions;
        this.shape = shape;

        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(PORTIONS_TAKEN, 0)
        );
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PORTIONS_TAKEN);
    }

    @Override
    protected VoxelShape getShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context) {
        return shape;
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hit) {

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.ITEM_PICKUP,
                SoundSource.PLAYERS,
                1.0F,
                1.0F
        );

        ItemStack portion = new ItemStack(portionItem.get());

        if (!player.getInventory().add(portion)) {
            player.drop(portion, false);
        }

        int taken = state.getValue(PORTIONS_TAKEN);

        if (taken >= totalPortions - 1) {
            level.removeBlock(pos, false);
        } else {
            level.setBlock(
                    pos,
                    state.setValue(PORTIONS_TAKEN, taken + 1),
                    3
            );
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public List<ItemStack> getDrops(
            BlockState state,
            net.minecraft.world.level.storage.loot.LootParams.Builder builder) {

        int remaining = totalPortions - state.getValue(PORTIONS_TAKEN);

        if (remaining <= 0) {
            return List.of();
        }

        return List.of(new ItemStack(portionItem.get(), remaining));
    }
}

