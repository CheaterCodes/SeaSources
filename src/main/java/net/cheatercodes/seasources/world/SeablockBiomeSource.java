package net.cheatercodes.seasources.world;

import com.google.common.collect.ImmutableSet;
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

    protected SeablockBiomeSource() {
        super(ImmutableSet.of(SeaSources.SEABLOCK_BIOME));
    }

    @Override
    public List<Biome> getSpawnBiomes() {
        return Lists.newArrayList(SeaSources.SEABLOCK_BIOME);
    }


    @Override
    public boolean hasStructureFeature(StructureFeature<?> var1) {
        return false;
    }

    @Override
    public Set<BlockState> getTopMaterials() {
        if (this.topMaterials.isEmpty()) {
            this.topMaterials.add(SeaSources.SEABLOCK_BIOME.getSurfaceConfig().getTopMaterial());
        }

        return this.topMaterials;
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return SeaSources.SEABLOCK_BIOME;
    }
}
