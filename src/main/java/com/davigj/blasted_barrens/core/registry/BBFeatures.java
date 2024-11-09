package com.davigj.blasted_barrens.core.registry;

import com.davigj.blasted_barrens.common.levelgen.feature.CraterFeature;
import com.davigj.blasted_barrens.common.levelgen.feature.PeripheryCraterFeature;
import com.davigj.blasted_barrens.common.levelgen.feature.SmearCraterFeature;
import com.davigj.blasted_barrens.core.BlastedBarrens;
import net.minecraft.commands.CommandFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.BlockBlobFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.RandomPatchFeature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class BBFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, BlastedBarrens.MOD_ID);

    public static final RegistryObject<Feature<RandomPatchConfiguration>> PATCH_SINGED_BUSH = FEATURES.register("patch_singed_bush", () -> new RandomPatchFeature(RandomPatchConfiguration.CODEC));
    public static final RegistryObject<Feature<BlockStateConfiguration>> CRATER = FEATURES.register("crater", () -> new CraterFeature(BlockStateConfiguration.CODEC));
    public static final RegistryObject<Feature<BlockStateConfiguration>> SMEAR_CRATER = FEATURES.register("smear_crater", () -> new SmearCraterFeature(BlockStateConfiguration.CODEC));
    public static final RegistryObject<Feature<BlockStateConfiguration>> PERIPHERY_CRATER = FEATURES.register("periphery_crater", () -> new PeripheryCraterFeature(BlockStateConfiguration.CODEC));

    public static final class BBConfiguredFeatures {
        public static final ResourceKey<ConfiguredFeature<?, ?>> BARRENS_VEGETATION_PATCH = createKey("barrens_vegetation_patch");
        public static final ResourceKey<ConfiguredFeature<?, ?>> CRATER = createKey("crater");
        public static final ResourceKey<ConfiguredFeature<?, ?>> SMEAR_CRATER = createKey("smear_crater");
        public static final ResourceKey<ConfiguredFeature<?, ?>> PERIPHERY_CRATER = createKey("periphery_crater");

        public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
            register(context, BARRENS_VEGETATION_PATCH, BBFeatures.PATCH_SINGED_BUSH.get(), grassPatch(
                    new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                            .add(BBBlocks.SINGED_BUSH.get().defaultBlockState(), 8)
                            .add(BBBlocks.SCORCHED_GRASS.get().defaultBlockState(), 20)), 16));
            register(context, CRATER, BBFeatures.CRATER.get(),
                    new BlockStateConfiguration(Blocks.AIR.defaultBlockState()));
            register(context, SMEAR_CRATER, BBFeatures.SMEAR_CRATER.get(),
                    new BlockStateConfiguration(Blocks.AIR.defaultBlockState()));
            register(context, PERIPHERY_CRATER, BBFeatures.PERIPHERY_CRATER.get(),
                    new BlockStateConfiguration(Blocks.AIR.defaultBlockState()));
        }

        public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
            return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(BlastedBarrens.MOD_ID, name));
        }

        public static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
            context.register(key, new ConfiguredFeature<>(feature, config));
        }

        private static RandomPatchConfiguration grassPatch(BlockStateProvider p_195203_, int p_195204_) {
            return FeatureUtils.simpleRandomPatchConfiguration(p_195204_, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(p_195203_)));
        }
    }

    public static final class BBPlacedFeatures {
        public static final ResourceKey<PlacedFeature> BARRENS_VEGETATION_PATCH = createKey("barrens_vegetation_patch");
        public static final ResourceKey<PlacedFeature> CRATER = createKey("crater");
        public static final ResourceKey<PlacedFeature> SMEAR_CRATER = createKey("smear_crater");
        public static final ResourceKey<PlacedFeature> PERIPHERY_CRATER = createKey("periphery_crater");

        public static void bootstrap(BootstapContext<PlacedFeature> context) {
            register(context, BARRENS_VEGETATION_PATCH, BBConfiguredFeatures.BARRENS_VEGETATION_PATCH, RarityFilter.onAverageOnceEvery(1),
                    InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
            register(context, CRATER, BBConfiguredFeatures.CRATER, RarityFilter.onAverageOnceEvery(2),
                    InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
            register(context, SMEAR_CRATER, BBConfiguredFeatures.SMEAR_CRATER, RarityFilter.onAverageOnceEvery(500),
                    InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
            register(context, PERIPHERY_CRATER, BBConfiguredFeatures.PERIPHERY_CRATER, RarityFilter.onAverageOnceEvery(50),
                    InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
        }

        public static ResourceKey<PlacedFeature> createKey(String name) {
            return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(BlastedBarrens.MOD_ID, name));
        }

        public static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> modifiers) {
            context.register(key, new PlacedFeature(context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(feature), modifiers));
        }

        public static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> feature, PlacementModifier... modifiers) {
            register(context, key, feature, List.of(modifiers));
        }
    }
}
