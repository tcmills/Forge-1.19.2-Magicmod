package net.tyler.magicmod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class ManaCrystalLampBlock extends Block {
    public static final BooleanProperty LIT = BooleanProperty.create("lit");

    public ManaCrystalLampBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult result) {

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if (hand == InteractionHand.MAIN_HAND) {
                level.setBlock(blockPos, state.cycle(LIT), 3);
                player.level.playSound(null, blockPos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1f, 1f);
                return InteractionResult.CONSUME;
            }
        }

        return super.use(state, level, blockPos, player, hand, result);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos blockPos, RandomSource source) {
        super.animateTick(state, level, blockPos, source);
        if (!state.getValue(ManaCrystalLampBlock.LIT)) {
            level.addParticle(ParticleTypes.DOLPHIN, (double)blockPos.getX() + 0.5D + ((3*source.nextDouble()) - 1.5D), (double)blockPos.getY() + 0.5D + ((3*source.nextDouble()) - 1.5D), (double)blockPos.getZ() + 0.5D + ((3*source.nextDouble()) - 1.5D), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
}
