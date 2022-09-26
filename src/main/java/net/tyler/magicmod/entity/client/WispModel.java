package net.tyler.magicmod.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.tyler.magicmod.MagicMod;
import net.tyler.magicmod.entity.custom.WispEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class WispModel extends AnimatedGeoModel<WispEntity> {
    @Override
    public ResourceLocation getModelResource(WispEntity object) {
        return new ResourceLocation(MagicMod.MOD_ID, "geo/wisp.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WispEntity object) {
        return new ResourceLocation(MagicMod.MOD_ID, "textures/entity/wisp_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WispEntity animatable) {
        return new ResourceLocation(MagicMod.MOD_ID, "animations/wisp.animation.json");
    }
}
