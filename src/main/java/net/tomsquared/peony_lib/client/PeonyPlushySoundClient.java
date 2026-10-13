package net.tomsquared.peony_lib.client;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.tomsquared.peony_lib.network.PlushySoundPayload;

public final class PeonyPlushySoundClient {
    private static final Map<Object, SoundInstance> PLAYING = new HashMap<>();

    private PeonyPlushySoundClient() {
    }

    public static void play(PlushySoundPayload payload) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }

        SoundManager soundManager = minecraft.getSoundManager();
        PLAYING.values().removeIf(sound -> !soundManager.isActive(sound));

        SoundEvent sound = payload.sound().value();
        Object key;
        SoundInstance instance;

        if (payload.followsEntity()) {
            Entity entity = minecraft.level.getEntity(payload.entityId());
            if (entity == null) {
                return;
            }

            key = payload.entityId() + "/" + payload.plushy();
            instance = new EntityBoundSoundInstance(sound, SoundSource.PLAYERS, 1.0F, 1.0F, entity, minecraft.level.getRandom().nextLong());
        } else {
            key = payload.pos();
            instance = new SimpleSoundInstance(sound, SoundSource.BLOCKS, 1.0F, 1.0F, minecraft.level.getRandom(), payload.pos());
        }

        SoundInstance previous = PLAYING.put(key, instance);
        if (previous != null) {
            soundManager.stop(previous);
        }

        soundManager.play(instance);
    }
}