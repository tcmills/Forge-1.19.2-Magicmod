package net.tyler.magicmod.info;

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

public class PlayerInfoProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<PlayerInfo> PLAYER_INFO = CapabilityManager.get(new CapabilityToken<PlayerInfo>() { });

    private PlayerInfo info = null;
    private final LazyOptional<PlayerInfo> optional = LazyOptional.of(this::createPlayerInfo);

    private PlayerInfo createPlayerInfo() {
        if (this.info == null) {
            this.info = new PlayerInfo();
        }

        return this.info;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_INFO) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerInfo().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerInfo().loadNBTData(nbt);
    }
}
