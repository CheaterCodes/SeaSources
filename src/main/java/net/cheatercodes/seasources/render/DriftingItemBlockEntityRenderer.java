package net.cheatercodes.seasources.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.cheatercodes.seasources.blockentities.DriftingItemBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;

public class DriftingItemBlockEntityRenderer extends BlockEntityRenderer<DriftingItemBlockEntity> {

    @Override
    public void render(DriftingItemBlockEntity blockEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        float time = blockEntity.getWorld().getTime() + partialTicks;
        GlStateManager.translated(x + 0.5 + 0.1 * Math.sin(time / 30), y + 0.30 + 0.05 * Math.sin(time / 16), z + 0.5 + 0.1 * Math.sin(time / 37 + 2));
        GlStateManager.rotatef(1000 * (float)Math.sin(time / 200), 0, 1, 0);
        MinecraftClient.getInstance().getItemRenderer().renderItem(blockEntity.itemStack, ModelTransformation.Type.GROUND);
        GlStateManager.popMatrix();
    }
}
