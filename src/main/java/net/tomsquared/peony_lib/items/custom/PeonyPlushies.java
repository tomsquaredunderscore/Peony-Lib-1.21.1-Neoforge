package net.tomsquared.peony_lib.items.custom;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.tomsquared.peony_lib.network.PlushySoundPayload;

public class PeonyPlushies extends BlockItem {
    public static final String MUTED_TAG = "Muted";

    private static final int SOUND_COOLDOWN_TICKS = 10;

    private final List<SoundEvent> sounds;

    public PeonyPlushies(Block block, Properties properties, SoundEvent... sounds) {
        super(block, properties);
        this.sounds = Arrays.stream(sounds)
                .filter(Objects::nonNull)
                .limit(10)
                .toList();
    }

    public static boolean isMuted(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).contains(MUTED_TAG);
    }

    public static void setMuted(ItemStack stack, boolean muted) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);

        stack.set(DataComponents.CUSTOM_DATA, customData.update(tag -> {
            if (muted) {
                tag.putBoolean(MUTED_TAG, true);
            } else {
                tag.remove(MUTED_TAG);
            }
        }));
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

        if (!isMuted(stack)) {
            playRandomSound(level, player);

            if (!level.isClientSide && !sounds.isEmpty()) {
                player.getCooldowns().addCooldown(this, SOUND_COOLDOWN_TICKS);
            }
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

        if (player != null && !isMuted(context.getItemInHand())) {
            playRandomSound(context.getLevel(), player);

            if (!context.getLevel().isClientSide && !sounds.isEmpty()) {
                player.getCooldowns().addCooldown(this, SOUND_COOLDOWN_TICKS);
            }
        }

        return player != null ? InteractionResult.CONSUME : super.useOn(context);
    }

    private void playRandomSound(Level level, Player player) {
        if (!(level instanceof ServerLevel serverLevel) || player == null || sounds.isEmpty()) {
            return;
        }

        SoundEvent sound = sounds.get(player.getRandom().nextInt(sounds.size()));
        PlushySoundPayload.playOnEntity(serverLevel, player, sound, BuiltInRegistries.ITEM.getKey(this));
    }
}