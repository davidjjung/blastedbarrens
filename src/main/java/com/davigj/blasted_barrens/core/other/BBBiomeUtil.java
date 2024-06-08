package com.davigj.blasted_barrens.core.other;

import com.davigj.blasted_barrens.core.registry.BBBiomes;
import com.teamabnormals.blueprint.core.registry.BlueprintBiomes;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

public class BBBiomeUtil {
    public static Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler, BiomeSource original,
                                              Registry<Biome> registry) {
        if (!original.getNoiseBiome(x,y,z,sampler).is(BiomeTags.IS_OCEAN)) {
            return selectBiomeBasedOnNoise(x, z, registry);
        } else {
            return registry.getHolderOrThrow(BlueprintBiomes.ORIGINAL_SOURCE_MARKER);
        }
    }

    private static Holder<Biome> selectBiomeBasedOnNoise(int x, int z, Registry<Biome> registry) {
        if (x > 2000 && z < 2000 || z < 300 || x < 200) {
            return registry.getHolderOrThrow(BlueprintBiomes.ORIGINAL_SOURCE_MARKER);
        }
        double noiseValue = trigNoise(x, z);

        if (noiseValue > 0) {
            return registry.getHolderOrThrow(BBBiomes.BLASTED_BARRENS);
        } else {
            return registry.getHolderOrThrow(BlueprintBiomes.ORIGINAL_SOURCE_MARKER);
        }
    }

    private static double trigNoise(int x, int z) {
        return -250 + (500 * Math.sin((Mth.sqrt(Mth.abs(x)) * 0.1) - 2.44)
                * Math.cos((Mth.sqrt(Mth.abs(z)) * 0.1)));
    }
}
