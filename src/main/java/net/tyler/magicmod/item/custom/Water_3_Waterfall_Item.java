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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tyler.magicmod.capability.info.PlayerInfoProvider;
import net.tyler.magicmod.capability.mana.PlayerManaProvider;
import net.tyler.magicmod.entity.custom.FireballProjectileEntity;
import net.tyler.magicmod.entity.custom.WaterfallProjectileEntity;
import net.tyler.magicmod.item.ModItems;
import net.tyler.magicmod.networking.ModMessages;
import net.tyler.magicmod.networking.packet.ManaDataSyncS2CPacket;
import net.tyler.magicmod.sound.ModSounds;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Water_3_Waterfall_Item extends Item {
    private int manaCost = 85;
    private double speed = 1.5D;

    public Water_3_Waterfall_Item(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
            player.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info -> {
                if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
                    if (info.getWater()) {
                        if (info.getSchoolLevel() >= 3) {
                            if (mana.getMana() >= manaCost) {
                                mana.subMana(manaCost);
                                ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana(), mana.getMaxMana()), (ServerPlayer) player);

                                player.level.playSound(null, player, SoundEvents.BOAT_PADDLE_WATER, SoundSource.PLAYERS, 5.0F, 0.6F + (player.getLevel().random.nextFloat() * 0.4F));

                                WaterfallProjectileEntity waterfall = new WaterfallProjectileEntity(player, player.level);
                                waterfall.setItem(ModItems.WATERFALL_PROJECTILE.get().getDefaultInstance());
                                waterfall.setDeltaMovement(player.getLookAngle().x*speed, player.getLookAngle().y*speed, player.getLookAngle().z*speed);
                                player.level.addFreshEntity(waterfall);

                                player.level.playSound(null, player, ModSounds.WATERFALL.get(), SoundSource.PLAYERS, 1.0F, 1);

                                player.getCooldowns().addCooldown(this, 2400);
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

        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            components.add(Component.literal("Mana Cost: 85\nCooldown Time: 2 minutes\nDamage: 25\nSlowness II Duration: 20 seconds\n\nRight click to launch a massive torrent of water!\nThe Waterfall pushes everything out of its way and restricts movement!").withStyle(ChatFormatting.BLUE));
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.YELLOW));
        }


        super.appendHoverText(stack, level, components, flag);
    }
}
