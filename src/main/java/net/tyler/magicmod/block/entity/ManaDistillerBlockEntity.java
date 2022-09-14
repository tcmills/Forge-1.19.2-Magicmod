package net.tyler.magicmod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.tyler.magicmod.block.custom.ManaDistillerBlock;
import net.tyler.magicmod.item.ModItems;
import net.tyler.magicmod.screen.ManaDistillerMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ManaDistillerBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0 -> stack.getItem() == ModItems.ARCANE_POWDER.get();
                case 1 -> stack.getItem() == ModItems.MANA_CRYSTAL.get();
                case 2 -> stack.getItem() == PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER).getItem();
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(Direction.UP, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> false,
                            (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s))),

                    Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 2,
                            (i, s) -> itemHandler.isItemValid(1, s) || itemHandler.isItemValid(2, s))),

                    Direction.NORTH, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> false,
                            (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(2, s))),

                    Direction.SOUTH, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> false,
                            (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(2, s))),

                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> false,
                            (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(2, s))),

                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> false,
                            (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(2, s))));

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;

    public ManaDistillerBlockEntity(BlockPos blockPos, BlockState state) {
        super(ModBlockEntities.MANA_DISTILLER.get(), blockPos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> ManaDistillerBlockEntity.this.progress;
                    case 1 -> ManaDistillerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> ManaDistillerBlockEntity.this.progress = value;
                    case 1 -> ManaDistillerBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Mana Distiller");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ManaDistillerMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if(side == null) {
                return lazyItemHandler.cast();
            }

            if(directionWrappedHandlerMap.containsKey(side)) {
                Direction localDir = this.getBlockState().getValue(ManaDistillerBlock.FACING);

                if(side == Direction.UP || side == Direction.DOWN) {
                    return directionWrappedHandlerMap.get(side).cast();
                }

                return switch (localDir) {
                    default -> directionWrappedHandlerMap.get(side.getOpposite()).cast();
                    case EAST -> directionWrappedHandlerMap.get(side.getClockWise()).cast();
                    case SOUTH -> directionWrappedHandlerMap.get(side).cast();
                    case WEST -> directionWrappedHandlerMap.get(side.getCounterClockWise()).cast();
                };
            }
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("mana_distiller.progress", this.progress);

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("mana_distiller.progress");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ManaDistillerBlockEntity pEntity) {
        if (level.isClientSide()) {
            return;
        }

        if (hasRecipe(pEntity)) {
            pEntity.progress++;
            setChanged(level, pos, state);

            if (pEntity.progress >= pEntity.maxProgress) {
                craftItem(pEntity);
            }
        } else {
            pEntity.resetProgress();
            setChanged(level, pos, state);
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static void craftItem(ManaDistillerBlockEntity pEntity) {

        if (hasRecipe(pEntity)) {
            pEntity.itemHandler.extractItem(1, 1, false);
            pEntity.itemHandler.setStackInSlot(2, new ItemStack(ModItems.MANA_POTION.get(),
                    pEntity.itemHandler.getStackInSlot(2).getCount()));

            pEntity.resetProgress();
        }

    }

    private static boolean hasRecipe(ManaDistillerBlockEntity entity) {
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        boolean hasManaCrystalInFirstSlot = entity.itemHandler.getStackInSlot(1).getItem() == ModItems.MANA_CRYSTAL.get();

        return hasManaCrystalInFirstSlot && canInsertItemIntoOutputSlot(inventory,
                PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER));
    }
//    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
//        return inventory.getItem(2).getMaxStackSize() > inventory.getItem(2).getCount();
//    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack stack) {
        return inventory.getItem(2).getItem() == stack.getItem();
    }
}
