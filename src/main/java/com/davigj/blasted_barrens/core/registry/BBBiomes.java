package com.davigj.blasted_barrens.core.registry;

import com.davigj.blasted_barrens.core.BlastedBarrens;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class BBBiomes {
    public static final ResourceKey<Biome> BLASTED_BARRENS = register("blasted_barrens");

    private static ResourceKey<Biome> register(String string) {
        return ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(BlastedBarrens.MOD_ID, string));
    }
}
