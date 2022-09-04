package net.tyler.magicmod.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tyler.magicmod.MagicMod;
import net.tyler.magicmod.item.ModItems;
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

}
