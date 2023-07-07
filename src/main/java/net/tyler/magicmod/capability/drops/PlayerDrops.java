package net.tyler.magicmod.capability.drops;

import net.minecraft.nbt.CompoundTag;

public class PlayerDrops {

    private int[] drops = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public int[] getDrops() {
        return drops;
    }

    public void addDropNumber(int index) {
        drops[index] = drops[index] + 1;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putIntArray("drops", drops);
    }

    public void loadNBTData(CompoundTag nbt) {
        drops = nbt.getIntArray("drops");
    }
}
