package net.tyler.magicmod.item.custom;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.tyler.magicmod.capability.casting.PlayerCastingProvider;
import net.tyler.magicmod.capability.info.PlayerInfoProvider;
import net.tyler.magicmod.capability.mana.PlayerManaProvider;
import net.tyler.magicmod.networking.ModMessages;
import net.tyler.magicmod.networking.packet.ManaDataSyncS2CPacket;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Earth_1_Burrow_Item extends Item {

    private int manaCost = 5;

    public Earth_1_Burrow_Item(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
            player.getCapability(PlayerInfoProvider.PLAYER_INFO).ifPresent(info -> {
                player.getCapability(PlayerCastingProvider.PLAYER_CASTING).ifPresent(cast -> {
                    if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
                        if (info.getEarth()) {
                            if (mana.getMana() >= manaCost) {
                                if (((ServerPlayer)player).gameMode.getGameModeForPlayer() != GameType.ADVENTURE && !info.getDungeonParty()) {

                                    BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

                                    if (blockhitresult.getType() == HitResult.Type.BLOCK) {
                                        BlockPos blockpos = blockhitresult.getBlockPos();

                                        if (checkBreakable(level.getBlockState(blockpos).getBlock())) {

                                            mana.subMana(manaCost);
                                            ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana(), mana.getMaxMana()), (ServerPlayer) player);

                                            ObjectArrayList<BlockPos> toBlow = getBreakableBlocks(level, blockpos, blockhitresult.getDirection());
                                            ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList<>();

                                            for(BlockPos blockPosBlow : toBlow) {
                                                BlockState blockstate = level.getBlockState(blockPosBlow);

                                                BlockPos blockpos1 = blockPosBlow.immutable();
                                                level.getProfiler().push("explosion_blocks");
                                                if (blockstate.canDropFromExplosion(level, blockPosBlow, null)) {
                                                    Level $$9 = level;
                                                    if ($$9 instanceof ServerLevel) {
                                                        ServerLevel serverlevel = (ServerLevel)$$9;
                                                        BlockEntity blockentity = blockstate.hasBlockEntity() ? level.getBlockEntity(blockPosBlow) : null;
                                                        LootContext.Builder lootcontext$builder = (new LootContext.Builder(serverlevel)).withRandom(level.random).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockPosBlow)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockentity).withOptionalParameter(LootContextParams.THIS_ENTITY, player);

                                                        blockstate.spawnAfterBreak(serverlevel, blockPosBlow, ItemStack.EMPTY, true);
                                                        blockstate.getDrops(lootcontext$builder).forEach((p_46074_) -> {
                                                            addBlockDrops(objectarraylist, p_46074_, blockpos1);
                                                        });
                                                    }
                                                }

                                                if (player.getLevel().random.nextInt(2) == 0) {
                                                    ((ServerLevel)player.getLevel()).sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, blockPosBlow.getX() + 0.5D, blockPosBlow.getY() + 0.5D, blockPosBlow.getZ() + 0.5D, 1,0.25D, 0.25D, 0.25D, 0.0D);
                                                }

                                                if (player.getLevel().random.nextInt(2) == 0) {
                                                    player.level.playSound(null, blockpos, SoundEvents.GRAVEL_BREAK, SoundSource.PLAYERS, 1.0F, 0.3F + player.getLevel().random.nextFloat());
                                                }

                                                blockstate.onBlockExploded(level, blockPosBlow, null);
                                                level.getProfiler().pop();
                                            }

                                            for(Pair<ItemStack, BlockPos> pair : objectarraylist) {
                                                Block.popResource(level, pair.getSecond(), pair.getFirst());
                                            }

//                                            player.level.playSound(null, blockpos, SoundEvents.GRAVEL_BREAK, SoundSource.PLAYERS, 2.0F, 0.3F + player.getLevel().random.nextFloat());
//                                            player.level.playSound(null, blockpos, SoundEvents.GRAVEL_BREAK, SoundSource.PLAYERS, 2.0F, 0.3F + player.getLevel().random.nextFloat());
//                                            player.level.playSound(null, blockpos, SoundEvents.GRAVEL_BREAK, SoundSource.PLAYERS, 2.0F, 0.3F + player.getLevel().random.nextFloat());

                                            ((ServerLevel)player.getLevel()).sendParticles(ParticleTypes.SWEEP_ATTACK, blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D, 5,1.0D, 1.0D, 1.0D, 0.0D);

                                            player.getCooldowns().addCooldown(this, 200);
                                        }
                                    }
                                } else {
                                    player.sendSystemMessage(Component.literal("You cannot break blocks in this area!").withStyle(ChatFormatting.YELLOW));
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

    private boolean checkBreakable(Block block) {
        return block == Blocks.STONE || block == Blocks.GRANITE || block == Blocks.DIORITE || block == Blocks.ANDESITE ||
                block == Blocks.DEEPSLATE || block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.GRAVEL ||
                block == Blocks.SAND || block == Blocks.SANDSTONE || block == Blocks.DRIPSTONE_BLOCK || block == Blocks.TERRACOTTA ||
                block == Blocks.RED_TERRACOTTA || block == Blocks.ORANGE_TERRACOTTA || block == Blocks.YELLOW_TERRACOTTA ||
                block == Blocks.BROWN_TERRACOTTA || block == Blocks.LIGHT_GRAY_TERRACOTTA || block == Blocks.WHITE_TERRACOTTA ||
                block == Blocks.CYAN_TERRACOTTA || block == Blocks.BLUE_TERRACOTTA || block == Blocks.BLACK_TERRACOTTA ||
                block == Blocks.GRAY_TERRACOTTA || block == Blocks.GREEN_TERRACOTTA || block == Blocks.LIGHT_BLUE_TERRACOTTA ||
                block == Blocks.LIME_TERRACOTTA || block == Blocks.PINK_TERRACOTTA || block == Blocks.MAGENTA_TERRACOTTA ||
                block == Blocks.PURPLE_TERRACOTTA || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL || block == Blocks.RED_SAND ||
                block == Blocks.RED_SANDSTONE || block == Blocks.ICE || block == Blocks.SNOW_BLOCK || block == Blocks.PACKED_ICE ||
                block == Blocks.BLUE_ICE || block == Blocks.POWDER_SNOW || block == Blocks.MUD || block == Blocks.MYCELIUM ||
                block == Blocks.ROOTED_DIRT || block == Blocks.INFESTED_STONE || block == Blocks.INFESTED_DEEPSLATE;
    }

    private ObjectArrayList<BlockPos> getBreakableBlocks(Level level, BlockPos blockPos, Direction direction) {

        Set<BlockPos> set = Sets.newHashSet();

        int blockPosX = 0;
        int blockPosY = 0;
        int blockPosZ = 0;

        int xDis = 0;
        int yDis = 0;
        int zDis = 0;

        if (direction == Direction.DOWN) {

            blockPosX = blockPos.getX() - 1;
            blockPosY = blockPos.getY();
            blockPosZ = blockPos.getZ() - 1;

            xDis = 3;
            yDis = 5;
            zDis = 3;

        } else if (direction == Direction.NORTH) {

            blockPosX = blockPos.getX() - 1;
            blockPosY = blockPos.getY() - 1;
            blockPosZ = blockPos.getZ();

            xDis = 3;
            yDis = 3;
            zDis = 5;

        } else if (direction == Direction.SOUTH) {

            blockPosX = blockPos.getX() - 1;
            blockPosY = blockPos.getY() - 1;
            blockPosZ = blockPos.getZ() - 4;

            xDis = 3;
            yDis = 3;
            zDis = 5;

        } else if (direction == Direction.EAST) {

            blockPosX = blockPos.getX() - 4;
            blockPosY = blockPos.getY() - 1;
            blockPosZ = blockPos.getZ() - 1;

            xDis = 5;
            yDis = 3;
            zDis = 3;

        } else if (direction == Direction.WEST) {

            blockPosX = blockPos.getX();
            blockPosY = blockPos.getY() - 1;
            blockPosZ = blockPos.getZ() - 1;

            xDis = 5;
            yDis = 3;
            zDis = 3;

        } else if (direction == Direction.UP) {

            blockPosX = blockPos.getX() - 1;
            blockPosY = blockPos.getY() - 4;
            blockPosZ = blockPos.getZ() - 1;

            xDis = 3;
            yDis = 5;
            zDis = 3;

        }

        for(int j = blockPosX; j < blockPosX + xDis; ++j) {
            for(int k = blockPosY; k < blockPosY + yDis; ++k) {
                for(int l = blockPosZ; l < blockPosZ + zDis; ++l) {
                    BlockPos blockPos1 = new BlockPos(j, k, l);
                    if (checkBreakable(level.getBlockState(blockPos1).getBlock())) {
                        set.add(blockPos1);
                    }
                }
            }
        }

        ObjectArrayList<BlockPos> toBlow = new ObjectArrayList<>();
        toBlow.addAll(set);

        return toBlow;
    }

    private static void addBlockDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> pDropPositionArray, ItemStack pStack, BlockPos pPos) {
        int i = pDropPositionArray.size();

        for(int j = 0; j < i; ++j) {
            Pair<ItemStack, BlockPos> pair = pDropPositionArray.get(j);
            ItemStack itemstack = pair.getFirst();
            if (ItemEntity.areMergable(itemstack, pStack)) {
                ItemStack itemstack1 = ItemEntity.merge(itemstack, pStack, 16);
                pDropPositionArray.set(j, Pair.of(itemstack1, pair.getSecond()));
                if (pStack.isEmpty()) {
                    return;
                }
            }
        }

        pDropPositionArray.add(Pair.of(pStack, pPos));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            components.add(Component.literal("Mana Cost: 5\nCooldown Time: 10 seconds\n\nRight click a block face to dig a 3x3x5 hole in that direction!").withStyle(ChatFormatting.GRAY));
        } else if (Screen.hasControlDown()) {
            components.add(Component.literal("Stone, Granite, Diorite, Andesite, Deepslate, Grass Block, Dirt, Gravel, Sand, Sandstone, Dripstone Block, Terracotta (All Colors), Coarse Dirt, Podzol, Red Sand, Red Sandstone, Ice, Snow Block, Packed Ice, Blue Ice, Powder Snow, Mud, Mycelium, Rooted Dirt, Infested Stone, Infested Deepslate").withStyle(ChatFormatting.GRAY));
        } else {
            components.add(Component.literal("Press SHIFT for more info\nPress CTRL to see list of breakable blocks").withStyle(ChatFormatting.YELLOW));
        }


        super.appendHoverText(stack, level, components, flag);
    }
}
