package net.tyler.magicmod.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties MANA_DUST =
            (new FoodProperties.Builder()).alwaysEat().nutrition(0).saturationMod(0f).effect(new MobEffectInstance(MobEffects.CONFUSION, 240, 0), 1.0f).build();
}
