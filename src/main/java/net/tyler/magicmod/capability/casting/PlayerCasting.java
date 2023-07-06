package net.tyler.magicmod.capability.casting;

import net.minecraft.nbt.CompoundTag;

public class PlayerCasting {
    private boolean flareBlitzCasting = false;

    private int flareBlitzTick = 0;

    public boolean getFlareBlitzCasting() {
        return flareBlitzCasting;
    }

    public void setFlareBlitzCasting(boolean cast) {
        flareBlitzCasting = cast;
    }

    public int getFlareBlitzTick() {
        return flareBlitzTick;
    }

    public void addFlareBlitzTick(int add) {
        flareBlitzTick += add;
    }

    public void setFlareBlitzTick(int tick) {
        flareBlitzTick = tick;
    }

    public void copyFrom(PlayerCasting source) {
        //this.flareBlitzCasting = source.flareBlitzCasting;
        this.flareBlitzCasting = false;
        this.flareBlitzTick = 0;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putBoolean("flareBlitzCasting", flareBlitzCasting);
        nbt.putInt("flareBlitzTick", flareBlitzTick);
    }

    public void loadNBTData(CompoundTag nbt) {
        flareBlitzCasting = nbt.getBoolean("flareBlitzCasting");
        flareBlitzTick = nbt.getInt("flareBlitzTick");
    }
}
