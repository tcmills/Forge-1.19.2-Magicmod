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

    public void clearCD() {
        magicMissile = 0.0F;
        aid = 0.0F;
        teleport = 0.0F;
        teleportHome = 0.0F;
        flareBlitz = 0.0F;
        scorchingRay = 0.0F;
        fireball = 0.0F;
    }

    public void copyFrom(PlayerCooldowns source) {
        this.magicMissile = source.magicMissile;
        this.aid = source.aid;
        this.teleport = source.teleport;
        this.teleportHome = source.teleportHome;
        this.flareBlitz = source.flareBlitz;
        this.scorchingRay = source.scorchingRay;
        this.fireball = source.fireball;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putFloat("magicMissile", magicMissile);
        nbt.putFloat("aid", aid);
        nbt.putFloat("teleport", teleport);
        nbt.putFloat("teleportHome", teleportHome);
        nbt.putFloat("flareBlitz", flareBlitz);
        nbt.putFloat("scorchingRay", scorchingRay);
        nbt.putFloat("fireball", fireball);
    }

    public void loadNBTData(CompoundTag nbt) {
        magicMissile = nbt.getFloat("magicMissile");
        aid = nbt.getFloat("aid");
        teleport = nbt.getFloat("teleport");
        teleportHome = nbt.getFloat("teleportHome");
        flareBlitz = nbt.getFloat("flareBlitz");
        scorchingRay = nbt.getFloat("scorchingRay");
        fireball = nbt.getFloat("fireball");
    }
}
