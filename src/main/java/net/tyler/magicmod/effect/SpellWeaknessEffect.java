package net.tyler.magicmod.effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SpellWeaknessEffect extends MobEffect {
    protected SpellWeaknessEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity instanceof ServerPlayer player) {
            //player.getLevel().sendParticles(ParticleTypes.WARPED_SPORE, player.getX(), player.getY()+1, player.getZ(), 1,0.5D, 0.5D, 0.5D, 0.0D);
            player.getLevel().addParticle(ParticleTypes.WARPED_SPORE, (double)player.getX() + (player.getLevel().random.nextDouble() * 0.5D), (double)player.getY() + (player.getLevel().random.nextDouble() * 0.5D), (double)player.getZ() + (player.getLevel().random.nextDouble() * 0.5D), 0.0D, 0.0D, 0.0D);
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }


    @Override
    public List<ItemStack> getCurativeItems() {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        return ret;
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        int k = 2 >> pAmplifier;
        if (k > 0) {
            return pDuration % k == 0;
        } else {
            return true;
        }
    }
}
