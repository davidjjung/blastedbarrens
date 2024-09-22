package com.davigj.blasted_barrens.core.registry;

import com.davigj.blasted_barrens.core.BlastedBarrens;
import com.davigj.blasted_barrens.core.other.BBGeneration;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = BlastedBarrens.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BBBiomes {
    public static final ResourceKey<Biome> BLASTED_BARRENS = createKey("blasted_barrens");

    public static void bootstrap(BootstapContext<Biome> context) {
        HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        context.register(BLASTED_BARRENS, blastedBarrens(features, carvers));
    }

    public static ResourceKey<Biome> createKey(String name) {
        return ResourceKey.create(Registries.BIOME, new ResourceLocation(BlastedBarrens.MOD_ID, name));
    }

    private static Biome blastedBarrens(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        BBGeneration.blastedBarrens(generation);

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 100, 4, 4));
        return biome(false, 2.0F, 0.0F, 8749688, 11583419, 8493445,
                6187392, 16250094, 4867670, spawns, generation, null);
    }

    private static Biome biome(boolean precipitation, float temperature, float downfall, int skyColor, int grassColor, int foliageColor, int waterColor, int waterFogColor, int fogColor, MobSpawnSettings.Builder spawns, BiomeGenerationSettings.Builder generation, @Nullable Music music) {
        return (new Biome.BiomeBuilder()).hasPrecipitation(precipitation).temperature(temperature).downfall(downfall)
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .grassColorOverride(grassColor).foliageColorOverride(foliageColor)
                        .waterColor(waterColor).waterFogColor(waterFogColor).fogColor(fogColor)
                        .skyColor(skyColor).ambientParticle(new AmbientParticleSettings(BBParticleTypes.BARRENS_ASH.get(), 0.005F))
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).backgroundMusic(music)
                        .build()).mobSpawnSettings(spawns.build()).generationSettings(generation.build()).build();
    }

}
