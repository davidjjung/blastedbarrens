package com.davigj.blasted_barrens.core.mixin;

import net.minecraftforge.fml.ModList;
import net.shizotoaster.foglooksmodernnow.client.FogManager;
import net.shizotoaster.foglooksmodernnow.config.FogLooksGoodNowConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(targets = "net.shizotoaster.foglooksmodernnow.config.FogLooksGoodNowConfig")
public class FogLooksGoodNowConfigMixin {
    @Inject(method = "getDensityConfigs", at = @At("HEAD"), remap = false, cancellable = true)
    private static void cooking(CallbackInfoReturnable<List<Pair<String, FogManager.BiomeFogDensity>>> cir) {
        if (!ModList.get().isLoaded("foglooksmodernnow")) return;
        List<Pair<String, FogManager.BiomeFogDensity>> list = new ArrayList<>();
        List<? extends String> densityConfigs = (List) FogLooksGoodNowConfig.CLIENT_CONFIG.biomeFogs.get();
        list.add(Pair.of("blasted_barrens:blasted_barrens", new FogManager.BiomeFogDensity(0.0f, 0.3f, 2105382)));
        for (String densityConfig : densityConfigs) {
            String[] options = densityConfig.split(",");
            try {
                list.add(Pair.of(options[0], new FogManager.BiomeFogDensity(Float.parseFloat(options[1]), Float.parseFloat(options[2]), Integer.parseInt(options[3]))));
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
        }
        cir.setReturnValue(list);
    }
}
