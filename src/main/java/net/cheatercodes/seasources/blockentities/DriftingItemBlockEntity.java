package net.cheatercodes.seasources.blockentities;

import net.cheatercodes.seasources.SeaSources;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Box;
import net.minecraft.world.loot.LootSupplier;
import net.minecraft.world.loot.context.LootContext;
import net.minecraft.world.loot.context.LootContextParameters;
import net.minecraft.world.loot.context.LootContextType;
import net.minecraft.world.loot.context.LootContextTypes;

import java.util.List;

public class DriftingItemBlockEntity extends BlockEntity implements BlockEntityClientSerializable {

    public ItemStack itemStack = ItemStack.EMPTY;

    public DriftingItemBlockEntity() {
        super(SeaSources.DRIFTING_ITEM_BLOCK_ENTITY);
    }

    public void SetItem(ItemStack itemStack)
    {
        this.itemStack = itemStack;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.put("itemStack", itemStack.toTag(new CompoundTag()));
        return tag;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        itemStack = ItemStack.fromTag(tag.getCompound("itemStack"));
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        tag.put("itemStack", itemStack.toTag(new CompoundTag()));
        return tag;
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        itemStack = ItemStack.fromTag(tag.getCompound("itemStack"));
    }

    public void onEntityCollided(Entity entity)
    {
        if(entity instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity)entity;
            if(player.inventory.insertStack(itemStack))
            {
                player.world.playSound(null, player.x, player.y, player.z,
                        SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F,
                        ((player.getRand().nextFloat() - player.getRand().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                world.setBlockState(pos, Blocks.WATER.getDefaultState());
            }
        }
    }
}
