package net.tomsquared.peony_lib.items.custom;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;


public class PeonyPlushies extends BlockItem {
    private static final int SOUND_COOLDOWN_TICKS = 10;
    private final List<SoundEvent> sounds;

    public PeonyPlushies(Block block, Properties properties, SoundEvent... sounds) {
        super(block, properties);
        this.sounds = Arrays.stream(sounds)
                .filter(Objects::nonNull)
                .limit(10)
                .toList();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.pass(stack);
        }

        if (player.isShiftKeyDown()) {
            return InteractionResultHolder.pass(stack);
        }

        playRandomSound(level, player);

        if (!level.isClientSide && !sounds.isEmpty()) {
            player.getCooldowns().addCooldown(this, SOUND_COOLDOWN_TICKS);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();

        if (player != null && player.isShiftKeyDown()) {
            return super.useOn(context);
        }

        if (player != null && player.getCooldowns().isOnCooldown(this)) {
            return InteractionResult.FAIL;
        }

        if (player != null) {
            playRandomSound(context.getLevel(), player);

            if (!context.getLevel().isClientSide && !sounds.isEmpty()) {
                player.getCooldowns().addCooldown(this, SOUND_COOLDOWN_TICKS);
            }

            return InteractionResult.CONSUME;
        }

        return super.useOn(context);
    }


    private void playRandomSound(Level level, Player player) {
        if (level.isClientSide || player == null || sounds.isEmpty()) {
            return;
        }

        SoundEvent sound = sounds.get(player.getRandom().nextInt(sounds.size()));
        float pitch = 1.0F;
        level.playSound(null, player.blockPosition(), sound, player.getSoundSource(), 1.0F, pitch);
    }
}