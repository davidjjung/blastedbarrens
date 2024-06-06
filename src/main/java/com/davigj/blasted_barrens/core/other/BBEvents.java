package com.davigj.blasted_barrens.core.other;

import com.davigj.blasted_barrens.core.BlastedBarrens;
import com.davigj.blasted_barrens.core.registry.BBBiomes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BlastedBarrens.MOD_ID)
public class BBEvents {

    @SubscribeEvent
    public static void oofOuchDustInMyEyes(LivingEvent.LivingVisibilityEvent event) {
        if (event.getLookingEntity() instanceof Creeper creeper) {
            BlockPos pos = creeper.getOnPos();
            if (creeper.level().getBiome(pos).is(BBBiomes.BLASTED_BARRENS)) {
                event.modifyVisibility(0.2);
            }
        }
    }

}