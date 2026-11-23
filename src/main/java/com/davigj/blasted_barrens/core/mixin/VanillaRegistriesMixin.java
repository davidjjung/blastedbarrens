package com.davigj.blasted_barrens.core.mixin;

import com.davigj.blasted_barrens.core.registry.BBBiomes;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.biome.BiomeData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BiomeData.class)
public abstract class VanillaRegistriesMixin {
    @Inject(method = "bootstrap", at = @At("TAIL"))
    private static void blasted$registerBiomePlaceholders(BootstrapContext<Biome> context, CallbackInfo ci) {
        bb$registerPlaceholder(context, BBBiomes.BLASTED_BARRENS);
    }

    @Unique
    private static void bb$registerPlaceholder(BootstrapContext<Biome> context, ResourceKey<Biome> key) {
        context.register(key, (new Biome.BiomeBuilder()).hasPrecipitation(true).temperature(0.8F).downfall(0.4F).specialEffects((new BiomeSpecialEffects.Builder()).waterColor(4159204).waterFogColor(329011).fogColor(12638463).skyColor(7907327).build()).mobSpawnSettings(MobSpawnSettings.EMPTY).generationSettings(BiomeGenerationSettings.EMPTY).build());
    }
}