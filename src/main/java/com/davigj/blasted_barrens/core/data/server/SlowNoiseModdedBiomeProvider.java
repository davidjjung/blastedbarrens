package com.davigj.blasted_barrens.core.data.server;

import com.davigj.blasted_barrens.core.registry.BBBiomes;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.blueprint.core.registry.BlueprintBiomes;
import com.teamabnormals.blueprint.core.util.BiomeUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

import java.util.Random;
import java.util.Set;

public class SlowNoiseModdedBiomeProvider implements BiomeUtil.ModdedBiomeProvider {

    public static final Codec<SlowNoiseModdedBiomeProvider> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                Codec.FLOAT.optionalFieldOf("frequency", 0.0000001f).forGetter(provider -> provider.frequency) // Adjusted frequency
        ).apply(instance, SlowNoiseModdedBiomeProvider::new);
    });

    private final float frequency;
    private final SimplexNoise noiseGenerator;

    public SlowNoiseModdedBiomeProvider(float frequency) {
        this.frequency = frequency;
        this.noiseGenerator = new SimplexNoise(RandomSource.create(333333333L));
    }

    @Override
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler, BiomeSource original,
                                       Registry<Biome> registry) {
        double distanceFromSpawn = Math.sqrt(x * x + z * z);
        if (distanceFromSpawn < 100) {
            double noiseValue = customNoiseFunction(x, z, frequency);
            System.out.println("Noise value: " + noiseValue);
        }
//            System.out.println("Distance from Spawn: " + distanceFromSpawn);
        return registry.getHolderOrThrow(BlueprintBiomes.ORIGINAL_SOURCE_MARKER);
//        if (original.getNoiseBiome(x,y,z,sampler).is(BiomeTags.IS_OCEAN) || original.getNoiseBiome(x,y,z,sampler).is(BiomeTags.IS_MOUNTAIN)) {
//            return registry.getHolderOrThrow(BlueprintBiomes.ORIGINAL_SOURCE_MARKER);
//        } else {
//            return selectBiomeBasedOnNoise(x, z, registry);
//        }
    }

    private double customNoiseFunction(int x, int z, float frequency) {
        return this.noiseGenerator.getValue(x * frequency, z * frequency);
    }

    private Holder<Biome> selectBiomeBasedOnNoise(int x, int z, Registry<Biome> registry) {
        double noiseValue = customNoiseFunction(x, z, frequency);
        double distanceFromSpawn = Math.sqrt(x * x + z * z);
        double spawnRadius = 2000;
        double maxDistance = 6000;

        double radiusDependentProb = Math.min((distanceFromSpawn - spawnRadius) / (maxDistance - spawnRadius), 1.0);

        double threshold = 1.0 - 0.5 * radiusDependentProb;

        // Debug logs to trace the values
//        System.out.println("Distance from Spawn: " + distanceFromSpawn);
//        System.out.println("Noise Value: " + noiseValue);
//        System.out.println("Threshold: " + threshold);

        if (noiseValue > threshold) {
//            System.out.println("Selected Biome: BLASTED_BARRENS");
            return registry.getHolderOrThrow(BBBiomes.BLASTED_BARRENS);
        } else {
            return registry.getHolderOrThrow(BlueprintBiomes.ORIGINAL_SOURCE_MARKER);
        }
    }



    @Override
    public Codec<? extends BiomeUtil.ModdedBiomeProvider> codec() {
        return CODEC;
    }

    @Override
    public Set<Holder<Biome>> getAdditionalPossibleBiomes(Registry<Biome> registry) {
        return Set.of(registry.getHolderOrThrow(BBBiomes.BLASTED_BARRENS));
    }
}
