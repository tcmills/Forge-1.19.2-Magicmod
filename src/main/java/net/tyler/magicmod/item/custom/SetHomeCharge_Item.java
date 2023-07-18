package net.tyler.magicmod.item.custom;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.tyler.magicmod.capability.location.PlayerLocationProvider;

public class SetHomeCharge_Item extends Item {
    private static final int USE_DURATION = 1;

    public SetHomeCharge_Item(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        super.finishUsingItem(stack, level, entity);

        Player player = entity instanceof Player ? (Player)entity : null;

        if (!level.isClientSide() && entity instanceof ServerPlayer serverplayer) {
            serverplayer.getCapability(PlayerLocationProvider.PLAYER_LOCATION).ifPresent(location -> {
                location.setHome(serverplayer.getX(), serverplayer.getY(), serverplayer.getZ());
            });
        }

        if (player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        entity.gameEvent(GameEvent.EAT);
        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return USE_DURATION;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.level.playSound(null, player, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.PLAYERS, 1f, 1.7f);
        player.level.playSound(null, player, SoundEvents.FIREWORK_ROCKET_TWINKLE, SoundSource.PLAYERS, 1f, 1f);
        player.level.playSound(null, player, SoundEvents.BELL_RESONATE, SoundSource.PLAYERS, 1f, 2f);
        return ItemUtils.startUsingInstantly(level, player, hand);
    }
}
