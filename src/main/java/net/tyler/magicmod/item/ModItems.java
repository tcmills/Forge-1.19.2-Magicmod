package net.tyler.magicmod.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tyler.magicmod.MagicMod;
import net.tyler.magicmod.entity.ModEntityTypes;
import net.tyler.magicmod.item.custom.*;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MagicMod.MOD_ID);

    public static final RegistryObject<Item> STAR_FRUIT = ITEMS.register("star_fruit",
            () -> new StarFruit_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).food(ModFoods.STAR_FRUIT)));
    public static final RegistryObject<Item> MANA_FRUIT = ITEMS.register("mana_fruit",
            () -> new ManaFruit_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).food(ModFoods.MANA_FRUIT)));
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

    public static final RegistryObject<Item> FURTHER_STUDIES = ITEMS.register("further_studies",
            () -> new FurtherStudies_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> CLEAR_GEM = ITEMS.register("clear_gem",
            () -> new ClearGem_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> FIRE_GEM = ITEMS.register("fire_gem",
            () -> new FireGem_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> WATER_GEM = ITEMS.register("water_gem",
            () -> new WaterGem_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> EARTH_GEM = ITEMS.register("earth_gem",
            () -> new EarthGem_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> AIR_GEM = ITEMS.register("air_gem",
            () -> new AirGem_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> SUMMONING_GEM = ITEMS.register("summoning_gem",
            () -> new SummoningGem_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> FORGE_GEM = ITEMS.register("forge_gem",
            () -> new ForgeGem_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> STORM_GEM = ITEMS.register("storm_gem",
            () -> new StormGem_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> ENDER_GEM = ITEMS.register("ender_gem",
            () -> new EnderGem_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> LIFE_GEM = ITEMS.register("life_gem",
            () -> new LifeGem_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> DEATH_GEM = ITEMS.register("death_gem",
            () -> new DeathGem_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> SUN_GEM = ITEMS.register("sun_gem",
            () -> new SunGem_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> MOON_GEM = ITEMS.register("moon_gem",
            () -> new MoonGem_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));

    public static final RegistryObject<Item> DUNGEON_BELL = ITEMS.register("dungeon_bell",
            () -> new DungeonBell_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> MANA_MITIGATOR = ITEMS.register("mana_mitigator",
            () -> new ManaMitigator_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> PLAYER_SCANNER = ITEMS.register("player_scanner",
            () -> new PlayerScanner_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> INVENTORY_SCANNER = ITEMS.register("inventory_scanner",
            () -> new InventoryScanner_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> SPELL_SCANNER = ITEMS.register("spell_scanner",
            () -> new SpellScanner_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));

    public static final RegistryObject<Item> SET_HOME_CHARGE = ITEMS.register("set_home_charge",
            () -> new SetHomeCharge_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));

    public static final RegistryObject<Item> COINS = ITEMS.register("coins",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB)));

    public static final RegistryObject<Item> MAGIC_MISSILE = ITEMS.register("magic_missile",
            () -> new Neutral_1_MagicMissile_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> MAGIC_MISSILE_PROJECTILE = ITEMS.register("magic_missile_projectile",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> AID = ITEMS.register("aid",
            () -> new Neutral_1_Aid_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> TELEPORT = ITEMS.register("teleport",
            () -> new Neutral_1_Teleport_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> TELEPORT_HOME = ITEMS.register("teleport_home",
            () -> new Neutral_1_TeleportHome_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> FLARE_BLITZ = ITEMS.register("flare_blitz",
            () -> new Fire_1_FlareBlitz_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> SCORCHING_RAY = ITEMS.register("scorching_ray",
            () -> new Fire_1_ScorchingRay_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> SCORCHING_RAY_PROJECTILE = ITEMS.register("scorching_ray_projectile",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> FIREBALL = ITEMS.register("fireball",
            () -> new Fire_2_Fireball_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> FIREBALL_PROJECTILE = ITEMS.register("fireball_projectile",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> FIERY_SOUL = ITEMS.register("fiery_soul",
            () -> new Fire_2_FierySoul_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> SUPER_CRITICAL = ITEMS.register("super_critical",
            () -> new Fire_3_SuperCritical_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> AQUAMARINE_BLESSING = ITEMS.register("aquamarine_blessing",
            () -> new Water_1_AquamarineBlessing_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> MAGICAL_ANGLER = ITEMS.register("magical_angler",
            () -> new Water_1_MagicalAngler_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> SHARK_LUNGE = ITEMS.register("shark_lunge",
            () -> new Water_2_SharkLunge_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> AMPHIBIOUS = ITEMS.register("amphibious",
            () -> new Water_2_Amphibious_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> WATERFALL = ITEMS.register("waterfall",
            () -> new Water_3_Waterfall_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> WATERFALL_PROJECTILE = ITEMS.register("waterfall_projectile",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GALEFORCE = ITEMS.register("galeforce",
            () -> new Air_1_Galeforce_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> YEET = ITEMS.register("yeet",
            () -> new Air_1_Yeet_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> AIR_DARTS = ITEMS.register("air_darts",
            () -> new Air_1_AirDarts_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> AIR_DART = ITEMS.register("air_dart",
            () -> new AirDart_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(16)));
    public static final RegistryObject<Item> AIR_DART_PROJECTILE = ITEMS.register("air_dart_projectile",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> TOSS = ITEMS.register("toss",
            () -> new Air_2_Toss_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> WINGS_OF_QUARTZ = ITEMS.register("wings_of_quartz",
            () -> new Air_3_WingsOfQuartz_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> WEIGHT_OF_PYRITE = ITEMS.register("weight_of_pyrite",
            () -> new Earth_1_WeightOfPyrite_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> BURROW = ITEMS.register("burrow",
            () -> new Earth_1_Burrow_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> ROCK_FORM = ITEMS.register("rock_form",
            () -> new Earth_2_RockForm_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> ROCK_FIST = ITEMS.register("rock_fist",
            () -> new RockFist_Item(Tiers.STONE, 5, -3.2F, new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> ONE_WITH_STONE = ITEMS.register("one_with_stone",
            () -> new Earth_2_OneWithStone_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> EARTHQUAKE = ITEMS.register("earthquake",
            () -> new Earth_3_Earthquake_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> STORM_BRINGER = ITEMS.register("storm_bringer",
            () -> new Storm_1_StormBringer_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));
    public static final RegistryObject<Item> DISCHARGE = ITEMS.register("discharge",
            () -> new Storm_1_Discharge_Item(new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB).stacksTo(1)));

    public static final RegistryObject<Item> WISP_SPAWN_EGG = ITEMS.register("wisp_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.WISP, 0x0de4e4, 0xe495c8,
                    new Item.Properties().tab(ModCreativeModeTab.MAGIC_TAB)));


    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
