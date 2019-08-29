package net.cheatercodes.seasources.blockentities;

import net.cheatercodes.seasources.SeaSources;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Box;

import java.util.List;

public class DriftingItemBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Tickable {

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

    @Override
    public void tick() {
         List<PlayerEntity> players = world.getEntities(PlayerEntity.class, new Box(pos), EntityPredicates.EXCEPT_SPECTATOR);
         if(players.size() > 0)
         {
             for (PlayerEntity player : players) {
                 if(player.inventory.insertStack(itemStack))
                 {
                     player.world.playSound(null, player.x, player.y, player.z,
                             SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F,
                             ((player.getRand().nextFloat() - player.getRand().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                     world.setBlockState(pos, Blocks.WATER.getDefaultState());
                     break;
                 }
             }
         }
    }
}
