package net.tyler.magicmod.item.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tyler.magicmod.capability.casting.PlayerCastingProvider;
import net.tyler.magicmod.entity.custom.AirDartProjectileEntity;
import net.tyler.magicmod.item.ModItems;

public class AirDart_Item extends Item {

    public AirDart_Item(Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.5F, 0.6F + (player.getLevel().random.nextFloat() * 0.3F));

        player.getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(cast -> {
            if (!level.isClientSide) {
                AirDartProjectileEntity dart = new AirDartProjectileEntity(player, level);
                dart.setItem(ModItems.AIR_DART_PROJECTILE.get().getDefaultInstance());
                dart.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
                level.addFreshEntity(dart);

                cast.subAirDartsProjectiles(1);
            }
        });


        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}