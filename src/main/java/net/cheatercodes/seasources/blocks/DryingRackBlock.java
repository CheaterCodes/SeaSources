package net.cheatercodes.seasources.blocks;

import net.cheatercodes.seasources.DryingRecipe;
import net.cheatercodes.seasources.blockentities.DriftingItemBlockEntity;
import net.cheatercodes.seasources.blockentities.DryingRackBlockEntity;
import net.cheatercodes.seasources.render.DryingRackBlockEntityRenderer;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
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
    public void onBlockRemoved(BlockState blockState_1, World world_1, BlockPos blockPos_1, BlockState blockState_2, boolean boolean_1) {
        BlockEntity blockEntity_1 = world_1.getBlockEntity(blockPos_1);
        if (blockEntity_1 instanceof DryingRackBlockEntity) {
            ItemScatterer.spawn(world_1,blockPos_1, DefaultedList.ofSize(1, ((DryingRackBlockEntity)blockEntity_1).getItem()));
        }

        super.onBlockRemoved(blockState_1, world_1, blockPos_1, blockState_2, boolean_1);
    }

    public BlockState rotate(BlockState blockState_1, BlockRotation blockRotation_1) {
        return (BlockState)blockState_1.with(FACING, blockRotation_1.rotate((Direction)blockState_1.get(FACING)));
    }

    public BlockState mirror(BlockState blockState_1, BlockMirror blockMirror_1) {
        return blockState_1.rotate(blockMirror_1.getRotation((Direction)blockState_1.get(FACING)));
    }

    protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactory$Builder_1) {
        stateFactory$Builder_1.add(FACING);
    }

    public BlockState getPlacementState(ItemPlacementContext itemPlacementContext_1) {
        return this.getDefaultState().with(FACING, itemPlacementContext_1.getPlayerFacing().getOpposite());
    }

    public boolean canPlaceAtSide(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, BlockPlacementEnvironment blockPlacementEnvironment_1) {
        return false;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, EntityContext entityContext_1) {
        switch (blockState_1.get(FACING)) {
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
    public BlockEntity createBlockEntity(BlockView var1) {
        return new DryingRackBlockEntity();
    }

    public BlockRenderType getRenderType(BlockState blockState_1) {
        return BlockRenderType.MODEL;
    }

    public boolean activate(BlockState blockState_1, World world_1, BlockPos blockPos_1, PlayerEntity playerEntity_1, Hand hand_1, BlockHitResult blockHitResult_1) {
        BlockEntity blockEntity_1 = world_1.getBlockEntity(blockPos_1);

        if (blockEntity_1 instanceof DryingRackBlockEntity) {
            DryingRackBlockEntity dryingRackEntity = (DryingRackBlockEntity)blockEntity_1;

            if(dryingRackEntity.getItem().isEmpty()) {
                ItemStack itemStack_1 = playerEntity_1.getStackInHand(hand_1);
                Optional<DryingRecipe> recipe = dryingRackEntity.getRecipeFor(itemStack_1);
                if (recipe.isPresent()) {
                    if (!world_1.isClient) {
                        dryingRackEntity.setItem(playerEntity_1.abilities.creativeMode ? itemStack_1.copy().split(1) : itemStack_1.split(1));
                    }

                    return true;
                }
            }
            else {
                if(!world_1.isClient) {
                    dryingRackEntity.giveItem((ServerPlayerEntity)playerEntity_1);
                }
                return true;
            }
        }


        return false;
    }
}
