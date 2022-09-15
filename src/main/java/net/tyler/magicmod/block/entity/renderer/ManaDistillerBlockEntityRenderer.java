package net.tyler.magicmod.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemStack;
import net.tyler.magicmod.block.custom.ManaDistillerBlock;
import net.tyler.magicmod.block.entity.ManaDistillerBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.LightTexture;

public class ManaDistillerBlockEntityRenderer implements BlockEntityRenderer<ManaDistillerBlockEntity> {

    public ManaDistillerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(ManaDistillerBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
                       MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        ItemStack itemStack = pBlockEntity.getRenderStack();
        pPoseStack.pushPose();

        switch (pBlockEntity.getBlockState().getValue(ManaDistillerBlock.FACING)) {
            case NORTH -> pPoseStack.translate(0.515625f, 0.546875f, 0.109375f);
            case EAST -> pPoseStack.translate(0.890625f, 0.546875f, 0.515625f);
            case SOUTH -> pPoseStack.translate(0.484375f, 0.546875f, 0.890625f);
            case WEST -> pPoseStack.translate(0.109375f, 0.546875f, 0.484375f);
        }

        pPoseStack.scale(0.5f, 0.5f, 0.5f);

        switch (pBlockEntity.getBlockState().getValue(ManaDistillerBlock.FACING)) {
            case NORTH -> pPoseStack.mulPose(Vector3f.YP.rotationDegrees(180));
            case EAST -> pPoseStack.mulPose(Vector3f.YP.rotationDegrees(90));
            case SOUTH -> pPoseStack.mulPose(Vector3f.YP.rotationDegrees(0));
            case WEST -> pPoseStack.mulPose(Vector3f.YP.rotationDegrees(270));
        }

        itemRenderer.renderStatic(itemStack, ItemTransforms.TransformType.GUI, getLightLevel(pBlockEntity.getLevel(),
                        pBlockEntity.getBlockPos()),
                OverlayTexture.NO_OVERLAY, pPoseStack, pBufferSource, 1);
        pPoseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
