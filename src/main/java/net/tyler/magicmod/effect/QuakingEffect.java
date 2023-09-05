package net.tyler.magicmod.effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tyler.magicmod.capability.info.PlayerInfoProvider;
import net.tyler.magicmod.misc.ModDamageSource;
import net.tyler.magicmod.sound.ModSounds;

import java.util.ArrayList;
import java.util.List;

public class QuakingEffect extends MobEffect {
    protected QuakingEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {

        if (!pLivingEntity.level.isClientSide() && pLivingEntity.isOnGround()) {
            float f2 = 6F;
            int k1 = Mth.floor(pLivingEntity.getX() - (double)f2 - 1.0D);
            int l1 = Mth.floor(pLivingEntity.getX() + (double)f2 + 1.0D);
            int i2 = Mth.floor(pLivingEntity.getY() - (double)f2 - 1.0D);
            int i1 = Mth.floor(pLivingEntity.getY() + (double)f2 + 1.0D);
            int j2 = Mth.floor(pLivingEntity.getZ() - (double)f2 - 1.0D);
            int j1 = Mth.floor(pLivingEntity.getZ() + (double)f2 + 1.0D);
            List<Entity> list = pLivingEntity.level.getEntities(pLivingEntity, new AABB((double)k1, (double)i2, (double)j2, (double)l1, (double)i1, (double)j1));
            Vec3 vec3 = new Vec3(pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ());

            for(int k2 = 0; k2 < list.size(); ++k2) {
                Entity entity = list.get(k2);
                if (entity instanceof LivingEntity entity1) {
                    if (pLivingEntity instanceof Player player1) {
                        double d12 = Math.sqrt(entity1.distanceToSqr(vec3)) / (double)f2;
                        if (d12 <= 1.0D) {
                            if (entity1 instanceof Player player2) {
                                player1.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info1 -> {
                                    player2.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info2 -> {
                                        if (!info1.getDungeonParty() || !info2.getDungeonParty()) {
                                            player2.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2, false, true, true));
                                            damage(player1, player2);
                                        }
                                    });
                                });
                            } else {
                                entity1.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2, false, true, true));
                                damage(player1, entity1);
                            }
                        }
                    }
                }

            }

            if (pLivingEntity.getLevel().random.nextInt(3) == 0) {
                pLivingEntity.getLevel().playSound(null, pLivingEntity, ModSounds.EARTHQUAKE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }

            ((ServerLevel)pLivingEntity.getLevel()).sendParticles(ParticleTypes.MYCELIUM, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), 40,3.0D, 0.5D, 3.0D, 0.0D);
            ((ServerLevel)pLivingEntity.getLevel()).sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), 20,3.0D, 0.5D, 3.0D, 0.0D);


        }

        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    private void damage(Player player, LivingEntity entity) {

        float num = 3F;

        if (player.hasEffect(ModEffects.SPELL_STRENGTH_2.get())) {
            num += 2F;
        } else if (player.hasEffect(ModEffects.SPELL_STRENGTH.get())) {
            num += 1F;
        }

        if (player.hasEffect(ModEffects.SPELL_WEAKNESS_2.get())) {
            num -= 3F;
        } else if (player.hasEffect(ModEffects.SPELL_WEAKNESS.get())) {
            num -= 2F;
        }

        //player1.sendSystemMessage(Component.literal(Math.max(num, 0F) + ""));
        entity.hurt(ModDamageSource.earthquake(), Math.max(num, 0F));
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
