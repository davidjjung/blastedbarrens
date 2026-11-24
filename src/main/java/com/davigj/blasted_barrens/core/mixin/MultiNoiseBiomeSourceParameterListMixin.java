package com.davigj.blasted_barrens.core.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.davigj.blasted_barrens.core.registry.BBOverworldBiomes;
import com.davigj.blasted_barrens.core.registry.BBOverworldBiomes.Merge;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList.Preset;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({MultiNoiseBiomeSourceParameterList.class})
public abstract class MultiNoiseBiomeSourceParameterListMixin {
    @Shadow
    @Final
    private MultiNoiseBiomeSourceParameterList.Preset preset;
    @Shadow
    @Final
    private Climate.ParameterList<Holder<Biome>> parameters;
    @Unique
    private volatile BBOverworldBiomes.Merge bb$merge;

    @Inject(method = {"<init>"}, at = {@At("TAIL")})
    private void bb$rememberOwnBiomes(MultiNoiseBiomeSourceParameterList.Preset preset, HolderGetter<Biome> biomes, CallbackInfo ci) {
        if (preset == Preset.OVERWORLD) {
            BBOverworldBiomes.remember(this.parameters);
        }
    }

    @ModifyReturnValue(method = {"parameters"}, at = {@At("RETURN")})
    private Climate.ParameterList<Holder<Biome>> bb$restoreDroppedBiomes(Climate.ParameterList<Holder<Biome>> list) {
        if (this.preset != Preset.OVERWORLD) {
            return list;
        } else {
            BBOverworldBiomes.Merge merge = Merge.of(this.bb$merge, list);
            this.bb$merge = merge;
            return merge.merged();
        }
    }
}
