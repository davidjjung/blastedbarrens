package com.davigj.blasted_barrens.core.mixin;

import com.davigj.blasted_barrens.core.registry.BBBiomes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(OverworldBiomeBuilder.class)
public abstract class OverworldBiomeBuilderMixin {

    @Inject(method = "addOffCoastBiomes", at = @At("TAIL"))
    private void bb$addBarrensBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, CallbackInfo ci) {
        Climate.Parameter empty = Climate.Parameter.point(2.0F);
        consumer.accept(Pair.of(Climate.parameters(empty, empty, empty, empty, empty, empty, 0.0F), BBBiomes.BLASTED_BARRENS));
    }
}