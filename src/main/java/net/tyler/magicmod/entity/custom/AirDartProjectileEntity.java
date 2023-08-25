package net.tyler.magicmod.entity.custom;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.tyler.magicmod.capability.info.PlayerInfoProvider;
import net.tyler.magicmod.effect.ModEffects;
import net.tyler.magicmod.entity.ModEntityTypes;
import net.tyler.magicmod.item.ModItems;
import net.tyler.magicmod.misc.ModDamageSource;

public class AirDartProjectileEntity  extends ThrowableItemProjectile {

    public AirDartProjectileEntity(EntityType<AirDartProjectileEntity> type, Level level) {
        super(type, level);
        //this.setBoundingBox(type.getDimensions().makeBoundingBox(this.getX(), this.getY() + 0.5, this.getZ()));
    }

    public AirDartProjectileEntity(LivingEntity entity, Level world) {
        super(ModEntityTypes.AIR_DART_PROJECTILE.get(), entity, world);
    }

    public AirDartProjectileEntity(double x, double y, double z, Level world) {
        super(ModEntityTypes.AIR_DART_PROJECTILE.get(), x, y, z, world);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.AIR_DART.get();
    }

    // A method to do things on entity or block-hit
    @Override
    protected void onHit(HitResult result) {
        //This line is checking the type of RayTraceResult, in this case
        //it will be when it hits and entity
        if (result.getType() == HitResult.Type.ENTITY) {

            //This is a variable that we have set, it gets the entity from the RayTraceResult.
            //We cast it to EntityRayTraceResult, just to ensure that it is infact an entity.
            Entity entity = ((EntityHitResult) result).getEntity();

//            if(RunicAgesEntityTagGen.AIR_WEAKNESS.equals(entity.getType()) && !this.level.isClientSide())
//            {
//                entity.hurt(DamageSource.thrown(this, this.getOwner()), (float) (damage*1.5));
//            }
//            else entity.hurt(DamageSource.thrown(this, this.getOwner()), damage);

            if (entity instanceof LivingEntity entity1) {
                if (this.getOwner() instanceof Player player1) {
                    if (entity1 instanceof Player player2) {
                        player1.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info1 -> {
                            player2.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info2 -> {
                                if (!info1.getDungeonParty() || !info2.getDungeonParty()) {
                                    damage(player1, player2);
                                }
                            });
                        });
                    } else {
                        damage(player1, entity1);
                    }
                }
            }

            if (!level.isClientSide) {
                //this.playSound((SoundEvent) SoundEvents.FIREWORK_ROCKET_BLAST, 5.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
                this.discard();
            }
        }
        //Just like before this checks the result and if it hits a block this code will run
        if (result.getType() == HitResult.Type.BLOCK) {
            if (!level.isClientSide) {
                //this.playSound((SoundEvent)SoundEvents.FIREWORK_ROCKET_BLAST, 5.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
                this.discard();
            }
        }
    }

    private void damage(Player player, LivingEntity entity) {

        float num = 2F;

        if (player.hasEffect(ModEffects.SPELL_STRENGTH_2.get())) {
            num += 6F;
        } else if (player.hasEffect(ModEffects.SPELL_STRENGTH.get())) {
            num += 3F;
        }

        if (player.hasEffect(ModEffects.SPELL_WEAKNESS_2.get())) {
            num -= 8F;
        } else if (player.hasEffect(ModEffects.SPELL_WEAKNESS.get())) {
            num -= 4F;
        }

        //player1.sendSystemMessage(Component.literal(Math.max(num, 0F) + ""));
        entity.hurt(ModDamageSource.airDart(this, this.getOwner()), Math.max(num, 0F));

        if (player.getLevel().random.nextInt(19) == 0) {
            entity.addEffect(new MobEffectInstance(ModEffects.BLEED.get(), 100, 0, false, false, true));
        }

        entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 20, 0, false, true, true));
    }
}