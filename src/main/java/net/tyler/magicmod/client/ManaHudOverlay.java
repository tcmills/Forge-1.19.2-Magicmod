package net.tyler.magicmod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.tyler.magicmod.MagicMod;

public class ManaHudOverlay {
    private static final ResourceLocation FILLED_MANA = new ResourceLocation(MagicMod.MOD_ID,
            "textures/mana/filled_mana.png");
    private static final ResourceLocation HALF_MANA = new ResourceLocation(MagicMod.MOD_ID,
            "textures/mana/half_mana.png");
    private static final ResourceLocation EMPTY_MANA = new ResourceLocation(MagicMod.MOD_ID,
            "textures/mana/empty_mana.png");

    public static final IGuiOverlay HUD_MANA = ((gui, poseStack, partialTick, screenWidth, screenHeight) -> {
        int x = screenWidth / 2;
        int y = screenHeight;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, EMPTY_MANA);
        for (int i = 0; i < ClientManaData.getPlayerMaxMana() / 10; i++) {
            GuiComponent.blit(poseStack, x - 95 + (i * 8), y - 73, 0, 0, 16, 16, 16, 16);
        }

        RenderSystem.setShaderTexture(0, FILLED_MANA);
        for (int i = 0; i < ClientManaData.getPlayerMaxMana() / 10; i++) {
            if(Math.floor(ClientManaData.getPlayerMana() / 10) > i) {
                GuiComponent.blit(poseStack, x - 95 + (i * 8), y - 73, 0, 0, 16, 16, 16, 16);
            }
        }

        RenderSystem.setShaderTexture(0, HALF_MANA);
        if (ClientManaData.getPlayerMana() % 10 != 0) {
            GuiComponent.blit(poseStack, x - 95 + ((int)(Math.ceil(ClientManaData.getPlayerMana() / 10)) * 8), y - 73, 0, 0, 16, 16, 16, 16);
        }

    });

}
