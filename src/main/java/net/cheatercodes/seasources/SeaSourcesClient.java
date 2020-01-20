package net.cheatercodes.seasources;

import net.cheatercodes.seasources.render.DriftingItemBlockEntityRenderer;
import net.cheatercodes.seasources.render.DryingRackBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;

public class SeaSourcesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //BlockEntityRenderers
        BlockEntityRendererRegistry.INSTANCE.register(SeaSources.DRIFTING_ITEM_BLOCK_ENTITY, DriftingItemBlockEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(SeaSources.DRYING_RACK_BLOCK_ENTITY, DryingRackBlockEntityRenderer::new);
    }
}
