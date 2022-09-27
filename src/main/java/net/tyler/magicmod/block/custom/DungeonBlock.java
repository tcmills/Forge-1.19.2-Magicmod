package net.tyler.magicmod.block.custom;


import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.tyler.magicmod.info.PlayerInfoProvider;
import net.tyler.magicmod.networking.ModMessages;
import net.tyler.magicmod.networking.packet.InfoDataSyncS2CPacket;

public class DungeonBlock extends Block {
    public DungeonBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!level.isClientSide() && entity instanceof ServerPlayer serverplayer) {
            if (serverplayer.getRespawnDimension() != level.dimension() || !pos.equals(serverplayer.getRespawnPosition())) {
                serverplayer.setRespawnPosition(level.dimension(), pos.above(), 0.0F, true, true);
            }
            serverplayer.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info -> {
                info.joinDungeonParty();
                ModMessages.sendToPlayer(new InfoDataSyncS2CPacket(info.getSchoolLevel(), info.getFire(),
                        info.getWater(), info.getEarth(), info.getAir(), info.getSummoning(), info.getForge(),
                        info.getStorm(), info.getEnder(), info.getLife(), info.getDeath(), info.getSun(),
                        info.getMoon(), info.getDungeonParty()), serverplayer);
            });
        }

        super.stepOn(level, pos, state, entity);
    }
}
