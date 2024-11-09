package com.davigj.blasted_barrens.common.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BarrensAshParticle extends BaseAshSmokeParticle {
    protected BarrensAshParticle(ClientLevel p_108512_, double p_108513_, double p_108514_, double p_108515_, double p_108516_, double p_108517_, double p_108518_, float p_108519_, SpriteSet p_108520_) {
        super(p_108512_, p_108513_, p_108514_, p_108515_, 0.1F, -0.1F, 0.1F, p_108516_, p_108517_, p_108518_, p_108519_, p_108520_, 0.0F, 20, 0.0125F, false);
//        this.rCol = 0.29F;
//        this.gCol = 0.28F;
//        this.bCol = 0.28F;
        this.rCol = 0.28F;
        this.gCol = 0.27F;
        this.bCol = 0.27F;
        this.lifetime = 300;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet p_108523_) {
            this.sprites = p_108523_;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double p_108539_, double p_108540_, double p_108541_) {
            RandomSource random = level.random;
            double d0 = (double)random.nextFloat() * (double)random.nextFloat() * 0.1D;
            double d1 = (double)random.nextFloat() * -0.05D * (double)random.nextFloat() * 0.1D;
            double d2 = (double)random.nextFloat() * (double)random.nextFloat() * 0.1D;
            return new BarrensAshParticle(level, x, y, z, d0, d1, d2, 1.0F, this.sprites);
        }
    }
}
