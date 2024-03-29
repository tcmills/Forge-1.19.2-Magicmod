package net.tyler.magicmod;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tyler.magicmod.block.ModBlocks;
import net.tyler.magicmod.block.entity.ModBlockEntities;
import net.tyler.magicmod.effect.ModEffects;
import net.tyler.magicmod.entity.ModEntityTypes;
import net.tyler.magicmod.entity.client.WispRenderer;
import net.tyler.magicmod.event.loot.ModAdditionModifiers;
import net.tyler.magicmod.item.ModItems;
import net.tyler.magicmod.networking.ModMessages;
import net.tyler.magicmod.painting.ModPaintings;
import net.tyler.magicmod.screen.ManaDistillerScreen;
import net.tyler.magicmod.screen.ModMenuTypes;
import net.tyler.magicmod.sound.ModSounds;
import net.tyler.magicmod.villager.ModVillagers;
import org.slf4j.Logger;
import software.bernie.geckolib3.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MagicMod.MOD_ID)
public class MagicMod {
    public static final String MOD_ID = "magicmod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public MagicMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModVillagers.register(modEventBus);
        ModPaintings.register(modEventBus);
        ModEntityTypes.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModEffects.register(modEventBus);
        ModSounds.register(modEventBus);
        ModAdditionModifiers.LOOT_MODIFIER.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        GeckoLib.initialize();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModMessages.register();
            ModVillagers.registerPOIs();
        });
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenuTypes.MANA_DISTILLER_MENU.get(), ManaDistillerScreen::new);

            EntityRenderers.register(ModEntityTypes.WISP.get(), WispRenderer::new);
        }
    }
}
