package net.tomsqaured.peony_lib.sound;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tomsqaured.peony_lib.Peony_Lib;

public class PeonySounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, Peony_Lib.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> TOM_PLUSHIE_VOICE_LINES_1 = register("tom_plushie_voice_lines_1");
    public static final DeferredHolder<SoundEvent, SoundEvent> TOM_PLUSHIE_VOICE_LINES_2 = register("tom_plushie_voice_lines_2");
    public static final DeferredHolder<SoundEvent, SoundEvent> TOM_PLUSHIE_VOICE_LINES_3 = register("tom_plushie_voice_lines_3");
    public static final DeferredHolder<SoundEvent, SoundEvent> TOM_PLUSHIE_VOICE_LINES_4 = register("tom_plushie_voice_lines_4");
    public static final DeferredHolder<SoundEvent, SoundEvent> TOM_PLUSHIE_VOICE_LINES_5 = register("tom_plushie_voice_lines_5");
    public static final DeferredHolder<SoundEvent, SoundEvent> TOM_PLUSHIE_VOICE_LINES_6 = register("tom_plushie_voice_lines_6");
    public static final DeferredHolder<SoundEvent, SoundEvent> TOM_PLUSHIE_VOICE_LINES_7 = register("tom_plushie_voice_lines_7");
    public static final DeferredHolder<SoundEvent, SoundEvent> TOM_PLUSHIE_VOICE_LINES_8 = register("tom_plushie_voice_lines_8");
    public static final DeferredHolder<SoundEvent, SoundEvent> TOM_PLUSHIE_VOICE_LINES_9 = register("tom_plushie_voice_lines_9");
    public static final DeferredHolder<SoundEvent, SoundEvent> TOM_PLUSHIE_VOICE_LINES_10 = register("tom_plushie_voice_lines_10");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Peony_Lib.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}