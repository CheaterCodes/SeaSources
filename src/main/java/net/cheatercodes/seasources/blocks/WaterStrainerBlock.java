package net.cheatercodes.seasources.blocks;

import net.cheatercodes.seasources.blockentities.WaterStrainerBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class WaterStrainerBlock extends BlockWithEntity {
    public WaterStrainerBlock(Settings blockSettings) {
        super(blockSettings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState blockState_1) {
        return BlockRenderType.MODEL;
    }

    public boolean activate(BlockState blockState_1, World world_1, BlockPos blockPos_1, PlayerEntity playerEntity, Hand hand_1, BlockHitResult blockHitResult_1) {
        if (world_1.isClient) {
            return true;
        } else {
            BlockEntity blockEntity_1 = world_1.getBlockEntity(blockPos_1);
            if (blockEntity_1 instanceof WaterStrainerBlockEntity) {
                playerEntity.openContainer((WaterStrainerBlockEntity)blockEntity_1);
            }

            return true;
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockView var1) {
        return new WaterStrainerBlockEntity();
    }
}
