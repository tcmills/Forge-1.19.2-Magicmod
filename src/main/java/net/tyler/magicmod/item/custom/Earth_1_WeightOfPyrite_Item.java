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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
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
import net.tyler.magicmod.networking.packet.ManaDataSyncS2CPacket;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Earth_1_WeightOfPyrite_Item extends Item {

    private int manaCost = 15;

    public Earth_1_WeightOfPyrite_Item(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
            player.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info -> {
                player.getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(cast -> {
                    if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
                        if (info.getEarth()) {
                            if (mana.getMana() >= manaCost) {

                                Vec3 start = player.getEyePosition();
                                Vec3 addition = player.getLookAngle().scale(10.0D);
                                EntityHitResult result = ProjectileUtil.getEntityHitResult(
                                        player.level,
                                        player,
                                        start,
                                        start.add(addition),
                                        player.getBoundingBox().inflate(10.0D),
                                        (val) -> true);

                                if (result.getEntity() != null && result.getEntity() instanceof LivingEntity entity1 && player.hasLineOfSight(entity1)) {
                                    if (entity1 instanceof Player player2) {
                                        player2.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info2 -> {
                                            if (!info.getDungeonParty() || !info2.getDungeonParty()) {
                                                mana.subMana(manaCost);
                                                ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana(), mana.getMaxMana()), (ServerPlayer) player);

                                                player2.addEffect(new MobEffectInstance(MobEffects.POISON, 160, 0, false, true, true));
                                                player2.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 160, 1, false, true, true));
                                                player2.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 160, 1, false, true, true));


                                                player.level.playSound(null, player2, SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 1.0F, player.getLevel().random.nextFloat() * 0.5F);

                                                ((ServerLevel)player.getLevel()).sendParticles(ParticleTypes.LANDING_HONEY, player2.getX(), player2.getEyeY() + 0.5, player2.getZ(), 30,0.5D, 0.5D, 0.15D, 0.0D);
                                            }
                                        });
                                    } else {
                                        mana.subMana(manaCost);
                                        ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana(), mana.getMaxMana()), (ServerPlayer) player);

                                        entity1.addEffect(new MobEffectInstance(MobEffects.POISON, 160, 0, false, true, true));
                                        entity1.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 160, 1, false, true, true));
                                        entity1.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 160, 1, false, true, true));

                                        player.level.playSound(null, entity1, SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 1.0F, player.getLevel().random.nextFloat() * 0.5F);

                                        ((ServerLevel)player.getLevel()).sendParticles(ParticleTypes.LANDING_HONEY, entity1.getX(), entity1.getEyeY() + 0.5, entity1.getZ(), 30,0.5D, 0.15D, 0.5D, 0.0D);
                                    }

                                    player.getCooldowns().addCooldown(this, 900);
                                }

                            } else {
                                player.sendSystemMessage(Component.literal("Not enough mana!").withStyle(ChatFormatting.DARK_AQUA));
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

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            components.add(Component.literal("Mana Cost: 15\nCooldown Time: 45 seconds\nRange: 10 blocks\n\nRight click to begin crushing your target!").withStyle(ChatFormatting.GRAY));
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.YELLOW));
        }


        super.appendHoverText(stack, level, components, flag);
    }
}
