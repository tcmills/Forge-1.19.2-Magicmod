package net.tyler.magicmod.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tyler.magicmod.MagicMod;
import net.tyler.magicmod.capability.casting.PlayerCasting;
import net.tyler.magicmod.capability.casting.PlayerCastingProvider;
import net.tyler.magicmod.capability.cooldowns.PlayerCooldowns;
import net.tyler.magicmod.capability.cooldowns.PlayerCooldownsProvider;
import net.tyler.magicmod.capability.drops.PlayerDrops;
import net.tyler.magicmod.capability.drops.PlayerDropsProvider;
import net.tyler.magicmod.effect.ModEffects;
import net.tyler.magicmod.entity.ModEntityTypes;
import net.tyler.magicmod.entity.custom.MagicMissileProjectileEntity;
import net.tyler.magicmod.entity.custom.ScorchingRayProjectileEntity;
import net.tyler.magicmod.entity.custom.WispEntity;
import net.tyler.magicmod.capability.info.PlayerInfo;
import net.tyler.magicmod.capability.info.PlayerInfoProvider;
import net.tyler.magicmod.misc.MagicalExplosion;
import net.tyler.magicmod.item.ModItems;
import net.tyler.magicmod.capability.location.PlayerLocation;
import net.tyler.magicmod.capability.location.PlayerLocationProvider;
import net.tyler.magicmod.capability.mana.PlayerMana;
import net.tyler.magicmod.capability.mana.PlayerManaProvider;
import net.tyler.magicmod.networking.ModMessages;
import net.tyler.magicmod.networking.packet.InfoDataSyncS2CPacket;
import net.tyler.magicmod.networking.packet.ManaDataSyncS2CPacket;
import net.tyler.magicmod.villager.ModVillagers;

import java.util.List;

public class ModEvents {

    @Mod.EventBusSubscriber(modid = MagicMod.MOD_ID)
    public static class ForgeEvents {

        @SubscribeEvent
        public static void addCustomTrades(VillagerTradesEvent event) {
            if(event.getType() == ModVillagers.ARCANIST.get()) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(ModItems.MANA_DUST.get(), 1);
                int villagerLevel = 1;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(ModItems.COINS.get(), 10),
                        stack,32,0,0.0F));
            }
        }

        @SubscribeEvent
        public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof Player) {
                if (!event.getObject().getCapability(PlayerManaProvider.PLAYER_MANA).isPresent()) {
                    event.addCapability(new ResourceLocation(MagicMod.MOD_ID, "properties"), new PlayerManaProvider());
                }
                if (!event.getObject().getCapability(PlayerInfoProvider.PLAYER_INFO).isPresent()) {
                    event.addCapability(new ResourceLocation(MagicMod.MOD_ID, "properties2"), new PlayerInfoProvider());
                }
                if (!event.getObject().getCapability(PlayerLocationProvider.PLAYER_LOCATION).isPresent()) {
                    event.addCapability(new ResourceLocation(MagicMod.MOD_ID, "properties3"), new PlayerLocationProvider());
                }
                if (!event.getObject().getCapability(PlayerDropsProvider.PLAYER_DROPS).isPresent()) {
                    event.addCapability(new ResourceLocation(MagicMod.MOD_ID, "properties4"), new PlayerDropsProvider());
                }
                if (!event.getObject().getCapability(PlayerCooldownsProvider.PLAYER_COOLDOWNS).isPresent()) {
                    event.addCapability(new ResourceLocation(MagicMod.MOD_ID, "properties5"), new PlayerCooldownsProvider());
                }
                if (!event.getObject().getCapability(PlayerCastingProvider.PLAYER_CASTING).isPresent()) {
                    event.addCapability(new ResourceLocation(MagicMod.MOD_ID, "properties6"), new PlayerCastingProvider());
                }
            }
        }

        @SubscribeEvent
        public static void onPlayerCloned(PlayerEvent.Clone event) {
            if (event.isWasDeath()) {
                event.getOriginal().revive();
                event.getEntity().getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(newStore ->{
                    event.getOriginal().getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(oldStore -> {
                        newStore.copyFrom(oldStore);
                    });
                });
                event.getEntity().getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(newStore2 ->{
                    event.getOriginal().getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(oldStore2 -> {
                        newStore2.copyFrom(oldStore2);
                    });
                });
                event.getEntity().getCapability(PlayerLocationProvider.PLAYER_LOCATION).ifPresent(newStore3 ->{
                    event.getOriginal().getCapability(PlayerLocationProvider.PLAYER_LOCATION).ifPresent(oldStore3 -> {
                        newStore3.copyFrom(oldStore3);
                    });
                });
                event.getOriginal().getCapability(PlayerDropsProvider.PLAYER_DROPS).ifPresent(oldStore4 -> {
                    int[] items = oldStore4.getDrops();

                    for (int i = 0; i < items[0]; i++) {
                        event.getEntity().addItem(new ItemStack(ModItems.MAGIC_MISSILE.get()));
                    }

                    for (int i = 0; i < items[1]; i++) {
                        event.getEntity().addItem(new ItemStack(ModItems.AID.get()));
                    }

                    for (int i = 0; i < items[2]; i++) {
                        event.getEntity().addItem(new ItemStack(ModItems.TELEPORT.get()));
                    }

                    for (int i = 0; i < items[3]; i++) {
                        event.getEntity().addItem(new ItemStack(ModItems.TELEPORT_HOME.get()));
                    }

                    for (int i = 0; i < items[4]; i++) {
                        event.getEntity().addItem(new ItemStack(ModItems.FLARE_BLITZ.get()));
                    }

                    for (int i = 0; i < items[5]; i++) {
                        event.getEntity().addItem(new ItemStack(ModItems.SCORCHING_RAY.get()));
                    }

                    for (int i = 0; i < items[6]; i++) {
                        event.getEntity().addItem(new ItemStack(ModItems.FIREBALL.get()));
                    }

                    for (int i = 0; i < items[7]; i++) {
                        event.getEntity().addItem(new ItemStack(ModItems.FIERY_SOUL.get()));
                    }

                    for (int i = 0; i < items[8]; i++) {
                        event.getEntity().addItem(new ItemStack(ModItems.SUPER_CRITICAL.get()));
                    }
                });
                event.getEntity().getCapability(PlayerCooldownsProvider.PLAYER_COOLDOWNS).ifPresent(newStore5 -> {
                    event.getOriginal().getCapability(PlayerCooldownsProvider.PLAYER_COOLDOWNS).ifPresent(oldStore5 -> {
                        newStore5.copyFrom(oldStore5);
                    });
                });
                event.getEntity().getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(newStore6 -> {
                    event.getOriginal().getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(oldStore6 -> {
                        newStore6.copyFrom(oldStore6);
                    });
                });

                event.getOriginal().invalidateCaps();
            }
        }

        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            event.register(PlayerMana.class);
            event.register(PlayerInfo.class);
            event.register(PlayerLocation.class);
            event.register(PlayerDrops.class);
            event.register(PlayerCooldowns.class);
            event.register(PlayerCasting.class);
        }

        @SubscribeEvent
        public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
            if (!event.getLevel().isClientSide()) {
                if (event.getEntity() instanceof ServerPlayer player) {
                    player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
                        ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana(), mana.getMaxMana()), player);
                    });
                    player.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info -> {
                        ModMessages.sendToPlayer(new InfoDataSyncS2CPacket(info.getSchoolLevel(), info.getFire(),
                                info.getWater(), info.getEarth(), info.getAir(), info.getSummoning(), info.getForge(),
                                info.getStorm(), info.getEnder(), info.getLife(), info.getDeath(), info.getSun(),
                                info.getMoon(), info.getDungeonParty()), player);
                    });
                    player.getCapability(PlayerCooldownsProvider.PLAYER_COOLDOWNS).ifPresent(cd -> {
                        player.getCooldowns().addCooldown(ModItems.MAGIC_MISSILE.get(), (int)(80 * cd.getMagicMissileCD()));
                        player.getCooldowns().addCooldown(ModItems.AID.get(), (int)(160 * cd.getAidCD()));
                        player.getCooldowns().addCooldown(ModItems.TELEPORT.get(), (int)(3600 * cd.getTeleportCD()));
                        player.getCooldowns().addCooldown(ModItems.TELEPORT_HOME.get(), (int)(3600 * cd.getTeleportHomeCD()));
                        player.getCooldowns().addCooldown(ModItems.FLARE_BLITZ.get(), (int)(600 * cd.getFlareBlitzCD()));
                        player.getCooldowns().addCooldown(ModItems.SCORCHING_RAY.get(), (int)(600 * cd.getScorchingRayCD()));
                        player.getCooldowns().addCooldown(ModItems.FIREBALL.get(), (int)(1200 * cd.getFireballCD()));
                        player.getCooldowns().addCooldown(ModItems.SUPER_CRITICAL.get(), (int)(60 * cd.getSuperCriticalCD()));
                    });
                }
            }
        }

        @SubscribeEvent
        public static void onPlayerLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
            event.getEntity().getCapability(PlayerCooldownsProvider.PLAYER_COOLDOWNS).ifPresent(cd -> {
                cd.setMagicMissileCD(event.getEntity().getCooldowns().getCooldownPercent(ModItems.MAGIC_MISSILE.get(), 0.0F));
                cd.setAidCD(event.getEntity().getCooldowns().getCooldownPercent(ModItems.AID.get(), 0.0F));
                cd.setTeleportCD(event.getEntity().getCooldowns().getCooldownPercent(ModItems.TELEPORT.get(), 0.0F));
                cd.setTeleportHomeCD(event.getEntity().getCooldowns().getCooldownPercent(ModItems.TELEPORT_HOME.get(), 0.0F));
                cd.setFlareBlitzCD(event.getEntity().getCooldowns().getCooldownPercent(ModItems.FLARE_BLITZ.get(), 0.0F));
                cd.setScorchingRayCD(event.getEntity().getCooldowns().getCooldownPercent(ModItems.SCORCHING_RAY.get(), 0.0F));
                cd.setFireballCD(event.getEntity().getCooldowns().getCooldownPercent(ModItems.FIREBALL.get(), 0.0F));
                cd.setSuperCriticalCD(event.getEntity().getCooldowns().getCooldownPercent(ModItems.SUPER_CRITICAL.get(), 0.0F));
            });
        }

        @SubscribeEvent
        public static void onPlayerDamage(LivingDamageEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                player.addEffect(new MobEffectInstance(ModEffects.COMBAT.get(), 200, 0, false, false, true));
            }
        }

        @SubscribeEvent
        public static void onPlayerTick(LivingEvent.LivingTickEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(cast -> {
                    player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
                        if (cast.getFlareBlitzCasting()) {
                            if (cast.getFlareBlitzTick() <= 100) {
                                ((ServerLevel)player.getLevel()).sendParticles(ParticleTypes.FLAME, player.getX(), player.getY(), player.getZ(), 1,1.0D, 1.0D, 1.0D, 0.0D);
                                cast.addFlareBlitzTick(1);

                                if (player.isOnGround()) {
                                    cast.setFlareBlitzCasting(false);
                                    cast.setFlareBlitzTick(0);

                                    MagicalExplosion explosion = new MagicalExplosion(player.getLevel(), player, "flareBlitz", (ExplosionDamageCalculator)null, player.getX(), player.getY()+1, player.getZ(), 3F, 14D, true, Explosion.BlockInteraction.NONE);
                                    if (!net.minecraftforge.event.ForgeEventFactory.onExplosionStart(player.getLevel(), explosion)) {
                                        explosion.explode();

                                        player.getLevel().playSound(null, player, SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 4.0F, (1.0F + (player.getLevel().random.nextFloat() - player.getLevel().random.nextFloat()) * 0.2F) * 0.7F);

                                        ((ServerLevel)player.getLevel()).sendParticles(ParticleTypes.EXPLOSION, player.getX(), player.getY(), player.getZ(), 10,2.0D, 2.0D, 2.0D, 1.0D);
                                    }
                                }
                            } else {
                                cast.setFlareBlitzCasting(false);
                                cast.setFlareBlitzTick(0);
                            }
                        }

                        if (cast.getScorchingRayCasting()) {

                            if (cast.getScorchingRayTick() == 0) {
                                int speed = 2;

                                player.getLevel().playSound(null, player, SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 4.0F, 1F / (player.getLevel().random.nextFloat() * 0.2F + 0.9F));

                                ScorchingRayProjectileEntity scorching_ray = new ScorchingRayProjectileEntity(player, player.level);
                                scorching_ray.setItem(ModItems.SCORCHING_RAY_PROJECTILE.get().getDefaultInstance());
                                scorching_ray.setDeltaMovement(player.getLookAngle().x*speed, player.getLookAngle().y*speed, player.getLookAngle().z*speed);
                                player.level.addFreshEntity(scorching_ray);

                                cast.subScorchingRayProjectiles(1);
                                cast.addScorchingRayTick(1);
                            }
                            else if (cast.getScorchingRayTick() >= 10) {
                                cast.setScorchingRayTick(0);
                            } else {
                                cast.addScorchingRayTick(1);
                            }

                            if (cast.getScorchingRayProjectiles() <= 0) {
                                cast.setScorchingRayCasting(false);
                                cast.setScorchingRayProjectiles(3);
                                cast.setScorchingRayTick(0);
                            }
                        }

                        if (cast.getFierySoulCasting()) {
                            if (cast.getFierySoulTick() == 0) {
                                if (mana.getMana() >= 5) {
                                    mana.subMana(5);
                                    ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana(), mana.getMaxMana()), (ServerPlayer) player);
                                    cast.addFierySoulTick(1);
                                } else {
                                    player.removeEffect(ModEffects.SPELL_STRENGTH.get());
                                    player.removeEffect(MobEffects.FIRE_RESISTANCE);
                                    cast.setFierySoulCasting(false);
                                    cast.setFierySoulTick(0);
                                    player.sendSystemMessage(Component.literal("Mana depleted!").withStyle(ChatFormatting.DARK_AQUA));
                                }
                            }
                            else if (cast.getFierySoulTick() >= 200) {
                                cast.setFierySoulTick(0);
                            } else {
                                cast.addFierySoulTick(1);
                            }

                            if (player.getLevel().random.nextInt(24) == 0) {
                                player.getLevel().playSound(null, player, SoundEvents.FIRE_AMBIENT, SoundSource.PLAYERS, 1.0F + player.getLevel().random.nextFloat(), player.getLevel().random.nextFloat() * 0.7F);
                            }

                            ((ServerLevel)player.getLevel()).sendParticles(ParticleTypes.FLAME, player.getX(), player.getY()+1, player.getZ(), 1,0.5D, 0.5D, 0.5D, 0.0D);
                            ((ServerLevel)player.getLevel()).sendParticles(ParticleTypes.WAX_OFF, player.getX(), player.getY()+1, player.getZ(), 1,0.5D, 0.5D, 0.5D, 0.0D);

                        }
                    });
                });
            }
        }

        @SubscribeEvent
        public static void onPlayerDrop(LivingDropsEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                ItemEntity[] droppedItems = new ItemEntity[event.getDrops().size()];
                ItemEntity[] finalDroppedItems = event.getDrops().toArray(droppedItems);
                event.getEntity().getCapability(PlayerDropsProvider.PLAYER_DROPS).ifPresent(drops -> {
                    for (int i = 0; i < finalDroppedItems.length; i++) {
                        if (finalDroppedItems[i].getItem().getItem() == ModItems.MAGIC_MISSILE.get()) {
                            finalDroppedItems[i].kill();
                            drops.addDropNumber(0);
                        } else if (finalDroppedItems[i].getItem().getItem() == ModItems.AID.get()) {
                            finalDroppedItems[i].kill();
                            drops.addDropNumber(1);
                        } else if (finalDroppedItems[i].getItem().getItem() == ModItems.TELEPORT.get()) {
                            finalDroppedItems[i].kill();
                            drops.addDropNumber(2);
                        } else if (finalDroppedItems[i].getItem().getItem() == ModItems.TELEPORT_HOME.get()) {
                            finalDroppedItems[i].kill();
                            drops.addDropNumber(3);
                        } else if (finalDroppedItems[i].getItem().getItem() == ModItems.FLARE_BLITZ.get()) {
                            finalDroppedItems[i].kill();
                            drops.addDropNumber(4);
                        } else if (finalDroppedItems[i].getItem().getItem() == ModItems.SCORCHING_RAY.get()) {
                            finalDroppedItems[i].kill();
                            drops.addDropNumber(5);
                        } else if (finalDroppedItems[i].getItem().getItem() == ModItems.FIREBALL.get()) {
                            finalDroppedItems[i].kill();
                            drops.addDropNumber(6);
                        } else if (finalDroppedItems[i].getItem().getItem() == ModItems.FIERY_SOUL.get()) {
                            finalDroppedItems[i].kill();
                            drops.addDropNumber(7);
                        } else if (finalDroppedItems[i].getItem().getItem() == ModItems.SUPER_CRITICAL.get()) {
                            finalDroppedItems[i].kill();
                            drops.addDropNumber(8);
                        }
                    }
                });
                player.getCapability(PlayerCooldownsProvider.PLAYER_COOLDOWNS).ifPresent(cd -> {
                    cd.setMagicMissileCD(player.getCooldowns().getCooldownPercent(ModItems.MAGIC_MISSILE.get(), 0.0F));
                    cd.setAidCD(player.getCooldowns().getCooldownPercent(ModItems.AID.get(), 0.0F));
                    cd.setTeleportCD(player.getCooldowns().getCooldownPercent(ModItems.TELEPORT.get(), 0.0F));
                    cd.setTeleportHomeCD(player.getCooldowns().getCooldownPercent(ModItems.TELEPORT_HOME.get(), 0.0F));
                    cd.setFlareBlitzCD(player.getCooldowns().getCooldownPercent(ModItems.FLARE_BLITZ.get(), 0.0F));
                    cd.setScorchingRayCD(player.getCooldowns().getCooldownPercent(ModItems.SCORCHING_RAY.get(), 0.0F));
                    cd.setFireballCD(player.getCooldowns().getCooldownPercent(ModItems.FIREBALL.get(), 0.0F));
                    cd.setSuperCriticalCD(player.getCooldowns().getCooldownPercent(ModItems.SUPER_CRITICAL.get(), 0.0F));
                });
            }
        }
    }

    @Mod.EventBusSubscriber(modid = MagicMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBusEvents {

        @SubscribeEvent
        public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
            event.put(ModEntityTypes.WISP.get(), WispEntity.setAttributes());
        }
    }
}
