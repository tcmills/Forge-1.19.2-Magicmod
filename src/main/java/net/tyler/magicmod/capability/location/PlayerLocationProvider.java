package net.tyler.magicmod.capability.location;

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

public class PlayerLocationProvider  implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<PlayerLocation> PLAYER_LOCATION = CapabilityManager.get(new CapabilityToken<PlayerLocation>() { });

    private PlayerLocation location = null;
    private final LazyOptional<PlayerLocation> optional = LazyOptional.of(this::createPlayerLocation);

    private PlayerLocation createPlayerLocation() {
        if (this.location == null) {
            this.location = new PlayerLocation();
        }

        return this.location;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_LOCATION) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerLocation().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerLocation().loadNBTData(nbt);
    }
}
