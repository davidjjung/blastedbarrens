package com.davigj.blasted_barrens.core.data.server;

import com.davigj.blasted_barrens.core.BlastedBarrens;
import com.davigj.blasted_barrens.core.registry.BBFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class BBDatapackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER;

    public BBDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of(BlastedBarrens.MOD_ID));
    }

    static {
        BUILDER = (new RegistrySetBuilder()).add(Registries.CONFIGURED_FEATURE,
                BBFeatures.BBConfiguredFeatures::bootstrap).add(Registries.PLACED_FEATURE, BBFeatures.BBPlacedFeatures::bootstrap);
    }
}


