package net.tyler.magicmod.location;

import net.minecraft.nbt.CompoundTag;

public class PlayerLocation {

    private boolean homeSet = false;
    private double homeX;
    private double homeY;
    private double homeZ;

    public double getHomeX() {
        return homeX;
    }

    public double getHomeY() {
        return homeY;
    }

    public double getHomeZ() {
        return homeZ;
    }

    public void setHome(double x, double y, double z) {
        homeX = x;
        homeY = y;
        homeZ = z;
        homeSet = true;
    }

    public boolean isHomeSet() {
        return homeSet;
    }

    public void copyFrom(PlayerLocation source) {
        this.homeX = source.homeX;
        this.homeY = source.homeY;
        this.homeZ = source.homeZ;
        this.homeSet = source.homeSet;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putDouble("homeX", homeX);
        nbt.putDouble("homeY", homeY);
        nbt.putDouble("homeZ", homeZ);

        nbt.putBoolean("homeSet", homeSet);
    }

    public void loadNBTData(CompoundTag nbt) {
        homeX = nbt.getDouble("homeX");
        homeY = nbt.getDouble("homeY");
        homeZ = nbt.getDouble("homeZ");

        homeSet = nbt.getBoolean("homeSet");
    }
}
