package com.davigj.blasted_barrens.core.other.compat;

import com.davigj.blasted_barrens.core.registry.BBBiomes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import virtuoel.pehkui.api.ScaleTypes;

public class PehkuiCompat {
    public static boolean manageVisibility(LivingEvent.LivingVisibilityEvent event, LivingEntity living) {
        if (ScaleTypes.BASE.getScaleData(living).getScale() > 1.0F) {
            event.modifyVisibility(1.3);
            return true;
        }
        return false;
    }

    public static void manageScale(LivingEntity living) {
        BlockPos pos = living.getOnPos();
        RandomSource random = living.getRandom();
        if (living.level().getBiome(pos).is(BBBiomes.BLASTED_BARRENS) && random.nextDouble() < 0.125) {
            ScaleTypes.BASE.getScaleData(living).setTargetScale(1.0F + (0.5F * random.nextFloat()));
            ScaleTypes.MOTION.getScaleData(living).setTargetScale(0.75F);
        }
    }
}
