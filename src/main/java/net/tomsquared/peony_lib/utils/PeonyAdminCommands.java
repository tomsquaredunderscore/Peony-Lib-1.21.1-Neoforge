package net.tomsquared.peony_lib.utils;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.tomsquared.peony_lib.Peony_Lib;

@EventBusSubscriber(modid = Peony_Lib.MOD_ID)
public final class PeonyAdminCommands {
    private static final Set<UUID> VANISHED = ConcurrentHashMap.newKeySet();
    private static final Map<UUID, Boolean> ADMIN_STATUS = new HashMap<>();
    private static final ArrayDeque<Long> TICK_TIMES = new ArrayDeque<>();

    private PeonyAdminCommands() {
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("peony")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("vanish")
                                .executes(PeonyAdminCommands::vanish))
                        .then(Commands.literal("tps")
                                .executes(PeonyAdminCommands::tps))
                        .then(Commands.literal("playerlist")
                                .executes(PeonyAdminCommands::playerList))
        );
    }

    public static boolean isHiddenFrom(UUID target, ServerPlayer viewer) {
        return VANISHED.contains(target)
                && !target.equals(viewer.getUUID())
                && !viewer.hasPermissions(2);
    }

    public static boolean isVanished(ServerPlayer player) {
        return VANISHED.contains(player.getUUID());
    }

    private static int vanish(CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        boolean enabled = VANISHED.add(player.getUUID());

        if (!enabled) {
            VANISHED.remove(player.getUUID());
        }

        refreshVisibility(context.getSource().getServer());

        context.getSource().sendSuccess(
                () -> Component.literal(
                        enabled ? "Vanish enabled." : "Vanish disabled."
                ),
                false
        );

        return Command.SINGLE_SUCCESS;
    }

    private static int tps(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        MinecraftServer server = source.getServer();

        if (TICK_TIMES.size() < 2) {
            source.sendFailure(Component.literal(
                    "Not enough tick samples yet. Try again shortly."
            ));
            return 0;
        }

        long elapsed = TICK_TIMES.getLast() - TICK_TIMES.getFirst();

        if (elapsed <= 0) {
            source.sendFailure(Component.literal(
                    "Unable to calculate TPS from the current samples."
            ));
            return 0;
        }

        double measuredTps = (TICK_TIMES.size() - 1) * 1_000_000_000.0 / elapsed;
        double mspt = server.getAverageTickTimeNanos() / 1_000_000.0;
        float target = server.tickRateManager().tickrate();

        String message = String.format(
                Locale.ROOT,
                "TPS: %.2f | Target: %.2f | Average MSPT: %.2f%s",
                measuredTps,
                target,
                mspt,
                server.tickRateManager().isFrozen() ? " | Simulation frozen" : ""
        );

        source.sendSuccess(() -> Component.literal(message), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int playerList(CommandContext<CommandSourceStack> context) {
        List<ServerPlayer> players = context.getSource().getServer()
                .getPlayerList().getPlayers();

        String names = players.stream()
                .sorted((first, second) -> String.CASE_INSENSITIVE_ORDER.compare(
                        first.getGameProfile().getName(),
                        second.getGameProfile().getName()
                ))
                .map(player -> player.getGameProfile().getName()
                        + (isVanished(player) ? " [vanished]" : ""))
                .collect(Collectors.joining(", "));

        String message = "Online players (" + players.size() + "): "
                + (names.isEmpty() ? "none" : names);

        context.getSource().sendSuccess(
                () -> Component.literal(message),
                false
        );

        return Command.SINGLE_SUCCESS;
    }

    private static void refreshVisibility(MinecraftServer server) {
        List<ServerPlayer> players = server.getPlayerList().getPlayers();

        for (ServerPlayer viewer : players) {
            viewer.connection.send(new ClientboundPlayerInfoUpdatePacket(
                    java.util.EnumSet.of(
                            ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED
                    ),
                    players
            ));
        }

        for (ServerPlayer player : players) {
            player.serverLevel().getChunkSource().chunkMap.move(player);
        }
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        TICK_TIMES.addLast(System.nanoTime());

        if (TICK_TIMES.size() > 101) {
            TICK_TIMES.removeFirst();
        }

        boolean refresh = false;

        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            UUID id = player.getUUID();
            boolean admin = player.hasPermissions(2);
            Boolean previous = ADMIN_STATUS.put(id, admin);

            if (previous == null || previous.booleanValue() != admin) {
                refresh = true;
            }

            if (!admin && VANISHED.remove(id)) {
                refresh = true;
                player.sendSystemMessage(Component.literal(
                        "Vanish disabled because you no longer have permission."
                ));
            }
        }

        if (refresh) {
            refreshVisibility(event.getServer());
        }
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        UUID id = event.getEntity().getUUID();
        VANISHED.remove(id);
        ADMIN_STATUS.remove(id);
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        VANISHED.clear();
        ADMIN_STATUS.clear();
        TICK_TIMES.clear();
    }
}