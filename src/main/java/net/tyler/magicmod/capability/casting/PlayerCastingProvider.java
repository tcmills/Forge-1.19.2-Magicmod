package net.tyler.magicmod.capability.casting;

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

public class PlayerCastingProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<PlayerCasting> PLAYER_CASTING = CapabilityManager.get(new CapabilityToken<PlayerCasting>() { });

    private PlayerCasting CASTING = null;
    private final LazyOptional<PlayerCasting> optional = LazyOptional.of(this::createPlayerCasting);

    private PlayerCasting createPlayerCasting() {
        if (this.CASTING == null) {
            this.CASTING = new PlayerCasting();
        }

        return this.CASTING;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_CASTING) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerCasting().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerCasting().loadNBTData(nbt);
    }
}
