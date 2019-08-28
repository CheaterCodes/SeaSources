package net.cheatercodes.seasources;

import net.cheatercodes.seasources.blockentities.DriftingItemBlockEntity;
import net.cheatercodes.seasources.blocks.DriftingItemBlock;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class SeaSources implements ModInitializer {

    public static final String ModId = "seasources";

    public static final Block DRIFTING_ITEM = new DriftingItemBlock();

    public static BlockEntityType<DriftingItemBlockEntity> DRIFTING_ITEM_BLOCK_ENTITY;

	@Override
	public void onInitialize() {

		Registry.register(Registry.BLOCK, new Identifier(ModId, "drifting_item"), DRIFTING_ITEM);

		DRIFTING_ITEM_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY, new Identifier(ModId, "drifting_item"),
				BlockEntityType.Builder.create(DriftingItemBlockEntity::new, DRIFTING_ITEM).build(null));
	}
}
