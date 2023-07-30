package net.tyler.magicmod.event;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tyler.magicmod.MagicMod;
import net.tyler.magicmod.block.entity.ModBlockEntities;
import net.tyler.magicmod.block.entity.renderer.ManaDistillerBlockEntityRenderer;
import net.tyler.magicmod.client.InfoHudOverlay;
import net.tyler.magicmod.client.ManaHudOverlay;
import net.tyler.magicmod.entity.ModEntityTypes;

@Mod.EventBusSubscriber(modid = MagicMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @Mod.EventBusSubscriber(modid = MagicMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerBelowAll("mana", ManaHudOverlay.HUD_MANA);
            event.registerAboveAll("info", InfoHudOverlay.HUD_INFO);
        }

        @SubscribeEvent
        public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.MANA_DISTILLER.get(),
                    ManaDistillerBlockEntityRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.MAGIC_MISSILE_PROJECTILE.get(),
                    ThrownItemRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.SCORCHING_RAY_PROJECTILE.get(),
                    ThrownItemRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.FIREBALL_PROJECTILE.get(),
                    ThrownItemRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.WATERFALL_PROJECTILE.get(),
                    ThrownItemRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.AIR_DART_PROJECTILE.get(),
                    ThrownItemRenderer::new);
        }
    }

}
