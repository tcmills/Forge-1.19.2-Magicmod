package net.tyler.magicmod.entity.custom;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.network.NetworkHooks;
import net.tyler.magicmod.entity.ModEntityTypes;
import net.tyler.magicmod.capability.info.PlayerInfoProvider;
import net.tyler.magicmod.item.ModItems;

public class MagicMissileProjectileEntity extends ThrowableItemProjectile {

    private int baseDamage = 9;

    // Three constructors, also make sure not to miss this line when altering it for copy-pasting
    public MagicMissileProjectileEntity(EntityType<MagicMissileProjectileEntity> type, Level world) {
        super(type, world);
        this.setNoGravity(true);
    }

    public MagicMissileProjectileEntity(LivingEntity entity, Level world) {
        super(ModEntityTypes.MAGIC_MISSILE_PROJECTILE.get(), entity, world);
        this.setNoGravity(true);
    }

    public MagicMissileProjectileEntity(double x, double y, double z, Level world) {
        super(ModEntityTypes.MAGIC_MISSILE_PROJECTILE.get(), x, y, z, world);
        this.setNoGravity(true);
    }

    // Get the item that the projectile is thrown from, blocks require ".asItem()" as well
    @Override
    protected Item getDefaultItem() {
        return ModItems.MAGIC_MISSILE_PROJECTILE.get().asItem();
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
        this.level.addParticle(this.getTrailParticle(), d2, d0 + 0.125D, d1, 0.0D, 0.0D, 0.0D);

        if (this.getY() > (double)(this.level.getMaxBuildHeight() + 64)) {
            this.outOfWorld();
        }
    }

    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.DOLPHIN;
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

            if (entity instanceof Player player2 && this.getOwner() instanceof Player player1) {
                player1.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info1 -> {
                    player2.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info2 -> {
                        if (!info1.getDungeonParty() || !info2.getDungeonParty()) {
                            entity.hurt(DamageSource.thrown(this, this.getOwner()), baseDamage);
                        }
                    });
                });
            } else {
                entity.hurt(DamageSource.thrown(this, this.getOwner()), baseDamage);
            }

            if (!level.isClientSide) {
                this.playSound((SoundEvent)SoundEvents.FIREWORK_ROCKET_BLAST, 5.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
                this.discard();
            }
        }
        //Just like before this checks the result and if it hits a block this code will run
        if (result.getType() == HitResult.Type.BLOCK) {
            if (!level.isClientSide) {
                this.playSound((SoundEvent)SoundEvents.FIREWORK_ROCKET_BLAST, 5.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
                this.discard();
            }
        }
    }
}
