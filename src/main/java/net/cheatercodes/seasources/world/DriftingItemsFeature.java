package net.cheatercodes.seasources.world;

import com.mojang.datafixers.Dynamic;
import net.cheatercodes.seasources.SeaSources;
import net.cheatercodes.seasources.blockentities.DriftingItemBlockEntity;
import net.cheatercodes.seasources.blocks.DriftingItemBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.loot.LootSupplier;
import net.minecraft.world.loot.context.LootContext;
import net.minecraft.world.loot.context.LootContextTypes;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class DriftingItemsFeature extends Feature<DefaultFeatureConfig> {

    public DriftingItemsFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> function_1) {
        super(function_1);
    }

    public ItemStack getItem(World world)
    {
        LootSupplier lootSupplier = world.getServer().getLootManager().getSupplier(new Identifier(SeaSources.ModId, "gameplay/drifting_item"));
        LootContext.Builder builder = new LootContext.Builder((ServerWorld) world);
        LootContext context = builder.build(LootContextTypes.EMPTY);
        List<ItemStack> stacks = lootSupplier.getDrops(context);
        if(stacks.size() == 0)
            return ItemStack.EMPTY;
        else
            return stacks.get(0);
    }

    @Override
    public boolean generate(IWorld var1, ChunkGenerator<? extends ChunkGeneratorConfig> var2, Random var3, BlockPos var4, DefaultFeatureConfig var5) {
        ChunkPos pos = new ChunkPos(var4);
        for(int z = 0; z < 16; z++)
        {
            for(int x = 0; x < 16; x++)
            {
                BlockPos blockPos = pos.toBlockPos(x, var1.getSeaLevel() - 1, z);
                if(var1.getBlockState(blockPos).getBlock() == Blocks.WATER)
                {
                    ItemStack item = getItem(var1.getWorld());
                    if(!item.isEmpty())
                    {
                        var1.setBlockState(blockPos, SeaSources.DRIFTING_ITEM.getDefaultState(), 2);
                        ((DriftingItemBlockEntity)var1.getBlockEntity(blockPos)).SetItem(item);
                    }
                }
            }
        }

        return true;
    }
}
