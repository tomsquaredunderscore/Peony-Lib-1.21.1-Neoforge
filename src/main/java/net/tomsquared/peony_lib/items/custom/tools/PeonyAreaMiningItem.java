package net.tomsquared.peony_lib.items.custom.tools;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.Block;
import net.tomsquared.peony_lib.Peony_Lib;
import net.tomsquared.peony_lib.attribute.PeonyAttributes;

public class PeonyAreaMiningItem extends DiggerItem {
    private static final ResourceLocation BASE_RADIUS_ID = ResourceLocation.fromNamespaceAndPath(Peony_Lib.MOD_ID, "base_mining_radius");

    public PeonyAreaMiningItem(Tier tier, TagKey<Block> mineable, int radius, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, mineable, properties.attributes(createAttributes(tier, radius, attackDamage, attackSpeed)));
    }

    public static ItemAttributeModifiers createAttributes(Tier tier, int radius, float attackDamage, float attackSpeed) {
        return DiggerItem.createAttributes(tier, attackDamage, attackSpeed).withModifierAdded(
                PeonyAttributes.MINING_RADIUS,
                new AttributeModifier(BASE_RADIUS_ID, radius, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND);
    }
}