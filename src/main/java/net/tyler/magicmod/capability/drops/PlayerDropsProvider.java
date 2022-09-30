package net.tyler.magicmod.capability.drops;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerDropsProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<PlayerDrops> PLAYER_DROPS = CapabilityManager.get(new CapabilityToken<PlayerDrops>() { });

    private PlayerDrops drops = null;
    private final LazyOptional<PlayerDrops> optional = LazyOptional.of(this::createPlayerDrops);

    private PlayerDrops createPlayerDrops() {
        if (this.drops == null) {
            this.drops = new PlayerDrops();
        }

        return this.drops;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_DROPS) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerDrops().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerDrops().loadNBTData(nbt);
    }
}
