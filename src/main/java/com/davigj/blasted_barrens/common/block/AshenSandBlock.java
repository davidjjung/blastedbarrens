package com.davigj.blasted_barrens.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;

public class AshenSandBlock extends FallingBlock {
    private final int dustColor;

    public AshenSandBlock(int color, Properties p_53205_) {
        super(p_53205_);
        this.dustColor = color;
    }

    public int getDustColor(BlockState p_55970_, BlockGetter p_55971_, BlockPos p_55972_) {
        return this.dustColor;
    }
}
