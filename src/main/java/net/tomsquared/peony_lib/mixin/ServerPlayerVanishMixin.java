package net.tomsquared.peony_lib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.level.ServerPlayer;
import net.tomsquared.peony_lib.utils.PeonyAdminCommands;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerVanishMixin {
    @Inject(
            method = "broadcastToPlayer",
            at = @At("HEAD"),
            cancellable = true
    )
    private void peony$hideVanishedPlayer(
            ServerPlayer viewer,
            CallbackInfoReturnable<Boolean> cir) {
        ServerPlayer player = (ServerPlayer) (Object) this;

        if (PeonyAdminCommands.isHiddenFrom(player.getUUID(), viewer)) {
            cir.setReturnValue(false);
        }
    }
}