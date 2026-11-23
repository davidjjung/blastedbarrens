package com.davigj.blasted_barrens.core.registry;

import com.davigj.blasted_barrens.common.client.particle.BarrensAshParticle;
import com.davigj.blasted_barrens.core.BlastedBarrens;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = BlastedBarrens.MOD_ID, value = Dist.CLIENT)
public class BBParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, BlastedBarrens.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BARRENS_ASH = PARTICLE_TYPES.register("barrens_ash", () -> new SimpleParticleType(true));

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(BARRENS_ASH.get(), BarrensAshParticle.Provider::new);
    }
}
