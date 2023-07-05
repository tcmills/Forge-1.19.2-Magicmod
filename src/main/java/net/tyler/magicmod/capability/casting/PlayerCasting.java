package net.tyler.magicmod.capability.casting;

import net.minecraft.nbt.CompoundTag;

public class PlayerCasting {
    private boolean flareBlitzCasting = false;

    public boolean getFlareBlitzCasting() {
        return flareBlitzCasting;
    }

    public void setFlareBlitzCasting(boolean cast) {
        flareBlitzCasting = cast;
    }

    public void copyFrom(PlayerCasting source) {
        this.flareBlitzCasting = source.flareBlitzCasting;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putBoolean("flareBlitzCasting", flareBlitzCasting);
    }

    public void loadNBTData(CompoundTag nbt) {
        flareBlitzCasting = nbt.getBoolean("flareBlitzCasting");
    }
}
