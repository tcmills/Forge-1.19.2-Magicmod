package net.tyler.magicmod.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.tyler.magicmod.capability.casting.PlayerCastingProvider;
import net.tyler.magicmod.capability.info.PlayerInfoProvider;
import net.tyler.magicmod.capability.mana.PlayerManaProvider;
import net.tyler.magicmod.effect.ModEffects;
import net.tyler.magicmod.networking.ModMessages;
import net.tyler.magicmod.networking.packet.ManaDataSyncS2CPacket;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Air_2_Toss_Item extends Item {

    private int manaCost = 20;

    public Air_2_Toss_Item(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
            player.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info -> {
                player.getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(cast -> {
                    if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
                        if (info.getAir()) {
                            if (info.getSchoolLevel() >= 2) {
                                if (mana.getMana() >= manaCost) {

                                    Vec3 start = player.getEyePosition();
                                    Vec3 addition = player.getLookAngle().scale(8.0D);
                                    EntityHitResult result = ProjectileUtil.getEntityHitResult(
                                            player.level,
                                            player,
                                            start,
                                            start.add(addition),
                                            player.getBoundingBox().inflate(8.0D),
                                            (val) -> true);

                                    if (result.getEntity() != null && result.getEntity() instanceof LivingEntity entity1 && player.hasLineOfSight(entity1)) {
                                        if (entity1 instanceof Player player2) {
                                            player2.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info2 -> {
                                                if (!info.getDungeonParty() || !info2.getDungeonParty()) {
                                                    mana.subMana(manaCost);
                                                    ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana(), mana.getMaxMana()), (ServerPlayer) player);

                                                    knockBack(player, player2);

                                                    player.level.playSound(null, player2, SoundEvents.AMBIENT_BASALT_DELTAS_MOOD, SoundSource.PLAYERS, 1.0F, 1.8F + (player.getLevel().random.nextFloat() * 0.2F));

                                                    ((ServerLevel)player.getLevel()).sendParticles(ParticleTypes.EXPLOSION, player2.getX(), player2.getY(), player2.getZ(), 3,1.0D, 1.0D, 1.0D, 0.1D);
                                                    ((ServerLevel)player.getLevel()).sendParticles(ParticleTypes.CLOUD, player2.getX(), player2.getY(), player2.getZ(), 5,1.0D, 1.0D, 1.0D, 0.1D);
                                                }
                                            });
                                        } else {
                                            mana.subMana(manaCost);
                                            ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana(), mana.getMaxMana()), (ServerPlayer) player);

                                            knockBack(player, entity1);

                                            player.level.playSound(null, entity1, SoundEvents.AMBIENT_BASALT_DELTAS_MOOD, SoundSource.PLAYERS, 3.0F, 1.8F + (player.getLevel().random.nextFloat() * 0.2F));

                                            ((ServerLevel)player.getLevel()).sendParticles(ParticleTypes.EXPLOSION, entity1.getX(), entity1.getY(), entity1.getZ(), 3,1.0D, 1.0D, 1.0D, 0.1D);
                                            ((ServerLevel)player.getLevel()).sendParticles(ParticleTypes.CLOUD, entity1.getX(), entity1.getY(), entity1.getZ(), 5,1.0D, 1.0D, 1.0D, 0.1D);
                                        }
                                    }

                                } else {
                                    player.sendSystemMessage(Component.literal("Not enough mana!").withStyle(ChatFormatting.DARK_AQUA));
                                }
                            } else {
                                player.sendSystemMessage(Component.literal("This spell is too complicated for you to cast!").withStyle(ChatFormatting.YELLOW));
                            }
                        } else {
                            player.sendSystemMessage(Component.literal("You don't understand the runes for this spell!").withStyle(ChatFormatting.YELLOW));
                        }
                    }
                });
            });
        });

        return super.use(level, player, hand);
    }

    private void knockBack(Player player, LivingEntity entity) {
        double d5 = entity.getX() - player.getX();
        double d7 = entity.getEyeY() - player.getY();
        double d9 = entity.getZ() - player.getZ();
        double d13 = Math.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
        if (d13 != 0.0D) {
            d5 /= d13;
            d7 /= d13;
            d9 /= d13;
            double d10 = 1.8D;

            entity.setDeltaMovement(entity.getDeltaMovement().add(d5, d10, d9));
        }

        player.getCooldowns().addCooldown(this, 600);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            components.add(Component.literal("Mana Cost: 20\nCooldown Time: 30 seconds\nRange: 8 blocks\n\nRight click to launch your target into the air!").withStyle(ChatFormatting.GREEN));
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.YELLOW));
        }


        super.appendHoverText(stack, level, components, flag);
    }
}
