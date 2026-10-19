package net.tomsquared.peony_lib.mining;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.tomsquared.peony_lib.Peony_Lib;
import net.tomsquared.peony_lib.attribute.PeonyAttributes;

@EventBusSubscriber(modid = Peony_Lib.MOD_ID)
public final class PeonyTreeMining {
    private static final int BLOCKS_PER_TICK = 8;
    private static final Map<UUID, TreeTask> TASKS = new HashMap<>();
    private static final ResourceLocation CHAINSAW =
            ResourceLocation.fromNamespaceAndPath("apothic_enchanting", "chainsaw");

    private PeonyTreeMining() {
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onBreak(BlockEvent.BreakEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)
                || player instanceof FakePlayer
                || player.isCreative()
                || player.isShiftKeyDown()
                || TASKS.containsKey(player.getUUID())
                || PeonyRadialMining.isMining(player)
                || !event.getState().is(BlockTags.LOGS)) {
            return;
        }

        int limit = getLimit(player);
        if (limit > 0 && isTree(player.serverLevel(), event.getPos())) {
            TASKS.put(player.getUUID(), new TreeTask(player, event, limit));
        }
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        for (TreeTask task : new ArrayList<>(TASKS.values())) {
            if (task.tick()) {
                TASKS.remove(task.player.getUUID(), task);
            }
        }
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        TASKS.clear();
    }

    public static boolean isHandling(Player player, BlockPos pos) {
        TreeTask task = TASKS.get(player.getUUID());
        return task != null && (task.processing || task.origin.equals(pos));
    }

    private static int getLimit(ServerPlayer player) {
        ItemStack tool = player.getMainHandItem();
        if (!tool.is(ItemTags.AXES) || hasNativeChainsaw(tool)) {
            return 0;
        }

        return (int) player.getAttributeValue(PeonyAttributes.TREE_FELLING_LIMIT);
    }

    private static boolean hasNativeChainsaw(ItemStack tool) {
        return BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE.getOptional(CHAINSAW)
                .map(effect -> EnchantmentHelper.has(tool, effect))
                .orElse(false);
    }

    private static boolean isTree(ServerLevel level, BlockPos origin) {
        BlockPos top = origin;

        while (level.getBlockState(top).is(BlockTags.LOGS)) {
            top = top.above();
            if (top.getY() >= level.getMaxBuildHeight()) {
                return false;
            }
        }

        for (BlockPos pos : BlockPos.betweenClosed(
                top.offset(-2, -2, -2), top.offset(2, 2, 2))) {
            if (level.hasChunkAt(pos) && level.getBlockState(pos).is(BlockTags.LEAVES)) {
                return true;
            }
        }

        return false;
    }

    private static final class TreeTask {
        private final ServerPlayer player;
        private final ServerLevel level;
        private final ItemStack tool;
        private final BlockEvent.BreakEvent trigger;
        private final BlockPos origin;
        private final int limit;
        private final Queue<BlockPos> pending = new ArrayDeque<>();
        private final Set<BlockPos> visited = new HashSet<>();

        private int broken = 1;
        private boolean started;
        private boolean processing;

        private TreeTask(ServerPlayer player, BlockEvent.BreakEvent trigger, int limit) {
            this.player = player;
            this.level = player.serverLevel();
            this.tool = player.getMainHandItem();
            this.trigger = trigger;
            this.origin = trigger.getPos().immutable();
            this.limit = limit;
            visited.add(origin);
        }

        private boolean tick() {
            if (player.isRemoved()
                    || !player.isAlive()
                    || player.serverLevel() != level
                    || player.isCreative()
                    || player.isShiftKeyDown()
                    || player.getMainHandItem() != tool
                    || tool.isEmpty()
                    || getLimit(player) <= 0) {
                return true;
            }

            if (!started) {
                if (trigger.isCanceled()
                        || !level.hasChunkAt(origin)
                        || level.getBlockState(origin).is(BlockTags.LOGS)) {
                    return true;
                }

                enqueueNeighbors(origin);
                started = true;
            }

            processing = true;
            ApotheosisRadialCompat.claim(player);

            try {
                int currentLimit = Math.min(limit, getLimit(player));

                for (int i = 0; i < BLOCKS_PER_TICK
                        && broken < currentLimit
                        && !pending.isEmpty()
                        && !tool.isEmpty(); i++) {
                    BlockPos pos = pending.remove();

                    if (!level.hasChunkAt(pos)) {
                        continue;
                    }

                    BlockState state = level.getBlockState(pos);
                    if (!state.is(BlockTags.LOGS)
                            || state.getDestroySpeed(level, pos) < 0
                            || !state.canHarvestBlock(level, pos, player)) {
                        continue;
                    }

                    if (player.gameMode.destroyBlock(pos)
                            && !level.getBlockState(pos).is(BlockTags.LOGS)) {
                        broken++;
                        enqueueNeighbors(pos);
                    }
                }

                return pending.isEmpty() || broken >= currentLimit || tool.isEmpty();
            } finally {
                ApotheosisRadialCompat.release(player);
                processing = false;
            }
        }

        private void enqueueNeighbors(BlockPos pos) {
            for (int y = 0; y <= 1; y++) {
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        BlockPos next = pos.offset(x, y, z);

                        if (visited.add(next)
                                && level.hasChunkAt(next)
                                && level.getBlockState(next).is(BlockTags.LOGS)) {
                            pending.add(next);
                        }
                    }
                }
            }
        }
    }
}