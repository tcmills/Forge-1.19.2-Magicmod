package net.tyler.magicmod.mana;

import net.minecraft.nbt.CompoundTag;

public class PlayerMana {
    private int mana;
    private final int MIN_MANA = 0;
    private int max_mana = 100;

    public int getMana() {
        return mana;
    }

    public int getMaxMana() {
        return max_mana;
    }

    public void addMana(int add) {
        this.mana = Math.min(mana + add, max_mana);
    }

    public void subMana(int sub) {
        if (mana - sub >= MIN_MANA) {
            this.mana = mana - sub;
        }
    }

    public void addMaxMana(int add) {
        this.max_mana = max_mana + add;
    }

    public void subMaxMana(int sub) {
        this.max_mana = Math.max(max_mana - sub, MIN_MANA);
        if (mana > max_mana) {
            this.mana = max_mana;
        }
    }

    public void copyFrom(PlayerMana source) {
        this.mana = 0;
        this.max_mana = source.max_mana;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("mana", mana);
        nbt.putInt("max_mana", max_mana);
    }

    public void loadNBTData(CompoundTag nbt) {
        mana = nbt.getInt("mana");
        max_mana = nbt.getInt("max_mana");
    }
}
