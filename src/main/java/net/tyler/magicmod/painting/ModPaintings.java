package net.tyler.magicmod.painting;

import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tyler.magicmod.MagicMod;

public class ModPaintings {
    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS =
            DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, MagicMod.MOD_ID);

    public static final RegistryObject<PaintingVariant> RUSTY = PAINTING_VARIANTS.register("rusty",
            () -> new PaintingVariant(16, 32));
    public static final RegistryObject<PaintingVariant> HOGUN = PAINTING_VARIANTS.register("hogun",
            () -> new PaintingVariant(16, 32));
    public static final RegistryObject<PaintingVariant> MAGIC = PAINTING_VARIANTS.register("magic",
            () -> new PaintingVariant(16, 16));

    public static void register(IEventBus eventBus) {
        PAINTING_VARIANTS.register(eventBus);
    }
}
