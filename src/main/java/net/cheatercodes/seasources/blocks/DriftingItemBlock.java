package net.cheatercodes.seasources.blocks;

import net.cheatercodes.seasources.blockentities.DriftingItemBlockEntity;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.List;

public class DriftingItemBlock extends Block implements FluidDrainable, BlockEntityProvider {

    public DriftingItemBlock() {
        super(FabricBlockSettings.of(Material.WATER).noCollision().build());
    }

    @Override
    public void onBlockRemoved(BlockState blockState_1, World world_1, BlockPos blockPos_1, BlockState blockState_2, boolean boolean_1) {
        BlockEntity blockEntity_1 = world_1.getBlockEntity(blockPos_1);
        if (blockEntity_1 instanceof DriftingItemBlockEntity) {
            ItemScatterer.spawn(world_1,blockPos_1, DefaultedList.ofSize(1, ((DriftingItemBlockEntity)blockEntity_1).itemStack));
        }

        super.onBlockRemoved(blockState_1, world_1, blockPos_1, blockState_2, boolean_1);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, EntityContext entityContext_1) {
        return VoxelShapes.empty();
    }

    @Override
    public BlockRenderType getRenderType(BlockState blockState_1) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState blockState_1, Direction direction_1, BlockState blockState_2, IWorld iWorld_1, BlockPos blockPos_1, BlockPos blockPos_2) {
        BlockState blockState_3 = super.getStateForNeighborUpdate(blockState_1, direction_1, blockState_2, iWorld_1, blockPos_1, blockPos_2);
        if (!blockState_3.isAir()) {
            iWorld_1.getFluidTickScheduler().schedule(blockPos_1, Fluids.WATER, Fluids.WATER.getTickRate(iWorld_1));
        }

        return blockState_3;
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return Fluids.WATER.getStill(false);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new DriftingItemBlockEntity();
    }

    @Override
    public Fluid tryDrainFluid(IWorld world, BlockPos blockPos, BlockState blockState) {
        world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 3);
        return Fluids.WATER;
    }
}