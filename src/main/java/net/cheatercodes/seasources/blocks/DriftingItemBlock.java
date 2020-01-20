package net.cheatercodes.seasources.blocks;

import net.cheatercodes.seasources.blockentities.DriftingItemBlockEntity;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
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
    public void onBlockRemoved(BlockState blockState, World world, BlockPos blockPos, BlockState newState, boolean moved) {
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if (blockEntity instanceof DriftingItemBlockEntity) {
            ItemScatterer.spawn(world,blockPos, DefaultedList.ofSize(1, ((DriftingItemBlockEntity)blockEntity).itemStack));
        }

        super.onBlockRemoved(blockState, world, blockPos, newState, moved);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext entityContext) {
        return VoxelShapes.empty();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos blockPos, BlockPos neighborPos) {
        BlockState newState = super.getStateForNeighborUpdate(state, facing, neighborState, world, blockPos, neighborPos);
        if (!newState.isAir()) {
            world.getFluidTickScheduler().schedule(blockPos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return newState;
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
    public void onEntityCollision(BlockState blockState, World world, BlockPos blockPos, Entity entity) {
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if(blockEntity instanceof  DriftingItemBlockEntity)
        {
            ((DriftingItemBlockEntity)blockEntity).onEntityCollided(entity);
        }
    }

    @Override
    public Fluid tryDrainFluid(IWorld world, BlockPos blockPos, BlockState blockState) {
        world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 3);
        return Fluids.WATER;
    }
}