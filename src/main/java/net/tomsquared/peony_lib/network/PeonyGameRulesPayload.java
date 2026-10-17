package net.tomsquared.peony_lib.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameRules;
import net.tomsquared.peony_lib.Peony_Lib;
import net.tomsquared.peony_lib.utils.PeonyGameRules;

public record PeonyGameRulesPayload(boolean showNametags, boolean showTablist, boolean showJoining) implements CustomPacketPayload {
    public static final Type<PeonyGameRulesPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Peony_Lib.MOD_ID, "gamerules"));

    public static final StreamCodec<ByteBuf, PeonyGameRulesPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, PeonyGameRulesPayload::showNametags,
            ByteBufCodecs.BOOL, PeonyGameRulesPayload::showTablist,
            ByteBufCodecs.BOOL, PeonyGameRulesPayload::showJoining,
            PeonyGameRulesPayload::new
    );

    public static PeonyGameRulesPayload from(GameRules rules) {
        return new PeonyGameRulesPayload(
                rules.getBoolean(PeonyGameRules.SHOW_PLAYER_NAMETAGS),
                rules.getBoolean(PeonyGameRules.SHOW_PLAYER_TABLIST),
                rules.getBoolean(PeonyGameRules.SHOW_PLAYER_JOINING));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}