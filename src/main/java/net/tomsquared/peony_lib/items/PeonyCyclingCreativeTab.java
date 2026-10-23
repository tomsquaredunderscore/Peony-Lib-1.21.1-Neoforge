package net.tomsquared.peony_lib.items;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class PeonyCyclingCreativeTab extends CreativeModeTab {
    private static final long ICON_INTERVAL_NANOS = 2_000_000_000L;

    private List<ItemStack> icons = List.of();
    private int iconIndex = -1;
    private long lastChange;

    public PeonyCyclingCreativeTab(CreativeModeTab.Builder builder) {
        super(builder);
    }

    @Override
    public void buildContents(ItemDisplayParameters parameters) {
        super.buildContents(parameters);

        icons = getDisplayItems().stream()
                .map(ItemStack::copy)
                .toList();

        iconIndex = -1;
    }

    @Override
    public ItemStack getIconItem() {
        if (icons.isEmpty()) {
            return super.getIconItem();
        }

        long now = System.nanoTime();

        if (iconIndex < 0 || now - lastChange >= ICON_INTERVAL_NANOS) {
            int count = icons.size();

            if (iconIndex < 0) {
                iconIndex = ThreadLocalRandom.current().nextInt(count);
            } else if (count > 1) {
                int next = ThreadLocalRandom.current().nextInt(count - 1);
                iconIndex = next >= iconIndex ? next + 1 : next;
            }

            lastChange = now;
        }

        return icons.get(iconIndex);
    }
}