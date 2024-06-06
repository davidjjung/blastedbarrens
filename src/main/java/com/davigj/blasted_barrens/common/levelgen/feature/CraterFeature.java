package com.davigj.blasted_barrens.common.levelgen.feature;

import com.davigj.blasted_barrens.core.registry.BBBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.material.Fluids;

public class CraterFeature extends Feature<BlockStateConfiguration> {
    public CraterFeature(Codec<BlockStateConfiguration> p_65248_) {
        super(p_65248_);
    }

    public boolean place(FeaturePlaceContext<BlockStateConfiguration> p_159471_) {
        BlockPos blockpos = p_159471_.origin();
        WorldGenLevel worldgenlevel = p_159471_.level();
        RandomSource randomsource = p_159471_.random();

        BlockStateConfiguration blockstateconfiguration;
        for(blockstateconfiguration = p_159471_.config(); blockpos.getY() > worldgenlevel.getMinBuildHeight() + 3; blockpos = blockpos.below()) {
            if (!worldgenlevel.isEmptyBlock(blockpos.below())) {
                BlockState blockstate = worldgenlevel.getBlockState(blockpos.below());
                if (blockstate.is(BlockTags.SAND)) {
                    break;
                }
            }
        }
        blockpos = blockpos.below();

        if (blockpos.getY() <= worldgenlevel.getSeaLevel()) {
            return false;
        } else {
            for(int l = 0; l < 3; ++l) {
                int i = randomsource.nextInt(3) + 1;
                int j = randomsource.nextInt(2) + 3;
                int k = randomsource.nextInt(3) + 1;
                float f = (float)(i + j + k) * 0.333F + 0.5F;

                for(BlockPos blockpos1 : BlockPos.betweenClosed(blockpos.offset(-i, -j, -k), blockpos.offset(i, j, k))) {
                    if (blockpos1.distSqr(blockpos) <= (double)(f * f)) {
                        if (!worldgenlevel.getBlockState(blockpos1).is(Blocks.WATER)) {
                            worldgenlevel.setBlock(blockpos1, blockstateconfiguration.state, 3);
                        } else {
                            worldgenlevel.setBlock(blockpos1, BBBlocks.ASHEN_SANDSTONE.get().defaultBlockState(), 3);
                        }
                    }
                }

                blockpos = blockpos.offset(-1 + randomsource.nextInt(2), -randomsource.nextInt(2), -1 + randomsource.nextInt(2));
            }

            return true;
        }
    }
}
