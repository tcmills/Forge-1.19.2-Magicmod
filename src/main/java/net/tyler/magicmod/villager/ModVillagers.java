package net.tyler.magicmod.villager;

import com.google.common.collect.ImmutableSet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tyler.magicmod.MagicMod;
import net.tyler.magicmod.block.ModBlocks;

import java.lang.reflect.InvocationTargetException;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(ForgeRegistries.POI_TYPES, MagicMod.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, MagicMod.MOD_ID);

    public static final RegistryObject<PoiType> UNOBTAINABLE_POI = POI_TYPES.register("unobtainable_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.UNOBTAINABLE.get().getStateDefinition().getPossibleStates()),
                    1, 1));
    public static final RegistryObject<VillagerProfession> ARCANIST = VILLAGER_PROFESSIONS.register("arcanist",
            () -> new VillagerProfession("arcanist", x -> x.get() == UNOBTAINABLE_POI.get(),
                    x -> x.get() == UNOBTAINABLE_POI.get(), ImmutableSet.of(), ImmutableSet.of(),
                    SoundEvents.VILLAGER_WORK_CLERIC));

    public static void registerPOIs() {
        try {
            ObfuscationReflectionHelper.findMethod(PoiType.class,
                    "registerBlockStates", PoiType.class).invoke(null, UNOBTAINABLE_POI.get());
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
