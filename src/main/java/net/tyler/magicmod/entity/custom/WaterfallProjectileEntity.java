package net.tyler.magicmod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.tyler.magicmod.capability.info.PlayerInfoProvider;
import net.tyler.magicmod.effect.ModEffects;
import net.tyler.magicmod.entity.ModEntityTypes;
import net.tyler.magicmod.item.ModItems;
import net.tyler.magicmod.misc.MagicalExplosion;
import net.tyler.magicmod.misc.ModDamageSource;
import net.tyler.magicmod.sound.ModSounds;

public class WaterfallProjectileEntity extends ThrowableItemProjectile {

    // Three constructors, also make sure not to miss this line when altering it for copy-pasting
    public WaterfallProjectileEntity(EntityType<WaterfallProjectileEntity> type, Level world) {
        super(type, world);
        this.setNoGravity(true);
    }

    public WaterfallProjectileEntity(LivingEntity entity, Level world) {
        super(ModEntityTypes.WATERFALL_PROJECTILE.get(), entity, world);
        this.setNoGravity(true);
    }

    public WaterfallProjectileEntity(double x, double y, double z, Level world) {
        super(ModEntityTypes.WATERFALL_PROJECTILE.get(), x, y, z, world);
        this.setNoGravity(true);
    }

    // Get the item that the projectile is thrown from, blocks require ".asItem()" as well
    @Override
    protected Item getDefaultItem() {
        return ModItems.WATERFALL_PROJECTILE.get().asItem();
    }

    // Spawns the entity, just as important as the above method
    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();

        Vec3 vec3 = this.getDeltaMovement();
        double d2 = this.getX() - vec3.x;
        double d0 = this.getY() - vec3.y;
        double d1 = this.getZ() - vec3.z;
        float f = 1.0F/0.99F;
        if (this.isInWater()) {
            f = 1.0F/0.8F;
        }

        this.setDeltaMovement(vec3.scale((double)f));

        for (int i = 0; i < 6; i++) {
            this.level.addParticle(this.getTrailParticle(), d2 + ((this.level.random.nextDouble() - this.level.random.nextDouble()) / 2D), d0 + ((this.level.random.nextDouble() - this.level.random.nextDouble()) / 2D), d1 + ((this.level.random.nextDouble() - this.level.random.nextDouble()) / 2D), 0.0D, 0.0D, 0.0D);
        }
        this.level.addParticle(ParticleTypes.CLOUD, d2 + ((this.level.random.nextDouble() - this.level.random.nextDouble()) / 2D), d0 + ((this.level.random.nextDouble() - this.level.random.nextDouble()) / 2D), d1 + ((this.level.random.nextDouble() - this.level.random.nextDouble()) / 2D), 0.0D, 0.0D, 0.0D);

        if (!level.isClientSide) {
            if (this.level.random.nextInt(20) == 0) {
                this.playSound(ModSounds.WATERFALL.get(), 1.0F, 1.0F);
            }
        }

        if (this.getY() > (double)(this.level.getMaxBuildHeight() + 64)) {
            this.outOfWorld();
        }
    }

    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.DRIPPING_DRIPSTONE_WATER;
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

                                    player2.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 1, false, true, true));

                                    double d0 = (this.getBoundingBox().minX + this.getBoundingBox().maxX) / 2.0D;
                                    double d1 = (this.getBoundingBox().minZ + this.getBoundingBox().maxZ) / 2.0D;

                                    double d2 = player2.getX() - d0;
                                    double d3 = player2.getZ() - d1;
                                    double d4 = Math.max(d2 * d2 + d3 * d3, 0.1D);
                                    player2.push(d2 / d4 * 8.0D, (double)0.8F, d3 / d4 * 8.0D);

//                                    double d5 = player2.getX() - this.getX();
//                                    double d7 = player2.getEyeY() - this.getY();
//                                    double d9 = player2.getZ() - this.getZ();
//                                    double d13 = Math.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
//                                    if (d13 != 0.0D) {
//                                        d5 /= d13;
//                                        d7 /= d13;
//                                        d9 /= d13;
//                                        double d10 = 10.0D;
//
//                                        player2.setDeltaMovement(player2.getDeltaMovement().add(d5 * d10, d7 * d10, d9 * d10));
//                                    }
                                }
                            });
                        });
                    } else {
                        damage(player1, entity1);

                        entity1.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 1, false, true, true));

                        double d0 = (this.getBoundingBox().minX + this.getBoundingBox().maxX) / 2.0D;
                        double d1 = (this.getBoundingBox().minZ + this.getBoundingBox().maxZ) / 2.0D;

                        double d2 = entity1.getX() - d0;
                        double d3 = entity1.getZ() - d1;
                        double d4 = Math.max(d2 * d2 + d3 * d3, 0.1D);
                        entity1.push(d2 / d4 * 4.0D, (double)0.4F, d3 / d4 * 4.0D);

//                        double d5 = entity1.getX() - this.getX();
//                        double d7 = entity1.getEyeY() - this.getY();
//                        double d9 = entity1.getZ() - this.getZ();
//                        double d13 = Math.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
//                        if (d13 != 0.0D) {
//                            d5 /= d13;
//                            d7 /= d13;
//                            d9 /= d13;
//                            double d10 = 1.0D;
//
//                            entity1.setDeltaMovement(entity1.getDeltaMovement().add(d5 * d10, d7 * d10, d9 * d10));
//                        }
                    }
                }
            }

            if (!level.isClientSide) {
                this.playSound((SoundEvent) SoundEvents.GENERIC_SPLASH, 5.0F, 0.6F + (this.random.nextFloat() * 0.4F));
            }
        }
        //Just like before this checks the result and if it hits a block this code will run
        if (result.getType() == HitResult.Type.BLOCK) {
            if (!level.isClientSide) {
                this.playSound((SoundEvent) SoundEvents.GENERIC_SPLASH, 5.0F, 0.6F + (this.random.nextFloat() * 0.4F));
                this.discard();
            }
        }
    }

    private void damage(Player player, LivingEntity entity) {

        float num = 25F;

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
        entity.hurt(ModDamageSource.waterfall(this, this.getOwner()), Math.max(num, 0F));
    }
}