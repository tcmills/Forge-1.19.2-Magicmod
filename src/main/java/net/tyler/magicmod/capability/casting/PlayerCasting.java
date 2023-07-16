package net.tyler.magicmod.capability.casting;

import net.minecraft.nbt.CompoundTag;

public class PlayerCasting {

    private int bleedTick = 0;

    private boolean flareBlitzCasting = false;

    private int flareBlitzTick = 0;

    private boolean scorchingRayCasting = false;

    private int scorchingRayProjectiles = 3;

    private int scorchingRayTick = 0;

    private boolean fierySoulCasting = false;

    private int fierySoulTick = 0;

    private boolean superCriticalCasting = false;

    private boolean aquamarineBlessingCasting = false;

    private int aquamarineBlessingTick = 0;

    public int getBleedTick() {
        return bleedTick;
    }

    public void addBleedTick(int add) {
        bleedTick += add;
    }

    public void setBleedTick(int tick) {
        bleedTick = tick;
    }

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

    public boolean getSuperCriticalCasting() {
        return superCriticalCasting;
    }

    public void setSuperCriticalCasting(boolean cast) {
        superCriticalCasting = cast;
    }

    public boolean getAquamarineBlessingCasting() {
        return aquamarineBlessingCasting;
    }

    public void setAquamarineBlessingCasting(boolean cast) {
        aquamarineBlessingCasting = cast;
    }

    public int getAquamarineBlessingTick() {
        return aquamarineBlessingTick;
    }

    public void addAquamarineBlessingTick(int add) {
        aquamarineBlessingTick += add;
    }

    public void setAquamarineBlessingTick(int tick) {
        aquamarineBlessingTick = tick;
    }

    public void copyFrom(PlayerCasting source) {
        this.bleedTick = 0;
        this.flareBlitzCasting = false;
        this.flareBlitzTick = 0;
        this.scorchingRayCasting = false;
        this.scorchingRayProjectiles = 3;
        this.scorchingRayTick = 0;
        this.fierySoulCasting = false;
        this.fierySoulTick = 0;
        this.superCriticalCasting = source.superCriticalCasting;
        this.aquamarineBlessingCasting = false;
        this.aquamarineBlessingTick = 0;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("bleedTick", bleedTick);
        nbt.putBoolean("flareBlitzCasting", flareBlitzCasting);
        nbt.putInt("flareBlitzTick", flareBlitzTick);
        nbt.putBoolean("scorchingRayCasting", scorchingRayCasting);
        nbt.putInt("scorchingRayProjectiles", scorchingRayProjectiles);
        nbt.putInt("scorchingRayTick", scorchingRayTick);
        nbt.putBoolean("fierySoulCasting", fierySoulCasting);
        nbt.putInt("fierySoulTick", fierySoulTick);
        nbt.putBoolean("superCriticalCasting", superCriticalCasting);
        nbt.putBoolean("aquamarineBlessingCasting", aquamarineBlessingCasting);
        nbt.putInt("aquamarineBlessingTick", aquamarineBlessingTick);
    }

    public void loadNBTData(CompoundTag nbt) {
        bleedTick = nbt.getInt("bleedTick");
        flareBlitzCasting = nbt.getBoolean("flareBlitzCasting");
        flareBlitzTick = nbt.getInt("flareBlitzTick");
        scorchingRayCasting = nbt.getBoolean("scorchingRayCasting");
        scorchingRayProjectiles = nbt.getInt("scorchingRayProjectiles");
        scorchingRayTick = nbt.getInt("scorchingRayTick");
        fierySoulCasting = nbt.getBoolean("fierySoulCasting");
        fierySoulTick = nbt.getInt("fierySoulTick");
        superCriticalCasting = nbt.getBoolean("superCriticalCasting");
        aquamarineBlessingCasting = nbt.getBoolean("aquamarineBlessingCasting");
        aquamarineBlessingTick = nbt.getInt("aquamarineBlessingTick");
    }
}
