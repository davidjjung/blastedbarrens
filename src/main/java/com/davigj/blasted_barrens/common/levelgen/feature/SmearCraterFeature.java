package com.davigj.blasted_barrens.common.levelgen.feature;

import com.davigj.blasted_barrens.core.other.BBLootTables;
import com.davigj.blasted_barrens.core.registry.BBBiomes;
import com.davigj.blasted_barrens.core.registry.BBBlocks;
import com.google.common.base.Stopwatch;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.BlockBlobFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Predicate;

public class SmearCraterFeature extends Feature<BlockStateConfiguration> {
    public SmearCraterFeature(Codec<BlockStateConfiguration> p_65248_) {
        super(p_65248_);
    }

    public boolean place(FeaturePlaceContext<BlockStateConfiguration> context) {
        BlockPos blockpos = context.origin();
        WorldGenLevel worldgenlevel = context.level();
        RandomSource randomsource = context.random();
        Predicate<Holder<Biome>> biomePredicate = biomeHolder -> biomeHolder.is(BBBiomes.BLASTED_BARRENS);
        Pair<BlockPos, Holder<Biome>> pair = context.level().getLevel().findClosestBiome3d(biomePredicate, blockpos, 2000, 64, 128);

        if (pair == null) {
            return false;
        } else {
            BlockPos biomePos = pair.getFirst();

            double dx = biomePos.getX() - blockpos.getX();
            double dz = biomePos.getZ() - blockpos.getZ();
            double length = Math.sqrt(dx * dx + dz * dz);
            double nx = dx / length;
            double nz = dz / length;

            BlockStateConfiguration blockstateconfiguration;
            for(blockstateconfiguration = context.config(); blockpos.getY() > worldgenlevel.getMinBuildHeight() + 3; blockpos = blockpos.below()) {
                if (!worldgenlevel.isEmptyBlock(blockpos.below())) {
                    BlockState blockstate = worldgenlevel.getBlockState(blockpos.below());
                    if (blockstate.is(BlockTags.OVERWORLD_CARVER_REPLACEABLES)) {
                        break;
                    }
                }
            }
            if (worldgenlevel.getBlockState(blockpos.below()).is(Blocks.WATER)) return false;
            blockpos = blockpos.below().below();

            if (blockpos.getY() <= worldgenlevel.getMinBuildHeight() + 3) {
                return false;
            } else {
                for (int l = 0; l < 3; ++l) {
                    int i = randomsource.nextInt(3 - l) + 3;
                    int j = randomsource.nextInt(2) + 3;
                    int k = randomsource.nextInt(3 - l) + 3;
                    float f = (float)(i + j + k) * 0.333F + 0.5F;

                    for(BlockPos blockpos1 : BlockPos.betweenClosed(blockpos.offset(-i, -j, -k), blockpos.offset(i, j, k))) {
                        if (blockpos1.distSqr(blockpos) <= (double)(f * f)) {
                            if (!worldgenlevel.getBlockState(blockpos1).is(Blocks.WATER)) {
                                worldgenlevel.setBlock(blockpos1, blockstateconfiguration.state, 3);
                                if (l == 0) {
                                    BlockPos bottomOfCrater = blockpos.offset(0, -j, 0).immutable();
                                    worldgenlevel.setBlock(bottomOfCrater.below(), BBBlocks.SUSPICIOUS_ASHEN_SAND.get().defaultBlockState(), 3);
                                    BlockEntity blockEntity = worldgenlevel.getBlockEntity(bottomOfCrater.below());
                                    if (blockEntity instanceof BrushableBlockEntity brushable) {
                                        if (randomsource.nextInt(3) == 0) {
                                            brushable.setLootTable(BBLootTables.RARE_CRATER_ARCHY, bottomOfCrater.asLong());
                                        } else {
                                            brushable.setLootTable(BBLootTables.CRATER_ARCHY, bottomOfCrater.asLong());
                                        }
                                    }
                                }

                            }
                        }
                    }
                    // Move the block position towards the direction of the biome
                    blockpos = blockpos.offset(
                            (int)(nx * (4 + randomsource.nextInt(2))),
                            1 + randomsource.nextInt(1),
                            (int)(nz * (4 + randomsource.nextInt(2)))
                    );
                }
                return true;
            }
        }
    }
}
