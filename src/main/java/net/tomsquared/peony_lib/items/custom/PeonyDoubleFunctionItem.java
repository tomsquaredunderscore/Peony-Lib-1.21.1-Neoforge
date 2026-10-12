package net.tomsquared.peony_lib.items.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public abstract class PeonyDoubleFunctionItem extends Item {
    private final boolean allowEntityAttacks;

    protected PeonyDoubleFunctionItem(Properties properties, boolean allowEntityAttacks) {
        super(properties);
        this.allowEntityAttacks = allowEntityAttacks;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            return secondaryUse(level, player, hand, stack);
        }

        return primaryUse(level, player, hand, stack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();

        if (player != null && player.isShiftKeyDown()) {
            return secondaryUseOn(context);
        }

        return primaryUseOn(context);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!allowEntityAttacks || !(attacker instanceof Player player)) {
            return false;
        }

        if (player.isShiftKeyDown()) {
            return secondaryHitEntity(stack, target, player);
        }

        return primaryHitEntity(stack, target, player);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!(miner instanceof Player player)) {
            return super.mineBlock(stack, level, state, pos, miner);
        }

        if (player.isShiftKeyDown()) {
            return secondaryMineBlock(stack, level, state, pos, player);
        }

        return primaryMineBlock(stack, level, state, pos, player);
    }

    protected InteractionResultHolder<ItemStack> primaryUse(Level level, Player player, InteractionHand hand, ItemStack stack) {
        return InteractionResultHolder.pass(stack);
    }

    protected InteractionResultHolder<ItemStack> secondaryUse(Level level, Player player, InteractionHand hand, ItemStack stack) {
        return InteractionResultHolder.pass(stack);
    }

    protected InteractionResult primaryUseOn(UseOnContext context) {
        return InteractionResult.PASS;
    }

    protected InteractionResult secondaryUseOn(UseOnContext context) {
        return InteractionResult.PASS;
    }

    protected boolean primaryHitEntity(ItemStack stack, LivingEntity target, Player player) {
        return super.hurtEnemy(stack, target, player);
    }

    protected boolean secondaryHitEntity(ItemStack stack, LivingEntity target, Player player) {
        return super.hurtEnemy(stack, target, player);
    }

    protected boolean primaryMineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, Player player) {
        return super.mineBlock(stack, level, state, pos, player);
    }

    protected boolean secondaryMineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, Player player) {
        return super.mineBlock(stack, level, state, pos, player);
    }
}