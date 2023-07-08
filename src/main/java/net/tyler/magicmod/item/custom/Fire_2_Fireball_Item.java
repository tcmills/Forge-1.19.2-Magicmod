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
import net.tyler.magicmod.capability.casting.PlayerCastingProvider;
import net.tyler.magicmod.capability.info.PlayerInfoProvider;
import net.tyler.magicmod.capability.mana.PlayerManaProvider;
import net.tyler.magicmod.entity.custom.FireballProjectileEntity;
import net.tyler.magicmod.entity.custom.MagicMissileProjectileEntity;
import net.tyler.magicmod.item.ModItems;
import net.tyler.magicmod.networking.ModMessages;
import net.tyler.magicmod.networking.packet.ManaDataSyncS2CPacket;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Fire_2_Fireball_Item extends Item {
    private int manaCost = 50;
    private int speed = 3;

    public Fire_2_Fireball_Item(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
            player.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info -> {
                player.getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(cast -> {
                    if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
                        if (info.getFire()) {
                            if (info.getSchoolLevel() >= 2) {
                                if (mana.getMana() >= manaCost) {
                                    mana.subMana(manaCost);
                                    ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana(), mana.getMaxMana()), (ServerPlayer) player);

                                    player.level.playSound(null, player, SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 2.0F, 1F / (player.getLevel().random.nextFloat() * 0.2F + 0.9F));

                                    FireballProjectileEntity fireball = new FireballProjectileEntity(player, player.level);
                                    fireball.setItem(ModItems.FIREBALL_PROJECTILE.get().getDefaultInstance());
                                    fireball.setDeltaMovement(player.getLookAngle().x*speed, player.getLookAngle().y*speed, player.getLookAngle().z*speed);
                                    player.level.addFreshEntity(fireball);

                                    player.getCooldowns().addCooldown(this, 1200);
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

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            components.add(Component.literal("Mana Cost: ?\nCooldown Time: ? seconds\nDamage: ?\n\nRight click to launch a fiery bolt!\nThe bolt creates a large explosion of fire on impact!").withStyle(ChatFormatting.RED));
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.YELLOW));
        }


        super.appendHoverText(stack, level, components, flag);
    }
}
