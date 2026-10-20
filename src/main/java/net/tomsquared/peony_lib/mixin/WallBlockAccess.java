package net.tomsquared.peony_lib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(WallBlock.class)
public interface WallBlockAccess {
    @Invoker("connectsTo")
    boolean peony$connectsTo(BlockState state, boolean solid, Direction direction);

    @Invoker("updateShape")
    BlockState peony$calculateShape(
            LevelReader level,
            BlockState state,
            BlockPos abovePos,
            BlockState above,
            boolean north,
            boolean east,
            boolean south,
            boolean west
    );
}