package net.cheatercodes.seasources.blocks;

import com.google.common.collect.ImmutableMap;
import net.cheatercodes.seasources.SeaSources;
import net.minecraft.block.*;
import net.minecraft.entity.EntityContext;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.ViewableWorld;
import net.minecraft.world.World;

public class WaterStrainerNet extends Block implements FluidFillable, FluidDrainable {

    static VoxelShape collisionShape=
            VoxelShapes.union(
                    createCuboidShape(0, 0, 0, 1, 16, 1),
                    createCuboidShape(15, 0, 0, 16, 16, 1),
                    createCuboidShape(0, 0, 15, 1, 16, 16),
                    createCuboidShape(15, 0, 15, 16, 16, 16),
                    createCuboidShape(1, 1, 0, 15, 14, 1),
                    createCuboidShape(1, 1, 15, 15, 14, 16),
                    createCuboidShape(0, 1, 1, 1, 14, 16),
                    createCuboidShape(15, 1, 1, 16, 14, 16)
            );

    public static final IntProperty WATER_LEVEL = IntProperty.of("water_level", 0, 9);

    public WaterStrainerNet() {
        super(Block.Settings.copy(SeaSources.WATER_STRAINER));
        setDefaultState(getStateFactory().getDefaultState().with(WATER_LEVEL, 0));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        FluidState fluid = context.getWorld().getBlockState(context.getBlockPos()).getFluidState();
        if(fluid.isStill())
            return getDefaultState().with(WATER_LEVEL, 9);
        else
            return getDefaultState().with(WATER_LEVEL, fluid.getLevel());
    }

    @Override
    protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactory$Builder_1) {
        stateFactory$Builder_1.add(WATER_LEVEL);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, EntityContext entityContext_1) {
        return collisionShape;
    }

    @Override
    public VoxelShape getRayTraceShape(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
        return collisionShape;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public Fluid tryDrainFluid(IWorld world, BlockPos thisPos, BlockState thisState) {
        if(thisState.get(WATER_LEVEL) == 9)
        {
            world.setBlockState(thisPos, thisState.with(WATER_LEVEL, 0), 3);
            return Fluids.WATER;
        }
        else
        {
            return Fluids.EMPTY;
        }
    }

    @Override
    public boolean canFillWithFluid(BlockView view, BlockPos thisPos, BlockState thisState, Fluid fillFluid) {
        if(thisState.get(WATER_LEVEL) < 9 && fillFluid.matchesType(Fluids.WATER))
            return true;
        else
            return false;
    }

    @Override
    public boolean tryFillWithFluid(IWorld world, BlockPos thisPos, BlockState thisState, FluidState fillFluid) {
        if(canFillWithFluid(world, thisPos, thisState, fillFluid.getFluid()))
        {
            if(fillFluid.isStill())
                world.setBlockState(thisPos, thisState.with(WATER_LEVEL, 9), 3);
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
        if(waterLevel == 9)
        {
            return Fluids.WATER.getStill(false);
        }
        else if(waterLevel == 8)
        {
            return Fluids.WATER.getFlowing(8, true);
        }
        else if(waterLevel > 0)
        {
            return Fluids.WATER.getFlowing(waterLevel, false);
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
