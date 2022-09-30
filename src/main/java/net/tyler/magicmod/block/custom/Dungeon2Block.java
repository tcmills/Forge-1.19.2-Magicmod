package net.tyler.magicmod.block.custom;


import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.tyler.magicmod.capability.info.PlayerInfoProvider;
import net.tyler.magicmod.networking.ModMessages;
import net.tyler.magicmod.networking.packet.InfoDataSyncS2CPacket;

public class Dungeon2Block extends Block {
    public Dungeon2Block(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!level.isClientSide() && entity instanceof ServerPlayer serverplayer) {
            if (serverplayer.getRespawnDimension() != level.dimension() || !pos.equals(serverplayer.getRespawnPosition())) {
                serverplayer.setRespawnPosition(null, null, 0.0F, true, true);
            }
            serverplayer.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info -> {
                info.leaveDungeonParty();
                ModMessages.sendToPlayer(new InfoDataSyncS2CPacket(info.getSchoolLevel(), info.getFire(),
                        info.getWater(), info.getEarth(), info.getAir(), info.getSummoning(), info.getForge(),
                        info.getStorm(), info.getEnder(), info.getLife(), info.getDeath(), info.getSun(),
                        info.getMoon(), info.getDungeonParty()), serverplayer);
            });
        }

        super.stepOn(level, pos, state, entity);
    }
}
