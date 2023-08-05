package net.tyler.magicmod.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.tyler.magicmod.capability.info.PlayerInfoProvider;
import net.tyler.magicmod.networking.ModMessages;
import net.tyler.magicmod.networking.packet.InfoDataSyncS2CPacket;
import net.tyler.magicmod.networking.packet.ManaDataSyncS2CPacket;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DungeonBell_Item extends Item {

    public DungeonBell_Item(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (!level.isClientSide() && player instanceof ServerPlayer player1) {
            if(Screen.hasShiftDown()) {
                player1.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info -> {

                    if (info.getDungeonParty()) {
                        info.leaveDungeonParty();
                    }
                    else {
                        info.joinDungeonParty();
                    }

                    ModMessages.sendToPlayer(new InfoDataSyncS2CPacket(info.getSchoolLevel(), info.getFire(),
                            info.getWater(), info.getEarth(), info.getAir(), info.getSummoning(), info.getForge(),
                            info.getStorm(), info.getEnder(), info.getLife(), info.getDeath(), info.getSun(),
                            info.getMoon(), info.getDungeonParty()), player1);
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

                if (result.getEntity() != null && result.getEntity() instanceof ServerPlayer player2) {
                    player2.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info2 -> {
                        if (info2.getDungeonParty()) {
                            info2.leaveDungeonParty();
                            player.sendSystemMessage(Component.literal(player2.getName().getString() + " left Dungeon Party!").withStyle(ChatFormatting.YELLOW));
                        }
                        else {
                            info2.joinDungeonParty();
                            player.sendSystemMessage(Component.literal(player2.getName().getString() + " joined Dungeon Party!").withStyle(ChatFormatting.YELLOW));
                        }

                        ModMessages.sendToPlayer(new InfoDataSyncS2CPacket(info2.getSchoolLevel(), info2.getFire(),
                                info2.getWater(), info2.getEarth(), info2.getAir(), info2.getSummoning(), info2.getForge(),
                                info2.getStorm(), info2.getEnder(), info2.getLife(), info2.getDeath(), info2.getSun(),
                                info2.getMoon(), info2.getDungeonParty()), player2);
                    });
                }
            }

            player.level.playSound(null, player, SoundEvents.BELL_BLOCK, SoundSource.PLAYERS, 1f, 1.5f);
            player.level.playSound(null, player, SoundEvents.BELL_RESONATE, SoundSource.PLAYERS, 1f, 2f);
        }

        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        components.add(Component.literal("Dev Item\nHold SHIFT to use this on yourself!").withStyle(ChatFormatting.GREEN));

        super.appendHoverText(stack, level, components, flag);
    }
}
