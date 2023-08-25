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

    public static final RegistryObject<MobEffect> SPELL_STRENGTH = MOB_EFFECTS.register("spell_strength",
            () -> new SpellStrengthEffect(MobEffectCategory.BENEFICIAL, 0xff239193));

    public static final RegistryObject<MobEffect> SPELL_STRENGTH_2 = MOB_EFFECTS.register("spell_strength_2",
            () -> new SpellStrength2Effect(MobEffectCategory.BENEFICIAL, 0xff239193));

    public static final RegistryObject<MobEffect> SPELL_WEAKNESS = MOB_EFFECTS.register("spell_weakness",
            () -> new SpellWeaknessEffect(MobEffectCategory.HARMFUL, 0xff4d484d));

    public static final RegistryObject<MobEffect> SPELL_WEAKNESS_2 = MOB_EFFECTS.register("spell_weakness_2",
            () -> new SpellWeaknessEffect(MobEffectCategory.HARMFUL, 0xff4d484d));

    public static final RegistryObject<MobEffect> MELTDOWN = MOB_EFFECTS.register("meltdown",
            () -> new MeltdownEffect(MobEffectCategory.BENEFICIAL, 0xffcf210c));

    public static final RegistryObject<MobEffect> BLEED = MOB_EFFECTS.register("bleed",
            () -> new BleedEffect(MobEffectCategory.HARMFUL, 0xff8a0303));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
