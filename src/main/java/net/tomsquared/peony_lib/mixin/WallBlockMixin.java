package net.tomsquared.peony_lib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.tomsquared.peony_lib.blockshape.PeonyBlockShapes;

@Mixin(WallBlock.class)
public abstract class WallBlockMixin {
    @Inject(method = "connectsTo", at = @At("HEAD"), cancellable = true)
    private void peony$connectShape(BlockState neighbor, boolean solid,
                                    Direction direction,
                                    CallbackInfoReturnable<Boolean> cir) {
        var shape = PeonyBlockShapes.getShape(neighbor);

        if (shape.isWall()) {
            cir.setReturnValue(true);
        } else if (shape.isGate()) {
            cir.setReturnValue(PeonyBlockShapes.gateConnects(neighbor, direction));
        }
    }

    @ModifyVariable(
            method = "shouldRaisePost",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 1
    )
    private BlockState peony$recognizeWallAbove(BlockState above) {
        var shape = PeonyBlockShapes.getShape(above);
        return shape.isWall() ? shape.wallState() : above;
    }
}