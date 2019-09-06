package net.cheatercodes.seasources.blocks;

import com.google.common.collect.ImmutableMap;
import net.cheatercodes.seasources.SeaSources;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
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

public class WaterStrainerNet extends WaterPermeable {

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

    public WaterStrainerNet() {
        super(Block.Settings.copy(SeaSources.WATER_STRAINER));
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
}
