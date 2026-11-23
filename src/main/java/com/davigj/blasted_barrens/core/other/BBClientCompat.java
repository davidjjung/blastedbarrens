package com.davigj.blasted_barrens.core.other;

import com.davigj.blasted_barrens.core.registry.BBBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;

public class BBClientCompat {
    public static void creativeTabPlacements() {
        BBBlocks.buildCreativeTabContents();
    }
}
