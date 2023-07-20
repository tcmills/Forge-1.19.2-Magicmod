package net.tyler.magicmod.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.tyler.magicmod.capability.casting.PlayerCastingProvider;
import net.tyler.magicmod.capability.cooldowns.PlayerCooldownsProvider;
import net.tyler.magicmod.capability.info.PlayerInfoProvider;
import net.tyler.magicmod.capability.mana.PlayerManaProvider;
import net.tyler.magicmod.effect.ModEffects;
import net.tyler.magicmod.item.ModItems;
import net.tyler.magicmod.misc.MagicalExplosion;
import net.tyler.magicmod.networking.ModMessages;
import net.tyler.magicmod.networking.packet.ManaDataSyncS2CPacket;
import net.tyler.magicmod.sound.ModSounds;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ManaMitigator_Item extends Item {

    public ManaMitigator_Item(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        player.getCapability(PlayerCooldownsProvider.PLAYER_COOLDOWNS).ifPresent(cd -> {
            cd.clearCD();
        });

        player.getCooldowns().removeCooldown(ModItems.MAGIC_MISSILE.get());
        player.getCooldowns().removeCooldown(ModItems.AID.get());
        player.getCooldowns().removeCooldown(ModItems.TELEPORT.get());
        player.getCooldowns().removeCooldown(ModItems.TELEPORT_HOME.get());
        player.getCooldowns().removeCooldown(ModItems.FLARE_BLITZ.get());
        player.getCooldowns().removeCooldown(ModItems.SCORCHING_RAY.get());
        player.getCooldowns().removeCooldown(ModItems.FIREBALL.get());
        player.getCooldowns().removeCooldown(ModItems.SUPER_CRITICAL.get());
        player.getCooldowns().removeCooldown(ModItems.SHARK_LUNGE.get());
        player.getCooldowns().removeCooldown(ModItems.WATERFALL.get());

        player.level.playSound(null, player, SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.8F);
        player.level.playSound(null, player, SoundEvents.BOAT_PADDLE_LAND, SoundSource.PLAYERS, 1.0F, 0.7F);

        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        components.add(Component.literal("Dev Item").withStyle(ChatFormatting.GREEN));

        super.appendHoverText(stack, level, components, flag);
    }
}
