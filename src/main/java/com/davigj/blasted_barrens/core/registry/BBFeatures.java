package com.davigj.blasted_barrens.core.registry;

import com.davigj.blasted_barrens.common.levelgen.feature.CraterFeature;
import com.davigj.blasted_barrens.core.BlastedBarrens;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.RandomPatchFeature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class BBFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, BlastedBarrens.MOD_ID);

    public static final DeferredHolder<Feature<?>, Feature<RandomPatchConfiguration>> PATCH_SINGED_BUSH = FEATURES.register("patch_singed_bush", () -> new RandomPatchFeature(RandomPatchConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<BlockStateConfiguration>> CRATER = FEATURES.register("crater", () -> new CraterFeature(BlockStateConfiguration.CODEC));

    public static final class BBConfiguredFeatures {
        public static final ResourceKey<ConfiguredFeature<?, ?>> BARRENS_VEGETATION_PATCH = createKey("barrens_vegetation_patch");
        public static final ResourceKey<ConfiguredFeature<?, ?>> CRATER = createKey("crater");

        public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
            HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
            HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

            register(context, BARRENS_VEGETATION_PATCH, BBFeatures.PATCH_SINGED_BUSH.get(),
                    grassPatch(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                            .add(BBBlocks.SINGED_BUSH.get().defaultBlockState(), 8)
                            .add(BBBlocks.SCORCHED_GRASS.get().defaultBlockState(), 20)), 16));
            register(context, CRATER, BBFeatures.CRATER.get(),
                    new BlockStateConfiguration(Blocks.AIR.defaultBlockState()));
        }

        public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
            return ResourceKey.create(Registries.CONFIGURED_FEATURE, BlastedBarrens.location(name));
        }

        public static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
            context.register(key, new ConfiguredFeature<>(feature, config));
        }

        private static RandomPatchConfiguration grassPatch(BlockStateProvider p_195203_, int p_195204_) {
            return FeatureUtils.simpleRandomPatchConfiguration(p_195204_, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(p_195203_)));
        }
    }

    public static final class BBPlacedFeatures {
        public static final ResourceKey<PlacedFeature> BARRENS_VEGETATION_PATCH = createKey("barrens_vegetation_patch");
        public static final ResourceKey<PlacedFeature> CRATER = createKey("crater");

        public static void bootstrap(BootstrapContext<PlacedFeature> context) {
            register(context, BARRENS_VEGETATION_PATCH, BBConfiguredFeatures.BARRENS_VEGETATION_PATCH, CountPlacement.of(1),
                    InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
            register(context, CRATER, BBConfiguredFeatures.CRATER, RarityFilter.onAverageOnceEvery(2),
                    InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
        }

        public static ResourceKey<PlacedFeature> createKey(String name) {
            return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(BlastedBarrens.MOD_ID, name));
        }

        public static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureHolder, PlacementModifier... modifiers) {
            context.register(key, new PlacedFeature(context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(configuredFeatureHolder), List.of(modifiers)));
        }
    }
}
