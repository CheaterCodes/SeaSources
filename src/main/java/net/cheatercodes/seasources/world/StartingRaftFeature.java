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
    public boolean generate(IWorld var1, ChunkGenerator<? extends ChunkGeneratorConfig> var2, Random var3, BlockPos var4, DefaultFeatureConfig var5) {
        ChunkPos chunk = new ChunkPos(var4);

        BlockState raftMaterial = SeaSources.MAKESHIFT_SLAB.getDefaultState()
                .with(PillarSlab.AXIS, Direction.Axis.X).with(PillarSlab.TYPE, SlabType.TOP).with(PillarSlab.WATERLOGGED, true);

        if(chunk.x == 0 & chunk.z == 0)
            var1.setBlockState(new BlockPos(0,var1.getSeaLevel() - 1, 0), raftMaterial, 2);
        else if(chunk.x == -1 & chunk.z == 0)
            var1.setBlockState(new BlockPos(-1,var1.getSeaLevel() - 1, 0), raftMaterial, 2);
        else if(chunk.x == 0 & chunk.z == -1)
            var1.setBlockState(new BlockPos(0,var1.getSeaLevel() - 1, -1), raftMaterial, 2);
        else if(chunk.x == -1 & chunk.z == -1)
            var1.setBlockState(new BlockPos(-1,var1.getSeaLevel() - 1, -1), raftMaterial, 2);

        return true;
    }
}
