package net.tyler.magicmod.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tyler.magicmod.MagicMod;
import net.tyler.magicmod.effect.ModEffects;
import net.tyler.magicmod.entity.ModEntityTypes;
import net.tyler.magicmod.entity.custom.WispEntity;
import net.tyler.magicmod.info.PlayerInfo;
import net.tyler.magicmod.info.PlayerInfoProvider;
import net.tyler.magicmod.item.ModItems;
import net.tyler.magicmod.mana.PlayerMana;
import net.tyler.magicmod.mana.PlayerManaProvider;
import net.tyler.magicmod.networking.ModMessages;
import net.tyler.magicmod.networking.packet.InfoDataSyncS2CPacket;
import net.tyler.magicmod.networking.packet.ManaDataSyncS2CPacket;
import net.tyler.magicmod.villager.ModVillagers;

import java.util.ArrayList;
import java.util.List;

public class ModEvents {

    @Mod.EventBusSubscriber(modid = MagicMod.MOD_ID)
    public static class ForgeEvents {
        private static ArrayList<Item> items = new ArrayList<>();

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

                if (items.size() > 0) {
                    for (int i = 0; i < items.size(); i++) {
                        event.getEntity().addItem(new ItemStack(items.get(i)));
                    }
                    items.clear();
                }

                event.getOriginal().invalidateCaps();
            }
        }

        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            event.register(PlayerMana.class);
            event.register(PlayerInfo.class);
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
                }
            }
        }

        @SubscribeEvent
        public static void onPlayerDamage(LivingDamageEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                player.addEffect(new MobEffectInstance(ModEffects.COMBAT.get(), 200, 0, false, false, true));
            }
        }

        @SubscribeEvent
        public static void onPlayerDrop(LivingDropsEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                ItemEntity[] droppedItems = new ItemEntity[event.getDrops().size()];
                droppedItems = event.getDrops().toArray(droppedItems);

                for (int i = 0; i < droppedItems.length; i++) {
                    if (droppedItems[i].getItem().getItem() == ModItems.MAGIC_MISSILE.get()) {
                        droppedItems[i].kill();
                        items.add(ModItems.MAGIC_MISSILE.get());
                    } else if (droppedItems[i].getItem().getItem() == ModItems.AID.get()) {
                        droppedItems[i].kill();
                        items.add(ModItems.AID.get());
                    } else if (droppedItems[i].getItem().getItem() == ModItems.TELEPORT.get()) {
                        droppedItems[i].kill();
                        items.add(ModItems.TELEPORT.get());
                    }
                }
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
