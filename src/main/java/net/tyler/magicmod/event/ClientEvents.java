package net.tyler.magicmod.event;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tyler.magicmod.MagicMod;
import net.tyler.magicmod.client.ManaHudOverlay;
import net.tyler.magicmod.entity.ModEntityTypes;
import net.tyler.magicmod.networking.ModMessages;
import net.tyler.magicmod.networking.packet.Add100ManaC2SPacket;
import net.tyler.magicmod.networking.packet.Add10MaxManaC2SPacket;
import net.tyler.magicmod.networking.packet.Set100MaxManaC2SPacket;
import net.tyler.magicmod.util.KeyBinding;

@Mod.EventBusSubscriber(modid = MagicMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @Mod.EventBusSubscriber(modid = MagicMod.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (KeyBinding.PRESS_J_KEY.consumeClick()) {
                ModMessages.sendToServer(new Set100MaxManaC2SPacket());
            }
        }
    }

    @Mod.EventBusSubscriber(modid = MagicMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.PRESS_J_KEY);
        }

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("mana", ManaHudOverlay.HUD_MANA);
        }
    }

    @SubscribeEvent
    public static void initRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.MAGIC_MISSILE.get(),
                ThrownItemRenderer::new);
    }

}
