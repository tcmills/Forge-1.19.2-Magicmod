package net.tyler.magicmod.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
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
import net.tyler.magicmod.capability.casting.PlayerCastingProvider;
import net.tyler.magicmod.capability.info.PlayerInfoProvider;
import net.tyler.magicmod.capability.mana.PlayerManaProvider;
import net.tyler.magicmod.effect.ModEffects;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Water_1_AquamarineBlessing_Item extends Item {

    public Water_1_AquamarineBlessing_Item(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
            player.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info -> {
                player.getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(cast -> {
                    if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
                        if (info.getWater()) {
                            if (mana.getMana() >= 5) {
                                if (!cast.getAquamarineBlessingCasting()) {
                                    player.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 72000, 0, false, false, true));
                                    player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 72000, 0, false, false, true));
                                    cast.setAquamarineBlessingCasting(true);

                                    player.level.playSound(null, player, SoundEvents.GENERIC_SWIM, SoundSource.PLAYERS, 1.0F, 0.9F + player.getLevel().random.nextFloat());

                                } else {
                                    player.removeEffect(MobEffects.CONDUIT_POWER);
                                    player.removeEffect(MobEffects.DOLPHINS_GRACE);
                                    cast.setAquamarineBlessingCasting(false);
                                    cast.setAquamarineBlessingTick(0);

                                    player.level.playSound(null, player, SoundEvents.GENERIC_SWIM, SoundSource.PLAYERS, 1.0F, 0.9F - player.getLevel().random.nextFloat());

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
            components.add(Component.literal("Mana Cost: 5 per 30 seconds\n\nRight click to give yourself a blessing while underwater!\nRight click again to stop!").withStyle(ChatFormatting.BLUE));
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.YELLOW));
        }


        super.appendHoverText(stack, level, components, flag);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return super.isFoil(pStack) || pStack.hasTag();
    }
}
