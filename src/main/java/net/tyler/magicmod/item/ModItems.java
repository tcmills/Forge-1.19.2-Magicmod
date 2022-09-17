package net.tyler.magicmod.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tyler.magicmod.MagicMod;
import net.tyler.magicmod.item.custom.*;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MagicMod.MOD_ID);

    public static final RegistryObject<Item> MANA_DUST = ITEMS.register("mana_dust",
            () -> new ManaDust_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB)));
    public static final RegistryObject<Item> MANA_CRYSTAL = ITEMS.register("mana_crystal",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB)));
    public static final RegistryObject<Item> MANA_POTION = ITEMS.register("mana_potion",
            () -> new ManaPotion_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> WISP_CORE = ITEMS.register("wisp_core",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB)));
    public static final RegistryObject<Item> ARCANE_POWDER = ITEMS.register("arcane_powder",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB)));
    public static final RegistryObject<Item> COINS = ITEMS.register("coins",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB)));

    public static final RegistryObject<Item> MAGIC_MISSILE = ITEMS.register("magic_missile",
            () -> new Neutral_1_MagicMissile_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> AID = ITEMS.register("aid",
            () -> new Neutral_1_Aid_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> TELEPORT = ITEMS.register("teleport",
            () -> new Neutral_1_Teleport_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> MAGIC_MISSILE_PROJECTILE = ITEMS.register("magic_missile_projectile",
            () -> new Item(new Item.Properties().stacksTo(1)));


    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
