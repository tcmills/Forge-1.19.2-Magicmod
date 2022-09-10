package net.tyler.magicmod.item.custom;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tyler.magicmod.MagicMod;
import net.tyler.magicmod.entity.ModEntityTypes;
import net.tyler.magicmod.entity.custom.MagicBoltEntity;
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
            if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND && mana.getMana() >= 5) {
                mana.subMana(5);
                ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana(), mana.getMaxMana()), (ServerPlayer) player);
                player.level.playSound(null, player, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1f, 1f);
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

        double x = player.getX() + player.getLookAngle().x;
        double y = player.getY() + player.getLookAngle().y;
        double z = player.getZ() + player.getLookAngle().z;

        MagicBoltEntity bolt = new MagicBoltEntity(ModEntityTypes.MAGIC_BOLT.get(), player.level);
        bolt.setPos(x, y+1.5, z);
        //bolt.setDeltaMovement(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z);
        bolt.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
        player.level.addFreshEntity(bolt);

        //Arrow arrow = new Arrow(EntityType.ARROW, player.level);
        //arrow.setPos(x, y+1.5, z);
        //arrow.setDeltaMovement(player.getLookAngle().x*4, player.getLookAngle().y*4, player.getLookAngle().z*4);
        //player.level.addFreshEntity(arrow);

    }
}
