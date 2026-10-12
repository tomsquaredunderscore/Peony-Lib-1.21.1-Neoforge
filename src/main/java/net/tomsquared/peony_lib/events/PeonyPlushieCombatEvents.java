package net.tomsquared.peony_lib.events;

import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.tomsquared.peony_lib.Peony_Lib;
import net.tomsquared.peony_lib.items.custom.PeonyPlushies;

@EventBusSubscriber(modid = Peony_Lib.MOD_ID)
public final class PeonyPlushieCombatEvents {
    private static final Map<Player, Long> CHARGE_START_TICKS = new WeakHashMap<>();

    private PeonyPlushieCombatEvents() {
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();

        if (stack.getItem() instanceof PeonyPlushies && player.isCrouching()) {
            CHARGE_START_TICKS.putIfAbsent(player, player.level().getGameTime());
            return;
        }

        CHARGE_START_TICKS.remove(player);
    }

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();

        if (!(stack.getItem() instanceof PeonyPlushies) || !(event.getTarget() instanceof LivingEntity target)) {
            return;
        }

        event.setCanceled(true);

        float knockback = getKnockbackStrength(player);
        applyKnockback(player, target, knockback);

        CHARGE_START_TICKS.remove(player);
    }

    private static float getKnockbackStrength(Player player) {
        float baseKnockback = 0.4F;

        if (!player.isCrouching()) {
            return baseKnockback;
        }

        Long startTick = CHARGE_START_TICKS.get(player);
        if (startTick == null) {
            return baseKnockback;
        }

        float chargedSeconds = (player.level().getGameTime() - startTick) / 20.0F;
        float charge = Mth.clamp(chargedSeconds / 5.0F, 0.0F, 1.0F);

        return baseKnockback * (1.0F + (4.0F * charge));
    }

    private static void applyKnockback(Player player, LivingEntity target, float strength) {
        double dx = player.getX() - target.getX();
        double dz = player.getZ() - target.getZ();
        target.knockback(strength, dx, dz);
    }
}