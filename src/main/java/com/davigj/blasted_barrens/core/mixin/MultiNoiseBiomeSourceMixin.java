package com.davigj.blasted_barrens.core.mixin;

import com.davigj.blasted_barrens.core.registry.BBOverworldBiomes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({MultiNoiseBiomeSource.class})
public abstract class MultiNoiseBiomeSourceMixin {
    @Unique
    private volatile BBOverworldBiomes.Merge bb$merge;

    @ModifyReturnValue(method = {"parameters"}, at = {@At("RETURN")})
    private Climate.ParameterList<Holder<Biome>> bb$restoreDroppedBiomes(Climate.ParameterList<Holder<Biome>> list) {
        BBOverworldBiomes.Merge merge = BBOverworldBiomes.Merge.of(this.bb$merge, list);
        this.bb$merge = merge;
        return merge.merged();
    }
}
