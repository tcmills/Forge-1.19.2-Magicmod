package net.tyler.magicmod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
import net.tyler.magicmod.entity.ModEntityTypes;
import net.tyler.magicmod.item.ModItems;
import net.tyler.magicmod.misc.MagicalExplosion;
import net.tyler.magicmod.misc.ModDamageSource;

public class FireballProjectileEntity extends ThrowableItemProjectile {

    // Three constructors, also make sure not to miss this line when altering it for copy-pasting
    public FireballProjectileEntity(EntityType<FireballProjectileEntity> type, Level world) {
        super(type, world);
        this.setNoGravity(true);
    }

    public FireballProjectileEntity(LivingEntity entity, Level world) {
        super(ModEntityTypes.FIREBALL_PROJECTILE.get(), entity, world);
        this.setNoGravity(true);
    }

    public FireballProjectileEntity(double x, double y, double z, Level world) {
        super(ModEntityTypes.FIREBALL_PROJECTILE.get(), x, y, z, world);
        this.setNoGravity(true);
    }

    // Get the item that the projectile is thrown from, blocks require ".asItem()" as well
    @Override
    protected Item getDefaultItem() {
        return ModItems.FIREBALL_PROJECTILE.get().asItem();
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
        this.level.addParticle(this.getTrailParticle(), d2, d0 + 0.125D, d1, 0D, 0D, 0D);

        if (this.getY() > (double)(this.level.getMaxBuildHeight() + 64)) {
            this.outOfWorld();
        }
    }

    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.FLAME;
    }

    // A method to do things on entity or block-hit
    @Override
    protected void onHit(HitResult result) {
        //This line is checking the type of RayTraceResult, in this case
        //it will be when it hits and entity

//            if(RunicAgesEntityTagGen.AIR_WEAKNESS.equals(entity.getType()) && !this.level.isClientSide())
//            {
//                entity.hurt(DamageSource.thrown(this, this.getOwner()), (float) (damage*1.5));
//            }
//            else entity.hurt(DamageSource.thrown(this, this.getOwner()), damage);

        if (!level.isClientSide && this.getOwner() instanceof ServerPlayer player) {

            MagicalExplosion explosion = new MagicalExplosion(this.getLevel(), player, "fireball", (ExplosionDamageCalculator)null, this.getX(), this.getY(), this.getZ(), 5F, 20D, true, Explosion.BlockInteraction.NONE);
            if (!net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.getLevel(), explosion)) {
                explosion.explode();

                this.playSound((SoundEvent) SoundEvents.DRAGON_FIREBALL_EXPLODE, 4.0F, 1F / (this.random.nextFloat() * 0.5F + 1.4F));
                this.playSound((SoundEvent) SoundEvents.DRAGON_FIREBALL_EXPLODE, 4.0F, 1F / (this.random.nextFloat() * 0.5F + 1.4F));
                this.playSound((SoundEvent) SoundEvents.DRAGON_FIREBALL_EXPLODE, 4.0F, 1F / (this.random.nextFloat() * 0.5F + 1.4F));

                ((ServerLevel)this.getLevel()).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 25,3.0D, 3.0D, 3.0D, 1.0D);
            }

            this.discard();
        }

    }
}