package net.tomsquared.peony_lib.client.blockshape;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.tomsquared.peony_lib.blockshape.PeonyBlockShape;
import net.tomsquared.peony_lib.blockshape.PeonyBlockShapes;
import net.tomsquared.peony_lib.blockshape.PeonyLogTextures;

public final class PeonyShapedModel extends BakedModelWrapper<BakedModel> {
    private final PeonyBlockShape shape;
    private final BlockState sourceState;
    private final BakedModel transformModel;
    private final Map<Direction, List<BakedQuad>> templateQuads;
    private final boolean itemPreview;

    public PeonyShapedModel(BakedModel original, PeonyBlockShape shape) {
        this(original, shape, null);
    }

    public PeonyShapedModel(BakedModel original, PeonyBlockShape shape,
                           @Nullable BlockState sourceState) {
        this(original, shape, sourceState, null, false);
    }

    private PeonyShapedModel(BakedModel original, PeonyBlockShape shape,
                            @Nullable BlockState sourceState,
                            @Nullable BakedModel inventoryTemplate,
                            boolean itemPreview) {
        super(PeonyStairGeometry.materialModel(original, sourceState));

        this.shape = shape;
        this.sourceState = sourceState;
        this.itemPreview = itemPreview;
        this.transformModel = inventoryTemplate != null
                ? inventoryTemplate
                : originalModel;

        if (inventoryTemplate != null) {
            this.templateQuads = PeonyStairGeometry.build(
                    originalModel, inventoryTemplate, null, sourceState
            );
        } else if (shape.hasTemplate() || shape.isPole()) {
            this.templateQuads = PeonyStairGeometry.build(
                    originalModel, shape, sourceState
            );
        } else {
            this.templateQuads = null;
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state,
                                    @Nullable Direction side, RandomSource random) {
        return getQuads(state, side, random, ModelData.EMPTY, null);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state,
                                    @Nullable Direction side, RandomSource random,
                                    ModelData data, @Nullable RenderType renderType) {
        if (templateQuads != null) {
            return templateQuads.get(side);
        }

        if (shape == PeonyBlockShape.FULL) {
            return originalModel.getQuads(state, side, random, data, renderType);
        }

        Direction innerFace = shape == PeonyBlockShape.SLAB_BOTTOM
                ? Direction.UP
                : Direction.DOWN;

        if (side == innerFace) {
            return List.of();
        }

        List<BakedQuad> result = new ArrayList<>();

        for (BakedQuad quad : originalModel.getQuads(
                state, side, random, data, renderType)) {
            result.add(reshape(quad));
        }

        if (side == null) {
            for (BakedQuad quad : originalModel.getQuads(
                    state, innerFace, random, data, renderType)) {
                result.add(reshape(quad));
            }
        }

        return result;
    }

    private BakedQuad reshape(BakedQuad quad) {
        int[] vertices = quad.getVertices().clone();
        int stride = vertices.length / 4;
        float offset = shape == PeonyBlockShape.SLAB_TOP ? 0.5F : 0.0F;

        for (int vertex = 0; vertex < 4; vertex++) {
            int index = vertex * stride + 1;
            float y = Float.intBitsToFloat(vertices[index]);
            vertices[index] = Float.floatToRawIntBits(y * 0.5F + offset);
        }

        return new BakedQuad(
                vertices,
                quad.getTintIndex(),
                quad.getDirection(),
                quad.getSprite(),
                quad.isShade(),
                quad.hasAmbientOcclusion()
        );
    }

    @Override
    public List<BakedModel> getRenderPasses(ItemStack stack, boolean fabulous) {
        PeonyBlockShape itemShape = PeonyBlockShapes.getShape(stack);
        List<BakedModel> passes = originalModel.getRenderPasses(stack, fabulous);

        if (!(stack.getItem() instanceof BlockItem item)) {
            return passes;
        }

        BlockState materialState = stack.getOrDefault(
                DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY
        ).apply(item.getBlock().defaultBlockState());

        boolean swapped = materialState.hasProperty(PeonyLogTextures.SWAPPED)
                && materialState.getValue(PeonyLogTextures.SWAPPED);

        if (itemShape == PeonyBlockShape.FULL && !swapped) {
            return passes;
        }

        PeonyBlockShape displayShape = itemShape.isStairs()
                ? PeonyBlockShape.STAIRS_SOUTH_BOTTOM_STRAIGHT
                : itemShape;

        BakedModel inventoryTemplate =
                PeonyStairGeometry.usesInventoryTemplate(itemShape)
                        ? PeonyStairGeometry.inventoryTemplate(itemShape)
                        : null;

        return passes.stream()
                .<BakedModel>map(pass -> new PeonyShapedModel(
                        pass, displayShape, materialState, inventoryTemplate, true
                ))
                .toList();
    }

    @Override
    public ItemTransforms getTransforms() {
        return transformModel.getTransforms();
    }

    @Override
    public BakedModel applyTransform(ItemDisplayContext context, PoseStack pose,
                                      boolean leftHand) {
        BakedModel transformed = transformModel.applyTransform(
                context, pose, leftHand
        );

        if (itemPreview && shape.isStairs() && isHeld(context)) {
            pose.translate(0.5D, 0.5D, 0.5D);
            pose.mulPose(Axis.YP.rotationDegrees(90.0F));
            pose.translate(-0.5D, -0.5D, -0.5D);
        }

        if (transformed == transformModel) {
            return this;
        }

        if (itemPreview && PeonyStairGeometry.usesInventoryTemplate(shape)) {
            return new PeonyShapedModel(
                    originalModel, shape, sourceState, transformed, true
            );
        }

        return new PeonyShapedModel(
                transformed, shape, sourceState, null, itemPreview
        );
    }

    private static boolean isHeld(ItemDisplayContext context) {
        return switch (context) {
            case FIRST_PERSON_LEFT_HAND, FIRST_PERSON_RIGHT_HAND,
                 THIRD_PERSON_LEFT_HAND, THIRD_PERSON_RIGHT_HAND -> true;
            default -> false;
        };
    }
}