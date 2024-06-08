package com.davigj.blasted_barrens.core.mixin;

import com.davigj.blasted_barrens.core.data.server.SlowNoiseModdedBiomeProvider;
import com.davigj.blasted_barrens.core.other.BBBiomeUtil;
import com.davigj.blasted_barrens.core.registry.BBBiomes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Mixin(BiomeSource.class)
public class BiomeSourceMixin {
    @Inject(method = "findClosestBiome3d", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void findBarrens(BlockPos pos, int x, int y, int z, Predicate<Holder<Biome>> predicate,
                            Climate.Sampler sampler, LevelReader level, CallbackInfoReturnable<Pair<BlockPos, Holder<Biome>>> cir) {
        Registry<Biome> registry = level.registryAccess().registryOrThrow(Registries.BIOME);
        if (predicate.test(registry.getHolderOrThrow(BBBiomes.BLASTED_BARRENS))) {
            BiomeSource source = (BiomeSource) (Object) this;
            Set<Holder<Biome>> set = source.possibleBiomes().stream().filter(predicate).collect(Collectors.toUnmodifiableSet());
            if (set.isEmpty()) {
                cir.setReturnValue(null);
            } else {
                int i = Math.floorDiv(x, y);
                int[] aint = Mth.outFromOrigin(pos.getY(), level.getMinBuildHeight() + 1, level.getMaxBuildHeight(), z).toArray();

                for(BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.spiralAround(BlockPos.ZERO, i, Direction.EAST, Direction.SOUTH)) {
                    int j = pos.getX() + blockpos$mutableblockpos.getX() * y;
                    int k = pos.getZ() + blockpos$mutableblockpos.getZ() * y;
                    int l = QuartPos.fromBlock(j);
                    int i1 = QuartPos.fromBlock(k);

                    for(int j1 : aint) {
                        int k1 = QuartPos.fromBlock(j1);
                        Holder<Biome> holder = BBBiomeUtil.getNoiseBiome(l, k1, i1, sampler, source, registry);
                        if (set.contains(holder)) {
                            cir.setReturnValue(Pair.of(new BlockPos(j, j1, k), holder));
                        }
                    }
                }
                cir.setReturnValue(null);
            }
        }
    }
}
