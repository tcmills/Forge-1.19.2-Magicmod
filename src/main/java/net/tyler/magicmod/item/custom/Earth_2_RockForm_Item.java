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
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
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
import net.tyler.magicmod.item.ModItems;
import net.tyler.magicmod.networking.ModMessages;
import net.tyler.magicmod.networking.packet.ManaDataSyncS2CPacket;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class Earth_2_RockForm_Item extends Item {

    private int manaCost = 30;

    public Earth_2_RockForm_Item(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
            player.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info -> {
                player.getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(cast -> {
                    if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
                        if (info.getEarth()) {
                            if (info.getSchoolLevel() >= 2) {
                                if (!cast.getRockFormCasting()) {
                                    if (mana.getMana() >= manaCost) {
                                        int flag = player.getInventory().getFreeSlot();
                                        if (flag != -1) {
                                            player.getAttribute(Attributes.MAX_HEALTH).addTransientModifier(
                                                    new AttributeModifier(UUID.fromString("00925d7d-6725-4531-8598-014d3bb1e081"),
                                                            "rock_form_health", 20, AttributeModifier.Operation.ADDITION));
                                            player.heal(20f);

                                            player.getAttribute(Attributes.ARMOR).addTransientModifier(
                                                    new AttributeModifier(UUID.fromString("feda2c47-ca5a-4f21-bf7b-dab1d843bd2a"),
                                                            "rock_form_armor", 8, AttributeModifier.Operation.ADDITION));

                                            cast.setRockFormCasting(true);
                                            cast.setRockFormContinue(true);

                                            player.addItem(new ItemStack(ModItems.ROCK_FIST.get(), 1));

                                            player.level.playSound(null, player, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);

                                            player.level.playSound(null, player, SoundEvents.IRON_GOLEM_DAMAGE, SoundSource.PLAYERS, 1.0F, 0.6F + (player.getLevel().random.nextFloat() * 0.1F));
                                            player.level.playSound(null, player, SoundEvents.IRON_GOLEM_DAMAGE, SoundSource.PLAYERS, 1.0F, 0.6F + (player.getLevel().random.nextFloat() * 0.1F));
                                            player.level.playSound(null, player, SoundEvents.IRON_GOLEM_DAMAGE, SoundSource.PLAYERS, 1.0F, 0.6F + (player.getLevel().random.nextFloat() * 0.1F));
                                        } else {
                                            player.sendSystemMessage(Component.literal("Your inventory is full!").withStyle(ChatFormatting.YELLOW));
                                        }
                                    } else {
                                        player.sendSystemMessage(Component.literal("Not enough mana!").withStyle(ChatFormatting.DARK_AQUA));
                                    }
                                } else {
                                    cast.setRockFormContinue(false);
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
            components.add(Component.literal("Mana Cost: 30 per 30 seconds\nMax Health Increase: 20\nArmor Increase: 8\nRock Fist Damage: 7\n\nRight click to cover yourself with rocks!\nRight click again to stop (The spell continues until the duration is up)!").withStyle(ChatFormatting.GRAY));
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
