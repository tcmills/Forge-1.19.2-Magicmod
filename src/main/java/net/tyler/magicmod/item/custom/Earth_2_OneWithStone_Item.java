package net.tyler.magicmod.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Earth_2_OneWithStone_Item extends Item {

    public Earth_2_OneWithStone_Item(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        components.add(Component.literal("Your connection to earth allows you to pull magic from the stone around you to strengthen your body!").withStyle(ChatFormatting.GRAY));


        super.appendHoverText(stack, level, components, flag);
    }
}
