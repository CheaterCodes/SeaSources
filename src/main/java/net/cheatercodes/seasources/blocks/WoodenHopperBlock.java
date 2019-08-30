package net.cheatercodes.seasources.blocks;

import net.cheatercodes.seasources.blockentities.WoodenHopperBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class WoodenHopperBlock extends HopperBlock {
    public WoodenHopperBlock(Settings block$Settings_1) {
        super(block$Settings_1);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView_1) {
        return new WoodenHopperBlockEntity();
    }

    public void onPlaced(World world_1, BlockPos blockPos_1, BlockState blockState_1, LivingEntity livingEntity_1, ItemStack itemStack_1) {
        if (itemStack_1.hasCustomName()) {
            BlockEntity blockEntity_1 = world_1.getBlockEntity(blockPos_1);
            if (blockEntity_1 instanceof WoodenHopperBlockEntity) {
                ((WoodenHopperBlockEntity)blockEntity_1).setCustomName(itemStack_1.getName());
            }
        }

    }

    public boolean activate(BlockState blockState_1, World world_1, BlockPos blockPos_1, PlayerEntity playerEntity_1, Hand hand_1, BlockHitResult blockHitResult_1) {
        if (world_1.isClient) {
            return true;
        } else {
            BlockEntity blockEntity_1 = world_1.getBlockEntity(blockPos_1);
            if (blockEntity_1 instanceof WoodenHopperBlockEntity) {
                playerEntity_1.openContainer((WoodenHopperBlockEntity)blockEntity_1);
                playerEntity_1.incrementStat(Stats.INSPECT_HOPPER);
            }

            return true;
        }
    }
}
