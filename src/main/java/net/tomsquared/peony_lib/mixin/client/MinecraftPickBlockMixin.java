package net.tomsquared.peony_lib.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.tomsquared.peony_lib.blockshape.PeonyBlockShapes;

@Mixin(Minecraft.class)
public abstract class MinecraftPickBlockMixin {
    @Redirect(
            method = "pickBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getCloneItemStack(Lnet/minecraft/world/phys/HitResult;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/item/ItemStack;"
            )
    )
    private ItemStack peony$copyShape(BlockState state, HitResult hit,
                                      LevelReader level, BlockPos pos, Player player) {
        ItemStack stack = state.getCloneItemStack(hit, level, pos, player);

        if (!stack.isEmpty()
                && player.getAbilities().instabuild
                && Screen.hasControlDown()
                && PeonyBlockShapes.supports(state)) {
            return PeonyBlockShapes.copyToStack(state, stack);
        }

        return stack;
    }
}