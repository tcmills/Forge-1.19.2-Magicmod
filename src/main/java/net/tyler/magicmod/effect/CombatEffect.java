package net.tyler.magicmod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class CombatEffect extends MobEffect {
    protected CombatEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

//    @Override
//    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
//        super.applyEffectTick(pLivingEntity, pAmplifier);
//    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
