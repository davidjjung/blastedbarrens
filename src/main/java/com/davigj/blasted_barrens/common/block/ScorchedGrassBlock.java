package com.davigj.blasted_barrens.common.block;

import com.davigj.blasted_barrens.core.registry.BBBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.GeodeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.Optional;

public class ScorchedGrassBlock extends BushBlock implements BonemealableBlock {
    public ScorchedGrassBlock(Properties p_51021_) {
        super(p_51021_);
    }

    protected boolean mayPlaceOn(BlockState p_52424_, BlockGetter p_52425_, BlockPos p_52426_) {
        return p_52424_.is(BlockTags.DEAD_BUSH_MAY_PLACE_ON);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader p_256559_, BlockPos p_50898_, BlockState p_50899_, boolean p_50900_) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level p_220878_, RandomSource p_220879_, BlockPos p_220880_, BlockState p_220881_) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        for (int j = 0; j < 8; j++) {
            BlockPos randomPos = pos.offset(random.nextInt(3) - 1,
                    random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
            for (int k = 0; k < 4; ++k) {
                if (level.isEmptyBlock(randomPos) && state.canSurvive(level, randomPos)) {
                    pos = randomPos;
                }
                randomPos = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
            }
            if (level.isEmptyBlock(randomPos) && state.canSurvive(level, randomPos)) {
                level.setBlock(randomPos, state, 2);
            }
        }
    }
}
