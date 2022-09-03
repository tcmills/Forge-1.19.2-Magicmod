package net.tyler.magicmod.block;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tyler.magicmod.MagicMod;
import net.tyler.magicmod.block.custom.JumpyBlock;
import net.tyler.magicmod.block.custom.ManaCrystalLampBlock;
import net.tyler.magicmod.item.ModCreativeModeTab;
import net.tyler.magicmod.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, MagicMod.MOD_ID);

    public static final RegistryObject<Block> MANA_CRYSTAL_BLOCK = registerBlock("mana_crystal_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
            .strength(5f).requiresCorrectToolForDrops().sound(SoundType.AMETHYST).lightLevel(state -> 6)), ModCreativeModeTab.MAGIC_TAB);
    public static final RegistryObject<Block> MANA_CRYSTAL_ORE = registerBlock("mana_crystal_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
            .strength(3f).requiresCorrectToolForDrops(), UniformInt.of(3, 7)), ModCreativeModeTab.MAGIC_TAB);
    public static final RegistryObject<Block> DEEPSLATE_MANA_CRYSTAL_ORE = registerBlock("deepslate_mana_crystal_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(4.5f).requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE),
                    UniformInt.of(3, 7)), ModCreativeModeTab.MAGIC_TAB);

    public static final RegistryObject<Block> JUMPY_BLOCK = registerBlock("jumpy_block",
            () -> new JumpyBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(1.5f).requiresCorrectToolForDrops()), ModCreativeModeTab.MAGIC_TAB);
    public static final RegistryObject<Block> MANA_CRYSTAL_LAMP = registerBlock("mana_crystal_lamp",
            () -> new ManaCrystalLampBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(0.3f).sound(SoundType.GLASS).lightLevel(state -> state.getValue(ManaCrystalLampBlock.LIT) ? 0 : 15)), ModCreativeModeTab.MAGIC_TAB);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);

        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
