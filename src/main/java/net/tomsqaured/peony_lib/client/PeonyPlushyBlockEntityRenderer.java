package net.tomsqaured.peony_lib.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.tomsqaured.peony_lib.blocks.custom.PeonyPlushyBlockEntity;

public class PeonyPlushyBlockEntityRenderer implements BlockEntityRenderer<PeonyPlushyBlockEntity> {
    private final BlockRenderDispatcher dispatcher;

    public PeonyPlushyBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.dispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(PeonyPlushyBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {
        float squish = blockEntity.getSquishProgress(partialTick);
        float xzScale = 1.0F + (0.10F * squish);
        float yScale = 1.0F - (0.10F * squish);

        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.scale(xzScale, yScale, xzScale);
        poseStack.translate(-0.5D, -0.5D, -0.5D);

        dispatcher.renderSingleBlock(
                blockEntity.getBlockState(),
                poseStack,
                buffer,
                packedLight,
                packedOverlay
        );

        poseStack.popPose();
    }
}