package net.tomsquared.peony_lib.attribute;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tomsquared.peony_lib.Peony_Lib;

public final class PeonyAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(Registries.ATTRIBUTE, Peony_Lib.MOD_ID);

    public static final DeferredHolder<Attribute, Attribute> MINING_RADIUS = ATTRIBUTES.register("mining_radius",
            () -> new RangedAttribute("attribute.name.peony_lib.mining_radius", 0.0D, 0.0D, 16.0D)
            .setSyncable(true));

    private PeonyAttributes() {
    }

    public static final DeferredHolder<Attribute, Attribute> TREE_FELLING_LIMIT =
        ATTRIBUTES.register("tree_felling_limit",
                () -> new RangedAttribute(
                        "attribute.name.peony_lib.tree_felling_limit",
                        0.0D, 0.0D, 1024.0D
                ).setSyncable(true));

    public static void register(IEventBus modEventBus) {
        ATTRIBUTES.register(modEventBus);
        modEventBus.addListener(PeonyAttributes::addToPlayers);
    }

    private static void addToPlayers(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, MINING_RADIUS);
        event.add(EntityType.PLAYER, TREE_FELLING_LIMIT);
    }
}