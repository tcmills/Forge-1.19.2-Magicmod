package net.tyler.magicmod.event.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.tyler.magicmod.capability.info.PlayerInfoProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;

public class MagicFromFishingAdditionModifier extends LootModifier {

    public static final Supplier<Codec<MagicFromFishingAdditionModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(inst -> codecStart(inst)
                    .and(ResourceLocation.CODEC.fieldOf("loot_table").forGetter((m) -> m.lootTableId))
                    .apply(inst, MagicFromFishingAdditionModifier::new)));

    private final ResourceLocation lootTableId;

    public MagicFromFishingAdditionModifier(LootItemCondition[] conditionsIn, ResourceLocation lootTableId) {
        super(conditionsIn);
        this.lootTableId = lootTableId;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {

        if (context.getParam(LootContextParams.KILLER_ENTITY) instanceof Player player) {

            Map<Enchantment, Integer> enchant = EnchantmentHelper.getEnchantments(generatedLoot.get(0));
            if (!enchant.isEmpty()) {
                generatedLoot.clear();
                generatedLoot.add(new ItemStack(Items.COD));
                //player.sendSystemMessage(Component.literal("Enchanted").withStyle(ChatFormatting.YELLOW));
            }

            player.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info -> {
                if (info.getWater()) {
                    LootTable extraTable = context.getLootTable(this.lootTableId);
                    extraTable.getRandomItems(context, generatedLoot::add);
                }
            });
        }

        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}