package net.tyler.magicmod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.tyler.magicmod.MagicMod;

public class InfoHudOverlay {
    private static final ResourceLocation FIRE_1 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/fire_one.png");
    private static final ResourceLocation FIRE_2 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/fire_two.png");
    private static final ResourceLocation FIRE_3 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/fire_three.png");
    private static final ResourceLocation WATER_1 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/water_one.png");
    private static final ResourceLocation WATER_2 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/water_two.png");
    private static final ResourceLocation WATER_3 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/water_three.png");
    private static final ResourceLocation EARTH_1 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/earth_one.png");
    private static final ResourceLocation EARTH_2 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/earth_two.png");
    private static final ResourceLocation EARTH_3 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/earth_three.png");
    private static final ResourceLocation AIR_1 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/air_one.png");
    private static final ResourceLocation AIR_2 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/air_two.png");
    private static final ResourceLocation AIR_3 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/air_three.png");
    private static final ResourceLocation SUMMONING_1 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/summoning_one.png");
    private static final ResourceLocation SUMMONING_2 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/summoning_two.png");
    private static final ResourceLocation SUMMONING_3 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/summoning_three.png");
    private static final ResourceLocation FORGE_1 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/forge_one.png");
    private static final ResourceLocation FORGE_2 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/forge_two.png");
    private static final ResourceLocation FORGE_3 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/forge_three.png");
    private static final ResourceLocation STORM_1 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/storm_one.png");
    private static final ResourceLocation STORM_2 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/storm_two.png");
    private static final ResourceLocation STORM_3 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/storm_three.png");
    private static final ResourceLocation ENDER_1 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/ender_one.png");
    private static final ResourceLocation ENDER_2 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/ender_two.png");
    private static final ResourceLocation ENDER_3 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/ender_three.png");
    private static final ResourceLocation LIFE_1 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/life_one.png");
    private static final ResourceLocation LIFE_2 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/life_two.png");
    private static final ResourceLocation LIFE_3 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/life_three.png");
    private static final ResourceLocation DEATH_1 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/death_one.png");
    private static final ResourceLocation DEATH_2 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/death_two.png");
    private static final ResourceLocation DEATH_3 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/death_three.png");
    private static final ResourceLocation SUN_1 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/sun_one.png");
    private static final ResourceLocation SUN_2 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/sun_two.png");
    private static final ResourceLocation SUN_3 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/sun_three.png");
    private static final ResourceLocation MOON_1 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/moon_one.png");
    private static final ResourceLocation MOON_2 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/moon_two.png");
    private static final ResourceLocation MOON_3 = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/moon_three.png");
    private static final ResourceLocation DUNGEON_PARTY = new ResourceLocation(MagicMod.MOD_ID,
            "textures/info/dungeon_party.png");

    public static final IGuiOverlay HUD_INFO = ((gui, poseStack, partialTick, screenWidth, screenHeight) -> {
        int x = screenWidth / 2;
        int y = screenHeight;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        if (ClientInfoData.getPlayerFire() && ClientInfoData.getPlayerSchoolLevel() == 1) {
            RenderSystem.setShaderTexture(0, FIRE_1);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if  (ClientInfoData.getPlayerFire() && ClientInfoData.getPlayerSchoolLevel() == 2) {
            RenderSystem.setShaderTexture(0, FIRE_2);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if  (ClientInfoData.getPlayerFire() && ClientInfoData.getPlayerSchoolLevel() == 3) {
            RenderSystem.setShaderTexture(0, FIRE_3);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if (ClientInfoData.getPlayerWater() && ClientInfoData.getPlayerSchoolLevel() == 1) {
            RenderSystem.setShaderTexture(0, WATER_1);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if  (ClientInfoData.getPlayerWater() && ClientInfoData.getPlayerSchoolLevel() == 2) {
            RenderSystem.setShaderTexture(0, WATER_2);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if  (ClientInfoData.getPlayerWater() && ClientInfoData.getPlayerSchoolLevel() == 3) {
            RenderSystem.setShaderTexture(0, WATER_3);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if (ClientInfoData.getPlayerEarth() && ClientInfoData.getPlayerSchoolLevel() == 1) {
            RenderSystem.setShaderTexture(0, EARTH_1);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if  (ClientInfoData.getPlayerEarth() && ClientInfoData.getPlayerSchoolLevel() == 2) {
            RenderSystem.setShaderTexture(0, EARTH_2);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if  (ClientInfoData.getPlayerEarth() && ClientInfoData.getPlayerSchoolLevel() == 3) {
            RenderSystem.setShaderTexture(0, EARTH_3);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if (ClientInfoData.getPlayerAir() && ClientInfoData.getPlayerSchoolLevel() == 1) {
            RenderSystem.setShaderTexture(0, AIR_1);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if  (ClientInfoData.getPlayerAir() && ClientInfoData.getPlayerSchoolLevel() == 2) {
            RenderSystem.setShaderTexture(0, AIR_2);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if  (ClientInfoData.getPlayerAir() && ClientInfoData.getPlayerSchoolLevel() == 3) {
            RenderSystem.setShaderTexture(0, AIR_3);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if (ClientInfoData.getPlayerSummoning() && ClientInfoData.getPlayerSchoolLevel() == 1) {
            RenderSystem.setShaderTexture(0, SUMMONING_1);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if  (ClientInfoData.getPlayerSummoning() && ClientInfoData.getPlayerSchoolLevel() == 2) {
            RenderSystem.setShaderTexture(0, SUMMONING_2);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if  (ClientInfoData.getPlayerSummoning() && ClientInfoData.getPlayerSchoolLevel() == 3) {
            RenderSystem.setShaderTexture(0, SUMMONING_3);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if (ClientInfoData.getPlayerForge() && ClientInfoData.getPlayerSchoolLevel() == 1) {
            RenderSystem.setShaderTexture(0, FORGE_1);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if  (ClientInfoData.getPlayerForge() && ClientInfoData.getPlayerSchoolLevel() == 2) {
            RenderSystem.setShaderTexture(0, FORGE_2);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if  (ClientInfoData.getPlayerForge() && ClientInfoData.getPlayerSchoolLevel() == 3) {
            RenderSystem.setShaderTexture(0, FORGE_3);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if (ClientInfoData.getPlayerStorm() && ClientInfoData.getPlayerSchoolLevel() == 1) {
            RenderSystem.setShaderTexture(0, STORM_1);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if  (ClientInfoData.getPlayerStorm() && ClientInfoData.getPlayerSchoolLevel() == 2) {
            RenderSystem.setShaderTexture(0, STORM_2);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if  (ClientInfoData.getPlayerStorm() && ClientInfoData.getPlayerSchoolLevel() == 3) {
            RenderSystem.setShaderTexture(0, STORM_3);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if (ClientInfoData.getPlayerEnder() && ClientInfoData.getPlayerSchoolLevel() == 1) {
            RenderSystem.setShaderTexture(0, ENDER_1);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if  (ClientInfoData.getPlayerEnder() && ClientInfoData.getPlayerSchoolLevel() == 2) {
            RenderSystem.setShaderTexture(0, ENDER_2);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if  (ClientInfoData.getPlayerEnder() && ClientInfoData.getPlayerSchoolLevel() == 3) {
            RenderSystem.setShaderTexture(0, ENDER_3);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if (ClientInfoData.getPlayerLife() && ClientInfoData.getPlayerSchoolLevel() == 1) {
            RenderSystem.setShaderTexture(0, LIFE_1);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if  (ClientInfoData.getPlayerLife() && ClientInfoData.getPlayerSchoolLevel() == 2) {
            RenderSystem.setShaderTexture(0, LIFE_2);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if  (ClientInfoData.getPlayerLife() && ClientInfoData.getPlayerSchoolLevel() == 3) {
            RenderSystem.setShaderTexture(0, LIFE_3);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if (ClientInfoData.getPlayerDeath() && ClientInfoData.getPlayerSchoolLevel() == 1) {
            RenderSystem.setShaderTexture(0, DEATH_1);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if  (ClientInfoData.getPlayerDeath() && ClientInfoData.getPlayerSchoolLevel() == 2) {
            RenderSystem.setShaderTexture(0, DEATH_2);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if  (ClientInfoData.getPlayerDeath() && ClientInfoData.getPlayerSchoolLevel() == 3) {
            RenderSystem.setShaderTexture(0, DEATH_3);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if (ClientInfoData.getPlayerSun() && ClientInfoData.getPlayerSchoolLevel() == 1) {
            RenderSystem.setShaderTexture(0, SUN_1);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if  (ClientInfoData.getPlayerSun() && ClientInfoData.getPlayerSchoolLevel() == 2) {
            RenderSystem.setShaderTexture(0, SUN_2);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if  (ClientInfoData.getPlayerSun() && ClientInfoData.getPlayerSchoolLevel() == 3) {
            RenderSystem.setShaderTexture(0, SUN_3);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if (ClientInfoData.getPlayerMoon() && ClientInfoData.getPlayerSchoolLevel() == 1) {
            RenderSystem.setShaderTexture(0, MOON_1);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if  (ClientInfoData.getPlayerMoon() && ClientInfoData.getPlayerSchoolLevel() == 2) {
            RenderSystem.setShaderTexture(0, MOON_2);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }
        else if  (ClientInfoData.getPlayerMoon() && ClientInfoData.getPlayerSchoolLevel() == 3) {
            RenderSystem.setShaderTexture(0, MOON_3);
            GuiComponent.blit(poseStack, x - 238, y - 250, 0, 0, 18, 18, 18, 18);
        }

        if (ClientInfoData.getPlayerDungeonParty()) {
            RenderSystem.setShaderTexture(0, DUNGEON_PARTY);
            GuiComponent.blit(poseStack, x - 238, y - 230, 0, 0, 18, 18, 18, 18);
        }

    });

}
