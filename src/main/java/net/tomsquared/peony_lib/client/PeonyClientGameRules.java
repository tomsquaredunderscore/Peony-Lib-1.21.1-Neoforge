package net.tomsquared.peony_lib.client;

import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientChatReceivedEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.util.TriState;
import net.tomsquared.peony_lib.Peony_Lib;
import net.tomsquared.peony_lib.network.PeonyGameRulesPayload;

@EventBusSubscriber(modid = Peony_Lib.MOD_ID, value = Dist.CLIENT)
public final class PeonyClientGameRules {
    private static boolean showNametags = true;
    private static boolean showTablist = true;
    private static boolean showJoining = true;

    private PeonyClientGameRules() {
    }

    public static void apply(PeonyGameRulesPayload payload) {
        showNametags = payload.showNametags();
        showTablist = payload.showTablist();
        showJoining = payload.showJoining();
    }

    @SubscribeEvent
    public static void onRenderNameTag(RenderNameTagEvent event) {
        if (!showNametags && event.getEntity() instanceof Player) {
            event.setCanRender(TriState.FALSE);
        }
    }

    @SubscribeEvent
    public static void onRenderGuiLayer(RenderGuiLayerEvent.Pre event) {
        if (!showTablist && event.getName().equals(VanillaGuiLayers.TAB_LIST)) {
            event.setCanceled(true);
        }
    }

        @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent event) {
        if (!showJoining && event.isSystem()
                && event.getMessage().getContents() instanceof TranslatableContents contents
                && isJoinOrLeave(contents.getKey())) {
            event.setCanceled(true);
        }
    }

    private static boolean isJoinOrLeave(String key) {
        return key.startsWith("multiplayer.player.joined") || key.equals("multiplayer.player.left");
    }
}