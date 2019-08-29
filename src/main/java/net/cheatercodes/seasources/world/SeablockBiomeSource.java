package net.cheatercodes.seasources.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.cheatercodes.seasources.SeaSources;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.*;

public class SeablockBiomeSource extends BiomeSource {

    private Biome biome = SeaSources.SEABLOCK_BIOME;

    @Override
    public Biome getBiome(int var1, int var2) {
        return biome;
    }

    @Override
    public List<Biome> getSpawnBiomes() {
        return Lists.newArrayList(biome);
    }

    @Override
    public Biome[] sampleBiomes(int x, int y, int xSize, int zSize, boolean var5) {
        Biome[] biomes = new Biome[xSize * zSize];
        Arrays.fill(biomes, 0, xSize * zSize, biome);
        return biomes;
    }

    @Override
    public Set<Biome> getBiomesInArea(int var1, int var2, int var3) {
        return Sets.newHashSet(biome);
    }

    @Override
    public BlockPos locateBiome(int var1, int var2, int var3, List<Biome> var4, Random var5) {
        return BlockPos.ORIGIN;
    }

    @Override
    public boolean hasStructureFeature(StructureFeature<?> var1) {
        return false;
    }

    @Override
    public Set<BlockState> getTopMaterials() {
        if (this.topMaterials.isEmpty()) {
            this.topMaterials.add(biome.getSurfaceConfig().getTopMaterial());
        }

        return this.topMaterials;
    }
}
