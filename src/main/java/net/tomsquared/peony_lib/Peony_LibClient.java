package net.tomsquared.peony_lib;


import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.tomsquared.peony_lib.blocks.PeonyBlockEntities;
import net.tomsquared.peony_lib.client.PeonyPlushyBlockEntityRenderer;

@Mod(value = Peony_Lib.MOD_ID, dist = Dist.CLIENT)
public class Peony_LibClient {
    public Peony_LibClient(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        modEventBus.addListener(this::registerRenderers);
        modEventBus.addListener(this::registerGuiLayers);
    }

    private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(PeonyBlockEntities.TOM_PLUSHIE.get(), PeonyPlushyBlockEntityRenderer::new);
    }

    private void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.CROSSHAIR, PeonyPlushyChargeOverlay.ID, PeonyPlushyChargeOverlay::render);
    }
}