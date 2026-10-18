package net.tomsquared.peony_lib.sound;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tomsquared.peony_lib.Peony_Lib;

public final class PeonySoundEvents {
    public static final DeferredRegister<GameEvent> GAME_EVENTS =
            DeferredRegister.create(Registries.GAME_EVENT, Peony_Lib.MOD_ID);

    public static final DeferredHolder<GameEvent, GameEvent> PLUSHY_SOUND =
            GAME_EVENTS.register("plushy_sound", () -> new GameEvent(GameEvent.DEFAULT_NOTIFICATION_RADIUS));

    private PeonySoundEvents() {
    }

    public static void register(IEventBus eventBus) {
        GAME_EVENTS.register(eventBus);
    }
}