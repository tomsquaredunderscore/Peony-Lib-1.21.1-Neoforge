package net.tomsquared.peony_lib;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;


import net.minecraft.world.item.CreativeModeTabs;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.tomsquared.peony_lib.blocks.PeonyBlockEntities;
import net.tomsquared.peony_lib.blocks.PeonyBlocks;
import net.tomsquared.peony_lib.items.PeonyItems;
import net.tomsquared.peony_lib.sound.PeonySounds;
import net.tomsquared.peony_lib.utils.PeonyGameRules;




@Mod(Peony_Lib.MOD_ID)
public class Peony_Lib {
    public static final String MOD_ID = "peony_lib";

    public static final Logger LOGGER = LogUtils.getLogger();

    public Peony_Lib(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);



        PeonySounds.register(modEventBus);
        PeonyBlocks.register(modEventBus);
        PeonyBlockEntities.register(modEventBus);
        PeonyItems.register(modEventBus);

        NeoForge.EVENT_BUS.addListener(
                BetaNotificationHandler::onPlayerLogin
        );

        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        
        event.enqueueWork(PeonyGameRules::register);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.OP_BLOCKS) {
            event.accept(PeonyItems.TOM_PLUSHIE.get());
            event.accept(PeonyItems.SHENDI_PLUSHIE.get());
            event.accept(PeonyItems.OVA_PLUSHIE.get());
            event.accept(PeonyItems.JACK_PLUSHIE.get());
            event.accept(PeonyItems.SHALAMBIAN_PLUSHIE.get());
            event.accept(PeonyItems.KANINSCHENSPEED_PLUSHIE.get());
            event.accept(PeonyItems.RYAN_PLUSHIE.get());
            event.accept(PeonyItems.TIE_PLUSHIE.get());
            event.accept(PeonyItems.POTATOWOLFIE_PLUSHIE.get());
            event.accept(PeonyItems.EPICCOOL_PLUSHIE.get());
            event.accept(PeonyItems.FULL_PIZZA.get());
            event.accept(PeonyItems.PIZZA_SLICE.get());
            event.accept(PeonyItems.CUBE.get());

        }
    }



    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
