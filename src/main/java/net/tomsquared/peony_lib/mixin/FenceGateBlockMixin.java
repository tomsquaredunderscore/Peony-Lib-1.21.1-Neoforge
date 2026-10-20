package net.tomsquared.peony_lib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.tomsquared.peony_lib.blockshape.PeonyBlockShapes;

@Mixin(FenceGateBlock.class)
public abstract class FenceGateBlockMixin {
    @Inject(method = "isWall", at = @At("HEAD"), cancellable = true)
    private void peony$recognizeWall(BlockState state,
                                     CallbackInfoReturnable<Boolean> cir) {
        if (PeonyBlockShapes.getShape(state).isWall()) {
            cir.setReturnValue(true);
        }
    }
}