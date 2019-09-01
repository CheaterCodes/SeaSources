package net.cheatercodes.seasources.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.FluidFillable;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.ViewableWorld;
import net.minecraft.world.World;

public class WaterPermeable extends Block implements FluidDrainable, FluidFillable {

    public static final IntProperty WATER_LEVEL = Properties.LEVEL_8;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public WaterPermeable(Block.Settings blockSettings)
    {
        super(blockSettings);
        setDefaultState(getStateFactory().getDefaultState().with(WATER_LEVEL, 0).with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactory$Builder_1) {
        stateFactory$Builder_1.add(WATER_LEVEL).add(WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        FluidState fluid = context.getWorld().getBlockState(context.getBlockPos()).getFluidState();
        if(fluid.getFluid().matchesType(Fluids.WATER))
        {
            if(fluid.isStill())
                return getDefaultState().with(WATERLOGGED, true);
            else
                return getDefaultState().with(WATER_LEVEL, fluid.getLevel());
        }
        else
        {
            return getDefaultState();
        }
    }

    @Override
    public Fluid tryDrainFluid(IWorld world, BlockPos thisPos, BlockState thisState) {
        if(thisState.get(WATERLOGGED))
        {
            world.setBlockState(thisPos, thisState.with(WATERLOGGED,false).with(WATER_LEVEL, 0), 3);
            return Fluids.WATER;
        }
        else
        {
            return Fluids.EMPTY;
        }
    }

    @Override
    public boolean canFillWithFluid(BlockView view, BlockPos thisPos, BlockState thisState, Fluid fillFluid) {
        if(!thisState.get(WATERLOGGED) && fillFluid.matchesType(Fluids.WATER))
            return true;
        else
            return false;
    }

    @Override
    public boolean tryFillWithFluid(IWorld world, BlockPos thisPos, BlockState thisState, FluidState fillFluid) {
        if(canFillWithFluid(world, thisPos, thisState, fillFluid.getFluid()))
        {
            if(fillFluid.isStill())
                world.setBlockState(thisPos, thisState.with(WATERLOGGED,true), 3);
            else if(fillFluid.getLevel() > thisState.get(WATER_LEVEL))
                world.setBlockState(thisPos, thisState.with(WATER_LEVEL, fillFluid.getLevel()), 3);
            else
                return false;
            return true;
        }
        else
            return false;
    }

    @Override
    public FluidState getFluidState(BlockState thisState) {
        int waterLevel = thisState.get(WATER_LEVEL);
        if(thisState.get(WATERLOGGED))
        {
            return Fluids.WATER.getStill(false);
        }
        else if(waterLevel > 0)
        {
            return Fluids.WATER.getFlowing(waterLevel, waterLevel == 8);
        }
        else
        {
            return Fluids.EMPTY.getDefaultState();
        }
    }

    @Override
    public int getTickRate(ViewableWorld viewableWorld_1) {
        return Fluids.WATER.getTickRate(viewableWorld_1);
    }

    @Override
    public void onBlockAdded(BlockState blockState_1, World world_1, BlockPos blockPos_1, BlockState blockState_2, boolean boolean_1) {
        world_1.getFluidTickScheduler().schedule(blockPos_1, blockState_1.getFluidState().getFluid(), this.getTickRate(world_1));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState blockState_1, Direction direction_1, BlockState blockState_2, IWorld iWorld_1, BlockPos blockPos_1, BlockPos blockPos_2) {
        if (blockState_1.getFluidState().isStill() || blockState_2.getFluidState().isStill()) {
            iWorld_1.getFluidTickScheduler().schedule(blockPos_1, blockState_1.getFluidState().getFluid(), this.getTickRate(iWorld_1));
        }

        return super.getStateForNeighborUpdate(blockState_1, direction_1, blockState_2, iWorld_1, blockPos_1, blockPos_2);
    }

    @Override
    public void neighborUpdate(BlockState blockState_1, World world_1, BlockPos blockPos_1, Block block_1, BlockPos blockPos_2, boolean boolean_1) {
        world_1.getFluidTickScheduler().schedule(blockPos_1, blockState_1.getFluidState().getFluid(), this.getTickRate(world_1));
    }
}
