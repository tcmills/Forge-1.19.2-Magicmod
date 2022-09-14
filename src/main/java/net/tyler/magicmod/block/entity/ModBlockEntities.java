package net.tyler.magicmod.block.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tyler.magicmod.MagicMod;
import net.tyler.magicmod.block.ModBlocks;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MagicMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<ManaDistillerBlockEntity>> MANA_DISTILLER =
            BLOCK_ENTITIES.register("mana_distiller", () ->
                    BlockEntityType.Builder.of(ManaDistillerBlockEntity::new,
                            ModBlocks.MANA_DISTILLER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
