package net.tomsquared.peony_lib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.tomsquared.peony_lib.blockshape.PeonyBlockShape;
import net.tomsquared.peony_lib.blockshape.PeonyBlockShapes;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {
    @Redirect(
            method = "updateBlockStateFromTag",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/component/BlockItemStateProperties;apply(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/level/block/state/BlockState;"
            )
    )
    private BlockState peony$keepPlacedShape(
            BlockItemStateProperties properties, BlockState state) {
        BlockState applied = properties.apply(state);

        if (!PeonyBlockShapes.supports(state)) {
            return applied;
        }

        PeonyBlockShape placed = PeonyBlockShapes.getShape(state);
        PeonyBlockShape stored = PeonyBlockShapes.getShape(applied);

        if (placed == PeonyBlockShape.FULL
                || PeonyBlockShapes.itemShape(placed)
                != PeonyBlockShapes.itemShape(stored)) {
            return applied;
        }

        applied = applied.setValue(PeonyBlockShapes.SHAPE, placed);

        if (placed.isPole()) {
            applied = applied.setValue(
                    RotatedPillarBlock.AXIS,
                    state.getValue(RotatedPillarBlock.AXIS)
            );
        }

        return applied;
    }
}