package net.tomsquared.peony_lib.client.blockshape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.tomsquared.peony_lib.blockshape.PeonyBlockShape;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.tomsquared.peony_lib.blockshape.PeonyBlockShapes;
import net.tomsquared.peony_lib.blockshape.PeonyLogTextures;

public final class PeonyStairGeometry {
    private static Map<PeonyBlockShape, BakedModel> templates = Map.of();
    private static Map<String, BakedModel> itemTemplates = Map.of();
    private static Map<BlockState, BakedModel> materialModels = Map.of();

    private PeonyStairGeometry() {
    }

    public static void reload(Map<ModelResourceLocation, BakedModel> models) {
        reloadMaterials(models);
        Map<PeonyBlockShape, BakedModel> updated = new HashMap<>();

        for (PeonyBlockShape shape : PeonyBlockShape.values()) {
            if (!shape.hasTemplate()) {
                continue;
            }

            BakedModel model = models.get(
                    BlockModelShaper.stateToModelLocation(shape.templateState())
            );

            if (model == null) {
                throw new IllegalStateException("Missing shape model for " + shape);
            }

            updated.put(shape, model);
        }

        Map<String, BakedModel> items = new HashMap<>();

        for (String id : List.of(
                "oak_fence", "cobblestone_wall", "spruce_trapdoor",
                "oak_fence_gate", "oak_button")) {
            BakedModel model = models.get(ModelResourceLocation.inventory(
                    ResourceLocation.withDefaultNamespace(id)
            ));

            items.put(id, Objects.requireNonNull(
                    model, "Missing inventory model: " + id
            ));
        }

        templates = Map.copyOf(updated);
        itemTemplates = Map.copyOf(items);
    }

    public static boolean usesInventoryTemplate(PeonyBlockShape shape) {
        return shape.isFence() || shape.isWall() || shape.isTrapdoor()
                || shape.isGate() || shape.isButton();
    }

    public static BakedModel materialModel(
            BakedModel original, @Nullable BlockState sourceState) {
        if (sourceState == null
                || !sourceState.hasProperty(PeonyLogTextures.SWAPPED)
                || !sourceState.getValue(PeonyLogTextures.SWAPPED)) {
            return original;
        }

        BlockState material = PeonyLogTextures.textureState(sourceState);

        return Objects.requireNonNull(
                materialModels.get(material),
                "Missing texture variant model for " + material
        );
    }

    public static BakedModel inventoryTemplate(PeonyBlockShape shape) {
        String id;

        if (shape.isFence()) {
            id = "oak_fence";
        } else if (shape.isWall()) {
            id = "cobblestone_wall";
        } else if (shape.isTrapdoor()) {
            id = "spruce_trapdoor";
        } else if (shape.isGate()) {
            id = "oak_fence_gate";
        } else if (shape.isButton()) {
            id = "oak_button";
        } else {
            throw new IllegalArgumentException("No inventory template for " + shape);
        }

        return Objects.requireNonNull(
                itemTemplates.get(id), "Inventory template not loaded: " + id
        );
    }

    public static Map<Direction, List<BakedQuad>> build(
            BakedModel original, PeonyBlockShape shape,
            @Nullable BlockState sourceState) {
        if (shape.isPole()) {
            return buildPole(original, shape, sourceState);
        }

        BakedModel template = templates.get(shape);
        if (template == null) {
            throw new IllegalStateException("Shape geometry not loaded for " + shape);
        }

        return build(original, template, shape.templateState(), sourceState);
    }

    public static Map<Direction, List<BakedQuad>> build(
            BakedModel original, BakedModel template,
            @Nullable BlockState templateState,
            @Nullable BlockState sourceState) {
        Map<Direction, BakedQuad> faces = new HashMap<>();

        for (int i = -1; i < Direction.values().length; i++) {
            Direction side = i < 0 ? null : Direction.values()[i];

            for (BakedQuad quad : original.getQuads(
                    sourceState, side, RandomSource.create(42))) {
                faces.putIfAbsent(quad.getDirection(), quad);
            }
        }

        Map<Direction, List<BakedQuad>> result = new HashMap<>();

        for (int i = -1; i < Direction.values().length; i++) {
            Direction side = i < 0 ? null : Direction.values()[i];
            List<BakedQuad> quads = new ArrayList<>();

            for (BakedQuad quad : template.getQuads(
                    templateState, side, RandomSource.create(42))) {
                BakedQuad source = faces.get(quad.getDirection());

                if (source == null) {
                    throw new IllegalStateException(
                            "Log model has no cube face for " + quad.getDirection()
                    );
                }

                quads.add(templateState == null
                        ? retextureItem(quad, source)
                        : retexture(quad, source));
            }

            result.put(side, List.copyOf(quads));
        }

        return result;
    }

    private static Map<Direction, List<BakedQuad>> buildPole(
            BakedModel original, PeonyBlockShape shape,
            @Nullable BlockState sourceState) {
        Map<Direction, List<BakedQuad>> result = new HashMap<>();
        List<BakedQuad> quads = new ArrayList<>();

        for (int i = -1; i < Direction.values().length; i++) {
            Direction side = i < 0 ? null : Direction.values()[i];

            for (BakedQuad quad : original.getQuads(
                    sourceState, side, RandomSource.create(42))) {
                int[] vertices = quad.getVertices().clone();
                int stride = vertices.length / 4;

                for (int vertex = 0; vertex < 4; vertex++) {
                    for (Direction.Axis axis : Direction.Axis.values()) {
                        if (axis == shape.poleAxis()) {
                            continue;
                        }

                        int component = switch (axis) {
                            case X -> 0;
                            case Y -> 1;
                            case Z -> 2;
                        };

                        int index = vertex * stride + component;
                        float coordinate = Float.intBitsToFloat(vertices[index]);

                        vertices[index] = Float.floatToRawIntBits(
                                (coordinate - 0.5F) * (4.0F / 16.0F) + 0.5F
                        );
                    }
                }

                BakedQuad geometry = new BakedQuad(
                        vertices,
                        quad.getTintIndex(),
                        quad.getDirection(),
                        quad.getSprite(),
                        quad.isShade(),
                        quad.hasAmbientOcclusion()
                );

                quads.add(retexture(geometry, quad));
            }

            if (side != null) {
                result.put(side, List.of());
            }
        }

        result.put(null, List.copyOf(quads));
        return result;
    }

    private static BakedQuad retextureItem(BakedQuad geometry, BakedQuad source) {
        int[] vertices = geometry.getVertices().clone();
        int stride = vertices.length / 4;
        var templateSprite = geometry.getSprite();
        var targetSprite = source.getSprite();

        for (int vertex = 0; vertex < 4; vertex++) {
            int offset = vertex * stride;

            float u = templateSprite.getUOffset(
                    Float.intBitsToFloat(vertices[offset + 4])
            );
            float v = templateSprite.getVOffset(
                    Float.intBitsToFloat(vertices[offset + 5])
            );

            vertices[offset + 4] = Float.floatToRawIntBits(targetSprite.getU(u));
            vertices[offset + 5] = Float.floatToRawIntBits(targetSprite.getV(v));
        }

        return new BakedQuad(
                vertices,
                source.getTintIndex(),
                geometry.getDirection(),
                targetSprite,
                geometry.isShade(),
                geometry.hasAmbientOcclusion()
        );
    }

    private static BakedQuad retexture(BakedQuad geometry, BakedQuad source) {
        int[] vertices = geometry.getVertices().clone();
        int stride = vertices.length / 4;
        int[] material = source.getVertices();
        int sourceStride = material.length / 4;

        float[] origin = position(material, 0);
        float[] first = subtract(position(material, sourceStride), origin);
        float[] second = subtract(position(material, sourceStride * 3), origin);

        float firstLength = dot(first, first);
        float secondLength = dot(second, second);

        if (firstLength == 0 || secondLength == 0) {
            throw new IllegalStateException("Cannot map a degenerate log face");
        }

        float u = Float.intBitsToFloat(material[4]);
        float v = Float.intBitsToFloat(material[5]);
        float firstU = Float.intBitsToFloat(material[sourceStride + 4]) - u;
        float firstV = Float.intBitsToFloat(material[sourceStride + 5]) - v;
        float secondU = Float.intBitsToFloat(material[sourceStride * 3 + 4]) - u;
        float secondV = Float.intBitsToFloat(material[sourceStride * 3 + 5]) - v;

        for (int vertex = 0; vertex < 4; vertex++) {
            int offset = vertex * stride;
            float[] relative = subtract(position(vertices, offset), origin);
            float a = dot(relative, first) / firstLength;
            float b = dot(relative, second) / secondLength;

            vertices[offset + 4] = Float.floatToRawIntBits(
                    u + a * firstU + b * secondU
            );
            vertices[offset + 5] = Float.floatToRawIntBits(
                    v + a * firstV + b * secondV
            );
        }

        return new BakedQuad(
                vertices,
                source.getTintIndex(),
                geometry.getDirection(),
                source.getSprite(),
                geometry.isShade(),
                geometry.hasAmbientOcclusion()
        );
    }

    private static float[] position(int[] vertices, int offset) {
        return new float[] {
                Float.intBitsToFloat(vertices[offset]),
                Float.intBitsToFloat(vertices[offset + 1]),
                Float.intBitsToFloat(vertices[offset + 2])
        };
    }

    private static float[] subtract(float[] first, float[] second) {
        return new float[] {
                first[0] - second[0],
                first[1] - second[1],
                first[2] - second[2]
        };
    }

    private static float dot(float[] first, float[] second) {
        return first[0] * second[0]
                + first[1] * second[1]
                + first[2] * second[2];
    }

    private static void reloadMaterials(
            Map<ModelResourceLocation, BakedModel> models) {
        Map<BlockState, BakedModel> updated = new HashMap<>();

        for (var block : BuiltInRegistries.BLOCK) {
            BlockState base = block.defaultBlockState();

            if (!base.hasProperty(PeonyLogTextures.SWAPPED)
                    || !base.hasProperty(PeonyBlockShapes.SHAPE)
                    || !base.hasProperty(RotatedPillarBlock.AXIS)) {
                continue;
            }

            for (Direction.Axis axis : Direction.Axis.values()) {
                BlockState state = base
                        .setValue(RotatedPillarBlock.AXIS, axis)
                        .setValue(PeonyBlockShapes.SHAPE, PeonyBlockShape.FULL)
                        .setValue(PeonyLogTextures.SWAPPED, false);

                BakedModel model = models.get(
                        BlockModelShaper.stateToModelLocation(state)
                );

                updated.put(state, Objects.requireNonNull(
                        model, "Missing material model for " + state
                ));
            }
        }

        materialModels = Map.copyOf(updated);
    }
}