package net.cheatercodes.seasources.world;

import com.mojang.datafixers.Dynamic;
import net.cheatercodes.seasources.SeaSources;
import net.cheatercodes.seasources.blocks.PillarSlab;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;
import java.util.function.Function;

public class StartingRaftFeature extends Feature<DefaultFeatureConfig> {

    public StartingRaftFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> function_1) {
        super(function_1);
    }

    @Override
    public boolean generate(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig defaultFeatureConfig) {
        ChunkPos chunk = new ChunkPos(blockPos);
        BlockPos spawnPos = world.getSpawnPos();
        BlockPos raftPos = new BlockPos(spawnPos.getX(), world.getSeaLevel() - 1, spawnPos.getZ());

        BlockState raftMaterial = SeaSources.MAKESHIFT_SLAB.getDefaultState()
                .with(PillarSlab.AXIS, Direction.Axis.X).with(PillarSlab.TYPE, SlabType.TOP).with(PillarSlab.WATERLOGGED, true);

        if(chunk.equals(new ChunkPos(raftPos)))
            world.setBlockState(raftPos, raftMaterial, 2);
        if(chunk.equals(new ChunkPos(raftPos.south())))
            world.setBlockState(raftPos.south(), raftMaterial, 2);
        if(chunk.equals(new ChunkPos(raftPos.west())))
            world.setBlockState(raftPos.west(), raftMaterial, 2);
        if(chunk.equals(new ChunkPos(raftPos.south().west())))
            world.setBlockState(raftPos.south().west(), raftMaterial, 2);

        return true;
    }
}
