package net.tyler.magicmod.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tyler.magicmod.MagicMod;
import net.tyler.magicmod.entity.custom.MagicMissileProjectileEntity;
import net.tyler.magicmod.entity.custom.WispEntity;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MagicMod.MOD_ID);

    public static final RegistryObject<EntityType<MagicMissileProjectileEntity>> MAGIC_MISSILE_PROJECTILE = ENTITY_TYPES.register("magic_missile_projectile",
        () -> EntityType.Builder.<MagicMissileProjectileEntity>of(MagicMissileProjectileEntity::new, MobCategory.MISC)
                .sized(0.25f, 0.25f)
                .build(new ResourceLocation(MagicMod.MOD_ID, "magic_missile_projectile").toString()));
    public static final RegistryObject<EntityType<WispEntity>> WISP = ENTITY_TYPES.register("wisp",
            () -> EntityType.Builder.of(WispEntity::new, MobCategory.MONSTER)
                    .sized(0.4f, 0.4f)
                    .build(new ResourceLocation(MagicMod.MOD_ID, "wisp").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
