package net.cheatercodes.seasources.blocks;

import net.cheatercodes.seasources.blockentities.BrickFurnaceBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.NameableContainerProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class BrickFurnaceBlock extends AbstractFurnaceBlock {
    public BrickFurnaceBlock(Settings block$Settings_1) {
        super(block$Settings_1);
    }

    @Override
    protected void openContainer(World world, BlockPos blockPos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if (blockEntity instanceof BrickFurnaceBlockEntity) {
            player.openContainer((NameableContainerProvider)blockEntity);
            player.incrementStat(Stats.INTERACT_WITH_FURNACE);
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockView var1) {
        return new BrickFurnaceBlockEntity();
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState blockState_1, World world_1, BlockPos blockPos_1, Random random_1) {
        if ((Boolean)blockState_1.get(LIT)) {
            double double_1 = (double)blockPos_1.getX() + 0.5D;
            double double_2 = (double)blockPos_1.getY();
            double double_3 = (double)blockPos_1.getZ() + 0.5D;
            if (random_1.nextDouble() < 0.1D) {
                world_1.playSound(double_1, double_2, double_3, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction_1 = (Direction)blockState_1.get(FACING);
            Direction.Axis direction$Axis_1 = direction_1.getAxis();
            double double_4 = 0.52D;
            double double_5 = random_1.nextDouble() * 0.6D - 0.3D;
            double double_6 = direction$Axis_1 == Direction.Axis.X ? (double)direction_1.getOffsetX() * 0.52D : double_5;
            double double_7 = random_1.nextDouble() * 6.0D / 16.0D;
            double double_8 = direction$Axis_1 == Direction.Axis.Z ? (double)direction_1.getOffsetZ() * 0.52D : double_5;
            world_1.addParticle(ParticleTypes.SMOKE, double_1 + double_6, double_2 + double_7, double_3 + double_8, 0.0D, 0.0D, 0.0D);
            world_1.addParticle(ParticleTypes.FLAME, double_1 + double_6, double_2 + double_7, double_3 + double_8, 0.0D, 0.0D, 0.0D);
        }
    }
}
