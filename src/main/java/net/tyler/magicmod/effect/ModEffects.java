package net.tyler.magicmod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tyler.magicmod.MagicMod;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MagicMod.MOD_ID);

    public static final RegistryObject<MobEffect> COMBAT = MOB_EFFECTS.register("combat",
            () -> new CombatEffect(MobEffectCategory.HARMFUL, 0xff990303));
    public static final RegistryObject<MobEffect> FLARE_BLITZ_EXPLOSION = MOB_EFFECTS.register("flare_blitz_explosion",
            () -> new FlareBlitzExplosionEffect(MobEffectCategory.NEUTRAL, 0xffdc3a17));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
