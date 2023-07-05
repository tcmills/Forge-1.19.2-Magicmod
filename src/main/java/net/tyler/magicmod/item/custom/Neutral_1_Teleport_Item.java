package net.tyler.magicmod.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.tyler.magicmod.effect.ModEffects;
import net.tyler.magicmod.capability.info.PlayerInfoProvider;
import net.tyler.magicmod.capability.mana.PlayerManaProvider;
import net.tyler.magicmod.networking.ModMessages;
import net.tyler.magicmod.networking.packet.ManaDataSyncS2CPacket;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Neutral_1_Teleport_Item extends Item {

    private static final int USE_DURATION = 200;
    private boolean cast;

    public Neutral_1_Teleport_Item(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        super.finishUsingItem(stack, level, entity);

        Player player = entity instanceof Player ? (Player)entity : null;

        if (!level.isClientSide() && player instanceof ServerPlayer serverplayer) {
            serverplayer.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
                serverplayer.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info -> {
                    if (mana.getMana() >= 10 && !player.hasEffect(ModEffects.COMBAT.get()) && !info.getDungeonParty()) {
                        mana.subMana(10);
                        ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana(), mana.getMaxMana()), serverplayer);

                        serverplayer.teleportTo(0.0D, 0.0D, 0.0D);
                        serverplayer.resetFallDistance();

                        player.level.playSound(null, player, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1f, 1f);

                        serverplayer.getCooldowns().addCooldown(this, 3600);
                    }
                });
            });
        }

        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return USE_DURATION;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
            player.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info -> {
                if (mana.getMana() >= 10 && !player.hasEffect(ModEffects.COMBAT.get()) && !info.getDungeonParty()) {
                    cast = true;
                } else {
                    cast = false;
                }
            });
        });

        if (cast) {
            player.level.playSound(null, player, SoundEvents.BEACON_AMBIENT, SoundSource.PLAYERS, 1f, 1f);
            return ItemUtils.startUsingInstantly(level, player, hand);
        } else {
            return super.use(level, player, hand);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            components.add(Component.literal("Mana Cost: 10\nCast Time: 10 seconds\nCooldown Time: 180 seconds\n\nHold down right click to teleport to Spawn!").withStyle(ChatFormatting.YELLOW));
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.YELLOW));
        }


        super.appendHoverText(stack, level, components, flag);
    }
}
