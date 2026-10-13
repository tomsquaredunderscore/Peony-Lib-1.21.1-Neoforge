package net.tomsquared.peony_lib.network;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.tomsquared.peony_lib.Peony_Lib;
import net.tomsquared.peony_lib.client.PeonyPlushySoundClient;

@EventBusSubscriber(modid = Peony_Lib.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class PeonyNetwork {
    private PeonyNetwork() {
    }

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        // lambda keeps the client class from loading on dedicated servers
        event.registrar("1").playToClient(PlushySoundPayload.TYPE, PlushySoundPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> PeonyPlushySoundClient.play(payload)));
    }
}