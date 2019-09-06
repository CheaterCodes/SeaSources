package net.cheatercodes.seasources.blockentities;

import net.cheatercodes.seasources.SeaSources;
import net.cheatercodes.seasources.blocks.WaterStrainerNet;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.container.Container;
import net.minecraft.container.GenericContainer;
import net.minecraft.data.server.LootTablesProvider;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.world.loot.LootManager;
import net.minecraft.world.loot.LootSupplier;
import net.minecraft.world.loot.LootTables;
import net.minecraft.world.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.world.loot.context.LootContext;
import net.minecraft.world.loot.context.LootContextParameters;
import net.minecraft.world.loot.context.LootContextType;
import net.minecraft.world.loot.context.LootContextTypes;

import java.util.Iterator;
import java.util.List;

public class WaterStrainerBlockEntity extends LootableContainerBlockEntity implements Tickable {

    private DefaultedList<ItemStack> inventory;

    public WaterStrainerBlockEntity() {
        super(SeaSources.WATER_STRAINER_BLOCK_ENTITY);
        this.inventory = DefaultedList.ofSize(this.getInvSize(), ItemStack.EMPTY);
    }

    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        if (!this.serializeLootTable(tag)) {
            Inventories.toTag(tag, this.inventory);
        }

        return tag;
    }

    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        this.inventory = DefaultedList.ofSize(this.getInvSize(), ItemStack.EMPTY);
        if (!this.deserializeLootTable(tag)) {
            Inventories.fromTag(tag, this.inventory);
        }
    }

    @Override
    protected DefaultedList<ItemStack> getInvStackList() {
        return this.inventory;
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> list) {
        this.inventory = list;
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("container.water_strainer", new Object[0]);
    }

    @Override
    protected Container createContainer(int int_1, PlayerInventory playerInventory) {
        return GenericContainer.createGeneric9x3(int_1, playerInventory, this);
    }

    @Override
    public int getInvSize() {
        return 27;
    }

    @Override
    public boolean isInvEmpty() {
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

    public List<ItemStack> getLoot()
    {
        LootSupplier lootSupplier = world.getServer().getLootManager().getSupplier(new Identifier(SeaSources.ModId, "gameplay/water_strainer"));
        LootContext.Builder builder = new LootContext.Builder((ServerWorld) world);
        BlockState above = world.getBlockState(pos.up());
        builder.put(LootContextParameters.BLOCK_STATE, above);
        LootContextType.Builder typeBuilder = new LootContextType.Builder().allow(LootContextParameters.BLOCK_STATE);
        LootContext context = builder.build(typeBuilder.build());
        return lootSupplier.getDrops(context);
    }

    public void generateItem() {
        List<ItemStack> lootStacks = getLoot();
        for (ItemStack lootStack: lootStacks) {
            insertItem(lootStack);
        }
    }

    public void insertItem(ItemStack itemStack) {
        for(int i = 0; i < getInvSize(); i++) {
            if(getInvStack(i).isItemEqual(itemStack)) {
                int amount = Math.min(getInvMaxStackAmount(), itemStack.getMaxCount()) - getInvStack(i).getCount();
                if(amount < itemStack.getCount()) {
                    getInvStack(i).increment(amount);
                    itemStack.decrement(amount);
                }
                else {
                    getInvStack(i).increment(itemStack.getCount());
                    itemStack = ItemStack.EMPTY;
                }
            }
        }

        for(int i = 0; i < getInvSize(); i++) {
            if(getInvStack(i).isEmpty()) {
                int amount = Math.min(getInvMaxStackAmount(), itemStack.getMaxCount());
                setInvStack(i, itemStack.split(amount));
            }
        }
    }

    @Override
    public void tick() {
        if(!world.isClient)
        {
            generateItem();
        }
    }
}
