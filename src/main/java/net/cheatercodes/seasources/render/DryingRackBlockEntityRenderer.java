package net.cheatercodes.seasources.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.cheatercodes.seasources.blockentities.DryingRackBlockEntity;
import net.cheatercodes.seasources.blocks.DryingRackBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Direction;

public class DryingRackBlockEntityRenderer extends BlockEntityRenderer<DryingRackBlockEntity> {

    public DryingRackBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(DryingRackBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        //Render item
        Direction dir = blockEntity.getCachedState().get(DryingRackBlock.FACING).getOpposite();

        matrices.push();
        matrices.translate(0.5F, 0.35F, 0.5F);
        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(dir.asRotation()));
        matrices.scale(0.4F, 0.4F, 0.4F);

        MinecraftClient.getInstance().getItemRenderer().renderItem(blockEntity.getItem(), ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers);

        matrices.pop();
    }
}
