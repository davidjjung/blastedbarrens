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
                Codec.FLOAT.optionalFieldOf("frequency", 0.0001f).forGetter(provider -> provider.frequency) // Adjusted frequency
        ).apply(instance, SlowNoiseModdedBiomeProvider::new);
    });

    private final float frequency;
    private final SimplexNoise noiseGenerator;

    public SlowNoiseModdedBiomeProvider(float frequency) {
        this.frequency = frequency;
        this.noiseGenerator = new SimplexNoise(RandomSource.create(1049L));
    }

    @Override
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler, BiomeSource original, Registry<Biome> registry) {
        double noiseValue = customNoiseFunction(x, z, this.frequency);
        return selectBiomeBasedOnNoise(noiseValue, registry);
    }

    private double customNoiseFunction(int x, int z, float frequency) {
        double noiseValue = this.noiseGenerator.getValue(x * frequency, z * frequency);
        // Normalize the noise value to range [0, 1]
        return (noiseValue + 70.0) / 140.0;
    }

    private Holder<Biome> selectBiomeBasedOnNoise(double noiseValue, Registry<Biome> registry) {
        System.out.println(noiseValue + " is Noise Value");
        if (noiseValue > 0.5) {
            System.out.println("Choosing BLASTED_BARRENS");
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
        return Set.of(
                registry.getHolderOrThrow(BlueprintBiomes.ORIGINAL_SOURCE_MARKER)
                , registry.getHolderOrThrow(BBBiomes.BLASTED_BARRENS)
        );
    }
}
