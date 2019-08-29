package net.cheatercodes.seasources.world;

import net.cheatercodes.seasources.SeaSources;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.SeagrassFeatureConfig;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

public class SeablockBiome extends Biome {
    public SeablockBiome() {
        super(new Biome.Settings()
                .configureSurfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_CONFIG)
                .precipitation(Precipitation.RAIN).category(Category.OCEAN).depth(-1.8f).scale(0.1f)
                .temperature(0.5f).downfall(0.5f).waterColor(0x88CAE2).waterFogColor(0x588391).parent(null));

        this.addFeature(GenerationStep.Feature.TOP_LAYER_MODIFICATION, configureFeature(SeaSources.STARTING_RAFT_FEATURE, FeatureConfig.DEFAULT, Decorator.NOPE, DecoratorConfig.DEFAULT));
        this.addFeature(GenerationStep.Feature.TOP_LAYER_MODIFICATION, configureFeature(SeaSources.DRIFTING_ITEMS_FEATURE, FeatureConfig.DEFAULT, Decorator.NOPE, DecoratorConfig.DEFAULT));
    }
}
