package net.tomsquared.peony_lib.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;

@Mixin(ClientboundPlayerInfoUpdatePacket.class)
public interface PlayerInfoPacketAccess {
    @Mutable
    @Accessor("entries")
    void peony$setEntries(
            List<ClientboundPlayerInfoUpdatePacket.Entry> entries
    );
}