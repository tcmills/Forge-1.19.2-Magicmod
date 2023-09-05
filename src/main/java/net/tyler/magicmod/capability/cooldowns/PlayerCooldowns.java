package net.tyler.magicmod.capability.cooldowns;

import net.minecraft.nbt.CompoundTag;

public class PlayerCooldowns {

    private float magicMissile = 0.0F;
    private float aid = 0.0F;
    private float teleport = 0.0F;
    private float teleportHome = 0.0F;
    private float flareBlitz = 0.0F;
    private float scorchingRay = 0.0F;
    private float fireball = 0.0F;
    private float superCritical = 0.0F;
    private float sharkLunge = 0.0F;
    private float waterfall = 0.0F;
    private float galeforce = 0.0F;
    private float yeet = 0.0F;
    private float airDarts = 0.0F;
    private float toss = 0.0F;
    private float wingsOfQuartz = 0.0F;
    private float weightOfPyrite = 0.0F;
    private float burrow = 0.0F;
    private float earthquake = 0.0F;

    public float getMagicMissileCD() {
        return magicMissile;
    }

    public void setMagicMissileCD(float sec) {
        magicMissile = sec;
    }

    public float getAidCD() {
        return aid;
    }

    public void setAidCD(float sec) {
        aid = sec;
    }

    public float getTeleportCD() {
        return teleport;
    }

    public void setTeleportCD(float sec) {
        teleport = sec;
    }

    public float getTeleportHomeCD() {
        return teleportHome;
    }

    public void setTeleportHomeCD(float sec) {
        teleportHome = sec;
    }

    public float getFlareBlitzCD() {
        return flareBlitz;
    }

    public void setFlareBlitzCD(float sec) {
        flareBlitz = sec;
    }

    public float getScorchingRayCD() {
        return scorchingRay;
    }

    public void setScorchingRayCD(float sec) {
        scorchingRay = sec;
    }

    public float getFireballCD() {
        return fireball;
    }

    public void setFireballCD(float sec) {
        fireball = sec;
    }

    public float getSuperCriticalCD() {
        return superCritical;
    }

    public void setSuperCriticalCD(float sec) {
        superCritical = sec;
    }

    public float getSharkLungeCD() {
        return sharkLunge;
    }

    public void setSharkLungeCD(float sec) {
        sharkLunge = sec;
    }

    public float getWaterfallCD() {
        return waterfall;
    }

    public void setWaterfallCD(float sec) {
        waterfall = sec;
    }

    public float getGaleforceCD() {
        return galeforce;
    }

    public void setGaleforceCD(float sec) {
        galeforce = sec;
    }

    public float getYeetCD() {
        return yeet;
    }

    public void setYeetCD(float sec) {
        yeet = sec;
    }

    public float getAirDartsCD() {
        return airDarts;
    }

    public void setAirDartsCD(float sec) {
        airDarts = sec;
    }

    public float getTossCD() {
        return toss;
    }

    public void setTossCD(float sec) {
        toss = sec;
    }

    public float getWingsOfQuartzCD() {
        return wingsOfQuartz;
    }

    public void setWingsOfQuartzCD(float sec) {
        wingsOfQuartz = sec;
    }

    public float getWeightOfPyriteCD() {
        return weightOfPyrite;
    }

    public void setWeightOfPyriteCD(float sec) {
        weightOfPyrite = sec;
    }

    public float getBurrowCD() {
        return burrow;
    }

    public void setBurrowCD(float sec) {
        burrow = sec;
    }

    public float getEarthquakeCD() {
        return earthquake;
    }

    public void setEarthquakeCD(float sec) {
        earthquake = sec;
    }

    public void clearCD() {
        magicMissile = 0.0F;
        aid = 0.0F;
        teleport = 0.0F;
        teleportHome = 0.0F;
        flareBlitz = 0.0F;
        scorchingRay = 0.0F;
        fireball = 0.0F;
        superCritical = 0.0F;
        sharkLunge = 0.0F;
        waterfall = 0.0F;
        galeforce = 0.0F;
        yeet = 0.0F;
        airDarts = 0.0F;
        toss = 0.0F;
        wingsOfQuartz = 0.0F;
        weightOfPyrite = 0.0F;
        burrow = 0.0F;
        earthquake = 0.0F;
    }

    public void copyFrom(PlayerCooldowns source) {
        this.magicMissile = source.magicMissile;
        this.aid = source.aid;
        this.teleport = source.teleport;
        this.teleportHome = source.teleportHome;
        this.flareBlitz = source.flareBlitz;
        this.scorchingRay = source.scorchingRay;
        this.fireball = source.fireball;
        this.superCritical = source.superCritical;
        this.sharkLunge = source.sharkLunge;
        this.waterfall = source.waterfall;
        this.galeforce = source.galeforce;
        this.yeet = source.yeet;
        this.airDarts = source.airDarts;
        this.toss = source.toss;
        this.wingsOfQuartz = source.wingsOfQuartz;
        this.weightOfPyrite = source.weightOfPyrite;
        this.burrow = source.burrow;
        this.earthquake = source.earthquake;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putFloat("magicMissile", magicMissile);
        nbt.putFloat("aid", aid);
        nbt.putFloat("teleport", teleport);
        nbt.putFloat("teleportHome", teleportHome);
        nbt.putFloat("flareBlitz", flareBlitz);
        nbt.putFloat("scorchingRay", scorchingRay);
        nbt.putFloat("fireball", fireball);
        nbt.putFloat("superCritical", superCritical);
        nbt.putFloat("sharkLunge", sharkLunge);
        nbt.putFloat("waterfall", waterfall);
        nbt.putFloat("galeforce", galeforce);
        nbt.putFloat("yeet", yeet);
        nbt.putFloat("airDarts", airDarts);
        nbt.putFloat("toss", toss);
        nbt.putFloat("wingsOfQuartz", wingsOfQuartz);
        nbt.putFloat("weightOfPyrite", weightOfPyrite);
        nbt.putFloat("burrow", burrow);
        nbt.putFloat("earthquake", earthquake);
    }

    public void loadNBTData(CompoundTag nbt) {
        magicMissile = nbt.getFloat("magicMissile");
        aid = nbt.getFloat("aid");
        teleport = nbt.getFloat("teleport");
        teleportHome = nbt.getFloat("teleportHome");
        flareBlitz = nbt.getFloat("flareBlitz");
        scorchingRay = nbt.getFloat("scorchingRay");
        fireball = nbt.getFloat("fireball");
        superCritical = nbt.getFloat("superCritical");
        sharkLunge = nbt.getFloat("sharkLunge");
        waterfall = nbt.getFloat("waterfall");
        galeforce = nbt.getFloat("galeforce");
        yeet = nbt.getFloat("yeet");
        airDarts = nbt.getFloat("airDarts");
        toss = nbt.getFloat("toss");
        wingsOfQuartz = nbt.getFloat("wingsOfQuartz");
        weightOfPyrite = nbt.getFloat("weightOfPyrite");
        burrow = nbt.getFloat("burrow");
        earthquake = nbt.getFloat("earthquake");
    }
}
