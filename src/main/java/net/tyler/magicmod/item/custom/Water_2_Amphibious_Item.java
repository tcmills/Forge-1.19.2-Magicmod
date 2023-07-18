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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tyler.magicmod.capability.casting.PlayerCastingProvider;
import net.tyler.magicmod.capability.info.PlayerInfoProvider;
import net.tyler.magicmod.capability.mana.PlayerManaProvider;
import net.tyler.magicmod.effect.ModEffects;
import net.tyler.magicmod.misc.ModDamageSource;
import net.tyler.magicmod.networking.ModMessages;
import net.tyler.magicmod.networking.packet.ManaDataSyncS2CPacket;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Water_2_Amphibious_Item extends Item {

    public Water_2_Amphibious_Item(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
            player.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info -> {
                player.getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(cast -> {
                    if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
                        if (info.getWater()) {
                            if (info.getSchoolLevel() >= 2) {
                                if (mana.getMana() >= 5) {
                                    if (!cast.getAmphibiousCasting()) {

                                        cast.setAmphibiousCasting(true);

                                        player.level.playSound(null, player, SoundEvents.SLIME_ATTACK, SoundSource.PLAYERS, 1.0F, 0.8F + player.getLevel().random.nextFloat());

                                    } else {
                                        player.removeEffect(MobEffects.REGENERATION);
                                        player.removeEffect(ModEffects.SPELL_STRENGTH.get());
                                        player.removeEffect(ModEffects.SPELL_STRENGTH_2.get());

                                        cast.setAmphibiousCasting(false);
                                        cast.setAmphibiousTick(0);

                                        player.level.playSound(null, player, SoundEvents.SLIME_ATTACK, SoundSource.PLAYERS, 1.0F, 0.8F + player.getLevel().random.nextFloat());

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

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            components.add(Component.literal("Mana Cost: 5 per 5 seconds\n\nRight click to mend your flesh and strengthen your spells!\nRight click again to stop!").withStyle(ChatFormatting.BLUE));
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
