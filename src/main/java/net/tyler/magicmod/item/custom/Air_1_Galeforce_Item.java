package net.tyler.magicmod.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.tyler.magicmod.capability.casting.PlayerCastingProvider;
import net.tyler.magicmod.capability.info.PlayerInfoProvider;
import net.tyler.magicmod.capability.mana.PlayerManaProvider;
import net.tyler.magicmod.networking.ModMessages;
import net.tyler.magicmod.networking.packet.ManaDataSyncS2CPacket;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Air_1_Galeforce_Item extends Item {

    private int manaCost = 10;

    public Air_1_Galeforce_Item(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
            player.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info -> {
                player.getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(cast -> {
                    if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
                        if (info.getAir()) {
                            if (mana.getMana() >= manaCost) {
                                mana.subMana(manaCost);
                                ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana(), mana.getMaxMana()), (ServerPlayer) player);

                                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 240, 4, false, false, true));
                                player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 240, 0, false, false, true));

                                player.level.playSound(null, player, SoundEvents.AMBIENT_BASALT_DELTAS_MOOD, SoundSource.PLAYERS, 2.0F, 1.8F + (player.getLevel().random.nextFloat() * 0.2F));

                                for (int i = 0; i < 360; i++) {
                                    if (i % 10 == 0) {
                                        ((ServerLevel)level).sendParticles(ParticleTypes.CLOUD, (double)player.getX() + Math.cos(i), (double)player.getY() + 0.25, (double)player.getZ() + Math.sin(i), 1,0.0D, 0.0D, 0.0D, 0.0D);
                                    }
                                }

                                player.getCooldowns().addCooldown(this, 600);
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
            components.add(Component.literal("Mana Cost: 10\nCooldown Time: 30 seconds\nDuration: 12 seconds\n\nRight click to move like the wind!").withStyle(ChatFormatting.GREEN));
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.YELLOW));
        }


        super.appendHoverText(stack, level, components, flag);
    }
}
