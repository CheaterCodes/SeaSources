package net.cheatercodes.seasources;

import com.sun.org.apache.xpath.internal.operations.Mod;
import net.cheatercodes.seasources.blockentities.DriftingItemBlockEntity;
import net.cheatercodes.seasources.blockentities.DryingRackBlockEntity;
import net.cheatercodes.seasources.blocks.DriftingItemBlock;
import net.cheatercodes.seasources.blocks.DryingRackBlock;
import net.cheatercodes.seasources.blocks.PillarSlab;
import net.cheatercodes.seasources.world.DriftingItemsFeature;
import net.cheatercodes.seasources.world.LevelGeneratorTypeCreator;
import net.cheatercodes.seasources.world.SeablockBiome;
import net.cheatercodes.seasources.world.StartingRaftFeature;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.level.LevelGeneratorType;


public class SeaSources implements ModInitializer {

    public static final String ModId = "seasources";

    public static final Block DRIFTING_ITEM = new DriftingItemBlock();
	public static final PillarBlock MAKESHIFT_PLANKS = new PillarBlock(Block.Settings.copy(Blocks.OAK_PLANKS));
	public static final PillarSlab MAKESHIFT_SLAB = new PillarSlab(Block.Settings.copy(Blocks.OAK_PLANKS));
	public static final DryingRackBlock DRYING_RACK = new DryingRackBlock(Block.Settings.copy(Blocks.OAK_PLANKS));

    public static BlockEntityType<DriftingItemBlockEntity> DRIFTING_ITEM_BLOCK_ENTITY;
	public static BlockEntityType<DryingRackBlockEntity> DRYING_RACK_BLOCK_ENTITY;

	public static LevelGeneratorType SEABLOCK_LEVEL_GENERATOR_TYPE;

	public static Biome SEABLOCK_BIOME;
	public static Feature STARTING_RAFT_FEATURE;
	public static Feature DRIFTING_ITEMS_FEATURE;

	@Override
	public void onInitialize() {
		//Blocks
		Registry.register(Registry.BLOCK, new Identifier(ModId, "drifting_item"), DRIFTING_ITEM);
		Registry.register(Registry.BLOCK, new Identifier(ModId, "makeshift_planks"), MAKESHIFT_PLANKS);
		Registry.register(Registry.BLOCK, new Identifier(ModId, "makeshift_slab"), MAKESHIFT_SLAB);
		Registry.register(Registry.BLOCK, new Identifier(ModId, "drying_rack"), DRYING_RACK);

		//BlockItems
		Registry.register(Registry.ITEM, new Identifier(ModId, "makeshift_planks"), new BlockItem(MAKESHIFT_PLANKS, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
		Registry.register(Registry.ITEM, new Identifier(ModId, "makeshift_slab"), new BlockItem(MAKESHIFT_SLAB, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
		Registry.register(Registry.ITEM, new Identifier(ModId,"drying_rack"), new BlockItem(DRYING_RACK, new Item.Settings().group(ItemGroup.DECORATIONS)));

		//BlockEntities
		DRIFTING_ITEM_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY, new Identifier(ModId, "drifting_item"), BlockEntityType.Builder.create(DriftingItemBlockEntity::new, DRIFTING_ITEM).build(null));
		DRYING_RACK_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY, new Identifier(ModId, "drying_rack"), BlockEntityType.Builder.create(DryingRackBlockEntity::new, DRYING_RACK).build(null));

		//Recipes
		DryingRecipe.TYPE = Registry.register(Registry.RECIPE_TYPE, new Identifier(ModId, "drying"), new DryingRecipe.Type());
		DryingRecipe.SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(ModId, "drying"), new DryingRecipe.Serializer());

		//World Gen
		SEABLOCK_LEVEL_GENERATOR_TYPE = LevelGeneratorTypeCreator.create("seablock");

		//Features
		STARTING_RAFT_FEATURE = Registry.register(Registry.FEATURE, new Identifier(ModId, "starting_raft"), new StartingRaftFeature(DefaultFeatureConfig::deserialize));
		DRIFTING_ITEMS_FEATURE = Registry.register(Registry.FEATURE, new Identifier(ModId, "drifting_items"), new DriftingItemsFeature(DefaultFeatureConfig::deserialize));

		//Biomes
		SEABLOCK_BIOME = Registry.register(Registry.BIOME, new Identifier(ModId, "seablock"), new SeablockBiome());
	}
}
