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

    private boolean sharkLungeCasting = false;

    private int sharkLungeTick = 0;

    private boolean amphibiousCasting = false;

    private int amphibiousTick = 0;

    private boolean airDartsCasting = false;

    private int airDartsProjectiles = 0;

    private boolean wingsOfQuartzCasting = false;

    private int wingsOfQuartzTick = 0;

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

    public void setScorchingRayProjectiles(int num) {
        scorchingRayProjectiles = num;
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

    public boolean getSharkLungeCasting() {
        return sharkLungeCasting;
    }

    public void setSharkLungeCasting(boolean cast) {
        sharkLungeCasting = cast;
    }

    public int getSharkLungeTick() {
        return sharkLungeTick;
    }

    public void addSharkLungeTick(int add) {
        sharkLungeTick += add;
    }

    public void setSharkLungeTick(int tick) {
        sharkLungeTick = tick;
    }

    public boolean getAmphibiousCasting() {
        return amphibiousCasting;
    }

    public void setAmphibiousCasting(boolean cast) {
        amphibiousCasting = cast;
    }

    public int getAmphibiousTick() {
        return amphibiousTick;
    }

    public void addAmphibiousTick(int add) {
        amphibiousTick += add;
    }

    public void setAmphibiousTick(int tick) {
        amphibiousTick = tick;
    }

    public boolean getAirDartsCasting() {
        return airDartsCasting;
    }

    public void setAirDartsCasting(boolean cast) {
        airDartsCasting = cast;
    }

    public int getAirDartsProjectiles() {
        return airDartsProjectiles;
    }

    public void subAirDartsProjectiles(int sub) {
        if (airDartsProjectiles - sub < 0) {
            airDartsProjectiles = 0;
        } else {
            airDartsProjectiles -= sub;
        }
    }

    public void setAirDartsProjectiles(int num) {
        airDartsProjectiles = num;
    }

    public boolean getWingsOfQuartzCasting() {
        return wingsOfQuartzCasting;
    }

    public void setWingsOfQuartzCasting(boolean cast) {
        wingsOfQuartzCasting = cast;
    }

    public int getWingsOfQuartzTick() {
        return wingsOfQuartzTick;
    }

    public void addWingsOfQuartzTick(int add) {
        wingsOfQuartzTick += add;
    }

    public void setWingsOfQuartzTick(int tick) {
        wingsOfQuartzTick = tick;
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
        this.sharkLungeCasting = false;
        this.sharkLungeTick = 0;
        this.amphibiousCasting = false;
        this.amphibiousTick = 0;
        this.airDartsCasting = source.airDartsCasting;
        this.airDartsProjectiles = source.airDartsProjectiles;
        this.wingsOfQuartzCasting = false;
        this.wingsOfQuartzTick = 0;
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
        nbt.putBoolean("sharkLungeCasting", sharkLungeCasting);
        nbt.putInt("sharkLungeTick", sharkLungeTick);
        nbt.putBoolean("amphibiousCasting", amphibiousCasting);
        nbt.putInt("amphibiousTick", amphibiousTick);
        nbt.putBoolean("airDartsCasting", airDartsCasting);
        nbt.putInt("airDartsProjectiles", airDartsProjectiles);
        nbt.putBoolean("wingsOfQuartzCasting", wingsOfQuartzCasting);
        nbt.putInt("wingsOfQuartzTick", wingsOfQuartzTick);
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
        sharkLungeCasting = nbt.getBoolean("sharkLungeCasting");
        sharkLungeTick = nbt.getInt("sharkLungeTick");
        amphibiousCasting = nbt.getBoolean("amphibiousCasting");
        amphibiousTick = nbt.getInt("amphibiousTick");
        airDartsCasting = nbt.getBoolean("airDartsCasting");
        airDartsProjectiles = nbt.getInt("airDartsProjectiles");
        wingsOfQuartzCasting = nbt.getBoolean("wingsOfQuartzCasting");
        wingsOfQuartzTick = nbt.getInt("wingsOfQuartzTick");
    }
}
