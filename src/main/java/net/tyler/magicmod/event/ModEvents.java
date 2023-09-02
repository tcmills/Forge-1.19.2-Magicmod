package net.tyler.magicmod.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.IContainerFactory;
import net.tyler.magicmod.MagicMod;
import net.tyler.magicmod.capability.casting.PlayerCasting;
import net.tyler.magicmod.capability.casting.PlayerCastingProvider;
import net.tyler.magicmod.capability.cooldowns.PlayerCooldowns;
import net.tyler.magicmod.capability.cooldowns.PlayerCooldownsProvider;
import net.tyler.magicmod.capability.drops.PlayerDrops;
import net.tyler.magicmod.capability.drops.PlayerDropsProvider;
import net.tyler.magicmod.effect.ModEffects;
import net.tyler.magicmod.entity.ModEntityTypes;
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
import net.tyler.magicmod.misc.ModDamageSource;
import net.tyler.magicmod.networking.ModMessages;
import net.tyler.magicmod.networking.packet.InfoDataSyncS2CPacket;
import net.tyler.magicmod.networking.packet.ManaDataSyncS2CPacket;
import net.tyler.magicmod.networking.packet.SharkLungeSyncS2CPacket;
import net.tyler.magicmod.sound.ModSounds;
import net.tyler.magicmod.util.InventoryUtil;
import net.tyler.magicmod.villager.ModVillagers;
import org.spongepowered.asm.launch.platform.container.IContainerHandle;

import java.util.List;
import java.util.UUID;

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
                    if (((ServerPlayer) event.getEntity()).gameMode.getGameModeForPlayer() == GameType.ADVENTURE && !newStore2.getDungeonParty()) {
                        ((ServerPlayer) event.getEntity()).setGameMode(GameType.SURVIVAL);
                    }
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

                    for (int i = 0; i < items[9]; i++) {
                        event.getEntity().addItem(new ItemStack(ModItems.AQUAMARINE_BLESSING.get()));
                    }

                    for (int i = 0; i < items[10]; i++) {
                        event.getEntity().addItem(new ItemStack(ModItems.SHARK_LUNGE.get()));
                    }

                    for (int i = 0; i < items[11]; i++) {
                        event.getEntity().addItem(new ItemStack(ModItems.AMPHIBIOUS.get()));
                    }

                    for (int i = 0; i < items[12]; i++) {
                        event.getEntity().addItem(new ItemStack(ModItems.WATERFALL.get()));
                    }

                    for (int i = 0; i < items[13]; i++) {
                        event.getEntity().addItem(new ItemStack(ModItems.GALEFORCE.get()));
                    }

                    for (int i = 0; i < items[14]; i++) {
                        event.getEntity().addItem(new ItemStack(ModItems.YEET.get()));
                    }

                    for (int i = 0; i < items[15]; i++) {
                        event.getEntity().addItem(new ItemStack(ModItems.AIR_DARTS.get()));
                    }

                    for (int i = 0; i < items[16]; i++) {
                        event.getEntity().addItem(new ItemStack(ModItems.TOSS.get()));
                    }

                    for (int i = 0; i < items[17]; i++) {
                        event.getEntity().addItem(new ItemStack(ModItems.WINGS_OF_QUARTZ.get()));
                    }

                    for (int i = 0; i < items[18]; i++) {
                        event.getEntity().addItem(new ItemStack(ModItems.WEIGHT_OF_PYRITE.get()));
                    }

                    for (int i = 0; i < items[19]; i++) {
                        event.getEntity().addItem(new ItemStack(ModItems.BURROW.get()));
                    }

                    for (int i = 0; i < items[20]; i++) {
                        event.getEntity().addItem(new ItemStack(ModItems.ROCK_FORM.get()));
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
                        player.getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(cast -> {
                            player.getCooldowns().addCooldown(ModItems.MAGIC_MISSILE.get(), (int)(80 * cd.getMagicMissileCD()));
                            player.getCooldowns().addCooldown(ModItems.AID.get(), (int)(160 * cd.getAidCD()));
                            player.getCooldowns().addCooldown(ModItems.TELEPORT.get(), (int)(3600 * cd.getTeleportCD()));
                            player.getCooldowns().addCooldown(ModItems.TELEPORT_HOME.get(), (int)(3600 * cd.getTeleportHomeCD()));
                            player.getCooldowns().addCooldown(ModItems.FLARE_BLITZ.get(), (int)(600 * cd.getFlareBlitzCD()));
                            player.getCooldowns().addCooldown(ModItems.SCORCHING_RAY.get(), (int)(600 * cd.getScorchingRayCD()));
                            player.getCooldowns().addCooldown(ModItems.FIREBALL.get(), (int)(1200 * cd.getFireballCD()));
                            player.getCooldowns().addCooldown(ModItems.SUPER_CRITICAL.get(), (int)(9600 * cd.getSuperCriticalCD()));
                            player.getCooldowns().addCooldown(ModItems.SHARK_LUNGE.get(), (int)(400 * cd.getSharkLungeCD()));
                            player.getCooldowns().addCooldown(ModItems.WATERFALL.get(), (int)(2400 * cd.getWaterfallCD()));
                            player.getCooldowns().addCooldown(ModItems.GALEFORCE.get(), (int)(600 * cd.getGaleforceCD()));
                            player.getCooldowns().addCooldown(ModItems.YEET.get(), (int)(400 * cd.getYeetCD()));
                            player.getCooldowns().addCooldown(ModItems.AIR_DARTS.get(), (int)(1200 * cd.getAirDartsCD()));
                            player.getCooldowns().addCooldown(ModItems.TOSS.get(), (int)(600 * cd.getTossCD()));
                            player.getCooldowns().addCooldown(ModItems.WINGS_OF_QUARTZ.get(), (int)(500 * cd.getWingsOfQuartzCD()));
                            player.getCooldowns().addCooldown(ModItems.WEIGHT_OF_PYRITE.get(), (int)(900 * cd.getWeightOfPyriteCD()));
                            player.getCooldowns().addCooldown(ModItems.BURROW.get(), (int)(200 * cd.getBurrowCD()));

                            if (player.isAlive()) {
                                cd.clearCD();
                            }
                        });
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
                cd.setSharkLungeCD(event.getEntity().getCooldowns().getCooldownPercent(ModItems.SHARK_LUNGE.get(), 0.0F));
                cd.setWaterfallCD(event.getEntity().getCooldowns().getCooldownPercent(ModItems.WATERFALL.get(), 0.0F));
                cd.setGaleforceCD(event.getEntity().getCooldowns().getCooldownPercent(ModItems.GALEFORCE.get(), 0.0F));
                cd.setYeetCD(event.getEntity().getCooldowns().getCooldownPercent(ModItems.YEET.get(), 0.0F));
                cd.setAirDartsCD(event.getEntity().getCooldowns().getCooldownPercent(ModItems.AIR_DARTS.get(), 0.0F));
                cd.setTossCD(event.getEntity().getCooldowns().getCooldownPercent(ModItems.TOSS.get(), 0.0F));
                cd.setWingsOfQuartzCD(event.getEntity().getCooldowns().getCooldownPercent(ModItems.WINGS_OF_QUARTZ.get(), 0.0F));
                cd.setWeightOfPyriteCD(event.getEntity().getCooldowns().getCooldownPercent(ModItems.WEIGHT_OF_PYRITE.get(), 0.0F));
                cd.setBurrowCD(event.getEntity().getCooldowns().getCooldownPercent(ModItems.BURROW.get(), 0.0F));
            });
        }

        @SubscribeEvent
        public static void onPlayerDamage(LivingDamageEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                player.addEffect(new MobEffectInstance(ModEffects.COMBAT.get(), 200, 0, false, false, true));
            }
        }

        @SubscribeEvent
        public static void onPlayerToss(ItemTossEvent event) {
            if (event.getEntity().getItem().getItem() == ModItems.AIR_DART.get() ||
                    event.getEntity().getItem().getItem() == ModItems.ROCK_FIST.get()) {
                event.setCanceled(true);
                event.getPlayer().addItem(event.getEntity().getItem());
            }
        }

        @SubscribeEvent
        public static void onPlayerInteract(PlayerInteractEvent.EntityInteract event) {
            if (event.getTarget() instanceof ItemFrame || event.getTarget() instanceof Allay) {
                if (event.getItemStack().getItem() == ModItems.ROCK_FIST.get() || event.getItemStack().getItem() == ModItems.AIR_DART.get()) {
                    event.setCanceled(true);
                }
            }
        }

        @SubscribeEvent
        public static void onPlayerContainerOpen(PlayerContainerEvent.Close event) {
            Container container = event.getContainer().getSlot(0).container;

            if (event.getContainer() != event.getEntity().inventoryMenu) {
                NonNullList<ItemStack> items = event.getContainer().getItems();

                for (int i = 0; i < items.size() - 36; i++) {
                    if (items.get(i).getItem() == ModItems.AIR_DART.get() ||
                            items.get(i).getItem() == ModItems.ROCK_FIST.get()) {
                        container.setItem(i, ItemStack.EMPTY);
                        event.getEntity().addItem(items.get(i));
                    }
                }
            }

        }

        @SubscribeEvent
        public static void onPlayerTick(LivingEvent.LivingTickEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(cast -> {
                    player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
                        player.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info -> {

                            if (player.hasEffect(ModEffects.SPELL_STRENGTH_2.get()) && player.hasEffect(ModEffects.SPELL_STRENGTH.get())) {
                                player.removeEffect(ModEffects.SPELL_STRENGTH.get());
                            }

                            if (player.hasEffect(ModEffects.SPELL_WEAKNESS_2.get()) && player.hasEffect(ModEffects.SPELL_WEAKNESS.get())) {
                                player.removeEffect(ModEffects.SPELL_WEAKNESS.get());
                            }

                            ItemStack armor0 = player.getInventory().getArmor(0);
                            ItemStack armor1 = player.getInventory().getArmor(1);
                            ItemStack armor2 = player.getInventory().getArmor(2);
                            ItemStack armor3 = player.getInventory().getArmor(3);

                            if (player.getInventory().getItem(40).sameItem(new ItemStack(Items.SHIELD)) ||
                                    player.getInventory().getSelected().sameItem(new ItemStack(Items.SHIELD))) {
                                player.addEffect(new MobEffectInstance(ModEffects.SPELL_WEAKNESS_2.get(), 200, 0, false, false, true));
                            } else {
                                if (!player.hasEffect(ModEffects.SPELL_WEAKNESS_2.get())) {
                                    if (armor3.sameItem(new ItemStack(Items.IRON_HELMET)) ||
                                            armor3.sameItem(new ItemStack(Items.GOLDEN_HELMET)) ||
                                            armor3.sameItem(new ItemStack(Items.CHAINMAIL_HELMET))) {
                                        player.addEffect(new MobEffectInstance(ModEffects.SPELL_WEAKNESS.get(), 200, 0, false, false, true));
                                    } else if (armor2.sameItem(new ItemStack(Items.IRON_CHESTPLATE)) ||
                                            armor2.sameItem(new ItemStack(Items.GOLDEN_CHESTPLATE)) ||
                                            armor2.sameItem(new ItemStack(Items.CHAINMAIL_CHESTPLATE))) {
                                        player.addEffect(new MobEffectInstance(ModEffects.SPELL_WEAKNESS.get(), 200, 0, false, false, true));
                                    } else if (armor1.sameItem(new ItemStack(Items.IRON_LEGGINGS)) ||
                                            armor1.sameItem(new ItemStack(Items.GOLDEN_LEGGINGS)) ||
                                            armor1.sameItem(new ItemStack(Items.CHAINMAIL_LEGGINGS))) {
                                        player.addEffect(new MobEffectInstance(ModEffects.SPELL_WEAKNESS.get(), 200, 0, false, false, true));
                                    } else if (armor0.sameItem(new ItemStack(Items.IRON_BOOTS)) ||
                                            armor0.sameItem(new ItemStack(Items.GOLDEN_BOOTS)) ||
                                            armor0.sameItem(new ItemStack(Items.CHAINMAIL_BOOTS))) {
                                        player.addEffect(new MobEffectInstance(ModEffects.SPELL_WEAKNESS.get(), 200, 0, false, false, true));
                                    }
                                }
                            }

                            if (player.hasEffect(ModEffects.BLEED.get())) {
                                if (player.isCrouching()) {
                                    cast.addBleedTick(1);
                                    if (cast.getBleedTick() >= 100) {
                                        player.removeEffect(ModEffects.BLEED.get());
                                        cast.setBleedTick(0);
                                    }
                                } else {
                                    cast.setBleedTick(0);
                                }
                            } else {
                                cast.setBleedTick(0);
                            }

                            if (info.getFire()) {
                                if (cast.getFlareBlitzCasting()) {
                                    if (cast.getFlareBlitzTick() <= 100) {
                                        ((ServerLevel)player.getLevel()).sendParticles(ParticleTypes.FLAME, player.getX(), player.getY(), player.getZ(), 1,1.0D, 1.0D, 1.0D, 0.0D);
                                        cast.addFlareBlitzTick(1);

                                        if (player.isOnGround()) {
                                            cast.setFlareBlitzCasting(false);
                                            cast.setFlareBlitzTick(0);

                                            MagicalExplosion explosion = new MagicalExplosion(player.getLevel(), player, "flareBlitz", (ExplosionDamageCalculator)null, player.getX(), player.getY()+1, player.getZ(), 4F, 14D, true, Explosion.BlockInteraction.NONE);
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

                                int index_fierySoul = InventoryUtil.getFirstInventoryIndex(player, ModItems.FIERY_SOUL.get());
                                if (cast.getFierySoulCasting()) {
                                    if (index_fierySoul != -1) {
                                        ItemStack fiery_soul = player.getInventory().getItem(index_fierySoul);
                                        if (!fiery_soul.hasTag()) {
                                            CompoundTag nbtData = new CompoundTag();
                                            nbtData.putString("magicmod.fiery_soul_cast", "Fiery Soul has been cast");
                                            fiery_soul.setTag(nbtData);
                                        }
                                    }

                                    if (!player.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                                        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 72000, 0, false, false, true));
                                    }

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

                                } else {
                                    if (index_fierySoul != -1) {
                                        ItemStack fiery_soul = player.getInventory().getItem(index_fierySoul);
                                        if (fiery_soul.hasTag()) {
                                            fiery_soul.removeTagKey("magicmod.fiery_soul_cast");
                                        }
                                    }
                                }

                                int index_superCritical = InventoryUtil.getFirstInventoryIndex(player, ModItems.SUPER_CRITICAL.get());
                                if (cast.getSuperCriticalCasting()) {
                                    if (index_superCritical != -1) {
                                        ItemStack super_critical = player.getInventory().getItem(index_superCritical);
                                        if (!super_critical.hasTag()) {
                                            CompoundTag nbtData = new CompoundTag();
                                            nbtData.putString("magicmod.super_critical_cast", "Super Critical has been cast");
                                            super_critical.setTag(nbtData);
                                        }
                                    }

                                    if (player.hasEffect(ModEffects.MELTDOWN.get())) {
                                        if (player.getEffect(ModEffects.MELTDOWN.get()).getDuration() <= 1) {
                                            cast.setSuperCriticalCasting(false);

                                            MagicalExplosion explosion = new MagicalExplosion(player.getLevel(), player, "superCritical", (ExplosionDamageCalculator)null, player.getX(), player.getY()+1, player.getZ(), 7F, 30D, true, Explosion.BlockInteraction.NONE);
                                            if (!net.minecraftforge.event.ForgeEventFactory.onExplosionStart(player.getLevel(), explosion)) {
                                                explosion.explode();

                                                player.getLevel().playSound(null, player, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.PLAYERS, 4.0F, 1F / (player.getLevel().random.nextFloat() * 0.5F + 1.4F));
                                                player.getLevel().playSound(null, player, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.PLAYERS, 4.0F, 1F / (player.getLevel().random.nextFloat() * 0.5F + 1.4F));
                                                player.getLevel().playSound(null, player, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.PLAYERS, 4.0F, 1F / (player.getLevel().random.nextFloat() * 0.5F + 1.4F));

                                                ((ServerLevel)player.getLevel()).sendParticles(ParticleTypes.EXPLOSION, player.getX(), player.getY(), player.getZ(), 10,2.0D, 2.0D, 2.0D, 1.0D);
                                            }

                                            player.getCooldowns().addCooldown(ModItems.SUPER_CRITICAL.get(), 9600);
                                        }
                                    } else {
                                        cast.setSuperCriticalCasting(false);
                                        player.getCooldowns().addCooldown(ModItems.SUPER_CRITICAL.get(), 9600);
                                    }
                                } else {
                                    if (index_superCritical != -1) {
                                        ItemStack super_critical = player.getInventory().getItem(index_superCritical);
                                        if (super_critical.hasTag()) {
                                            super_critical.removeTagKey("magicmod.super_critical_cast");
                                        }
                                    }
                                }
                            }

                            if (info.getWater()) {
                                int index_aquamarineBlessing = InventoryUtil.getFirstInventoryIndex(player, ModItems.AQUAMARINE_BLESSING.get());
                                if (cast.getAquamarineBlessingCasting()) {
                                    if (index_aquamarineBlessing != -1) {
                                        ItemStack aquamarine_blessing = player.getInventory().getItem(index_aquamarineBlessing);
                                        if (!aquamarine_blessing.hasTag()) {
                                            CompoundTag nbtData = new CompoundTag();
                                            nbtData.putString("magicmod.aquamarine_blessing_cast", "Aquamarine Blessing has been cast");
                                            aquamarine_blessing.setTag(nbtData);
                                        }
                                    }

                                    if (cast.getAquamarineBlessingTick() == 0) {
                                        if (mana.getMana() >= 5) {
                                            mana.subMana(5);
                                            ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana(), mana.getMaxMana()), (ServerPlayer) player);
                                            cast.addAquamarineBlessingTick(1);
                                        } else {
                                            player.removeEffect(MobEffects.CONDUIT_POWER);
                                            player.removeEffect(MobEffects.DOLPHINS_GRACE);
                                            cast.setAquamarineBlessingCasting(false);
                                            cast.setAquamarineBlessingTick(0);
                                            player.sendSystemMessage(Component.literal("Mana depleted!").withStyle(ChatFormatting.DARK_AQUA));
                                        }
                                    }
                                    else if (cast.getAquamarineBlessingTick() >= 600) {
                                        cast.setAquamarineBlessingTick(0);
                                    } else {
                                        cast.addAquamarineBlessingTick(1);
                                    }

                                    if (player.getLevel().random.nextInt(100) == 0) {
                                        player.getLevel().playSound(null, player, SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundSource.PLAYERS, 1.0F + player.getLevel().random.nextFloat(), 0.7F + (player.getLevel().random.nextFloat() * 0.4F));
                                    }

                                    if (player.getLevel().random.nextInt(2) == 0) {
                                        ((ServerLevel)player.getLevel()).sendParticles(ParticleTypes.SCRAPE, player.getX(), player.getY()+1, player.getZ(), 1,0.5D, 0.5D, 0.5D, 0.0D);
                                    }

                                } else {
                                    if (index_aquamarineBlessing != -1) {
                                        ItemStack aquamarine_blessing = player.getInventory().getItem(index_aquamarineBlessing);
                                        if (aquamarine_blessing.hasTag()) {
                                            aquamarine_blessing.removeTagKey("magicmod.aquamarine_blessing_cast");
                                        }
                                    }
                                }

                                if (cast.getSharkLungeCasting()) {
                                    if (cast.getSharkLungeTick() <= 100) {
                                        ((ServerLevel)player.getLevel()).sendParticles(ParticleTypes.FISHING, player.getX(), player.getY(), player.getZ(), 1,1.0D, 1.0D, 1.0D, 0.0D);
                                        cast.addSharkLungeTick(1);

                                        if (player.isOnGround()) {
                                            cast.setSharkLungeCasting(false);
                                            ModMessages.sendToPlayer(new SharkLungeSyncS2CPacket(cast.getSharkLungeCasting()), (ServerPlayer) player);

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
                                                                        float num = 14F;

                                                                        if (player.hasEffect(ModEffects.SPELL_STRENGTH_2.get())) {
                                                                            num += 6F;
                                                                        } else if (player.hasEffect(ModEffects.SPELL_STRENGTH.get())) {
                                                                            num += 3F;
                                                                        }

                                                                        if (player.hasEffect(ModEffects.SPELL_WEAKNESS_2.get())) {
                                                                            num -= 8F;
                                                                        } else if (player.hasEffect(ModEffects.SPELL_WEAKNESS.get())) {
                                                                            num -= 4F;
                                                                        }

                                                                        //player1.sendSystemMessage(Component.literal(Math.max(num, 0F) + ""));
                                                                        player2.hurt(ModDamageSource.sharkLunge(null, player), Math.max(num, 0F));

                                                                        if (player.getLevel().random.nextInt(2) == 0) {
                                                                            player2.addEffect(new MobEffectInstance(ModEffects.BLEED.get(), 200, 0, false, false, true));
                                                                        }

                                                                    }
                                                                });
                                                            });
                                                        } else {
                                                            float num = 14F;

                                                            if (player.hasEffect(ModEffects.SPELL_STRENGTH_2.get())) {
                                                                num += 6F;
                                                            } else if (player.hasEffect(ModEffects.SPELL_STRENGTH.get())) {
                                                                num += 3F;
                                                            }

                                                            if (player.hasEffect(ModEffects.SPELL_WEAKNESS_2.get())) {
                                                                num -= 8F;
                                                            } else if (player.hasEffect(ModEffects.SPELL_WEAKNESS.get())) {
                                                                num -= 4F;
                                                            }

                                                            //player1.sendSystemMessage(Component.literal(Math.max(num, 0F) + ""));
                                                            entity1.hurt(ModDamageSource.sharkLunge(null, player), Math.max(num, 0F));

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

                                            player.getCooldowns().addCooldown(ModItems.SHARK_LUNGE.get(), 400);
                                        }
                                    } else {
                                        cast.setSharkLungeCasting(false);
                                        ModMessages.sendToPlayer(new SharkLungeSyncS2CPacket(cast.getSharkLungeCasting()), (ServerPlayer) player);

                                        cast.setSharkLungeTick(0);
                                        player.getCooldowns().addCooldown(ModItems.SHARK_LUNGE.get(), 400);
                                    }
                                }

                                int index_amphibious = InventoryUtil.getFirstInventoryIndex(player, ModItems.AMPHIBIOUS.get());
                                if (cast.getAmphibiousCasting()) {
                                    if (index_amphibious != -1) {
                                        ItemStack amphibious = player.getInventory().getItem(index_amphibious);
                                        if (!amphibious.hasTag()) {
                                            CompoundTag nbtData = new CompoundTag();
                                            nbtData.putString("magicmod.amphibious_cast", "Amphibious has been cast");
                                            amphibious.setTag(nbtData);
                                        }
                                    }

                                    if (player.isInWater()) {
                                        if (!player.hasEffect(ModEffects.SPELL_STRENGTH_2.get()) || !player.hasEffect(MobEffects.REGENERATION)) {
                                            player.removeEffect(MobEffects.REGENERATION);
                                            player.removeEffect(ModEffects.SPELL_STRENGTH.get());

                                            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 72000, 1, false, false, true));
                                            player.addEffect(new MobEffectInstance(ModEffects.SPELL_STRENGTH_2.get(), 72000, 0, false, false, true));
                                        }
                                    } else {
                                        if (!player.hasEffect(ModEffects.SPELL_STRENGTH.get()) || !player.hasEffect(MobEffects.REGENERATION)) {
                                            player.removeEffect(MobEffects.REGENERATION);
                                            player.removeEffect(ModEffects.SPELL_STRENGTH_2.get());

                                            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 72000, 0, false, false, true));
                                            player.addEffect(new MobEffectInstance(ModEffects.SPELL_STRENGTH.get(), 72000, 0, false, false, true));
                                        }
                                    }

                                    if (cast.getAmphibiousTick() == 0) {
                                        if (mana.getMana() >= 5) {
                                            mana.subMana(5);
                                            ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana(), mana.getMaxMana()), (ServerPlayer) player);
                                            cast.addAmphibiousTick(1);
                                        } else {
                                            player.removeEffect(MobEffects.REGENERATION);
                                            player.removeEffect(ModEffects.SPELL_STRENGTH.get());
                                            player.removeEffect(ModEffects.SPELL_STRENGTH_2.get());

                                            cast.setAmphibiousCasting(false);
                                            cast.setAmphibiousTick(0);
                                            player.sendSystemMessage(Component.literal("Mana depleted!").withStyle(ChatFormatting.DARK_AQUA));
                                        }
                                    }
                                    else if (cast.getAmphibiousTick() >= 100) {
                                        cast.setAmphibiousTick(0);
                                    } else {
                                        cast.addAmphibiousTick(1);
                                    }

                                    ((ServerLevel)player.getLevel()).sendParticles(ParticleTypes.FALLING_HONEY, player.getX(), player.getY()+1, player.getZ(), 1,0.5D, 0.5D, 0.5D, 0.0D);

                                } else {
                                    if (index_amphibious != -1) {
                                        ItemStack amphibious = player.getInventory().getItem(index_amphibious);
                                        if (amphibious.hasTag()) {
                                            amphibious.removeTagKey("magicmod.amphibious_cast");
                                        }
                                    }
                                }
                            }

                            if (info.getAir()) {

//                                player.sendSystemMessage(Component.literal("" + cast.getAirDartsCasting()).withStyle(ChatFormatting.YELLOW));
//                                player.sendSystemMessage(Component.literal("" + cast.getAirDartsProjectiles()).withStyle(ChatFormatting.YELLOW));

                                if (cast.getAirDartsCasting()) {
                                    if (cast.getAirDartsProjectiles() == 0) {
                                        player.getCooldowns().addCooldown(ModItems.AIR_DARTS.get(), 1200);

                                        int index_airDart = InventoryUtil.getFirstInventoryIndex(player, ModItems.AIR_DART.get());
                                        while (index_airDart != -1) {
                                            player.getInventory().removeItem(index_airDart, 16);
                                            index_airDart = InventoryUtil.getFirstInventoryIndex(player, ModItems.AIR_DART.get());
                                        }

                                        cast.setAirDartsCasting(false);
                                    }
                                }

                                if (cast.getWingsOfQuartzCasting()) {
                                    if (cast.getWingsOfQuartzTick() <= 120) {

                                        if (cast.getWingsOfQuartzTick() % 4 == 0) {
                                            double xRot = (double) player.getYHeadRot();

                                            for (int i = 1; i < 11; i++) {
                                                double a = i/10D + 0.5D;
                                                player.getLevel().sendParticles(ParticleTypes.END_ROD, player.getX() + (-1 * Math.sin(Math.toRadians(xRot + 145D)) * a), player.getY() + 1.5D, player.getZ() + (Math.cos(Math.toRadians(xRot + 145D)) * a), 1, 0.0D, i/18D, 0.0D, 0.0D);
                                                player.getLevel().sendParticles(ParticleTypes.END_ROD, player.getX() + (-1 * Math.sin(Math.toRadians(xRot - 145D)) * a), player.getY() + 1.5D, player.getZ() + (Math.cos(Math.toRadians(xRot - 145D)) * a), 1, 0.0D, i/18D, 0.0D, 0.0D);
                                            }
                                        }

                                        cast.addWingsOfQuartzTick(1);
                                    } else {
                                        cast.setWingsOfQuartzCasting(false);
                                        cast.setWingsOfQuartzTick(0);
                                        player.getAbilities().flying = false;
                                        player.getAbilities().mayfly = false;
                                        player.onUpdateAbilities();

                                        player.level.playSound(null, player, ModSounds.WINGS_OF_QUARTZ.get(), SoundSource.PLAYERS, 1.0F, 0.7F);
                                    }
                                }
                            }

                            if (info.getEarth()) {

                                //player.sendSystemMessage(Component.literal(player.getHealth() + "/" + player.getMaxHealth()).withStyle(ChatFormatting.YELLOW));
                                //player.sendSystemMessage(Component.literal("" + player.getAttribute(Attributes.ARMOR).).withStyle(ChatFormatting.YELLOW));

                                int index_rockForm = InventoryUtil.getFirstInventoryIndex(player, ModItems.ROCK_FORM.get());
                                if (cast.getRockFormCasting()) {
                                    if (cast.getRockFormContinue()) {
                                        if (index_rockForm != -1) {
                                            ItemStack rock_form = player.getInventory().getItem(index_rockForm);
                                            if (!rock_form.hasTag()) {
                                                CompoundTag nbtData = new CompoundTag();
                                                nbtData.putString("magicmod.rock_form_cast", "Rock Form has been cast");
                                                rock_form.setTag(nbtData);
                                            }
                                        }
                                    } else {
                                        if (index_rockForm != -1) {
                                            ItemStack rock_form = player.getInventory().getItem(index_rockForm);
                                            if (rock_form.hasTag()) {
                                                rock_form.removeTagKey("magicmod.rock_form_cast");
                                            }
                                        }
                                    }

                                    if (cast.getRockFormTick() == 0) {
                                        if (!cast.getRockFormContinue()) {

                                            player.getAttribute(Attributes.MAX_HEALTH).removeModifier(UUID.fromString("00925d7d-6725-4531-8598-014d3bb1e081"));
                                            if (player.getHealth() > player.getMaxHealth()) {
                                                player.setHealth(player.getMaxHealth());
                                            }

                                            player.getAttribute(Attributes.ARMOR).removeModifier(UUID.fromString("feda2c47-ca5a-4f21-bf7b-dab1d843bd2a"));

                                            int index_rockFist = InventoryUtil.getFirstInventoryIndex(player, ModItems.ROCK_FIST.get());
                                            while (index_rockFist != -1) {
                                                player.getInventory().removeItem(index_rockFist, 1);
                                                index_rockFist = InventoryUtil.getFirstInventoryIndex(player, ModItems.ROCK_FIST.get());
                                            }

                                            player.level.playSound(null, player, SoundEvents.IRON_GOLEM_DAMAGE, SoundSource.PLAYERS, 1.0F, player.getLevel().random.nextFloat() * 0.1F);
                                            player.level.playSound(null, player, SoundEvents.IRON_GOLEM_DAMAGE, SoundSource.PLAYERS, 1.0F, player.getLevel().random.nextFloat() * 0.1F);
                                            player.level.playSound(null, player, SoundEvents.IRON_GOLEM_DAMAGE, SoundSource.PLAYERS, 1.0F, player.getLevel().random.nextFloat() * 0.1F);

                                            cast.setRockFormCasting(false);
                                            cast.setRockFormContinue(false);
                                            cast.setRockFormTick(0);

                                        } else if (mana.getMana() >= 30) {
                                            mana.subMana(30);
                                            ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana(), mana.getMaxMana()), (ServerPlayer) player);
                                            cast.addRockFormTick(1);
                                        } else {

                                            player.getAttribute(Attributes.MAX_HEALTH).removeModifier(UUID.fromString("00925d7d-6725-4531-8598-014d3bb1e081"));
                                            if (player.getHealth() > player.getMaxHealth()) {
                                                player.setHealth(player.getMaxHealth());
                                            }

                                            player.getAttribute(Attributes.ARMOR).removeModifier(UUID.fromString("feda2c47-ca5a-4f21-bf7b-dab1d843bd2a"));

                                            int index_rockFist = InventoryUtil.getFirstInventoryIndex(player, ModItems.ROCK_FIST.get());
                                            while (index_rockFist != -1) {
                                                player.getInventory().removeItem(index_rockFist, 1);
                                                index_rockFist = InventoryUtil.getFirstInventoryIndex(player, ModItems.ROCK_FIST.get());
                                            }

                                            player.level.playSound(null, player, SoundEvents.IRON_GOLEM_DAMAGE, SoundSource.PLAYERS, 0.5F, player.getLevel().random.nextFloat() * 0.1F);
                                            player.level.playSound(null, player, SoundEvents.IRON_GOLEM_DAMAGE, SoundSource.PLAYERS, 0.5F, player.getLevel().random.nextFloat() * 0.1F);
                                            player.level.playSound(null, player, SoundEvents.IRON_GOLEM_DAMAGE, SoundSource.PLAYERS, 0.5F, player.getLevel().random.nextFloat() * 0.1F);

                                            cast.setRockFormCasting(false);
                                            cast.setRockFormContinue(false);
                                            cast.setRockFormTick(0);
                                            player.sendSystemMessage(Component.literal("Mana depleted!").withStyle(ChatFormatting.DARK_AQUA));
                                        }
                                    }
                                    else if (cast.getRockFormTick() >= 600) {
                                        cast.setRockFormTick(0);
                                    } else {
                                        cast.addRockFormTick(1);
                                    }

                                    if (player.getLevel().random.nextInt(100) == 0) {
                                        player.getLevel().playSound(null, player, SoundEvents.GRAVEL_STEP, SoundSource.PLAYERS, 0.5F, player.getLevel().random.nextFloat() * 0.7F);
                                    }

                                    ((ServerLevel)player.getLevel()).sendParticles(ParticleTypes.MYCELIUM, player.getX(), player.getY()+1, player.getZ(), 2,0.5D, 0.5D, 0.5D, 0.0D);

                                } else {
                                    if (index_rockForm != -1) {
                                        ItemStack rock_form = player.getInventory().getItem(index_rockForm);
                                        if (rock_form.hasTag()) {
                                            rock_form.removeTagKey("magicmod.rock_form_cast");
                                        }
                                    }
                                }

                                //One with Stone
                                if (info.getSchoolLevel() >= 2 && player.getY() < 32) {
                                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 0, false, false, true));
                                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 0, false, false, true));
                                }

                            }

                        });
                    });
                });
            }
        }

        @SubscribeEvent
        public static void onPlayerDrop(LivingDropsEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                ItemEntity[] droppedItems = new ItemEntity[event.getDrops().size()];
                ItemEntity[] finalDroppedItems = event.getDrops().toArray(droppedItems);
                player.getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(cast -> {
                    player.getCapability(PlayerDropsProvider.PLAYER_DROPS).ifPresent(drops -> {
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
                            } else if (finalDroppedItems[i].getItem().getItem() == ModItems.AQUAMARINE_BLESSING.get()) {
                                finalDroppedItems[i].kill();
                                drops.addDropNumber(9);
                            } else if (finalDroppedItems[i].getItem().getItem() == ModItems.SHARK_LUNGE.get()) {
                                finalDroppedItems[i].kill();
                                drops.addDropNumber(10);
                            } else if (finalDroppedItems[i].getItem().getItem() == ModItems.AMPHIBIOUS.get()) {
                                finalDroppedItems[i].kill();
                                drops.addDropNumber(11);
                            } else if (finalDroppedItems[i].getItem().getItem() == ModItems.WATERFALL.get()) {
                                finalDroppedItems[i].kill();
                                drops.addDropNumber(12);
                            } else if (finalDroppedItems[i].getItem().getItem() == ModItems.GALEFORCE.get()) {
                                finalDroppedItems[i].kill();
                                drops.addDropNumber(13);
                            } else if (finalDroppedItems[i].getItem().getItem() == ModItems.YEET.get()) {
                                finalDroppedItems[i].kill();
                                drops.addDropNumber(14);
                            } else if (finalDroppedItems[i].getItem().getItem() == ModItems.AIR_DARTS.get()) {
                                finalDroppedItems[i].kill();
                                drops.addDropNumber(15);
                            } else if (finalDroppedItems[i].getItem().getItem() == ModItems.AIR_DART.get()) {
                                cast.subAirDartsProjectiles(finalDroppedItems[i].getItem().getCount());
                                finalDroppedItems[i].kill();
                            } else if (finalDroppedItems[i].getItem().getItem() == ModItems.TOSS.get()) {
                                finalDroppedItems[i].kill();
                                drops.addDropNumber(16);
                            } else if (finalDroppedItems[i].getItem().getItem() == ModItems.WINGS_OF_QUARTZ.get()) {
                                finalDroppedItems[i].kill();
                                drops.addDropNumber(17);
                            } else if (finalDroppedItems[i].getItem().getItem() == ModItems.WEIGHT_OF_PYRITE.get()) {
                                finalDroppedItems[i].kill();
                                drops.addDropNumber(18);
                            } else if (finalDroppedItems[i].getItem().getItem() == ModItems.BURROW.get()) {
                                finalDroppedItems[i].kill();
                                drops.addDropNumber(19);
                            } else if (finalDroppedItems[i].getItem().getItem() == ModItems.ROCK_FORM.get()) {
                                finalDroppedItems[i].kill();
                                drops.addDropNumber(20);
                            } else if (finalDroppedItems[i].getItem().getItem() == ModItems.ROCK_FIST.get()) {
                                finalDroppedItems[i].kill();
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
                        cd.setSharkLungeCD(player.getCooldowns().getCooldownPercent(ModItems.SHARK_LUNGE.get(), 0.0F));
                        cd.setWaterfallCD(player.getCooldowns().getCooldownPercent(ModItems.WATERFALL.get(), 0.0F));
                        cd.setGaleforceCD(player.getCooldowns().getCooldownPercent(ModItems.GALEFORCE.get(), 0.0F));
                        cd.setYeetCD(player.getCooldowns().getCooldownPercent(ModItems.YEET.get(), 0.0F));
                        if (cast.getAirDartsCasting() && cast.getAirDartsProjectiles() == 0) {
                            cd.setAirDartsCD(1.0F);
                        } else {
                            cd.setAirDartsCD(player.getCooldowns().getCooldownPercent(ModItems.AIR_DARTS.get(), 0.0F));
                        }
                        cd.setTossCD(player.getCooldowns().getCooldownPercent(ModItems.TOSS.get(), 0.0F));
                        cd.setWingsOfQuartzCD(player.getCooldowns().getCooldownPercent(ModItems.WINGS_OF_QUARTZ.get(), 0.0F));
                        cd.setWeightOfPyriteCD(player.getCooldowns().getCooldownPercent(ModItems.WEIGHT_OF_PYRITE.get(), 0.0F));
                        cd.setBurrowCD(player.getCooldowns().getCooldownPercent(ModItems.BURROW.get(), 0.0F));
                    });
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
