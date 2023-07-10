package net.tyler.magicmod.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.tyler.magicmod.capability.casting.PlayerCastingProvider;
import net.tyler.magicmod.capability.info.PlayerInfoProvider;
import net.tyler.magicmod.capability.mana.PlayerManaProvider;
import net.tyler.magicmod.effect.ModEffects;
import net.tyler.magicmod.entity.custom.FireballProjectileEntity;
import net.tyler.magicmod.item.ModItems;
import net.tyler.magicmod.misc.MagicalExplosion;
import net.tyler.magicmod.networking.ModMessages;
import net.tyler.magicmod.networking.packet.ManaDataSyncS2CPacket;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Fire_3_SuperCritical_Item extends Item {
    private int manaCost = 75;

    public Fire_3_SuperCritical_Item(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
            player.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info -> {
                player.getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(cast -> {
                    if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
                        if (info.getFire()) {
                            if (info.getSchoolLevel() >= 3) {
                                if (!player.hasEffect(ModEffects.MELTDOWN.get())) {
                                    if (mana.getMana() >= manaCost) {
                                        mana.subMana(manaCost);
                                        ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana(), mana.getMaxMana()), (ServerPlayer) player);

                                        cast.setSuperCriticalCasting(true);

                                        player.addEffect(new MobEffectInstance(ModEffects.MELTDOWN.get(), 1200, 0, false, false, true));

                                        player.level.playSound(null, player, SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 4.0F, 1F / (player.getLevel().random.nextFloat() * 0.2F + 0.9F));
                                    } else {
                                        player.sendSystemMessage(Component.literal("Not enough mana!").withStyle(ChatFormatting.DARK_AQUA));
                                    }
                                } else {
                                    cast.setSuperCriticalCasting(false);

                                    player.removeEffect(ModEffects.MELTDOWN.get());

                                    MagicalExplosion explosion = new MagicalExplosion(player.getLevel(), player, "superCritical", (ExplosionDamageCalculator)null, player.getX(), player.getY()+1, player.getZ(), 6F, 30D, true, Explosion.BlockInteraction.NONE);
                                    if (!net.minecraftforge.event.ForgeEventFactory.onExplosionStart(player.getLevel(), explosion)) {
                                        explosion.explode();

                                        player.getLevel().playSound(null, player, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.PLAYERS, 4.0F, 1F / (player.getLevel().random.nextFloat() * 0.5F + 1.4F));
                                        player.getLevel().playSound(null, player, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.PLAYERS, 4.0F, 1F / (player.getLevel().random.nextFloat() * 0.5F + 1.4F));
                                        player.getLevel().playSound(null, player, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.PLAYERS, 4.0F, 1F / (player.getLevel().random.nextFloat() * 0.5F + 1.4F));

                                        ((ServerLevel)player.getLevel()).sendParticles(ParticleTypes.EXPLOSION, player.getX(), player.getY(), player.getZ(), 10,2.0D, 2.0D, 2.0D, 1.0D);
                                    }

                                    player.getCooldowns().addCooldown(this, 9600);
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

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            components.add(Component.literal("Mana Cost: 75\nCooldown Time: 8 minutes\nAura Damage: 2 per second\nExplosion Damage: 30\n\nRight click to start a Meltdown!\nRight click again to go Super Critical and release all your flames at once!").withStyle(ChatFormatting.RED));
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.YELLOW));
        }


        super.appendHoverText(stack, level, components, flag);
    }
}
