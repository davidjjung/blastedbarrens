package com.davigj.blasted_barrens.common.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class AshenSandBlock extends FallingBlock {
    public static final MapCodec<AshenSandBlock> CODEC = RecordCodecBuilder.mapCodec(
            p_308812_ -> p_308812_.group(ColorRGBA.CODEC.fieldOf("falling_dust_color").forGetter(p_304722_ -> p_304722_.dustColor), propertiesCodec())
                    .apply(p_308812_, AshenSandBlock::new)
    );
    private final ColorRGBA dustColor;

    public AshenSandBlock(ColorRGBA dustColor, BlockBehaviour.Properties properties) {
        super(properties);
        this.dustColor = dustColor;
    }

    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return CODEC;
    }

    @Override
    public int getDustColor(BlockState state, BlockGetter level, BlockPos pos) {
        return this.dustColor.rgba();
    }
}
