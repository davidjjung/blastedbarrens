package com.davigj.blasted_barrens.core.data.server;

import com.davigj.blasted_barrens.core.registry.BBBiomes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.blueprint.core.registry.BlueprintBiomes;
import com.teamabnormals.blueprint.core.util.BiomeUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

import java.util.Set;

public class SlowNoiseModdedBiomeProvider implements BiomeUtil.ModdedBiomeProvider {

    public static final Codec<SlowNoiseModdedBiomeProvider> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                Codec.FLOAT.optionalFieldOf("frequency", 0.1f).forGetter(provider -> provider.frequency)
        ).apply(instance, SlowNoiseModdedBiomeProvider::new);
    });

    private final float frequency;

    public SlowNoiseModdedBiomeProvider(float frequency) {
        this.frequency = frequency;
    }

    @Override
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler, BiomeSource original,
                                       Registry<Biome> registry) {
        return !original.getNoiseBiome(x,y,z,sampler).is(BiomeTags.IS_OCEAN) ? selectBiomeBasedOnNoise(x, z, registry)
                : registry.getHolderOrThrow(BlueprintBiomes.ORIGINAL_SOURCE_MARKER);
    }

    private Holder<Biome> selectBiomeBasedOnNoise(int x, int z, Registry<Biome> registry) {
        if (x > 2000 && z < 2000 || z < 300 || x < 200) {
            return registry.getHolderOrThrow(BlueprintBiomes.ORIGINAL_SOURCE_MARKER);
        }
        return trigNoise(x, z) > 0 ? registry.getHolderOrThrow(BBBiomes.BLASTED_BARRENS)
                : registry.getHolderOrThrow(BlueprintBiomes.ORIGINAL_SOURCE_MARKER);
    }

    private double trigNoise(int x, int z) {
        return -250 + (500 * Math.sin((Mth.sqrt(Mth.abs(x)) * frequency) - 2.44)
                * Math.cos((Mth.sqrt(Mth.abs(z)) * frequency)));
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
