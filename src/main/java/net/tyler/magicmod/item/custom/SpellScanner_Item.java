package net.tyler.magicmod.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.tyler.magicmod.capability.casting.PlayerCastingProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpellScanner_Item extends Item {

    public SpellScanner_Item(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (!level.isClientSide() && player instanceof ServerPlayer player1) {
            if(Screen.hasShiftDown()) {
                player1.getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(cast -> {
                    player.sendSystemMessage(Component.literal("Flare Blitz Cast: " + cast.getFlareBlitzCasting()).withStyle(ChatFormatting.YELLOW));
                    player.sendSystemMessage(Component.literal("Scorching Ray Cast: " + cast.getScorchingRayCasting()).withStyle(ChatFormatting.YELLOW));
                    player.sendSystemMessage(Component.literal("Scorching Ray Projectiles: " + cast.getScorchingRayProjectiles()).withStyle(ChatFormatting.YELLOW));
                    player.sendSystemMessage(Component.literal("Fiery Soul Cast: " + cast.getFierySoulCasting()).withStyle(ChatFormatting.YELLOW));
                    player.sendSystemMessage(Component.literal("Super Critical Cast: " + cast.getSuperCriticalCasting()).withStyle(ChatFormatting.YELLOW));
                    player.sendSystemMessage(Component.literal("Aquamarine Blessing Cast: " + cast.getAquamarineBlessingCasting()).withStyle(ChatFormatting.YELLOW));
                    player.sendSystemMessage(Component.literal("Shark Lunge Cast: " + cast.getSharkLungeCasting()).withStyle(ChatFormatting.YELLOW));
                    player.sendSystemMessage(Component.literal("Amphibious Cast: " + cast.getAmphibiousCasting()).withStyle(ChatFormatting.YELLOW));
                    player.sendSystemMessage(Component.literal("Air Darts Cast: " + cast.getAirDartsCasting()).withStyle(ChatFormatting.YELLOW));
                    player.sendSystemMessage(Component.literal("Air Darts Projectile: " + cast.getAirDartsProjectiles()).withStyle(ChatFormatting.YELLOW));
                    player.sendSystemMessage(Component.literal("Wings of Quartz Cast: " + cast.getWingsOfQuartzCasting()).withStyle(ChatFormatting.YELLOW));
                });
            } else {
                Vec3 start = player.getEyePosition();
                Vec3 addition = player.getLookAngle().scale(10.0D);
                EntityHitResult result = ProjectileUtil.getEntityHitResult(
                        player.level,
                        player,
                        start,
                        start.add(addition),
                        player.getBoundingBox().inflate(10.0D),
                        (val) -> true);

                if (result.getEntity() != null && result.getEntity() instanceof Player player2) {
                    player2.getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(cast2 -> {
                        player.sendSystemMessage(Component.literal("Flare Blitz Cast: " + cast2.getFlareBlitzCasting()).withStyle(ChatFormatting.YELLOW));
                        player.sendSystemMessage(Component.literal("Scorching Ray Cast: " + cast2.getScorchingRayCasting()).withStyle(ChatFormatting.YELLOW));
                        player.sendSystemMessage(Component.literal("Scorching Ray Projectiles: " + cast2.getScorchingRayProjectiles()).withStyle(ChatFormatting.YELLOW));
                        player.sendSystemMessage(Component.literal("Fiery Soul Cast: " + cast2.getFierySoulCasting()).withStyle(ChatFormatting.YELLOW));
                        player.sendSystemMessage(Component.literal("Super Critical Cast: " + cast2.getSuperCriticalCasting()).withStyle(ChatFormatting.YELLOW));
                        player.sendSystemMessage(Component.literal("Aquamarine Blessing Cast: " + cast2.getAquamarineBlessingCasting()).withStyle(ChatFormatting.YELLOW));
                        player.sendSystemMessage(Component.literal("Shark Lunge Cast: " + cast2.getSharkLungeCasting()).withStyle(ChatFormatting.YELLOW));
                        player.sendSystemMessage(Component.literal("Amphibious Cast: " + cast2.getAmphibiousCasting()).withStyle(ChatFormatting.YELLOW));
                        player.sendSystemMessage(Component.literal("Air Darts Cast: " + cast2.getAirDartsCasting()).withStyle(ChatFormatting.YELLOW));
                        player.sendSystemMessage(Component.literal("Air Darts Projectile: " + cast2.getAirDartsProjectiles()).withStyle(ChatFormatting.YELLOW));
                        player.sendSystemMessage(Component.literal("Wings of Quartz Cast: " + cast2.getWingsOfQuartzCasting()).withStyle(ChatFormatting.YELLOW));
                    });
                }
            }
        }

        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        components.add(Component.literal("Dev Item\nHold SHIFT to use this on yourself!").withStyle(ChatFormatting.GREEN));

        super.appendHoverText(stack, level, components, flag);
    }
}
