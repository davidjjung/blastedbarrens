package com.davigj.blasted_barrens.core.registry;

import com.davigj.blasted_barrens.common.client.particle.BarrensAshParticle;
import com.davigj.blasted_barrens.core.BlastedBarrens;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = BlastedBarrens.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BBParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, BlastedBarrens.MOD_ID);

    public static final RegistryObject<SimpleParticleType> BARRENS_ASH = PARTICLE_TYPES.register("barrens_ash", () -> new SimpleParticleType(true));

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(BARRENS_ASH.get(), BarrensAshParticle.Provider::new);
    }
}
