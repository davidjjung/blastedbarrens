package com.davigj.blasted_barrens.core.other;

import com.davigj.blasted_barrens.core.registry.BBBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;

public class BBClientCompat {
    public static void registerRenderLayers() {
        ItemBlockRenderTypes.setRenderLayer(BBBlocks.SCORCHED_GRASS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BBBlocks.SINGED_BUSH.get(), RenderType.cutout());
    }
}
