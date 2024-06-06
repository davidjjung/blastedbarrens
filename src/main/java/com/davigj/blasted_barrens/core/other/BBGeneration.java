package com.davigj.blasted_barrens.core.other;

import com.davigj.blasted_barrens.core.BlastedBarrens;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.fml.common.Mod;

import static com.davigj.blasted_barrens.core.registry.BBFeatures.BBPlacedFeatures.*;

@Mod.EventBusSubscriber(modid = BlastedBarrens.MOD_ID)
public class BBGeneration {
    public static void blastedBarrens(BiomeGenerationSettings.Builder generation) {
        BiomeDefaultFeatures.addFossilDecoration(generation);
        OverworldBiomes.globalOverworldGeneration(generation);
        generation.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, CRATER);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addDefaultSoftDisks(generation);
        BiomeDefaultFeatures.addDefaultFlowers(generation);
        BiomeDefaultFeatures.addDefaultGrass(generation);
        BiomeDefaultFeatures.addDesertVegetation(generation);
        BiomeDefaultFeatures.addDefaultMushrooms(generation);
        BiomeDefaultFeatures.addDesertExtraVegetation(generation);
        BiomeDefaultFeatures.addDesertExtraDecoration(generation);

        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, BARRENS_VEGETATION_PATCH);
    }
}
