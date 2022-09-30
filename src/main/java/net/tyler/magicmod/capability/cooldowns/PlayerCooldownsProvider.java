package net.tyler.magicmod.capability.cooldowns;

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

public class PlayerCooldownsProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<PlayerCooldowns> PLAYER_COOLDOWNS = CapabilityManager.get(new CapabilityToken<PlayerCooldowns>() { });

    private PlayerCooldowns COOLDOWNS = null;
    private final LazyOptional<PlayerCooldowns> optional = LazyOptional.of(this::createPlayerCooldowns);

    private PlayerCooldowns createPlayerCooldowns() {
        if (this.COOLDOWNS == null) {
            this.COOLDOWNS = new PlayerCooldowns();
        }

        return this.COOLDOWNS;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_COOLDOWNS) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerCooldowns().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerCooldowns().loadNBTData(nbt);
    }
}
