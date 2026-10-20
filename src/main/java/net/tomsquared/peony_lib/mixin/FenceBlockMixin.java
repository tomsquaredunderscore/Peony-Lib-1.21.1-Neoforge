package net.tomsquared.peony_lib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.tomsquared.peony_lib.blockshape.PeonyBlockShapes;

@Mixin(FenceBlock.class)
public abstract class FenceBlockMixin extends Block {
    protected FenceBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "connectsTo", at = @At("HEAD"), cancellable = true)
    private void peony$connectShape(BlockState neighbor, boolean solid,
                                    Direction direction,
                                    CallbackInfoReturnable<Boolean> cir) {
        var shape = PeonyBlockShapes.getShape(neighbor);

        if (shape.isFence()) {
            cir.setReturnValue(defaultBlockState().is(BlockTags.WOODEN_FENCES));
        } else if (shape.isGate()) {
            cir.setReturnValue(PeonyBlockShapes.gateConnects(neighbor, direction));
        }
    }
}