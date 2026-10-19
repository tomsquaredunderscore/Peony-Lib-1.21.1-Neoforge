package net.tomsquared.peony_lib.items.custom.tools;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;

public class PeonyHammerItem extends PeonyAreaMiningItem {
    public PeonyHammerItem(Tier tier, Properties properties) {
        super(tier, BlockTags.MINEABLE_WITH_PICKAXE, 1, 6.0F, -3.2F, properties);
    }
}