package net.tomsquared.peony_lib.events;

import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
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
    private static final int MAX_CHARGE_TICKS = 100;
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

        if (player.level().isClientSide) {
            return;
        }

        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof PeonyPlushies) || !(event.getTarget() instanceof LivingEntity target)) {
            return;
        }

        float charge = getChargeProgress(player);

        event.setCanceled(true);
        applyKnockback(player, target, charge);
        CHARGE_START_TICKS.remove(player);
    }

    public static float getChargeProgress(Player player) {
        if (player == null || !player.isCrouching()) {
            return 0.0F;
        }

        Long startTick = CHARGE_START_TICKS.get(player);
        if (startTick == null) {
            return 0.0F;
        }

        float chargedTicks = player.level().getGameTime() - startTick.longValue();
        return Mth.clamp(chargedTicks / (float) MAX_CHARGE_TICKS, 0.0F, 1.0F);
    }

    private static void applyKnockback(Player player, LivingEntity target, float charge) {
        float strength = 0.45F + (3.55F * charge);
        double dx = player.getX() - target.getX();
        double dz = player.getZ() - target.getZ();

        target.knockback(strength, dx, dz);
        target.setDeltaMovement(target.getDeltaMovement().add(0.0D, 0.08D + (0.12D * charge), 0.0D));

        if (target instanceof ServerPlayer pushed) {
            pushed.connection.send(new ClientboundSetEntityMotionPacket(pushed));
        }
    }
}