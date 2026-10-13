package net.tomsquared.peony_lib.network;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.tomsquared.peony_lib.Peony_Lib;

public record PlushySoundPayload(Holder<SoundEvent> sound, ResourceLocation plushy, int entityId, BlockPos pos)
        implements CustomPacketPayload {

    public static final Type<PlushySoundPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Peony_Lib.MOD_ID, "plushy_sound"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlushySoundPayload> STREAM_CODEC = StreamCodec.composite(
            SoundEvent.STREAM_CODEC, PlushySoundPayload::sound,
            ResourceLocation.STREAM_CODEC, PlushySoundPayload::plushy,
            ByteBufCodecs.VAR_INT, PlushySoundPayload::entityId,
            BlockPos.STREAM_CODEC, PlushySoundPayload::pos,
            PlushySoundPayload::new
    );

    private static final double RANGE = 16.0D;

    public static void playOnEntity(ServerLevel level, Entity entity, SoundEvent sound, ResourceLocation plushy) {
        PlushySoundPayload payload = new PlushySoundPayload(
                BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound), plushy, entity.getId(), entity.blockPosition());
        PacketDistributor.sendToPlayersNear(level, null, entity.getX(), entity.getY(), entity.getZ(), RANGE, payload);
    }

    public static void playAtBlock(ServerLevel level, BlockPos pos, SoundEvent sound, ResourceLocation plushy) {
        PlushySoundPayload payload = new PlushySoundPayload(
                BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound), plushy, -1, pos);
        PacketDistributor.sendToPlayersNear(level, null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, RANGE, payload);
    }

    public boolean followsEntity() {
        return entityId >= 0;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}