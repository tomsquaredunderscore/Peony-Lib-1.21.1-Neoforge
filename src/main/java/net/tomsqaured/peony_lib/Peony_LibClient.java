package net.tomsqaured.peony_lib;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.tomsqaured.peony_lib.blocks.PeonyBlockEntities;
import net.tomsqaured.peony_lib.client.PeonyPlushyBlockEntityRenderer;

@Mod(value = Peony_Lib.MOD_ID, dist = Dist.CLIENT)
public class Peony_LibClient {
    public Peony_LibClient(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        modEventBus.addListener(this::registerRenderers);
    }

    private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(PeonyBlockEntities.TOM_PLUSHIE.get(), PeonyPlushyBlockEntityRenderer::new);
    }
}