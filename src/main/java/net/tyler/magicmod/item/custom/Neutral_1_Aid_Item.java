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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tyler.magicmod.capability.mana.PlayerManaProvider;
import net.tyler.magicmod.effect.ModEffects;
import net.tyler.magicmod.misc.ModDamageSource;
import net.tyler.magicmod.networking.ModMessages;
import net.tyler.magicmod.networking.packet.ManaDataSyncS2CPacket;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Neutral_1_Aid_Item extends Item {

    public Neutral_1_Aid_Item(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
            if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
                if (mana.getMana() >= 15) {
                    mana.subMana(15);
                    ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana(), mana.getMaxMana()), (ServerPlayer) player);

                    player.level.playSound(null, player, SoundEvents.BEACON_POWER_SELECT, SoundSource.PLAYERS, 1f, 1f);

                    if (player.hasEffect(ModEffects.SPELL_STRENGTH_2.get())) {
                        player.heal(13.0f);
                        //player.sendSystemMessage(Component.literal("13 aid"));
                    } else if (player.hasEffect(ModEffects.SPELL_STRENGTH.get())) {
                        player.heal(10.0f);
                        //player.sendSystemMessage(Component.literal("10 aid"));
                    } else {
                        player.heal(7.0f);
                        //player.sendSystemMessage(Component.literal("7 aid"));
                    }

                    player.removeEffect(ModEffects.BLEED.get());

                    player.getCooldowns().addCooldown(this, 160);

                    for (int i = 0; i < 360; i++) {
                        if (i % 20 == 0) {
                            ((ServerLevel)level).sendParticles(ParticleTypes.HEART, (double)player.getX() + Math.cos(i), (double)player.getY() + 1.0D, (double)player.getZ() + Math.sin(i), 1,0.0D, 0.0D, 0.0D, 0.0D);
                        }
                    }
                } else {
                    player.sendSystemMessage(Component.literal("Not enough mana!").withStyle(ChatFormatting.DARK_AQUA));
                }
            }
        });

        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            components.add(Component.literal("Mana Cost: 15\nCooldown Time: 8 seconds\nHeal: 7\n\nRight click to heal yourself!").withStyle(ChatFormatting.YELLOW));
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.YELLOW));
        }


        super.appendHoverText(stack, level, components, flag);
    }
}
