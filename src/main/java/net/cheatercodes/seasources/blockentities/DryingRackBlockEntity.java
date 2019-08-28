package net.cheatercodes.seasources.blockentities;

import net.cheatercodes.seasources.DryingRecipe;
import net.cheatercodes.seasources.SeaSources;
import net.cheatercodes.seasources.blocks.DryingRackBlock;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Tickable;

import java.util.Optional;

public class DryingRackBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Tickable {

    private ItemStack item = ItemStack.EMPTY;

    private int dryingTime;

    public DryingRackBlockEntity() {
        super(SeaSources.DRYING_RACK_BLOCK_ENTITY);
    }

    public Optional<DryingRecipe> getRecipeFor(ItemStack itemStack_1) {
        return this.world.getRecipeManager().getFirstMatch(DryingRecipe.TYPE, new BasicInventory(new ItemStack[]{itemStack_1}), this.world);
    }

    public void setItem(ItemStack itemStack) {
        item = itemStack;

        Optional<DryingRecipe> recipe = getRecipeFor(itemStack);
        if(recipe.isPresent()) {
            dryingTime = recipe.get().getTime();
        }
        else {
            dryingTime = 0;
        }

        this.markDirty();
        this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), 3);
    }

    public ItemStack getItem() {
        return item;
    }

    public void craftItem() {
        Optional<DryingRecipe> recipe = getRecipeFor(item);
        if(recipe.isPresent()) {
            setItem(recipe.get().getOutput().copy());
        }
    }

    public void giveItem(ServerPlayerEntity player) {
        if(!player.giveItemStack(item)) {
            player.dropItem(item, false);
        }
        else {
            player.world.playSound(null, player.x, player.y, player.z, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRand().nextFloat() - player.getRand().nextFloat()) * 0.7F + 1.0F) * 2.0F);
        }

        item = ItemStack.EMPTY;
        dryingTime = 0;

        this.markDirty();
        this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), 3);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        toClientTag(tag);
        tag.putInt("time", dryingTime);
        return tag;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        fromClientTag(tag);
        dryingTime = tag.getInt("time");
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        CompoundTag itemTag = (CompoundTag) tag.getTag("item");
        item = ItemStack.fromTag(itemTag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        CompoundTag itemTag = new CompoundTag();
        item.toTag(itemTag);
        tag.put("item", itemTag);
        return tag;
    }

    @Override
    public void tick() {
        if(!world.isClient) {
            if (!item.isEmpty()) {
                dryingTime--;
                if (dryingTime == 0) {
                    craftItem();
                }
            }
        }
    }
}
