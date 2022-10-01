package net.tyler.magicmod.item.custom;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.tyler.magicmod.capability.info.PlayerInfoProvider;
import net.tyler.magicmod.networking.ModMessages;
import net.tyler.magicmod.networking.packet.InfoDataSyncS2CPacket;

public class DungeonBell_Item extends Item {
    private static final int USE_DURATION = 1;

    public DungeonBell_Item(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        super.finishUsingItem(stack, level, entity);

        if (!level.isClientSide() && entity instanceof ServerPlayer serverplayer) {
            serverplayer.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info -> {

                if (info.getDungeonParty()) {
                    info.leaveDungeonParty();
                }
                else {
                    info.joinDungeonParty();
                }

                ModMessages.sendToPlayer(new InfoDataSyncS2CPacket(info.getSchoolLevel(), info.getFire(),
                        info.getWater(), info.getEarth(), info.getAir(), info.getSummoning(), info.getForge(),
                        info.getStorm(), info.getEnder(), info.getLife(), info.getDeath(), info.getSun(),
                        info.getMoon(), info.getDungeonParty()), serverplayer);
            });
        }
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
        player.level.playSound(null, player, SoundEvents.BELL_BLOCK, SoundSource.PLAYERS, 1f, 1.5f);
        player.level.playSound(null, player, SoundEvents.BELL_RESONATE, SoundSource.PLAYERS, 1f, 2f);
        return ItemUtils.startUsingInstantly(level, player, hand);
    }
}
