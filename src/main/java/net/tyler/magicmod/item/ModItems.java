package net.tyler.magicmod.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tyler.magicmod.MagicMod;
import net.tyler.magicmod.item.custom.Neutral_1_MagicMissile_Item;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MagicMod.MOD_ID);

    public static final RegistryObject<Item> MANA_DUST = ITEMS.register("mana_dust",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).food(ModFoods.MANA_DUST)));
    public static final RegistryObject<Item> MANA_CRYSTAL = ITEMS.register("mana_crystal",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB)));

    public static final RegistryObject<Item> MAGIC_MISSILE = ITEMS.register("magic_missile",
            () -> new Neutral_1_MagicMissile_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
