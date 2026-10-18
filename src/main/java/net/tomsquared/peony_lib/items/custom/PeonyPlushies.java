package net.tomsquared.peony_lib.items.custom;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.tomsquared.peony_lib.sound.PeonySoundEvents;
import net.tomsquared.peony_lib.network.PlushySoundPayload;
import net.tomsquared.peony_lib.sound.PeonySounds;

public class PeonyPlushies extends BlockItem {
    public static final String MUTED_TAG = "Muted";
    public static final String SQUEAKY_TAG = "Squeaky";

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
        return hasFlag(stack, MUTED_TAG);
    }

    public static void setMuted(ItemStack stack, boolean muted) {
        setFlag(stack, MUTED_TAG, muted);
    }

    public static boolean isSqueaky(ItemStack stack) {
        return hasFlag(stack, SQUEAKY_TAG);
    }

    public static void setSqueaky(ItemStack stack, boolean squeaky) {
        setFlag(stack, SQUEAKY_TAG, squeaky);
    }

    private static boolean hasFlag(ItemStack stack, String key) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).contains(key);
    }

    private static void setFlag(ItemStack stack, String key, boolean value) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            if (value) {
                tag.putBoolean(key, true);
            } else {
                tag.remove(key);
            }
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown() || player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.pass(stack);
        }

        squeeze(level, player, stack);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();

        if (player == null || player.isShiftKeyDown()) {
            return super.useOn(context);
        }

        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResult.FAIL;
        }

        squeeze(context.getLevel(), player, context.getItemInHand());
        return InteractionResult.CONSUME;
    }

        private void squeeze(Level level, Player player, ItemStack stack) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        SoundEvent sound = pickSound(stack, player);
        if (sound != null) {
            PlushySoundPayload.playOnEntity(serverLevel, player, sound, BuiltInRegistries.ITEM.getKey(this));
                level.gameEvent(player, PeonySoundEvents.PLUSHY_SOUND, player.position());
        }

        player.getCooldowns().addCooldown(this, SOUND_COOLDOWN_TICKS);
    }

    private SoundEvent pickSound(ItemStack stack, Player player) {
        if (isSqueaky(stack)) {
            return PeonySounds.SQUEAK.get();
        }

        if (isMuted(stack) || sounds.isEmpty()) {
            return null;
        }

        return sounds.get(player.getRandom().nextInt(sounds.size()));
    }
}