package net.cheatercodes.seasources.blockentities;

import net.cheatercodes.seasources.SeaSources;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.container.Container;
import net.minecraft.container.FurnaceContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class BrickFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    public BrickFurnaceBlockEntity() {
        super(SeaSources.BRICK_FURNACE_BLOCK_ENTITY, RecipeType.SMELTING);
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("container.brick_furnace");
    }

    @Override
    protected Container createContainer(int var1, PlayerInventory var2) {
        return new FurnaceContainer(var1, var2, this, this.propertyDelegate);
    }
}
