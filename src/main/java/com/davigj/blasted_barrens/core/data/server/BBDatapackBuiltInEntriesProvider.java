package com.davigj.blasted_barrens.core.data.server;

import com.davigj.blasted_barrens.core.BlastedBarrens;
import com.davigj.blasted_barrens.core.data.BBBiomeSlices;
import com.davigj.blasted_barrens.core.data.server.modifiers.BBBiomeModifierProvider;
import com.davigj.blasted_barrens.core.registry.BBBiomes;
import com.davigj.blasted_barrens.core.registry.BBFeatures;
import com.teamabnormals.blueprint.core.registry.BlueprintDataPackRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class BBDatapackBuiltInEntriesProvider  extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, BBFeatures.BBConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, BBFeatures.BBPlacedFeatures::bootstrap)
            .add(Registries.BIOME, BBBiomes::bootstrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, BBBiomeModifierProvider::bootstrap)
            .add(BlueprintDataPackRegistries.MODDED_BIOME_SLICES, BBBiomeSlices::bootstrap)
            ;

    public BBDatapackBuiltInEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of(BlastedBarrens.MOD_ID));
    }
}
