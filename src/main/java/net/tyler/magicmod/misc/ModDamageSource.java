package net.tyler.magicmod.misc;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class ModDamageSource extends DamageSource {

    public ModDamageSource(String pMessageId) {
        super(pMessageId);
    }

    public static DamageSource magicMissile(Entity pSource, @Nullable Entity pIndirectEntity) {
        return (new IndirectEntityDamageSource("magicMissile", pSource, pIndirectEntity)).setProjectile();
    }

    public static DamageSource scorchingRay(Entity pSource, @Nullable Entity pIndirectEntity) {
        return (new IndirectEntityDamageSource("scorchingRay", pSource, pIndirectEntity)).setProjectile();
    }

    public static DamageSource superCritical(Entity pSource, @Nullable Entity pIndirectEntity) {
        return (new IndirectEntityDamageSource("superCritical", pSource, pIndirectEntity));
    }

}
