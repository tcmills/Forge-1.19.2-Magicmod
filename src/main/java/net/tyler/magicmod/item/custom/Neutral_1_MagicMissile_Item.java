package net.tyler.magicmod.item.custom;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tyler.magicmod.mana.PlayerManaProvider;
import net.tyler.magicmod.networking.ModMessages;
import net.tyler.magicmod.networking.packet.ManaDataSyncS2CPacket;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Neutral_1_MagicMissile_Item extends Item {
    public Neutral_1_MagicMissile_Item(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
            if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND && mana.getMana() >= 10) {
                mana.subMana(10);
                ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana()), (ServerPlayer) player);
                shootBolt(player);
                player.getCooldowns().addCooldown(this, 80);
            }
        });

        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            components.add(Component.literal("Right click to shoot!").withStyle(ChatFormatting.AQUA));
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.YELLOW));
        }


        super.appendHoverText(stack, level, components, flag);
    }

    private void shootBolt(Player player) {

        Arrow arrow = new Arrow(EntityType.ARROW, player.level);

        double x = player.getX() + player.getLookAngle().x;
        double y = player.getY() + player.getLookAngle().y;
        double z = player.getZ() + player.getLookAngle().z;

        arrow.setPos(x, y+1.5, z);

        arrow.setDeltaMovement(player.getLookAngle().x*4, player.getLookAngle().y*4, player.getLookAngle().z*4);

        player.level.addFreshEntity(arrow);

    }
}
