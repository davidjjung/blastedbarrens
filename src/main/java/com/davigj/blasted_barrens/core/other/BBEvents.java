package com.davigj.blasted_barrens.core.other;

import com.davigj.blasted_barrens.core.BlastedBarrens;
import com.davigj.blasted_barrens.core.other.compat.PehkuiCompat;
import com.davigj.blasted_barrens.core.other.tags.BBEntityTypeTags;
import com.davigj.blasted_barrens.core.registry.BBBiomes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

import static com.davigj.blasted_barrens.core.other.compat.ModConstants.PEHKUI;

@EventBusSubscriber(modid = BlastedBarrens.MOD_ID)
public class BBEvents {

    @SubscribeEvent
    public static void oofOuchDustInMyEyes(LivingEvent.LivingVisibilityEvent event) {
        if (event.getLookingEntity() instanceof LivingEntity living && living.getType().is(BBEntityTypeTags.LOW_VISION)) {
            BlockPos pos = living.getOnPos();
            if (living.level().getBiome(pos).is(BBBiomes.BLASTED_BARRENS)) {
                if (PEHKUI) {
                    if (PehkuiCompat.manageVisibility(event, living)) return;
                }
                event.modifyVisibility(0.2);
            }
        }
    }

    @SubscribeEvent
    public static void creeperVariants(EntityJoinLevelEvent event) {
        if (!PEHKUI) return;
        if (event.getEntity() instanceof LivingEntity living && living.getType().is(BBEntityTypeTags.RESIZABLE)) {
            PehkuiCompat.manageScale(living);
        }
    }

}