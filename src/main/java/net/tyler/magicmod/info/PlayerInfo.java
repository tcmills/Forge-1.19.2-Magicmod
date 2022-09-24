package net.tyler.magicmod.info;

import net.minecraft.nbt.CompoundTag;

public class PlayerInfo {

    private int schoolLevel;

    private boolean fire;
    private boolean water;
    private boolean earth;
    private boolean air;
    private boolean summoning;
    private boolean forge;
    private boolean storm;
    private boolean ender;
    private boolean life;
    private boolean death;
    private boolean sun;
    private boolean moon;

    private boolean dungeonParty;


    public int getSchoolLevel() {
        return schoolLevel;
    }

    public void setSchoolLevel(int set) {
        schoolLevel = set;
    }

    public void schoolLevelUp() {
        if (schoolLevel < 3) {
            schoolLevel++;
        }
    }

    public void expel() {
        fire = false;
        water = false;
        earth = false;
        air = false;
        summoning = false;
        forge = false;
        storm = false;
        ender = false;
        life = false;
        death = false;
        sun = false;
        moon = false;
    }

    public boolean getFire() {
        return fire;
    }

    public void joinFire() {
        fire = true;
        water = false;
        earth = false;
        air = false;
        summoning = false;
        forge = false;
        storm = false;
        ender = false;
        life = false;
        death = false;
        sun = false;
        moon = false;
    }

    public boolean getWater() {
        return water;
    }

    public void joinWater() {
        fire = false;
        water = true;
        earth = false;
        air = false;
        summoning = false;
        forge = false;
        storm = false;
        ender = false;
        life = false;
        death = false;
        sun = false;
        moon = false;
    }

    public boolean getEarth() {
        return earth;
    }

    public void joinEarth() {
        fire = false;
        water = false;
        earth = true;
        air = false;
        summoning = false;
        forge = false;
        storm = false;
        ender = false;
        life = false;
        death = false;
        sun = false;
        moon = false;
    }

    public boolean getAir() {
        return air;
    }

    public void joinAir() {
        fire = false;
        water = false;
        earth = false;
        air = true;
        summoning = false;
        forge = false;
        storm = false;
        ender = false;
        life = false;
        death = false;
        sun = false;
        moon = false;
    }

    public boolean getSummoning() {
        return summoning;
    }

    public void joinSummoning() {
        fire = false;
        water = false;
        earth = false;
        air = false;
        summoning = true;
        forge = false;
        storm = false;
        ender = false;
        life = false;
        death = false;
        sun = false;
        moon = false;
    }

    public boolean getForge() {
        return forge;
    }

    public void joinForge() {
        fire = false;
        water = false;
        earth = false;
        air = false;
        summoning = false;
        forge = true;
        storm = false;
        ender = false;
        life = false;
        death = false;
        sun = false;
        moon = false;
    }

    public boolean getStorm() {
        return storm;
    }

    public void joinStorm() {
        fire = false;
        water = false;
        earth = false;
        air = false;
        summoning = false;
        forge = false;
        storm = true;
        ender = false;
        life = false;
        death = false;
        sun = false;
        moon = false;
    }

    public boolean getEnder() {
        return ender;
    }

    public void joinEnder() {
        fire = false;
        water = false;
        earth = false;
        air = false;
        summoning = false;
        forge = false;
        storm = false;
        ender = true;
        life = false;
        death = false;
        sun = false;
        moon = false;
    }

    public boolean getLife() {
        return life;
    }

    public void joinLife() {
        fire = false;
        water = false;
        earth = false;
        air = false;
        summoning = false;
        forge = false;
        storm = false;
        ender = false;
        life = true;
        death = false;
        sun = false;
        moon = false;
    }

    public boolean getDeath() {
        return death;
    }

    public void joinDeath() {
        fire = false;
        water = false;
        earth = false;
        air = false;
        summoning = false;
        forge = false;
        storm = false;
        ender = false;
        life = false;
        death = true;
        sun = false;
        moon = false;
    }

    public boolean getSun() {
        return sun;
    }

    public void joinSun() {
        fire = false;
        water = false;
        earth = false;
        air = false;
        summoning = false;
        forge = false;
        storm = false;
        ender = false;
        life = false;
        death = false;
        sun = true;
        moon = false;
    }

    public boolean getMoon() {
        return moon;
    }

    public void joinMoon() {
        fire = false;
        water = false;
        earth = false;
        air = false;
        summoning = false;
        forge = false;
        storm = false;
        ender = false;
        life = false;
        death = false;
        sun = false;
        moon = true;
    }

    public boolean getDungeonParty() {
        return dungeonParty;
    }

    public void joinDungeonParty() {
        dungeonParty = true;
    }

    public void leaveDungeonParty() {
        dungeonParty = false;
    }


    public void copyFrom(PlayerInfo source) {
        this.schoolLevel = source.schoolLevel;

        this.fire = source.fire;
        this.water = source.water;
        this.earth = source.earth;
        this.air = source.air;
        this.summoning = source.summoning;
        this.forge = source.forge;
        this.storm = source.storm;
        this.ender = source.ender;
        this.life = source.life;
        this.death = source.death;
        this.sun = source.sun;
        this.moon = source.moon;

        this.dungeonParty = source.dungeonParty;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("schoolLevel", schoolLevel);

        nbt.putBoolean("fire", fire);
        nbt.putBoolean("water", water);
        nbt.putBoolean("earth", earth);
        nbt.putBoolean("air", air);
        nbt.putBoolean("summoning", summoning);
        nbt.putBoolean("forge", forge);
        nbt.putBoolean("storm", storm);
        nbt.putBoolean("ender", ender);
        nbt.putBoolean("life", life);
        nbt.putBoolean("death", death);
        nbt.putBoolean("sun", sun);
        nbt.putBoolean("moon", moon);

        nbt.putBoolean("dungeonParty", dungeonParty);
    }

    public void loadNBTData(CompoundTag nbt) {
        schoolLevel = nbt.getInt("schoolLevel");

        fire = nbt.getBoolean("fire");
        water = nbt.getBoolean("water");
        earth = nbt.getBoolean("earth");
        air = nbt.getBoolean("air");
        summoning = nbt.getBoolean("summoning");
        forge = nbt.getBoolean("forge");
        storm = nbt.getBoolean("storm");
        ender = nbt.getBoolean("ender");
        life = nbt.getBoolean("life");
        death = nbt.getBoolean("death");
        sun = nbt.getBoolean("sun");
        moon = nbt.getBoolean("moon");

        dungeonParty = nbt.getBoolean("dungeonParty");
    }

}
