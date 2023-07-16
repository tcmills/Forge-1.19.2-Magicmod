package net.tyler.magicmod.effect;

import com.mojang.math.Vector3f;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tyler.magicmod.capability.casting.PlayerCastingProvider;
import net.tyler.magicmod.capability.info.PlayerInfoProvider;
import net.tyler.magicmod.misc.ModDamageSource;

import java.util.ArrayList;
import java.util.List;

public class BleedEffect extends MobEffect {
    protected BleedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {

        if (!pLivingEntity.level.isClientSide()) {
            if (pLivingEntity instanceof Player) {
                if (!pLivingEntity.isCrouching()) {
                    //pLivingEntity.sendSystemMessage(Component.literal("" + pLivingEntity.getSpeed()));
                    pLivingEntity.hurt(ModDamageSource.bleed(), 1F);
                    //pLivingEntity.sendSystemMessage(Component.literal("" + pLivingEntity.getSpeed() + "\n"));

                    ((ServerLevel)pLivingEntity.getLevel()).sendParticles(new DustParticleOptions(new Vector3f(Vec3.fromRGB24(16711680)), 1.0F), pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), 20,0.25D, 0.25D, 0.25D, 0.0D);
                    ((ServerLevel)pLivingEntity.getLevel()).sendParticles(ParticleTypes.LANDING_LAVA, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), 20,0.25D, 0.25D, 0.25D, 0.0D);
                }
            } else {
                pLivingEntity.hurt(ModDamageSource.bleed(), 1F);

                ((ServerLevel)pLivingEntity.getLevel()).sendParticles(new DustParticleOptions(new Vector3f(Vec3.fromRGB24(16711680)), 1.0F), pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), 20,0.25D, 0.25D, 0.25D, 0.0D);
                ((ServerLevel)pLivingEntity.getLevel()).sendParticles(ParticleTypes.LANDING_LAVA, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), 20,0.25D, 0.25D, 0.25D, 0.0D);
            }
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
        int k = 20 >> pAmplifier;
        if (k > 0) {
            return pDuration % k == 0;
        } else {
            return true;
        }
    }
}
