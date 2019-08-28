package net.cheatercodes.seasources.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;

public class PillarSlab extends SlabBlock {

    public static final EnumProperty<Direction.Axis> AXIS = Properties.AXIS;

    public PillarSlab(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(AXIS, Direction.Axis.Y));
    }

    public BlockState rotate(BlockState blockState, BlockRotation rotation) {
        switch(rotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch(blockState.get(AXIS)) {
                    case X:
                        return blockState.with(AXIS, Direction.Axis.Z);
                    case Z:
                        return blockState.with(AXIS, Direction.Axis.X);
                    default:
                        return blockState;
                }
            default:
                return blockState;
        }
    }

    protected void appendProperties(StateFactory.Builder<Block, BlockState> blockStateBuilder) {
        super.appendProperties(blockStateBuilder);
        blockStateBuilder.add(AXIS);
    }

    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockState slabState = super.getPlacementState(context);
        return slabState.with(AXIS, context.getSide().getAxis());
    }
}
