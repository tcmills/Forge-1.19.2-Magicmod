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
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
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
import net.tyler.magicmod.client.ClientInfoData;
import net.tyler.magicmod.client.ClientManaData;
import net.tyler.magicmod.effect.ModEffects;
import net.tyler.magicmod.misc.ModDamageSource;
import net.tyler.magicmod.networking.ModMessages;
import net.tyler.magicmod.networking.packet.ManaDataSyncS2CPacket;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Water_2_SharkLunge_Item extends Item {

    private int manaCost = 50;

    public Water_2_SharkLunge_Item(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (hand == InteractionHand.MAIN_HAND && ClientInfoData.getPlayerWater() && ClientInfoData.getPlayerSchoolLevel() >= 2 && ClientManaData.getPlayerMana() >= manaCost) {

            float f7 = player.getYRot();
            float f = player.getXRot();
            float f1 = -Mth.sin(f7 * ((float)Math.PI / 180F)) * Mth.cos(f * ((float)Math.PI / 180F));
            float f2 = -Mth.sin(f * ((float)Math.PI / 180F));
            float f3 = Mth.cos(f7 * ((float)Math.PI / 180F)) * Mth.cos(f * ((float)Math.PI / 180F));
            float f4 = Mth.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
            float f5 = 3.0F * ((1.0F + (float)2) / 4.0F);
            f1 *= f5 / f4;
            f2 *= f5 / f4;
            f3 *= f5 / f4;
            player.push((double)f1, (double)f2, (double)f3);
            player.startAutoSpinAttack(20);
            if (player.isOnGround()) {
                float f6 = 1.1999999F;
                player.move(MoverType.SELF, new Vec3(0.0D, (double)f6, 0.0D));
            }

        }

        player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
            player.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info -> {
                player.getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(cast -> {
                    if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
                        if (info.getWater()) {
                            if (info.getSchoolLevel() >= 2) {
                                if (!cast.getSharkLungeCasting()) {
                                    if (mana.getMana() >= manaCost) {
                                        cast.setSharkLungeCasting(true);

                                        mana.subMana(manaCost);
                                        ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana(), mana.getMaxMana()), (ServerPlayer) player);

                                        player.level.playSound(null, player, SoundEvents.TRIDENT_RIPTIDE_2, SoundSource.PLAYERS, 1f, 1f);
                                        ((ServerLevel)level).sendParticles(ParticleTypes.SPLASH, player.getX(), player.getY(), player.getZ(), 10,2.0D, 2.0D, 2.0D, 1.0D);
                                    } else {
                                        player.sendSystemMessage(Component.literal("Not enough mana!").withStyle(ChatFormatting.DARK_AQUA));
                                    }
                                } else {
                                    cast.setSharkLungeCasting(false);
                                    cast.setSharkLungeTick(0);

                                    float f2 = 4F;
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
                                                    player.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info1 -> {
                                                        player2.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info2 -> {
                                                            if (!info1.getDungeonParty() || !info2.getDungeonParty()) {
                                                                if (player.hasEffect(ModEffects.SPELL_STRENGTH_2.get())) {
                                                                    player2.hurt(ModDamageSource.sharkLunge(null, player), 20F);
                                                                    //player1.sendSystemMessage(Component.literal(baseDamage + 3F + ""));
                                                                } else if (player.hasEffect(ModEffects.SPELL_STRENGTH.get())) {
                                                                    player2.hurt(ModDamageSource.sharkLunge(null, player), 17F);
                                                                    //player1.sendSystemMessage(Component.literal(baseDamage + 3F + ""));
                                                                } else {
                                                                    player2.hurt(ModDamageSource.sharkLunge(null, player), 14F);
                                                                    //player1.sendSystemMessage(Component.literal(baseDamage + ""));
                                                                }

                                                                if (player.getLevel().random.nextInt(2) == 0) {
                                                                    player2.addEffect(new MobEffectInstance(ModEffects.BLEED.get(), 200, 0, false, false, true));
                                                                }

                                                            }
                                                        });
                                                    });
                                                } else {
                                                    if (player.hasEffect(ModEffects.SPELL_STRENGTH_2.get())) {
                                                        entity1.hurt(ModDamageSource.sharkLunge(null, player), 20F);
                                                        //player1.sendSystemMessage(Component.literal(baseDamage + 3F + ""));
                                                    } else if (player.hasEffect(ModEffects.SPELL_STRENGTH.get())) {
                                                        entity1.hurt(ModDamageSource.sharkLunge(null, player), 17F);
                                                        //player1.sendSystemMessage(Component.literal(baseDamage + 3F + ""));
                                                    } else {
                                                        entity1.hurt(ModDamageSource.sharkLunge(null, player), 14F);
                                                        //player1.sendSystemMessage(Component.literal(baseDamage + ""));
                                                    }

                                                    if (player.getLevel().random.nextInt(2) == 0) {
                                                        entity1.addEffect(new MobEffectInstance(ModEffects.BLEED.get(), 140, 0, false, false, true));
                                                    }

                                                }
                                            }
                                        }

                                    }

                                    ((ServerLevel)player.getLevel()).sendParticles(ParticleTypes.SPLASH, player.getX(), player.getY(), player.getZ(), 30,2.0D, 2.0D, 2.0D, 1.0D);

                                    player.getLevel().playSound(null, player, SoundEvents.EVOKER_FANGS_ATTACK, SoundSource.PLAYERS, 2.0F,  0.6F + (player.getLevel().random.nextFloat() * 0.6F));
                                    player.getLevel().playSound(null, player, SoundEvents.EVOKER_FANGS_ATTACK, SoundSource.PLAYERS, 2.0F,  0.6F + (player.getLevel().random.nextFloat() * 0.6F));
                                    player.getLevel().playSound(null, player, SoundEvents.EVOKER_FANGS_ATTACK, SoundSource.PLAYERS, 2.0F,  0.6F + (player.getLevel().random.nextFloat() * 0.6F));

                                    ((ServerLevel)player.getLevel()).sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX(), player.getY()+1, player.getZ(), 10,2.0D, 0.5D, 2.0D, 1.0D);


                                    player.getCooldowns().addCooldown(this, 400);
                                }
                            } else {
                                player.sendSystemMessage(Component.literal("This spell is too complicated for you to cast!").withStyle(ChatFormatting.YELLOW));
                            }
                        } else {
                            player.sendSystemMessage(Component.literal("You are unable to cast this spell!").withStyle(ChatFormatting.YELLOW));
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
            components.add(Component.literal("Mana Cost: 50\nCooldown Time: 20 seconds\nDamage: 14\nBleed Chance: 33%\n\nRight click to launch yourself forward!\nLand with a flurry of hemorrhaging slashes!").withStyle(ChatFormatting.BLUE));
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.YELLOW));
        }


        super.appendHoverText(stack, level, components, flag);
    }
}
