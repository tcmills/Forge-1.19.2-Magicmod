package net.tyler.magicmod.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.multiplayer.chat.report.ReportEnvironment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tyler.magicmod.MagicMod;
import net.tyler.magicmod.item.ModItems;
import net.tyler.magicmod.mana.PlayerMana;
import net.tyler.magicmod.mana.PlayerManaProvider;
import net.tyler.magicmod.networking.ModMessages;
import net.tyler.magicmod.networking.packet.ManaDataSyncS2CPacket;
import net.tyler.magicmod.villager.ModVillagers;

import java.util.List;

@Mod.EventBusSubscriber(modid = MagicMod.MOD_ID)
public class ModEvents {
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
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerMana.class);
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide()) {
            if (event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
                    ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana(), mana.getMaxMana()), player);
                });
            }
        }
    }

}
