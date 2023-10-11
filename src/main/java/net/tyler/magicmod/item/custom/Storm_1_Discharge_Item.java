package net.tyler.magicmod.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tyler.magicmod.capability.casting.PlayerCastingProvider;
import net.tyler.magicmod.capability.info.PlayerInfoProvider;
import net.tyler.magicmod.capability.mana.PlayerManaProvider;
import net.tyler.magicmod.effect.ModEffects;
import net.tyler.magicmod.misc.ModDamageSource;
import net.tyler.magicmod.networking.ModMessages;
import net.tyler.magicmod.networking.packet.ManaDataSyncS2CPacket;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Storm_1_Discharge_Item extends Item {

    private int manaCost = 35;

    private List<Entity> target = new ArrayList<Entity>();

    public Storm_1_Discharge_Item(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
            player.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info -> {
                player.getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(cast -> {
                    if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
                        if (info.getStorm()) {
                            if (mana.getMana() >= manaCost) {

                                float f2 = 10F;
                                int k1 = Mth.floor(player.getX() - (double)f2 - 1.0D);
                                int l1 = Mth.floor(player.getX() + (double)f2 + 1.0D);
                                int i2 = Mth.floor(player.getY() - (double)f2 - 1.0D);
                                int i1 = Mth.floor(player.getY() + (double)f2 + 1.0D);
                                int j2 = Mth.floor(player.getZ() - (double)f2 - 1.0D);
                                int j1 = Mth.floor(player.getZ() + (double)f2 + 1.0D);
                                List<Entity> list = player.level.getEntities(player, new AABB((double)k1, (double)i2, (double)j2, (double)l1, (double)i1, (double)j1));
                                Vec3 vec3 = new Vec3(player.getX(), player.getY(), player.getZ());

                                for(int k2 = 0; k2 < list.size(); ++k2) {
                                    Entity entity = list.get(k2);
                                    if (entity instanceof LivingEntity entity1) {
                                        double d12 = Math.sqrt(entity1.distanceToSqr(vec3)) / (double)f2;
                                        if (d12 <= 1.0D) {
                                            if (entity1 instanceof Player player2) {
                                                player.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info1 -> {
                                                    player2.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info2 -> {
                                                        if (!info1.getDungeonParty() || !info2.getDungeonParty()) {
                                                            target.add(player2);
                                                        }
                                                    });
                                                });
                                            } else if (entity1 instanceof Enemy) {
                                                target.add(entity1);
                                            }
                                        }
                                    }
                                }

                                if (!target.isEmpty()) {
                                    mana.subMana(manaCost);
                                    ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana(), mana.getMaxMana()), (ServerPlayer) player);

                                    if (player.level.isRaining() && player.level.isThundering()) {
                                        attack(player);
                                        attack(player);
                                        attack(player);
                                    } else {
                                        attack(player);
                                        attack(player);
                                    }

                                    target.clear();

                                    player.level.playSound(null, player, SoundEvents.GUARDIAN_ATTACK, SoundSource.PLAYERS, 1.0F, 1.5F + (player.getLevel().random.nextFloat() * 0.3F));
                                    player.level.playSound(null, player, SoundEvents.GUARDIAN_ATTACK, SoundSource.PLAYERS, 1.0F, 1.5F + (player.getLevel().random.nextFloat() * 0.3F));
                                    player.level.playSound(null, player, SoundEvents.GUARDIAN_ATTACK, SoundSource.PLAYERS, 1.0F, 1.5F + (player.getLevel().random.nextFloat() * 0.3F));

                                    player.getCooldowns().addCooldown(this, 800);
                                }

                            } else {
                                player.sendSystemMessage(Component.literal("Not enough mana!").withStyle(ChatFormatting.DARK_AQUA));
                            }
                        } else {
                            player.sendSystemMessage(Component.literal("You don't understand the runes for this spell!").withStyle(ChatFormatting.YELLOW));
                        }
                    }
                });
            });
        });

        return super.use(level, player, hand);
    }

    private void attack(Player player) {
        if (!target.isEmpty()) {

            int removeIndex = 0;

            if (target.size() > 1) {
                Vec3 vec3 = new Vec3(player.getX(), player.getY(), player.getZ());

                for (int i = 1; i < target.size(); i++) {
                    if (Math.sqrt(target.get(i).distanceToSqr(vec3)) < Math.sqrt(target.get(removeIndex).distanceToSqr(vec3))) {
                        removeIndex = i;
                    }
                }
            }

            Entity entity = target.remove(removeIndex);

            LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(player.level);
            lightningbolt.moveTo(entity.getX(), entity.getY(), entity.getZ());
            lightningbolt.setVisualOnly(true);
            player.level.addFreshEntity(lightningbolt);

            damage(player, (LivingEntity) entity);
        }
    }

    private void damage(Player player, LivingEntity entity) {

        float num = 12F;

        if (player.hasEffect(ModEffects.SPELL_STRENGTH_2.get())) {
            num += 6F;
        } else if (player.hasEffect(ModEffects.SPELL_STRENGTH.get())) {
            num += 3F;
        }

        if (player.hasEffect(ModEffects.SPELL_WEAKNESS_2.get())) {
            num -= 8F;
        } else if (player.hasEffect(ModEffects.SPELL_WEAKNESS.get())) {
            num -= 4F;
        }

        entity.setSecondsOnFire(3);

        //player1.sendSystemMessage(Component.literal(Math.max(num, 0F) + ""));
        entity.hurt(ModDamageSource.discharge(null, player), Math.max(num, 0F));

        BlockPos blockpos = new BlockPos(entity.getX(), entity.getY(), entity.getZ());
        if (player.level.getBlockState(blockpos).isAir() && player.level.getBlockState(blockpos.below()).isSolidRender(player.level, blockpos.below())) {
            player.level.setBlockAndUpdate(blockpos, BaseFireBlock.getState(player.level, blockpos));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            components.add(Component.literal("Mana Cost: 35\nCooldown Time: 40 seconds\nDamage per Bolt: 12\nFire Duration: 3 seconds\n\nRight click to release your stored charge at your enemies!").withStyle(ChatFormatting.DARK_BLUE));
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.YELLOW));
        }


        super.appendHoverText(stack, level, components, flag);
    }
}
