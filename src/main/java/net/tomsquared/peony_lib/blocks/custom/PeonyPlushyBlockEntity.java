package net.tomsquared.peony_lib.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.tomsquared.peony_lib.blocks.PeonyBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.tomsquared.peony_lib.blocks.PeonyBlockEntities;

public class PeonyPlushyBlockEntity extends BlockEntity {
    private static final int ANIMATION_TICKS = 10;
    private static final String START_TICK = "squishStartTick";

    private long squishStartTick = -1L;
    public PeonyPlushyBlockEntity(BlockPos pos, BlockState state) {
        super(PeonyBlockEntities.TOM_PLUSHIE.get(), pos, state);
    }

    public boolean canSquish(long gameTime) {
        return squishStartTick < 0 || gameTime - squishStartTick >= ANIMATION_TICKS;
    }

    public void startSquish(long gameTime) {
        squishStartTick = gameTime;

        setChanged();

        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public float getSquishProgress(float partialTick) {
        if (level == null || squishStartTick < 0) {
            return 0.0F;
        }

        float elapsed = (level.getGameTime() - squishStartTick) + partialTick;
        if (elapsed < 0.0F || elapsed > ANIMATION_TICKS) {
            return 0.0F;
        }

        return Mth.sin((elapsed / ANIMATION_TICKS) * Mth.PI);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong(START_TICK, squishStartTick);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        squishStartTick = tag.contains(START_TICK) ? tag.getLong(START_TICK) : -1L;
    }
}