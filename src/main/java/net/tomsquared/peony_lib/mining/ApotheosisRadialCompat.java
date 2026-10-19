package net.tomsquared.peony_lib.mining;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.UUID;

import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;
import net.tomsquared.peony_lib.Peony_Lib;

// Apotheosis Compatibility for radial mining
final class ApotheosisRadialCompat {
    private static final ThreadLocal<Set<UUID>> APOTH_BREAKERS = findBreakers();

    private ApotheosisRadialCompat() {
    }

    static boolean isMining(Player player) {
        return APOTH_BREAKERS != null && APOTH_BREAKERS.get().contains(player.getUUID());
    }

    static void claim(Player player) {
        if (APOTH_BREAKERS != null) {
            APOTH_BREAKERS.get().add(player.getUUID());
        }
    }

    static void release(Player player) {
        if (APOTH_BREAKERS != null) {
            APOTH_BREAKERS.get().remove(player.getUUID());
        }
    }

    @SuppressWarnings("unchecked")
    private static ThreadLocal<Set<UUID>> findBreakers() {
        if (!ModList.get().isLoaded("apotheosis")) {
            return null;
        }

        try {
            Field field = Class.forName("dev.shadowsoffire.apotheosis.util.RadialUtil").getDeclaredField("breakers");
            field.setAccessible(true);
            return (ThreadLocal<Set<UUID>>) field.get(null);
        } catch (ReflectiveOperationException | RuntimeException e) {
            Peony_Lib.LOGGER.warn("Couldn't hook into Apotheosis radial mining, tools with both may over-mine", e);
            return null;
        }
    }
}