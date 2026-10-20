package net.tomsquared.peony_lib.client.blockshape;

import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.tomsquared.peony_lib.Peony_Lib;
import net.tomsquared.peony_lib.blockshape.PeonyBlockShape;
import net.tomsquared.peony_lib.blockshape.PeonyBlockShapes;
import net.tomsquared.peony_lib.blockshape.PeonyLogTextures;

@EventBusSubscriber(
        modid = Peony_Lib.MOD_ID,
        bus = EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public final class PeonyShapeModels {
    private PeonyShapeModels() {
    }

    @SubscribeEvent
    public static void onModelsBaked(ModelEvent.ModifyBakingResult event) {
        var models = event.getModels();
        PeonyStairGeometry.reload(models);

        for (var block : BuiltInRegistries.BLOCK) {
            if (!block.defaultBlockState().hasProperty(PeonyBlockShapes.SHAPE)) {
                continue;
            }

            for (var state : block.getStateDefinition().getPossibleStates()) {
                PeonyBlockShape shape = PeonyBlockShapes.getShape(state);
                boolean swapped = state.hasProperty(PeonyLogTextures.SWAPPED)
                        && state.getValue(PeonyLogTextures.SWAPPED);

                if (swapped && !PeonyLogTextures.hasTexturePair(state)) {
                    continue;
                }

                if (shape == PeonyBlockShape.FULL && !swapped) {
                    continue;
                }

                var key = BlockModelShaper.stateToModelLocation(state);
                models.computeIfPresent(key,
                    (id, model) -> new PeonyShapedModel(model, shape, state));
            }

            if (block.asItem() != Items.AIR) {
                var key = new ModelResourceLocation(
                        BuiltInRegistries.ITEM.getKey(block.asItem()),
                        "inventory"
                );

                models.computeIfPresent(key,
                        (id, model) -> new PeonyShapedModel(
                                model, PeonyBlockShape.FULL
                        ));
            }
        }
    }
}