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
        


        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.OP_BLOCKS) {

        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
