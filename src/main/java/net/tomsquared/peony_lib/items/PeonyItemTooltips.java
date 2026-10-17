package net.tomsquared.peony_lib.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.tomsquared.peony_lib.Peony_Lib;
import net.tomsquared.peony_lib.items.custom.PeonyPlushies;

@EventBusSubscriber(modid = Peony_Lib.MOD_ID)
public class PeonyItemTooltips {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        var stack = event.getItemStack();

        if (stack.is(PeonyItems.TOM_PLUSHIE.get())) {
            event.getToolTip().add(Component.translatable("item.peony_lib.tom_plushie.desc")
                    .withStyle(ChatFormatting.GRAY));
        } else if (stack.is(PeonyItems.SHENDI_PLUSHIE.get())) {
            event.getToolTip().add(Component.translatable("item.peony_lib.shendi_plushie.desc")
                    .withStyle(ChatFormatting.GRAY));
        } else if (stack.is(PeonyItems.RYAN_PLUSHIE.get())) {
            event.getToolTip().add(Component.translatable("item.peony_lib.ryan_plushie.desc")
                    .withStyle(ChatFormatting.GRAY));
        } else if (stack.is(PeonyItems.OVA_PLUSHIE.get())) {
            event.getToolTip().add(Component.translatable("item.peony_lib.ova_plushie.desc")
                    .withStyle(ChatFormatting.GRAY));
        } else if (stack.is(PeonyItems.JACK_PLUSHIE.get())) {
            event.getToolTip().add(Component.translatable("item.peony_lib.jack_plushie.desc")
                    .withStyle(ChatFormatting.GRAY));
        } else if (stack.is(PeonyItems.KANINSCHENSPEED_PLUSHIE.get())) {
            event.getToolTip().add(Component.translatable("item.peony_lib.kaninschenspeed_plushie.desc")
                    .withStyle(ChatFormatting.GRAY));
        } else if (stack.is(PeonyItems.SHALAMBIAN_PLUSHIE.get())) {
            event.getToolTip().add(Component.translatable("item.peony_lib.shalambian_plushie.desc")
                    .withStyle(ChatFormatting.GRAY));
        } else if (stack.is(PeonyItems.POTATOWOLFIE_PLUSHIE.get())) {
            event.getToolTip().add(Component.translatable("item.peony_lib.potatowolfie_plushie.desc")
                    .withStyle(ChatFormatting.GRAY));
        } else if (stack.is(PeonyItems.TIE_PLUSHIE.get())) {
            event.getToolTip().add(Component.translatable("item.peony_lib.tie_plushie.desc")
                    .withStyle(ChatFormatting.GRAY));
        }else if (stack.is(PeonyItems.EPICCOOL_PLUSHIE.get())) {
            event.getToolTip().add(Component.translatable("item.peony_lib.epiccool_plushie.desc")
                    .withStyle(ChatFormatting.GRAY));
        }else if (stack.is(PeonyItems.JAMPOTTBONG_PLUSHIE.get())) {
            event.getToolTip().add(Component.translatable("item.peony_lib.jampottbong_plushie.desc")
                    .withStyle(ChatFormatting.GRAY));
        }
          if (stack.getItem() instanceof PeonyPlushies && PeonyPlushies.isMuted(stack)) {
            event.getToolTip().add(Component.translatable("item.peony_lib.plushie.muted")
                    .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        }
    }
}