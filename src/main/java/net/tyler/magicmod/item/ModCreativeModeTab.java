package net.tyler.magicmod.item;

import com.google.common.collect.Ordering;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.tyler.magicmod.block.ModBlocks;

import java.util.Arrays;
import java.util.List;

public class ModCreativeModeTab {
    public static final CreativeModeTab MAGIC_TAB = new CreativeModeTab("magictab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.MANA_DUST.get());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> pItems) {
            super.fillItemList(pItems);

            List<Item> order = Arrays.asList(ModBlocks.MANA_CRYSTAL_ORE.get().asItem(), ModBlocks.DEEPSLATE_MANA_CRYSTAL_ORE.get().asItem(), ModBlocks.MANA_CRYSTAL_BLOCK.get().asItem(), ModBlocks.MANA_CRYSTAL_LAMP.get().asItem(), ModBlocks.MANA_DISTILLER.get().asItem(), ModBlocks.JUMPY_BLOCK.get().asItem(), ModBlocks.DUNGEON_BLOCK.get().asItem(), ModBlocks.DUNGEON2_BLOCK.get().asItem(), ModBlocks.UNOBTAINABLE.get().asItem(),
                    ModItems.MANA_DUST.get(), ModItems.MANA_CRYSTAL.get(), ModItems.MANA_POTION.get(), ModItems.STAR_FRUIT.get(), ModItems.MANA_FRUIT.get(), ModItems.WISP_CORE.get(), ModItems.ARCANE_POWDER.get(), ModItems.COINS.get(),
                    ModItems.FURTHER_STUDIES.get(), ModItems.SET_HOME_CHARGE.get(), ModItems.FIRE_GEM.get(), ModItems.WATER_GEM.get(), ModItems.EARTH_GEM.get(), ModItems.AIR_GEM.get(), ModItems.SUMMONING_GEM.get(), ModItems.FORGE_GEM.get(), ModItems.STORM_GEM.get(), ModItems.ENDER_GEM.get(), ModItems.LIFE_GEM.get(), ModItems.DEATH_GEM.get(), ModItems.SUN_GEM.get(), ModItems.MOON_GEM.get(), ModItems.CLEAR_GEM.get(),
                    ModItems.MAGIC_MISSILE.get(), ModItems.AID.get(), ModItems.TELEPORT.get(), ModItems.TELEPORT_HOME.get(),
                    ModItems.FLARE_BLITZ.get(), ModItems.SCORCHING_RAY.get(), ModItems.FIREBALL.get(), ModItems.FIERY_SOUL.get(), ModItems.SUPER_CRITICAL.get(), ModItems.AQUAMARINE_BLESSING.get(), ModItems.MAGICAL_ANGLER.get(), ModItems.SHARK_LUNGE.get(), ModItems.AMPHIBIOUS.get(),
                    ModItems.WISP_SPAWN_EGG.get(), ModItems.DUNGEON_BELL.get(), ModItems.MANA_MITIGATOR.get());
            Ordering<ItemStack> tabSorter = Ordering.explicit(order).onResultOf(ItemStack::getItem);
            pItems.sort(tabSorter);
        }
    };

}
