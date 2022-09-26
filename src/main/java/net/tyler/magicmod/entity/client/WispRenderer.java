package net.tyler.magicmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.tyler.magicmod.MagicMod;
import net.tyler.magicmod.entity.custom.WispEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class WispRenderer extends GeoEntityRenderer<WispEntity> {
    public WispRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new WispModel());
    }

    @Override
    public ResourceLocation getTextureLocation(WispEntity instance) {
        return new ResourceLocation(MagicMod.MOD_ID, "textures/entity/wisp_texture.png");
    }

    @Override
    public RenderType getRenderType(WispEntity animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer,
                                    @Nullable VertexConsumer vertexBuilder,
                                    int packedLightIn, ResourceLocation textureLocation) {
        stack.scale(0.8f,0.8f,0.8f);
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
