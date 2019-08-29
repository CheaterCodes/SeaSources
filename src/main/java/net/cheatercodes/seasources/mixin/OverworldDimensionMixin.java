package net.cheatercodes.seasources.mixin;

import net.cheatercodes.seasources.SeaSources;
import net.cheatercodes.seasources.world.SeablockChunkGenerator;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.level.LevelGeneratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OverworldDimension.class)
public abstract class OverworldDimensionMixin extends Dimension {

    public OverworldDimensionMixin(World world_1, DimensionType dimensionType_1) {
        super(world_1, dimensionType_1);
    }

    @Inject(method = "createChunkGenerator", at = @At("HEAD"), cancellable = true)
    private void onCreateChunkGenerator(CallbackInfoReturnable<ChunkGenerator<? extends ChunkGeneratorConfig>> info) {
        LevelGeneratorType levelGeneratorType = super.world.getLevelProperties().getGeneratorType();
        if(levelGeneratorType == SeaSources.SEABLOCK_LEVEL_GENERATOR_TYPE) {
            SeablockChunkGenerator chunkGenerator = new SeablockChunkGenerator(world);
            info.setReturnValue(chunkGenerator);
            info.cancel();
        }
    }
}
