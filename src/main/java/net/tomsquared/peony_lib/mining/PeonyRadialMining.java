package net.tomsquared.peony_lib.mining;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.tomsquared.peony_lib.Peony_Lib;
import net.tomsquared.peony_lib.attribute.PeonyAttributes;

@EventBusSubscriber(modid = Peony_Lib.MOD_ID)
public final class PeonyRadialMining {
    private static final ThreadLocal<Set<UUID>> BREAKERS = ThreadLocal.withInitial(HashSet::new);

    private PeonyRadialMining() {
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
public static void onBreak(BlockEvent.BreakEvent event) {
    if (!(event.getPlayer() instanceof ServerPlayer player)
            || player.isShiftKeyDown()
            || PeonyTreeMining.isHandling(player, event.getPos())) {
        return;
    }

    RadialData data = getRadialData(player);
    if (data != null) {
        breakExtraBlocks(player, event.getPos(), data);
    }
}

public static boolean isMining(Player player) {
    return BREAKERS.get().contains(player.getUUID())
            || ApotheosisRadialCompat.isMining(player);
}

    @Nullable
    public static RadialData getRadialData(Player player) {
        int radius = (int) player.getAttributeValue(PeonyAttributes.MINING_RADIUS);
        return radius > 0 ? RadialData.square(radius) : null;
    }

    public static void breakExtraBlocks(ServerPlayer player, BlockPos origin, RadialData data) {
        if (ApotheosisRadialCompat.isMining(player) || !BREAKERS.get().add(player.getUUID())) {
            return;
        }

        ApotheosisRadialCompat.claim(player);
        try {
            BlockHitResult hit = traceLook(player);
            if (hit.getType() != HitResult.Type.BLOCK) {
                return;
            }

            ItemStack tool = player.getMainHandItem();
            for (BlockPos pos : getBrokenBlocks(player, hit.getDirection(), origin, data)) {
                if (tool.isEmpty()) {
                    break;
                }
                player.gameMode.destroyBlock(pos);
            }
        } finally {
            BREAKERS.get().remove(player.getUUID());
            ApotheosisRadialCompat.release(player);
        }
    }

    public static List<BlockPos> getBrokenBlocks(Player player, Direction face, BlockPos origin, RadialData data) {
        Level level = player.level();
        ItemStack tool = player.getMainHandItem();
        float originSpeed = level.getBlockState(origin).getDestroySpeed(level, origin);

        boolean flat = face.getAxis().isVertical();
        Direction up = flat ? player.getDirection() : Direction.UP;
        Direction right = flat ? player.getDirection().getClockWise() : face.getClockWise();

        List<BlockPos> blocks = new ArrayList<>();
        for (int y = data.minY(); y <= data.maxY(); y++) {
            for (int x = data.minX(); x <= data.maxX(); x++) {
                BlockPos pos = origin.relative(right, x).relative(up, y);
                if (pos.equals(origin)) {
                    continue;
                }

                BlockState state = level.getBlockState(pos);
                float speed = state.getDestroySpeed(level, pos);
                if (!state.isAir() && speed >= 0 && speed <= originSpeed * 3.0F && tool.isCorrectToolForDrops(state)) {
                    blocks.add(pos);
                }
            }
        }

        return blocks;
    }

    private static BlockHitResult traceLook(Player player) {
        Vec3 eyes = player.getEyePosition();
        Vec3 end = eyes.add(player.getLookAngle().scale(player.blockInteractionRange()));
        return player.level().clip(new ClipContext(eyes, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
    }

    public record RadialData(int width, int height, int xOffset, int yOffset) {
        public static final Codec<RadialData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Codec.INT.fieldOf("width").forGetter(RadialData::width),
                Codec.INT.fieldOf("height").forGetter(RadialData::height),
                Codec.INT.optionalFieldOf("x_offset", 0).forGetter(RadialData::xOffset),
                Codec.INT.optionalFieldOf("y_offset", 0).forGetter(RadialData::yOffset)
        ).apply(inst, RadialData::new));

        public static RadialData square(int radius) {
            int size = radius * 2 + 1;
            return new RadialData(size, size, 0, 0);
        }

        int minX() {
            return -(width - 1) / 2 + xOffset;
        }

        int maxX() {
            return width / 2 + xOffset;
        }

        int minY() {
            return -(height - 1) / 2 + yOffset;
        }

        int maxY() {
            return height / 2 + yOffset;
        }
    }
}