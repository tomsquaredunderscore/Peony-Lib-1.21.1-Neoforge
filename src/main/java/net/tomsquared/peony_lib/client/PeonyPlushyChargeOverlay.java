package net.tomsquared.peony_lib.client;


import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.tomsquared.peony_lib.Peony_Lib;
import net.tomsquared.peony_lib.events.PeonyPlushieCombatEvents;
import net.tomsquared.peony_lib.items.custom.PeonyPlushies;

public final class PeonyPlushyChargeOverlay {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Peony_Lib.MOD_ID, "plushy_charge");

    private static final ResourceLocation BACKGROUND = ResourceLocation.withDefaultNamespace("hud/crosshair_attack_indicator_background");
    private static final ResourceLocation PROGRESS = ResourceLocation.withDefaultNamespace("hud/crosshair_attack_indicator_progress");
    private static final ResourceLocation FULL = ResourceLocation.withDefaultNamespace("hud/crosshair_attack_indicator_full");

    private PeonyPlushyChargeOverlay() {
    }

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.options.hideGui || !minecraft.options.getCameraType().isFirstPerson()) {
            return;
        }

        if (!(minecraft.player.getMainHandItem().getItem() instanceof PeonyPlushies) || !minecraft.player.isCrouching()) {
            return;
        }

        float charge = PeonyPlushieCombatEvents.getChargeProgress(minecraft.player);
        int x = guiGraphics.guiWidth() / 2 - 8;
        int y = guiGraphics.guiHeight() / 2 - 7 + 16;

        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
                GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );

        if (charge >= 1.0F) {
            guiGraphics.blitSprite(FULL, x, y, 16, 16);
        } else {
            guiGraphics.blitSprite(BACKGROUND, x, y, 16, 4);
            guiGraphics.blitSprite(PROGRESS, 16, 4, 0, 0, x, y, (int) (charge * 17.0F), 4);
        }

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}