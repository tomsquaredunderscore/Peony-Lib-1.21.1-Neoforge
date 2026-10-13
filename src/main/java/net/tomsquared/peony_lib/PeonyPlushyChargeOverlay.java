package net.tomsquared.peony_lib;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.tomsquared.peony_lib.Peony_Lib;
import net.tomsquared.peony_lib.events.PeonyPlushieCombatEvents;
import net.tomsquared.peony_lib.items.custom.PeonyPlushies;

public final class PeonyPlushyChargeOverlay {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Peony_Lib.MOD_ID, "plushy_charge");

    private static final ResourceLocation CROSSHAIR_BACKGROUND =
            ResourceLocation.fromNamespaceAndPath("minecraft", "hud/crosshair_attack_indicator_background");
    private static final ResourceLocation CROSSHAIR_PROGRESS =
            ResourceLocation.fromNamespaceAndPath("minecraft", "hud/crosshair_attack_indicator_progress");
    private static final ResourceLocation CROSSHAIR_FULL =
            ResourceLocation.fromNamespaceAndPath("minecraft", "hud/crosshair_attack_indicator_full");

    private PeonyPlushyChargeOverlay() {
    }

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null || minecraft.options.hideGui) {
            return;
        }

        ItemStack stack = minecraft.player.getMainHandItem();
        if (!(stack.getItem() instanceof PeonyPlushies) || !minecraft.player.isCrouching()) {
            return;
        }

        float charge = PeonyPlushieCombatEvents.getChargeProgress(minecraft.player);

        int x = guiGraphics.guiWidth() / 2 - 8;
        int y = guiGraphics.guiHeight() / 2 + 8;

        guiGraphics.blitSprite(CROSSHAIR_BACKGROUND, x, y, 16, 4);

        if (charge >= 1.0F) {
            guiGraphics.blitSprite(CROSSHAIR_FULL, x, y, 16, 16);
            return;
        }

        if (charge > 0.0F) {
            int progressWidth = Mth.clamp(Mth.ceil(charge * 16.0F), 1, 16);
            guiGraphics.blitSprite(CROSSHAIR_PROGRESS, x, y, progressWidth, 4);
        }
    }
}