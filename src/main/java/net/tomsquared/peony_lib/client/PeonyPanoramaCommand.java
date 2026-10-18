package net.tomsquared.peony_lib.client;


import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.Window;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.tomsquared.peony_lib.Peony_Lib;

@EventBusSubscriber(modid = Peony_Lib.MOD_ID, value = Dist.CLIENT)
public final class PeonyPanoramaCommand {
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss");
    private static final int DEFAULT_SIZE = 1024;

    private static int pendingSize;

    private PeonyPanoramaCommand() {
    }

        @SubscribeEvent
    public static void registerCommands(RegisterClientCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("peony")
                .then(Commands.literal("panorama")
                        .executes(context -> queue(DEFAULT_SIZE))
                        .then(Commands.argument("size", IntegerArgumentType.integer(64, 4096))
                                .executes(context -> queue(IntegerArgumentType.getInteger(context, "size"))))));
    }

    private static int queue(int size) {
        pendingSize = size;
        return 1;
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();

        if (pendingSize == 0 || minecraft.screen != null || minecraft.player == null) {
            return;
        }

        int size = pendingSize;
        pendingSize = 0;
        minecraft.player.displayClientMessage(capture(minecraft, size), false);
    }

    private static Component capture(Minecraft minecraft, int size) {
        Path folder = FMLPaths.GAMEDIR.get().resolve("panorama").resolve(LocalDateTime.now().format(TIMESTAMP));
        LocalPlayer player = minecraft.player;
        Window window = minecraft.getWindow();

        int windowWidth = window.getWidth();
        int windowHeight = window.getHeight();
        float pitch = player.getXRot();
        float yaw = player.getYRot();
        float oldPitch = player.xRotO;
        float oldYaw = player.yRotO;
        RenderTarget target = new TextureTarget(size, size, true, Minecraft.ON_OSX);

        try {
            Files.createDirectories(folder);
            minecraft.gameRenderer.setRenderBlockOutline(false);
            minecraft.gameRenderer.setPanoramicMode(true);
            minecraft.levelRenderer.graphicsChanged();
            window.setWidth(size);
            window.setHeight(size);

            for (int face = 0; face < 6; face++) {
                
                float faceYaw = switch (face) {
                    case 1 -> yaw + 90.0F;
                    case 2 -> yaw + 180.0F;
                    case 3 -> yaw - 90.0F;
                    default -> yaw;
                };
                float facePitch = face == 4 ? -90.0F : face == 5 ? 90.0F : 0.0F;

                player.setYRot(faceYaw % 360.0F);
                player.setXRot(facePitch);
                player.yRotO = player.getYRot();
                player.xRotO = player.getXRot();

                target.bindWrite(true);
                minecraft.gameRenderer.renderLevel(DeltaTracker.ONE);

                try (NativeImage image = Screenshot.takeScreenshot(target)) {
                    image.writeToFile(folder.resolve("panorama_" + face + ".png"));
                }
            }

            Component link = Component.literal(folder.getFileName().toString())
                    .withStyle(ChatFormatting.UNDERLINE)
                    .withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, folder.toAbsolutePath().toString())));
            return Component.translatable("screenshot.success", link);
        } catch (Exception e) {
            Peony_Lib.LOGGER.error("Couldn't save panorama", e);
            return Component.translatable("screenshot.failure", e.getMessage());
        } finally {
            player.setXRot(pitch);
            player.setYRot(yaw);
            player.xRotO = oldPitch;
            player.yRotO = oldYaw;
            minecraft.gameRenderer.setRenderBlockOutline(true);
            minecraft.gameRenderer.setPanoramicMode(false);
            window.setWidth(windowWidth);
            window.setHeight(windowHeight);
            target.destroyBuffers();
            minecraft.levelRenderer.graphicsChanged();
            minecraft.getMainRenderTarget().bindWrite(true);
        }
    }
}