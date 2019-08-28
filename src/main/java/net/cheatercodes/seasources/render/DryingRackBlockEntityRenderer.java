package net.cheatercodes.seasources.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.cheatercodes.seasources.blockentities.DryingRackBlockEntity;
import net.cheatercodes.seasources.blocks.DryingRackBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.util.math.Direction;

public class DryingRackBlockEntityRenderer extends BlockEntityRenderer<DryingRackBlockEntity> {

    @Override
    public void render(DryingRackBlockEntity blockEntity_1, double x, double y, double z, float float_1, int int_1) {
        //Render item
        Direction dir = blockEntity_1.getCachedState().get(DryingRackBlock.FACING).getOpposite();

        GlStateManager.pushMatrix();
        GlStateManager.translatef((float)x + 0.5F, (float)y + 0.35F, (float)z + 0.5F);
        GlStateManager.rotatef(-dir.asRotation(), 0.0F, 1.0F, 0.0F);
        GlStateManager.scalef(0.4F, 0.4F, 0.4F);

        MinecraftClient.getInstance().getItemRenderer().renderItem(blockEntity_1.getItem(), ModelTransformation.Type.FIXED);

        GlStateManager.popMatrix();

        super.render(blockEntity_1, x, y, z, float_1, int_1);
    }
}
