package net.cheatercodes.seasources;

import net.cheatercodes.seasources.blockentities.DriftingItemBlockEntity;
import net.cheatercodes.seasources.render.DriftingItemBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;

public class SeaSourcesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(DriftingItemBlockEntity.class,
                new DriftingItemBlockEntityRenderer());
    }
}
