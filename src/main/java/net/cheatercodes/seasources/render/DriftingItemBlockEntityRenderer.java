package net.cheatercodes.seasources.render;

import net.cheatercodes.seasources.blockentities.DriftingItemBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.MathHelper;

public class DriftingItemBlockEntityRenderer extends BlockEntityRenderer<DriftingItemBlockEntity> {

    public DriftingItemBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(DriftingItemBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        long hashCode = MathHelper.hashCode(blockEntity.getPos().getX(), blockEntity.getPos().getY(), blockEntity.getPos().getZ());
        //Progress from -PI to +PI
        float progress = ((float)(short)(blockEntity.getWorld().getTime() + hashCode)) / Short.MAX_VALUE * (float)Math.PI;

        matrices.push();
        matrices.translate(0.5 + 0.1 * Math.sin(progress * 256), 0.30 + 0.05 * Math.sin(progress * 512), 0.5 + 0.1 * Math.sin(progress * 256 + 2));
        matrices.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion(128 * (float)Math.sin(progress)));

        MinecraftClient.getInstance().getItemRenderer().renderItem(blockEntity.itemStack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers);

        matrices.pop();
    }
}
