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
            if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND && mana.getMana() >= 15) {
                mana.subMana(15);
                ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana(), mana.getMaxMana()), (ServerPlayer) player);

                player.level.playSound(null, player, SoundEvents.BEACON_POWER_SELECT, SoundSource.PLAYERS, 1f, 1f);

                player.heal(7.0f);

                player.getCooldowns().addCooldown(this, 160);

                if (level instanceof ServerLevel) {
                    for (int i = 0; i < 360; i++) {
                        if (i % 20 == 0) {
                            ((ServerLevel)level).sendParticles(ParticleTypes.HEART, (double)player.getX() + Math.cos(i), (double)player.getY() + 1.0D, (double)player.getZ() + Math.sin(i), 1,0.0D, 0.0D, 0.0D, 0.0D);
                        }
                    }
                }
            }
        });

        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            components.add(Component.literal("Right click to heal!").withStyle(ChatFormatting.AQUA));
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.YELLOW));
        }


        super.appendHoverText(stack, level, components, flag);
    }
}
