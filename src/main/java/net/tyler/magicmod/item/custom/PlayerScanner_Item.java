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
import net.tyler.magicmod.capability.info.PlayerInfoProvider;
import net.tyler.magicmod.capability.mana.PlayerManaProvider;
import net.tyler.magicmod.networking.ModMessages;
import net.tyler.magicmod.networking.packet.InfoDataSyncS2CPacket;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PlayerScanner_Item extends Item {

    public PlayerScanner_Item(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (!level.isClientSide() && player instanceof ServerPlayer player1) {
            if(Screen.hasShiftDown()) {
                player1.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
                    player1.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info -> {
                        player.sendSystemMessage(Component.literal("Name: " + player.getName().getString()).withStyle(ChatFormatting.YELLOW));
                        player.sendSystemMessage(Component.literal("Experience Level: " + player.experienceLevel).withStyle(ChatFormatting.YELLOW));
                        player.sendSystemMessage(Component.literal("Mana: " + mana.getMana()).withStyle(ChatFormatting.YELLOW));
                        player.sendSystemMessage(Component.literal("Max Mana: " + mana.getMaxMana()).withStyle(ChatFormatting.YELLOW));
                        if (info.getFire()) {
                            player.sendSystemMessage(Component.literal("School: Fire " + info.getSchoolLevel()).withStyle(ChatFormatting.YELLOW));
                        } else if (info.getWater()) {
                            player.sendSystemMessage(Component.literal("School: Water " + info.getSchoolLevel()).withStyle(ChatFormatting.YELLOW));
                        } else if (info.getAir()) {
                            player.sendSystemMessage(Component.literal("School: Air " + info.getSchoolLevel()).withStyle(ChatFormatting.YELLOW));
                        } else if (info.getEarth()) {
                            player.sendSystemMessage(Component.literal("School: Earth " + info.getSchoolLevel()).withStyle(ChatFormatting.YELLOW));
                        } else if (info.getStorm()) {
                            player.sendSystemMessage(Component.literal("School: Storm " + info.getSchoolLevel()).withStyle(ChatFormatting.YELLOW));
                        } else if (info.getForge()) {
                            player.sendSystemMessage(Component.literal("School: Forge " + info.getSchoolLevel()).withStyle(ChatFormatting.YELLOW));
                        } else if (info.getSummoning()) {
                            player.sendSystemMessage(Component.literal("School: Summoning " + info.getSchoolLevel()).withStyle(ChatFormatting.YELLOW));
                        } else if (info.getEnder()) {
                            player.sendSystemMessage(Component.literal("School: Ender " + info.getSchoolLevel()).withStyle(ChatFormatting.YELLOW));
                        } else if (info.getSun()) {
                            player.sendSystemMessage(Component.literal("School: Sun " + info.getSchoolLevel()).withStyle(ChatFormatting.YELLOW));
                        } else if (info.getMoon()) {
                            player.sendSystemMessage(Component.literal("School: Moon " + info.getSchoolLevel()).withStyle(ChatFormatting.YELLOW));
                        } else if (info.getLife()) {
                            player.sendSystemMessage(Component.literal("School: Life " + info.getSchoolLevel()).withStyle(ChatFormatting.YELLOW));
                        } else if (info.getDeath()) {
                            player.sendSystemMessage(Component.literal("School: Death " + info.getSchoolLevel()).withStyle(ChatFormatting.YELLOW));
                        } else {
                            player.sendSystemMessage(Component.literal("School: None").withStyle(ChatFormatting.YELLOW));
                        }

                        player.sendSystemMessage(Component.literal("Dungeon Party: " + info.getDungeonParty()).withStyle(ChatFormatting.YELLOW));
                    });
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
                    player2.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana2 -> {
                        player2.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info2 -> {
                            player.sendSystemMessage(Component.literal("Name: " + player2.getName().getString()).withStyle(ChatFormatting.YELLOW));
                            player.sendSystemMessage(Component.literal("Experience Level: " + player2.experienceLevel).withStyle(ChatFormatting.YELLOW));
                            player.sendSystemMessage(Component.literal("Mana: " + mana2.getMana()).withStyle(ChatFormatting.YELLOW));
                            player.sendSystemMessage(Component.literal("Max Mana: " + mana2.getMaxMana()).withStyle(ChatFormatting.YELLOW));
                            if (info2.getFire()) {
                                player.sendSystemMessage(Component.literal("School: Fire " + info2.getSchoolLevel()).withStyle(ChatFormatting.YELLOW));
                            } else if (info2.getWater()) {
                                player.sendSystemMessage(Component.literal("School: Water " + info2.getSchoolLevel()).withStyle(ChatFormatting.YELLOW));
                            } else if (info2.getAir()) {
                                player.sendSystemMessage(Component.literal("School: Air " + info2.getSchoolLevel()).withStyle(ChatFormatting.YELLOW));
                            } else if (info2.getEarth()) {
                                player.sendSystemMessage(Component.literal("School: Earth " + info2.getSchoolLevel()).withStyle(ChatFormatting.YELLOW));
                            } else if (info2.getStorm()) {
                                player.sendSystemMessage(Component.literal("School: Storm " + info2.getSchoolLevel()).withStyle(ChatFormatting.YELLOW));
                            } else if (info2.getForge()) {
                                player.sendSystemMessage(Component.literal("School: Forge " + info2.getSchoolLevel()).withStyle(ChatFormatting.YELLOW));
                            } else if (info2.getSummoning()) {
                                player.sendSystemMessage(Component.literal("School: Summoning " + info2.getSchoolLevel()).withStyle(ChatFormatting.YELLOW));
                            } else if (info2.getEnder()) {
                                player.sendSystemMessage(Component.literal("School: Ender " + info2.getSchoolLevel()).withStyle(ChatFormatting.YELLOW));
                            } else if (info2.getSun()) {
                                player.sendSystemMessage(Component.literal("School: Sun " + info2.getSchoolLevel()).withStyle(ChatFormatting.YELLOW));
                            } else if (info2.getMoon()) {
                                player.sendSystemMessage(Component.literal("School: Moon " + info2.getSchoolLevel()).withStyle(ChatFormatting.YELLOW));
                            } else if (info2.getLife()) {
                                player.sendSystemMessage(Component.literal("School: Life " + info2.getSchoolLevel()).withStyle(ChatFormatting.YELLOW));
                            } else if (info2.getDeath()) {
                                player.sendSystemMessage(Component.literal("School: Death " + info2.getSchoolLevel()).withStyle(ChatFormatting.YELLOW));
                            } else {
                                player.sendSystemMessage(Component.literal("School: None ").withStyle(ChatFormatting.YELLOW));
                            }

                            player.sendSystemMessage(Component.literal("Dungeon Party: " + info2.getDungeonParty()).withStyle(ChatFormatting.YELLOW));
                        });
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
