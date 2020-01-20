package net.cheatercodes.seasources;

import net.cheatercodes.seasources.blockentities.*;
import net.cheatercodes.seasources.blocks.*;
import net.cheatercodes.seasources.world.DriftingItemsFeature;
import net.cheatercodes.seasources.world.LevelGeneratorTypeCreator;
import net.cheatercodes.seasources.world.SeablockBiome;
import net.cheatercodes.seasources.world.StartingRaftFeature;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.level.LevelGeneratorType;
import org.apache.commons.lang3.ObjectUtils;


public class SeaSources implements ModInitializer {

    public static final Block DRIFTING_ITEM = new DriftingItemBlock();
	public static final PillarBlock MAKESHIFT_PLANKS = new PillarBlock(Block.Settings.copy(Blocks.OAK_PLANKS));
	public static final PillarSlab MAKESHIFT_SLAB = new PillarSlab(Block.Settings.copy(Blocks.OAK_PLANKS));
	public static final DryingRackBlock DRYING_RACK = new DryingRackBlock(Block.Settings.copy(Blocks.OAK_PLANKS));
	public static final PillarBlock MAKESHIFT_LOG = new PillarBlock(Block.Settings.copy(Blocks.OAK_PLANKS));

    public static BlockEntityType<DriftingItemBlockEntity> DRIFTING_ITEM_BLOCK_ENTITY;
	public static BlockEntityType<DryingRackBlockEntity> DRYING_RACK_BLOCK_ENTITY;


	public static LevelGeneratorType SEABLOCK_LEVEL_GENERATOR_TYPE;

	public static Biome SEABLOCK_BIOME;
	public static Feature STARTING_RAFT_FEATURE;
	public static Feature DRIFTING_ITEMS_FEATURE;

	@Override
	public void onInitialize() {
		//Blocks
		Registry.register(Registry.BLOCK, new Identifier("seasources", "drifting_item"), DRIFTING_ITEM);
		Registry.register(Registry.BLOCK, new Identifier("seasources", "makeshift_planks"), MAKESHIFT_PLANKS);
		Registry.register(Registry.BLOCK, new Identifier("seasources", "makeshift_slab"), MAKESHIFT_SLAB);
		Registry.register(Registry.BLOCK, new Identifier("seasources", "drying_rack"), DRYING_RACK);
		Registry.register(Registry.BLOCK, new Identifier("seasources", "makeshift_log"), MAKESHIFT_LOG);

		//BlockItems
		Registry.register(Registry.ITEM, new Identifier("seasources", "makeshift_planks"), new BlockItem(MAKESHIFT_PLANKS, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
		Registry.register(Registry.ITEM, new Identifier("seasources", "makeshift_slab"), new BlockItem(MAKESHIFT_SLAB, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
		Registry.register(Registry.ITEM, new Identifier("seasources","drying_rack"), new BlockItem(DRYING_RACK, new Item.Settings().group(ItemGroup.DECORATIONS)));
		Registry.register(Registry.ITEM, new Identifier("seasources", "makeshift_log"), new BlockItem(MAKESHIFT_LOG, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));

		//BlockEntities
		DRIFTING_ITEM_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY, new Identifier("seasources", "drifting_item"), BlockEntityType.Builder.create(DriftingItemBlockEntity::new, DRIFTING_ITEM).build(null));
		DRYING_RACK_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY, new Identifier("seasources", "drying_rack"), BlockEntityType.Builder.create(DryingRackBlockEntity::new, DRYING_RACK).build(null));

		//Items

		//Recipes
		DryingRecipe.TYPE = Registry.register(Registry.RECIPE_TYPE, new Identifier("seasources", "drying"), new DryingRecipe.Type());
		DryingRecipe.SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier("seasources", "drying"), new DryingRecipe.Serializer());

		//Loot Tables

		//World Gen
		SEABLOCK_LEVEL_GENERATOR_TYPE = LevelGeneratorTypeCreator.create("seablock");

		//Features
		STARTING_RAFT_FEATURE = Registry.register(Registry.FEATURE, new Identifier("seasources", "starting_raft"), new StartingRaftFeature(DefaultFeatureConfig::deserialize));
		DRIFTING_ITEMS_FEATURE = Registry.register(Registry.FEATURE, new Identifier("seasources", "drifting_items"), new DriftingItemsFeature(DefaultFeatureConfig::deserialize));

		//Biomes
		SEABLOCK_BIOME = Registry.register(Registry.BIOME, new Identifier("seasources", "seablock"), new SeablockBiome());
	}
}
