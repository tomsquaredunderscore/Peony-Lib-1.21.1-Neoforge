package net.tomsquared.peony_lib.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.tomsquared.peony_lib.Peony_Lib;

@EventBusSubscriber(modid = Peony_Lib.MOD_ID)
public class PeonyItemTooltips {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        var stack = event.getItemStack();

        if (stack.is(net.tomsquared.peony_lib.items.PeonyItems.TOM_PLUSHIE.get())) {
            event.getToolTip().add(Component.translatable("item.peony_lib.tom_plushie.desc")
                    .withStyle(ChatFormatting.GRAY));
        } else if (stack.is(net.tomsquared.peony_lib.items.PeonyItems.SHENDI_PLUSHIE.get())) {
            event.getToolTip().add(Component.translatable("item.peony_lib.shendi_plushie.desc")
                    .withStyle(ChatFormatting.GRAY));
        } else if (stack.is(net.tomsquared.peony_lib.items.PeonyItems.RYAN_PLUSHIE.get())) {
            event.getToolTip().add(Component.translatable("item.peony_lib.ryan_plushie.desc")
                    .withStyle(ChatFormatting.GRAY));
        } else if (stack.is(net.tomsquared.peony_lib.items.PeonyItems.OVA_PLUSHIE.get())) {
            event.getToolTip().add(Component.translatable("item.peony_lib.ova_plushie.desc")
                    .withStyle(ChatFormatting.GRAY));
        } else if (stack.is(net.tomsquared.peony_lib.items.PeonyItems.JACK_PLUSHIE.get())) {
            event.getToolTip().add(Component.translatable("item.peony_lib.jack_plushie.desc")
                    .withStyle(ChatFormatting.GRAY));
        } else if (stack.is(net.tomsquared.peony_lib.items.PeonyItems.KANINSCHENSPEED_PLUSHIE.get())) {
            event.getToolTip().add(Component.translatable("item.peony_lib.kaninschenspeed_plushie.desc")
                    .withStyle(ChatFormatting.GRAY));
        } else if (stack.is(net.tomsquared.peony_lib.items.PeonyItems.SHALAMBIAN_PLUSHIE.get())) {
            event.getToolTip().add(Component.translatable("item.peony_lib.shalambian_plushie.desc")
                    .withStyle(ChatFormatting.GRAY));
        } else if (stack.is(net.tomsquared.peony_lib.items.PeonyItems.POTATOWOLFIE_PLUSHIE.get())) {
            event.getToolTip().add(Component.translatable("item.peony_lib.potatowolfie_plushie.desc")
                    .withStyle(ChatFormatting.GRAY));
        } else if (stack.is(net.tomsquared.peony_lib.items.PeonyItems.TIE_PLUSHIE.get())) {
            event.getToolTip().add(Component.translatable("item.peony_lib.tie_plushie.desc")
                    .withStyle(ChatFormatting.GRAY));
        }
    }
}