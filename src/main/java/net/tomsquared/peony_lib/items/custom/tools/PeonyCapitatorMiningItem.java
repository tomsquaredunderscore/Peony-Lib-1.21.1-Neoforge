package net.tomsquared.peony_lib.items.custom.tools;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.tomsquared.peony_lib.Peony_Lib;
import net.tomsquared.peony_lib.attribute.PeonyAttributes;

public class PeonyCapitatorMiningItem extends AxeItem {
    private static final ResourceLocation BASE_FELLING_ID =
            ResourceLocation.fromNamespaceAndPath(Peony_Lib.MOD_ID, "base_tree_felling");

    public PeonyCapitatorMiningItem(Tier tier, int logLimit, float damage,
                                   float speed, Properties properties) {
        super(tier, properties.attributes(createAttributes(tier, logLimit, damage, speed)));
    }

    private static ItemAttributeModifiers createAttributes(Tier tier, int logLimit,
                                                           float damage, float speed) {
        if (logLimit < 1 || logLimit > 1024) {
            throw new IllegalArgumentException("Tree felling limit must be between 1 and 1024");
        }

        return AxeItem.createAttributes(tier, damage, speed).withModifierAdded(
                PeonyAttributes.TREE_FELLING_LIMIT,
                new AttributeModifier(BASE_FELLING_ID, logLimit, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
        );
    }
}