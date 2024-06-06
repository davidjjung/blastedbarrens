package com.davigj.blasted_barrens.common.levelgen.feature;

import com.davigj.blasted_barrens.core.registry.BBBiomes;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Predicate;

public class PeripheryCraterFeature extends Feature<BlockStateConfiguration> {
    public PeripheryCraterFeature(Codec<BlockStateConfiguration> p_65248_) {
        super(p_65248_);
    }

    public boolean place(FeaturePlaceContext<BlockStateConfiguration> context) {
        BlockPos blockpos = context.origin();
        WorldGenLevel worldgenlevel = context.level();
        RandomSource randomsource = context.random();
        Predicate<Holder<Biome>> biomePredicate = biomeHolder -> biomeHolder.is(BBBiomes.BLASTED_BARRENS);
        Pair<BlockPos, Holder<Biome>> pair = context.level().getLevel().findClosestBiome3d(biomePredicate, blockpos, 500, 64, 64);

        if (pair == null) {
            return false;
        } else {
            BlockStateConfiguration blockstateconfiguration;
            for (blockstateconfiguration = context.config(); blockpos.getY() > worldgenlevel.getMinBuildHeight() + 3; blockpos = blockpos.below()) {
                if (!worldgenlevel.isEmptyBlock(blockpos.below())) {
                    BlockState blockstate = worldgenlevel.getBlockState(blockpos.below());
                    if (blockstate.is(BlockTags.OVERWORLD_CARVER_REPLACEABLES)) {
                        break;
                    }
                }
            }
            if (worldgenlevel.getBlockState(blockpos.below()).is(Blocks.WATER)) return false;

            if (blockpos.getY() <= worldgenlevel.getMinBuildHeight() + 3) {
                return false;
            } else {
                for (int l = 0; l < 3; ++l) {
                    int i = randomsource.nextInt(2) + 1;
                    int j = randomsource.nextInt(2) + 2;
                    int k = randomsource.nextInt(2) + 1;
                    float f = (float) (i + j + k) * 0.333F + 0.5F;

                    for (BlockPos blockpos1 : BlockPos.betweenClosed(blockpos.offset(-i, -j, -k), blockpos.offset(i, j, k))) {
                        if (blockpos1.distSqr(blockpos) <= (double) (f * f)) {
                            if (!worldgenlevel.getBlockState(blockpos1).is(Blocks.WATER)) {
                                worldgenlevel.setBlock(blockpos1, blockstateconfiguration.state, 3);
                            }
                        }
                    }
                    blockpos = blockpos.offset(-1 + randomsource.nextInt(2), -randomsource.nextInt(2), -1 + randomsource.nextInt(2));
                }

                return true;
            }
        }
    }
}
