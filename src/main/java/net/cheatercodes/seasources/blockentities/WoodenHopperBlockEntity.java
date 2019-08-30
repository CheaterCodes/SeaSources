package net.cheatercodes.seasources.blockentities;

import net.cheatercodes.seasources.SeaSources;
import net.cheatercodes.seasources.blocks.WoodenHopperBlock;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.container.Container;
import net.minecraft.container.HopperContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.BooleanBiFunction;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WoodenHopperBlockEntity extends LootableContainerBlockEntity implements Hopper, Tickable {
    private DefaultedList<ItemStack> inventory;
    private int transferCooldown;
    private long lastTickTime;

    public WoodenHopperBlockEntity() {
        super(SeaSources.WOODEN_HOPPER_BLOCK_ENTITY);
        this.inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
        this.transferCooldown = -1;
    }

    public void fromTag(CompoundTag compoundTag_1) {
        super.fromTag(compoundTag_1);
        this.inventory = DefaultedList.ofSize(this.getInvSize(), ItemStack.EMPTY);
        if (!this.deserializeLootTable(compoundTag_1)) {
            Inventories.fromTag(compoundTag_1, this.inventory);
        }

        this.transferCooldown = compoundTag_1.getInt("TransferCooldown");
    }

    public CompoundTag toTag(CompoundTag compoundTag_1) {
        super.toTag(compoundTag_1);
        if (!this.serializeLootTable(compoundTag_1)) {
            Inventories.toTag(compoundTag_1, this.inventory);
        }

        compoundTag_1.putInt("TransferCooldown", this.transferCooldown);
        return compoundTag_1;
    }

    public int getInvSize() {
        return this.inventory.size();
    }

    public ItemStack takeInvStack(int int_1, int int_2) {
        this.checkLootInteraction((PlayerEntity)null);
        return Inventories.splitStack(this.getInvStackList(), int_1, int_2);
    }

    public void setInvStack(int int_1, ItemStack itemStack_1) {
        this.checkLootInteraction((PlayerEntity)null);
        this.getInvStackList().set(int_1, itemStack_1);
        if (itemStack_1.getCount() > this.getInvMaxStackAmount()) {
            itemStack_1.setCount(this.getInvMaxStackAmount());
        }

    }

    protected Text getContainerName() {
        return new TranslatableText("container.wooden_hopper", new Object[0]);
    }

    public void tick() {
        if (this.world != null && !this.world.isClient) {
            --this.transferCooldown;
            this.lastTickTime = this.world.getTime();
            if (!this.needsCooldown()) {
                this.setCooldown(0);
                this.insertAndExtract(() -> {
                    return extract(this);
                });
            }

        }
    }

    private boolean insertAndExtract(Supplier<Boolean> supplier_1) {
        if (this.world != null && !this.world.isClient) {
            if (!this.needsCooldown() && (Boolean)this.getCachedState().get(WoodenHopperBlock.ENABLED)) {
                boolean boolean_1 = false;
                if (!this.isEmpty()) {
                    boolean_1 = this.insert();
                }

                if (!this.isFull()) {
                    boolean_1 |= (Boolean)supplier_1.get();
                }

                if (boolean_1) {
                    this.setCooldown(16);
                    this.markDirty();
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    private boolean isEmpty() {
        Iterator var1 = this.inventory.iterator();

        ItemStack itemStack_1;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack_1 = (ItemStack)var1.next();
        } while(itemStack_1.isEmpty());

        return false;
    }

    public boolean isInvEmpty() {
        return this.isEmpty();
    }

    private boolean isFull() {
        Iterator var1 = this.inventory.iterator();

        ItemStack itemStack_1;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack_1 = (ItemStack)var1.next();
        } while(!itemStack_1.isEmpty() && itemStack_1.getCount() == itemStack_1.getMaxCount());

        return false;
    }

    private boolean insert() {
        Inventory inventory_1 = this.getOutputInventory();
        if (inventory_1 == null) {
            return false;
        } else {
            Direction direction_1 = ((Direction)this.getCachedState().get(WoodenHopperBlock.FACING)).getOpposite();
            if (this.isInventoryFull(inventory_1, direction_1)) {
                return false;
            } else {
                for(int int_1 = 0; int_1 < this.getInvSize(); ++int_1) {
                    if (!this.getInvStack(int_1).isEmpty()) {
                        ItemStack itemStack_1 = this.getInvStack(int_1).copy();
                        ItemStack itemStack_2 = transfer(this, inventory_1, this.takeInvStack(int_1, 1), direction_1);
                        if (itemStack_2.isEmpty()) {
                            inventory_1.markDirty();
                            return true;
                        }

                        this.setInvStack(int_1, itemStack_1);
                    }
                }

                return false;
            }
        }
    }

    private static IntStream getAvailableSlots(Inventory inventory_1, Direction direction_1) {
        return inventory_1 instanceof SidedInventory ? IntStream.of(((SidedInventory)inventory_1).getInvAvailableSlots(direction_1)) : IntStream.range(0, inventory_1.getInvSize());
    }

    private boolean isInventoryFull(Inventory inventory_1, Direction direction_1) {
        return getAvailableSlots(inventory_1, direction_1).allMatch((int_1) -> {
            ItemStack itemStack_1 = inventory_1.getInvStack(int_1);
            return itemStack_1.getCount() >= itemStack_1.getMaxCount();
        });
    }

    private static boolean isInventoryEmpty(Inventory inventory_1, Direction direction_1) {
        return getAvailableSlots(inventory_1, direction_1).allMatch((int_1) -> {
            return inventory_1.getInvStack(int_1).isEmpty();
        });
    }

    public static boolean extract(Hopper hopper_1) {
        Inventory inventory_1 = getInputInventory(hopper_1);
        if (inventory_1 != null) {
            Direction direction_1 = Direction.DOWN;
            return isInventoryEmpty(inventory_1, direction_1) ? false : getAvailableSlots(inventory_1, direction_1).anyMatch((int_1) -> {
                return extract(hopper_1, inventory_1, int_1, direction_1);
            });
        } else {
            Iterator var2 = getInputItemEntities(hopper_1).iterator();

            ItemEntity itemEntity_1;
            do {
                if (!var2.hasNext()) {
                    return false;
                }

                itemEntity_1 = (ItemEntity)var2.next();
            } while(!extract(hopper_1, itemEntity_1));

            return true;
        }
    }

    private static boolean extract(Hopper hopper_1, Inventory inventory_1, int int_1, Direction direction_1) {
        ItemStack itemStack_1 = inventory_1.getInvStack(int_1);
        if (!itemStack_1.isEmpty() && canExtract(inventory_1, itemStack_1, int_1, direction_1)) {
            ItemStack itemStack_2 = itemStack_1.copy();
            ItemStack itemStack_3 = transfer(inventory_1, hopper_1, inventory_1.takeInvStack(int_1, 1), (Direction)null);
            if (itemStack_3.isEmpty()) {
                inventory_1.markDirty();
                return true;
            }

            inventory_1.setInvStack(int_1, itemStack_2);
        }

        return false;
    }

    public static boolean extract(Inventory inventory_1, ItemEntity itemEntity_1) {
        boolean boolean_1 = false;
        ItemStack itemStack_1 = itemEntity_1.getStack().copy();
        ItemStack itemStack_2 = transfer((Inventory)null, inventory_1, itemStack_1, (Direction)null);
        if (itemStack_2.isEmpty()) {
            boolean_1 = true;
            itemEntity_1.remove();
        } else {
            itemEntity_1.setStack(itemStack_2);
        }

        return boolean_1;
    }

    public static ItemStack transfer(Inventory inventory_1, Inventory inventory_2, ItemStack itemStack_1, Direction direction_1) {
        if (inventory_2 instanceof SidedInventory && direction_1 != null) {
            SidedInventory sidedInventory_1 = (SidedInventory)inventory_2;
            int[] ints_1 = sidedInventory_1.getInvAvailableSlots(direction_1);

            for(int int_1 = 0; int_1 < ints_1.length && !itemStack_1.isEmpty(); ++int_1) {
                itemStack_1 = transfer(inventory_1, inventory_2, itemStack_1, ints_1[int_1], direction_1);
            }
        } else {
            int int_2 = inventory_2.getInvSize();

            for(int int_3 = 0; int_3 < int_2 && !itemStack_1.isEmpty(); ++int_3) {
                itemStack_1 = transfer(inventory_1, inventory_2, itemStack_1, int_3, direction_1);
            }
        }

        return itemStack_1;
    }

    private static boolean canInsert(Inventory inventory_1, ItemStack itemStack_1, int int_1, Direction direction_1) {
        if (!inventory_1.isValidInvStack(int_1, itemStack_1)) {
            return false;
        } else {
            return !(inventory_1 instanceof SidedInventory) || ((SidedInventory)inventory_1).canInsertInvStack(int_1, itemStack_1, direction_1);
        }
    }

    private static boolean canExtract(Inventory inventory_1, ItemStack itemStack_1, int int_1, Direction direction_1) {
        return !(inventory_1 instanceof SidedInventory) || ((SidedInventory)inventory_1).canExtractInvStack(int_1, itemStack_1, direction_1);
    }

    private static ItemStack transfer(Inventory inventory_1, Inventory inventory_2, ItemStack itemStack_1, int int_1, Direction direction_1) {
        ItemStack itemStack_2 = inventory_2.getInvStack(int_1);
        if (canInsert(inventory_2, itemStack_1, int_1, direction_1)) {
            boolean boolean_1 = false;
            boolean boolean_2 = inventory_2.isInvEmpty();
            if (itemStack_2.isEmpty()) {
                inventory_2.setInvStack(int_1, itemStack_1);
                itemStack_1 = ItemStack.EMPTY;
                boolean_1 = true;
            } else if (canMergeItems(itemStack_2, itemStack_1)) {
                int int_2 = itemStack_1.getMaxCount() - itemStack_2.getCount();
                int int_3 = Math.min(itemStack_1.getCount(), int_2);
                itemStack_1.decrement(int_3);
                itemStack_2.increment(int_3);
                boolean_1 = int_3 > 0;
            }

            if (boolean_1) {
                if (boolean_2 && inventory_2 instanceof WoodenHopperBlockEntity) {
                    WoodenHopperBlockEntity hopperBlockEntity_1 = (WoodenHopperBlockEntity)inventory_2;
                    if (!hopperBlockEntity_1.isDisabled()) {
                        int int_4 = 0;
                        if (inventory_1 instanceof WoodenHopperBlockEntity) {
                            WoodenHopperBlockEntity hopperBlockEntity_2 = (WoodenHopperBlockEntity)inventory_1;
                            if (hopperBlockEntity_1.lastTickTime >= hopperBlockEntity_2.lastTickTime) {
                                int_4 = 1;
                            }
                        }

                        hopperBlockEntity_1.setCooldown(16 - int_4);
                    }
                }

                inventory_2.markDirty();
            }
        }

        return itemStack_1;
    }

    private Inventory getOutputInventory() {
        Direction direction_1 = (Direction)this.getCachedState().get(WoodenHopperBlock.FACING);
        return getInventoryAt(this.getWorld(), this.pos.offset(direction_1));
    }

    public static Inventory getInputInventory(Hopper hopper_1) {
        return getInventoryAt(hopper_1.getWorld(), hopper_1.getHopperX(), hopper_1.getHopperY() + 1.0D, hopper_1.getHopperZ());
    }

    public static List<ItemEntity> getInputItemEntities(Hopper hopper_1) {
        return (List)hopper_1.getInputAreaShape().getBoundingBoxes().stream().flatMap((box_1) -> {
            return hopper_1.getWorld().getEntities(ItemEntity.class, box_1.offset(hopper_1.getHopperX() - 0.5D, hopper_1.getHopperY() - 0.5D, hopper_1.getHopperZ() - 0.5D), EntityPredicates.VALID_ENTITY).stream();
        }).collect(Collectors.toList());
    }

    public static Inventory getInventoryAt(World world_1, BlockPos blockPos_1) {
        return getInventoryAt(world_1, (double)blockPos_1.getX() + 0.5D, (double)blockPos_1.getY() + 0.5D, (double)blockPos_1.getZ() + 0.5D);
    }

    public static Inventory getInventoryAt(World world_1, double double_1, double double_2, double double_3) {
        Inventory inventory_1 = null;
        BlockPos blockPos_1 = new BlockPos(double_1, double_2, double_3);
        BlockState blockState_1 = world_1.getBlockState(blockPos_1);
        Block block_1 = blockState_1.getBlock();
        if (block_1 instanceof InventoryProvider) {
            inventory_1 = ((InventoryProvider)block_1).getInventory(blockState_1, world_1, blockPos_1);
        } else if (block_1.hasBlockEntity()) {
            BlockEntity blockEntity_1 = world_1.getBlockEntity(blockPos_1);
            if (blockEntity_1 instanceof Inventory) {
                inventory_1 = (Inventory)blockEntity_1;
                if (inventory_1 instanceof ChestBlockEntity && block_1 instanceof ChestBlock) {
                    inventory_1 = ChestBlock.getInventory(blockState_1, world_1, blockPos_1, true);
                }
            }
        }

        if (inventory_1 == null) {
            List<Entity> list_1 = world_1.getEntities((Entity)null, new Box(double_1 - 0.5D, double_2 - 0.5D, double_3 - 0.5D, double_1 + 0.5D, double_2 + 0.5D, double_3 + 0.5D), EntityPredicates.VALID_INVENTORIES);
            if (!list_1.isEmpty()) {
                inventory_1 = (Inventory)list_1.get(world_1.random.nextInt(list_1.size()));
            }
        }

        return (Inventory)inventory_1;
    }

    private static boolean canMergeItems(ItemStack itemStack_1, ItemStack itemStack_2) {
        if (itemStack_1.getItem() != itemStack_2.getItem()) {
            return false;
        } else if (itemStack_1.getDamage() != itemStack_2.getDamage()) {
            return false;
        } else if (itemStack_1.getCount() > itemStack_1.getMaxCount()) {
            return false;
        } else {
            return ItemStack.areTagsEqual(itemStack_1, itemStack_2);
        }
    }

    public double getHopperX() {
        return (double)this.pos.getX() + 0.5D;
    }

    public double getHopperY() {
        return (double)this.pos.getY() + 0.5D;
    }

    public double getHopperZ() {
        return (double)this.pos.getZ() + 0.5D;
    }

    private void setCooldown(int int_1) {
        this.transferCooldown = int_1;
    }

    private boolean needsCooldown() {
        return this.transferCooldown > 0;
    }

    private boolean isDisabled() {
        return this.transferCooldown > 16;
    }

    protected DefaultedList<ItemStack> getInvStackList() {
        return this.inventory;
    }

    protected void setInvStackList(DefaultedList<ItemStack> defaultedList_1) {
        this.inventory = defaultedList_1;
    }

    public void onEntityCollided(Entity entity_1) {
        if (entity_1 instanceof ItemEntity) {
            BlockPos blockPos_1 = this.getPos();
            if (VoxelShapes.matchesAnywhere(VoxelShapes.cuboid(entity_1.getBoundingBox().offset((double)(-blockPos_1.getX()), (double)(-blockPos_1.getY()), (double)(-blockPos_1.getZ()))), this.getInputAreaShape(), BooleanBiFunction.AND)) {
                this.insertAndExtract(() -> {
                    return extract(this, (ItemEntity)entity_1);
                });
            }
        }

    }

    protected Container createContainer(int int_1, PlayerInventory playerInventory_1) {
        return new HopperContainer(int_1, playerInventory_1, this);
    }
}
