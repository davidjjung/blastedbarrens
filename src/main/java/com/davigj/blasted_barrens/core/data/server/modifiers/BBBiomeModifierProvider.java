package com.davigj.blasted_barrens.core.data.server.modifiers;

import com.davigj.blasted_barrens.core.BlastedBarrens;
import com.davigj.blasted_barrens.core.other.tags.BBBiomeTags;
import com.davigj.blasted_barrens.core.registry.BBFeatures;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddFeaturesBiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BBBiomeModifierProvider {

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        addFeature(context, "smear_crater", BBBiomeTags.HAS_SMEAR_CRATER, Decoration.LOCAL_MODIFICATIONS, BBFeatures.BBPlacedFeatures.SMEAR_CRATER);
        addFeature(context, "periphery_crater", BBBiomeTags.HAS_PERIPHERY_CRATER, Decoration.LOCAL_MODIFICATIONS, BBFeatures.BBPlacedFeatures.PERIPHERY_CRATER);
    }

    @SafeVarargs
    private static void addFeature(BootstapContext<BiomeModifier> context, String name, TagKey<Biome> biomes, Decoration step, ResourceKey<PlacedFeature>... features) {
        register(context, "add_feature/" + name, () -> new AddFeaturesBiomeModifier(context.lookup(Registries.BIOME).getOrThrow(biomes), featureSet(context, features), step));
    }

    private static void register(BootstapContext<BiomeModifier> context, String name, Supplier<? extends BiomeModifier> modifier) {
        context.register(ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(BlastedBarrens.MOD_ID, name)), modifier.get());
    }

    @SafeVarargs
    private static HolderSet<PlacedFeature> featureSet(BootstapContext<?> context, ResourceKey<PlacedFeature>... features) {
        return HolderSet.direct(Stream.of(features).map(key -> context.lookup(Registries.PLACED_FEATURE).getOrThrow(key)).collect(Collectors.toList()));
    }
}
