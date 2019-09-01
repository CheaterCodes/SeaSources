package net.cheatercodes.seasources.blockentities;

import net.cheatercodes.seasources.SeaSources;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.container.Container;
import net.minecraft.container.GenericContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Tickable;

import java.util.Iterator;

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


    @Override
    public void tick() {

    }
}
