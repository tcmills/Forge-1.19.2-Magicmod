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

public class Air_1_Yeet_Item extends Item {

    private int manaCost = 10;

    public Air_1_Yeet_Item(Item.Properties properties) {
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

                                float f2 = 5F;
                                int k1 = Mth.floor(player.getX() - (double)f2 - 1.0D);
                                int l1 = Mth.floor(player.getX() + (double)f2 + 1.0D);
                                int i2 = Mth.floor(player.getY() - (double)f2 - 1.0D);
                                int i1 = Mth.floor(player.getY() + (double)f2 + 1.0D);
                                int j2 = Mth.floor(player.getZ() - (double)f2 - 1.0D);
                                int j1 = Mth.floor(player.getZ() + (double)f2 + 1.0D);
                                List<Entity> list = player.level.getEntities(player, new AABB((double)k1, (double)i2, (double)j2, (double)l1, (double)i1, (double)j1));
                                Vec3 vec3 = new Vec3(player.getX(), player.getY(), player.getZ());

                                for(int k2 = 0; k2 < list.size(); ++k2) {
                                    Entity entity = list.get(k2);
                                    if (entity instanceof LivingEntity entity1) {
                                        double d12 = Math.sqrt(entity1.distanceToSqr(vec3)) / (double)f2;
                                        if (d12 <= 1.0D) {
                                            if (entity1 instanceof Player player2) {
                                                player2.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info2 -> {
                                                    if (!info.getDungeonParty() || !info2.getDungeonParty()) {
                                                        knockBack(player, player2);
                                                    }
                                                });
                                            } else {
                                                knockBack(player, entity1);
                                            }
                                        }
                                    }
                                }

                                player.level.playSound(null, player, SoundEvents.AMBIENT_BASALT_DELTAS_MOOD, SoundSource.PLAYERS, 3.0F, 1.8F + (player.getLevel().random.nextFloat() * 0.2F));
                                player.level.playSound(null, player, SoundEvents.AMBIENT_BASALT_DELTAS_MOOD, SoundSource.PLAYERS, 3.0F, 1.8F + (player.getLevel().random.nextFloat() * 0.2F));
                                player.level.playSound(null, player, SoundEvents.AMBIENT_BASALT_DELTAS_MOOD, SoundSource.PLAYERS, 3.0F, 1.8F + (player.getLevel().random.nextFloat() * 0.2F));

                                ((ServerLevel)player.getLevel()).sendParticles(ParticleTypes.EXPLOSION, player.getX(), player.getY(), player.getZ(), 5,3.0D, 2.0D, 3.0D, 1.0D);
                                ((ServerLevel)player.getLevel()).sendParticles(ParticleTypes.CLOUD, player.getX(), player.getY(), player.getZ(), 20,2.0D, 2.0D, 2.0D, 1.0D);

                                player.getCooldowns().addCooldown(this, 400);
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

    private void knockBack(Player player, LivingEntity entity) {
        double d5 = entity.getX() - player.getX();
        double d7 = entity.getEyeY() - player.getY();
        double d9 = entity.getZ() - player.getZ();
        double d13 = Math.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
        if (d13 != 0.0D) {
            d5 /= d13;
            d7 /= d13;
            d9 /= d13;
            double d10 = 3.0D;

            entity.setDeltaMovement(entity.getDeltaMovement().add(d5 * d10, d7 * (d10/2.0D), d9 * d10));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            components.add(Component.literal("Mana Cost: 10\nCooldown Time: 20 seconds\n\nRight click to push everything away from you!").withStyle(ChatFormatting.GREEN));
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.YELLOW));
        }


        super.appendHoverText(stack, level, components, flag);
    }
}
