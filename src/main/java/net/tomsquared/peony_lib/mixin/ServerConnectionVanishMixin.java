package net.tomsquared.peony_lib.mixin;

import java.util.EnumSet;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.tomsquared.peony_lib.utils.PeonyAdminCommands;

@Mixin(ServerCommonPacketListenerImpl.class)
public abstract class ServerConnectionVanishMixin {
    @ModifyVariable(
            method = "send(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketSendListener;)V",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 0
    )
    private Packet<?> peony$filterVanishedPlayers(Packet<?> packet) {
        if (!((Object) this instanceof ServerGamePacketListenerImpl connection)
                || !(packet instanceof ClientboundPlayerInfoUpdatePacket info)) {
            return packet;
        }

        boolean needsFiltering = info.entries().stream().anyMatch(
                entry -> entry.listed()
                        && PeonyAdminCommands.isHiddenFrom(
                                entry.profileId(), connection.player
                        )
        );

        if (!needsFiltering) {
            return packet;
        }

        List<ClientboundPlayerInfoUpdatePacket.Entry> entries =
                info.entries().stream().map(entry -> {
                    if (!PeonyAdminCommands.isHiddenFrom(
                            entry.profileId(), connection.player)) {
                        return entry;
                    }

                    return new ClientboundPlayerInfoUpdatePacket.Entry(
                            entry.profileId(),
                            entry.profile(),
                            false,
                            entry.latency(),
                            entry.gameMode(),
                            entry.displayName(),
                            entry.chatSession()
                    );
                }).toList();

        EnumSet<ClientboundPlayerInfoUpdatePacket.Action> actions =
                info.actions().clone();

        actions.add(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED);

        ClientboundPlayerInfoUpdatePacket filtered =
                new ClientboundPlayerInfoUpdatePacket(actions, List.of());

        ((PlayerInfoPacketAccess) filtered).peony$setEntries(entries);
        return filtered;
    }
}