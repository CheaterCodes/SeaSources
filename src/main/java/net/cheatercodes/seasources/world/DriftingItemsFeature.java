package net.cheatercodes.seasources.world;

import com.mojang.datafixers.Dynamic;
import net.cheatercodes.seasources.SeaSources;
import net.cheatercodes.seasources.blockentities.DriftingItemBlockEntity;
import net.cheatercodes.seasources.blocks.DriftingItemBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

public class DriftingItemsFeature extends Feature<DefaultFeatureConfig> {

    public DriftingItemsFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> function_1) {
        super(function_1);
    }

    @Override
    public boolean generate(IWorld var1, ChunkGenerator<? extends ChunkGeneratorConfig> var2, Random var3, BlockPos var4, DefaultFeatureConfig var5) {
        ChunkPos pos = new ChunkPos(var4);
        for(int i = 0; i < 4; i++)
        {
            BlockPos randomPos = pos.toBlockPos(var3.nextInt(16), var1.getSeaLevel() - 1, var3.nextInt(16));
            if(var1.getBlockState(randomPos).getBlock() == Blocks.WATER)
            {
                var1.setBlockState(randomPos, SeaSources.DRIFTING_ITEM.getDefaultState(), 2);
                ItemStack item;
                if(var3.nextInt(100) < 60)
                    item = new ItemStack(Items.STICK, 1);
                else
                    item = new ItemStack(Items.STRING, 1);
                ((DriftingItemBlockEntity)var1.getBlockEntity(randomPos)).SetItem(item);
            }
        }

        return true;
    }
}
