package net.cheatercodes.seasources.blocks;

import net.cheatercodes.seasources.DryingRecipe;
import net.cheatercodes.seasources.blockentities.DryingRackBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Optional;

public class DryingRackBlock extends BlockWithEntity {
    public static final DirectionProperty FACING;

    public static final VoxelShape N_SIDE;
    public static final VoxelShape E_SIDE;
    public static final VoxelShape S_SIDE;
    public static final VoxelShape W_SIDE;

    public static final VoxelShape NS_BAR;
    public static final VoxelShape WE_BAR;

    public static final VoxelShape NS_SHAPE;
    public static final VoxelShape WE_SHAPE;

    static {
        FACING = Properties.HORIZONTAL_FACING;

        W_SIDE = createCuboidShape(1, 0, 1, 15, 14, 4);
        E_SIDE = createCuboidShape(1, 0, 12, 15, 14, 15);
        S_SIDE = createCuboidShape(1, 0, 1, 4, 14, 15);
        N_SIDE = createCuboidShape(12, 0, 1, 15, 14, 15);

        NS_BAR = createCuboidShape(1, 8.5, 6.5, 15, 11.5, 9.5);
        WE_BAR = createCuboidShape(6.5, 8.5, 1, 9.5, 11.5, 15);

        NS_SHAPE = VoxelShapes.union(VoxelShapes.union(N_SIDE, S_SIDE), NS_BAR);
        WE_SHAPE = VoxelShapes.union(VoxelShapes.union(W_SIDE, E_SIDE), WE_BAR);
    }

    public DryingRackBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onBlockRemoved(BlockState blockState, World world, BlockPos blockPos, BlockState newState, boolean moved) {
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if (blockEntity instanceof DryingRackBlockEntity) {
            ItemScatterer.spawn(world,blockPos, DefaultedList.ofSize(1, ((DryingRackBlockEntity)blockEntity).getItem()));
        }

        super.onBlockRemoved(blockState, world, blockPos, newState, moved);
    }

    @Override
    public BlockState rotate(BlockState blockState, BlockRotation blockRotation) {
        return blockState.with(FACING, blockRotation.rotate(blockState.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, BlockMirror blockMirror) {
        return blockState.rotate(blockMirror.getRotation(blockState.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext itemPlacementContext_1) {
        return this.getDefaultState().with(FACING, itemPlacementContext_1.getPlayerFacing().getOpposite());
    }

    @Override
    public boolean canPlaceAtSide(BlockState blockState, BlockView blockView, BlockPos blockPos, BlockPlacementEnvironment env) {
        return false;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, EntityContext entityContext) {
        switch (blockState.get(FACING)) {
            case NORTH:
            case SOUTH:
                return NS_SHAPE;
            case WEST:
            case EAST:
                return WE_SHAPE;
        }
        return VoxelShapes.empty();
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new DryingRackBlockEntity();
    }

    @Override
    public BlockRenderType getRenderType(BlockState blockState) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(blockPos);

        if (blockEntity instanceof DryingRackBlockEntity) {
            DryingRackBlockEntity dryingRackEntity = (DryingRackBlockEntity)blockEntity;

            if(dryingRackEntity.getItem().isEmpty()) {
                ItemStack itemStack_1 = player.getStackInHand(hand);
                Optional<DryingRecipe> recipe = dryingRackEntity.getRecipeFor(itemStack_1);
                if (recipe.isPresent()) {
                    if (!world.isClient) {
                        dryingRackEntity.setItem(player.abilities.creativeMode ? itemStack_1.copy().split(1) : itemStack_1.split(1));
                    }

                    return ActionResult.SUCCESS;
                }
            }
            else {
                if(!world.isClient) {
                    dryingRackEntity.giveItem((ServerPlayerEntity)player);
                }
                return ActionResult.SUCCESS;
            }
        }


        return ActionResult.PASS;
    }
}
