package net.tomsquared.peony_lib.utils;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.tomsquared.peony_lib.network.PeonyGameRulesPayload;

public final class PeonyGameRules {
    public static final GameRules.Key<GameRules.BooleanValue> SHOW_PLAYER_NAMETAGS = GameRules.register(
            "peonyshowplayernametags", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true, (server, value) -> sync(server)));
    public static final GameRules.Key<GameRules.BooleanValue> SHOW_PLAYER_TABLIST = GameRules.register(
            "peonyshowplayertablist", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true, (server, value) -> sync(server)));
    public static final GameRules.Key<GameRules.BooleanValue> SHOW_PLAYER_JOINING = GameRules.register(
            "peonyshowplayerjoining", GameRules.Category.CHAT, GameRules.BooleanValue.create(true, (server, value) -> sync(server)));

    private PeonyGameRules() {
    }

    public static void register() {
        NeoForge.EVENT_BUS.addListener(PeonyGameRules::onDatapackSync);
    }

    private static void onDatapackSync(OnDatapackSyncEvent event) {
        PeonyGameRulesPayload payload = PeonyGameRulesPayload.from(event.getPlayerList().getServer().getGameRules());
        event.getRelevantPlayers().forEach(player -> PacketDistributor.sendToPlayer(player, payload));
    }

    private static void sync(MinecraftServer server) {
        PacketDistributor.sendToAllPlayers(PeonyGameRulesPayload.from(server.getGameRules()));
    }
}