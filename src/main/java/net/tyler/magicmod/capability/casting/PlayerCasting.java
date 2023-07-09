package net.tyler.magicmod.capability.casting;

import net.minecraft.nbt.CompoundTag;

public class PlayerCasting {
    private boolean flareBlitzCasting = false;

    private int flareBlitzTick = 0;

    private boolean scorchingRayCasting = false;

    private int scorchingRayProjectiles = 3;

    private int scorchingRayTick = 0;

    private boolean fierySoulCasting = false;

    private int fierySoulTick = 0;

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

    public boolean getScorchingRayCasting() {
        return scorchingRayCasting;
    }

    public void setScorchingRayCasting(boolean cast) {
        scorchingRayCasting = cast;
    }

    public int getScorchingRayProjectiles() {
        return scorchingRayProjectiles;
    }

    public void subScorchingRayProjectiles(int sub) {
        scorchingRayProjectiles -= sub;
    }

    public void setScorchingRayProjectiles(int tick) {
        scorchingRayProjectiles = tick;
    }

    public int getScorchingRayTick() {
        return scorchingRayTick;
    }

    public void addScorchingRayTick(int add) {
        scorchingRayTick += add;
    }

    public void setScorchingRayTick(int tick) {
        scorchingRayTick = tick;
    }

    public boolean getFierySoulCasting() {
        return fierySoulCasting;
    }

    public void setFierySoulCasting(boolean cast) {
        fierySoulCasting = cast;
    }

    public int getFierySoulTick() {
        return fierySoulTick;
    }

    public void addFierySoulTick(int add) {
        fierySoulTick += add;
    }

    public void setFierySoulTick(int tick) {
        fierySoulTick = tick;
    }

    public void copyFrom(PlayerCasting source) {
        this.flareBlitzCasting = false;
        this.flareBlitzTick = 0;
        this.scorchingRayCasting = false;
        this.scorchingRayProjectiles = 3;
        this.scorchingRayTick = 0;
        this.fierySoulCasting = false;
        this.fierySoulTick = 0;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putBoolean("flareBlitzCasting", flareBlitzCasting);
        nbt.putInt("flareBlitzTick", flareBlitzTick);
        nbt.putBoolean("scorchingRayCasting", scorchingRayCasting);
        nbt.putInt("scorchingRayProjectiles", scorchingRayProjectiles);
        nbt.putInt("scorchingRayTick", scorchingRayTick);
        nbt.putBoolean("fierySoulCasting", fierySoulCasting);
        nbt.putInt("fierySoulTick", fierySoulTick);
    }

    public void loadNBTData(CompoundTag nbt) {
        flareBlitzCasting = nbt.getBoolean("flareBlitzCasting");
        flareBlitzTick = nbt.getInt("flareBlitzTick");
        scorchingRayCasting = nbt.getBoolean("scorchingRayCasting");
        scorchingRayProjectiles = nbt.getInt("scorchingRayProjectiles");
        scorchingRayTick = nbt.getInt("scorchingRayTick");
        fierySoulCasting = nbt.getBoolean("fierySoulCasting");
        fierySoulTick = nbt.getInt("fierySoulTick");
    }
}
