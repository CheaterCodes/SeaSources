package net.cheatercodes.seasources.world;

import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;

public class SeablockChunkGenerator extends OverworldChunkGenerator {

    public SeablockChunkGenerator(IWorld iWorld_1) {
        super(iWorld_1, new SeablockBiomeSource(), new SeablockChunkGeneratorConfig());
    }

    @Override
    public int getSpawnHeight() {
        return this.world.getSeaLevel();
    }
}
