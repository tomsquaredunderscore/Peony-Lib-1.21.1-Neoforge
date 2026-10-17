
package net.tomsquared.peony_lib;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class BetaNotificationHandler {

    private static final String[][] MODS = {
            {"geological", "Geological"},
            {"magical", "Magical"}
    };

    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        List<String> installedMods = new ArrayList<>();

        for (String[] mod : MODS) {
            if (ModList.get().isLoaded(mod[0])) {
                installedMods.add(mod[1]);
            }
        }

        if (installedMods.isEmpty()) {
            return;
        }

        String modNames;

        if (installedMods.size() == 1) {
            modNames = installedMods.get(0);
        } else if (installedMods.size() == 2) {
            modNames = installedMods.get(0)
                    + " and "
                    + installedMods.get(1);
        } else {
            modNames = String.join(
                    ", ",
                    installedMods.subList(0, installedMods.size() - 1)
            ) + ", and " + installedMods.get(installedMods.size() - 1);
        }

        boolean singleMod = installedMods.size() == 1;

        String noun = singleMod ? "mod" : "mods";
        String verb = singleMod ? "is" : "are";

        Component message = Component.literal(
                "⚠ The " + noun + " " + modNames
                        + " " + verb
                        + " in very early access. Not all features are finished, "
                        + "so please bare with us! ⚠"
        ).withStyle(ChatFormatting.GOLD);

        event.getEntity().sendSystemMessage(message);
    }
}
