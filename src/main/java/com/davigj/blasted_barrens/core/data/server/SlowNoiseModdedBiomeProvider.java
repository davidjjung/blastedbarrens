package com.davigj.blasted_barrens.core.data.server;

import com.davigj.blasted_barrens.core.registry.BBBiomes;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.blueprint.core.registry.BlueprintBiomes;
import com.teamabnormals.blueprint.core.util.BiomeUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;
import net.minecraftforge.common.Tags;

import java.util.Set;

public class SlowNoiseModdedBiomeProvider implements BiomeUtil.ModdedBiomeProvider {

    public static final Codec<SlowNoiseModdedBiomeProvider> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                Codec.FLOAT.optionalFieldOf("frequency", 0.1f).forGetter(provider -> provider.frequency)
        ).apply(instance, SlowNoiseModdedBiomeProvider::new);
    });

    private final float frequency;
    private final SimplexNoise simplexNoise;

    public SlowNoiseModdedBiomeProvider(float frequency) {
        this.frequency = frequency;
        this.simplexNoise = new SimplexNoise(RandomSource.create(2048L));
    }

    @Override
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler, BiomeSource original,
                                       Registry<Biome> registry) {
        if (original.getNoiseBiome(x,y,z,sampler).is(Tags.Biomes.IS_DRY_OVERWORLD)) {
            return selectBiomeBasedOnNoise(x, z, registry);
        } else {
            return registry.getHolderOrThrow(BlueprintBiomes.ORIGINAL_SOURCE_MARKER);
        }
    }

    private double combinedNoiseFunction(int x, int z) {
        return -250 + (500 * Math.sin((Mth.sqrt(Mth.abs(x)) * frequency) - 1.6) * Math.cos((Mth.sqrt(Mth.abs(z)) * frequency)));
//        double simplexNoiseValue = 500 * simplexNoise.getValue(x * frequency, z * frequency);
//        return ((trigonometricNoise * 1.9) + (simplexNoiseValue) * 0.1) / 2.0;
    }

    private Holder<Biome> selectBiomeBasedOnNoise(int x, int z, Registry<Biome> registry) {
        if (x > 2000 && z < 2000) {
            return registry.getHolderOrThrow(BlueprintBiomes.ORIGINAL_SOURCE_MARKER);
        }
        double noiseValue = combinedNoiseFunction(x, z);

        if (noiseValue > 0) {
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
