package com.davigj.blasted_barrens.core.other;

import com.davigj.blasted_barrens.core.BlastedBarrens;
import com.davigj.blasted_barrens.core.registry.BBBiomes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import virtuoel.pehkui.api.ScaleTypes;

@Mod.EventBusSubscriber(modid = BlastedBarrens.MOD_ID)
public class BBEvents {

    @SubscribeEvent
    public static void oofOuchDustInMyEyes(LivingEvent.LivingVisibilityEvent event) {
        if (event.getLookingEntity() instanceof Creeper creeper) {
            BlockPos pos = creeper.getOnPos();
            if (creeper.level().getBiome(pos).is(BBBiomes.BLASTED_BARRENS)) {
                if (ModList.get().isLoaded("pehkui")) {
                    if (ScaleTypes.BASE.getScaleData(creeper).getScale() > 1.0F) {
                        event.modifyVisibility(1.3);
                        return;
                    }
                }
                event.modifyVisibility(0.2);
            }
        }
    }

    @SubscribeEvent
    public static void creeperVariants(EntityJoinLevelEvent event) {
        if (!ModList.get().isLoaded("pehkui")) return;
        if (event.getEntity() instanceof Creeper creeper) {
            BlockPos pos = creeper.getOnPos();
            RandomSource random = creeper.getRandom();
            if (creeper.level().getBiome(pos).is(BBBiomes.BLASTED_BARRENS) && random.nextDouble() < 0.125) {
                ScaleTypes.BASE.getScaleData(creeper).setTargetScale(1.0F + (0.5F * random.nextFloat()));
                ScaleTypes.MOTION.getScaleData(creeper).setTargetScale(0.75F);
            }
        }
    }

}