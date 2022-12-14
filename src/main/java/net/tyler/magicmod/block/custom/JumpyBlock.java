package net.tyler.magicmod.block.custom;


import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class JumpyBlock extends Block {
    public JumpyBlock(Properties p_49795_) {
        super(p_49795_);
    }

//    @Override
//    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
//
//        if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
//            player.level.playSound(null, pos, SoundEvents.SLIME_BLOCK_BREAK, SoundSource.BLOCKS, 1f, 1f);
//        }
//
//        return super.use(state, level, pos, player, hand, blockHitResult);
//    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if(entity instanceof LivingEntity livingEntity){
            livingEntity.addEffect(new MobEffectInstance(MobEffects.JUMP, 10, 2, false, false));
        }

        super.stepOn(level, pos, state, entity);
    }
}
