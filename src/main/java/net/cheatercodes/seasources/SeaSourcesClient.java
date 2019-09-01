package net.cheatercodes.seasources;

import net.cheatercodes.seasources.blockentities.DriftingItemBlockEntity;
import net.cheatercodes.seasources.blockentities.DryingRackBlockEntity;
import net.cheatercodes.seasources.blockentities.WaterStrainerBlockEntity;
import net.cheatercodes.seasources.render.DriftingItemBlockEntityRenderer;
import net.cheatercodes.seasources.render.DryingRackBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;

public class SeaSourcesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //BlockEntityRenderers
        BlockEntityRendererRegistry.INSTANCE.register(DriftingItemBlockEntity.class, new DriftingItemBlockEntityRenderer());
        BlockEntityRendererRegistry.INSTANCE.register(DryingRackBlockEntity.class, new DryingRackBlockEntityRenderer());
    }
}
