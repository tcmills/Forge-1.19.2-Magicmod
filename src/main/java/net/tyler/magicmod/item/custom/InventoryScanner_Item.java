package net.tyler.magicmod.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.tyler.magicmod.capability.info.PlayerInfoProvider;
import net.tyler.magicmod.capability.mana.PlayerManaProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InventoryScanner_Item extends Item {

    public InventoryScanner_Item(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (!level.isClientSide()) {
            if(Screen.hasShiftDown()) {
                for(int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack currentStack = player.getInventory().getItem(i);
                    if (!currentStack.isEmpty()) {
                        player.sendSystemMessage(Component.literal("Slot " + i + ": " + currentStack).withStyle(ChatFormatting.YELLOW));
                    }
                }
            } else {
                Vec3 start = player.getEyePosition();
                Vec3 addition = player.getLookAngle().scale(10.0D);
                EntityHitResult result = ProjectileUtil.getEntityHitResult(
                        player.level,
                        player,
                        start,
                        start.add(addition),
                        player.getBoundingBox().inflate(10.0D),
                        (val) -> true);

                if (result.getEntity() != null && result.getEntity() instanceof Player player2) {
                    player.sendSystemMessage(Component.literal("Player Name: " + player2.getName().getString()).withStyle(ChatFormatting.YELLOW));
                    for(int i = 0; i < player2.getInventory().getContainerSize(); i++) {
                        ItemStack currentStack = player2.getInventory().getItem(i);
                        if (!currentStack.isEmpty()) {
                            player.sendSystemMessage(Component.literal("Slot " + i + ": " + currentStack).withStyle(ChatFormatting.YELLOW));
                        }
                    }
                }
            }
        }

        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        components.add(Component.literal("Dev Item\nHold SHIFT to use this on yourself!").withStyle(ChatFormatting.GREEN));

        super.appendHoverText(stack, level, components, flag);
    }
}
