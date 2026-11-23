package com.davigj.blasted_barrens.common.levelgen.structure;

import com.davigj.blasted_barrens.core.registry.BBStructureTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import org.jetbrains.annotations.NotNull;

public class BarrensStructure extends Structure {
    public static final MapCodec<BarrensStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
                    instance.group(settingsCodec(instance), Biome.CODEC.fieldOf("island_biome")
                            .forGetter(structure -> structure.biomeHolder)).apply(instance, BarrensStructure::new));

    private final Holder<Biome> biomeHolder;

    protected BarrensStructure(Structure.StructureSettings settings, Holder<Biome> biomeHolder) {
        super(settings);
        this.biomeHolder = biomeHolder;
    }

    public @NotNull Optional<GenerationStub> findGenerationPoint(GenerationContext ctx) {
        ChunkPos chunkPos = ctx.chunkPos();
        int centerX = chunkPos.getMiddleBlockX();
        int centerZ = chunkPos.getMiddleBlockZ();
        int seaLevel = ctx.chunkGenerator().getSeaLevel();
        BlockPos origin = new BlockPos(centerX, seaLevel, centerZ);
        long noiseSeed = ctx.seed() ^ origin.asLong();
        int radius = this.getRadius(ctx);
        int buildRadius = (int) ((float) radius * 1.5F);
        int islandHeight = this.getIslandHeight(ctx);
        int chunkRadius = (int) Math.ceil((double) buildRadius / 16.0F);
        Optional<Registry<Biome>> registry = ctx.registryAccess().registry(Registries.BIOME);
        return registry.isPresent() && this.biomeHolder != null ? Optional.of(new Structure.GenerationStub(origin, (builder) -> {
            for(int dx = -chunkRadius; dx <= chunkRadius; ++dx) {
                for(int dz = -chunkRadius; dz <= chunkRadius; ++dz) {
                    int minX = centerX + dx * 16 - 8;
                    int minZ = centerZ + dz * 16 - 8;
                    int maxX = minX + 15;
                    int maxZ = minZ + 15;
                    int localChunkCenterX = minX + 8;
                    int localChunkCenterZ = minZ + 8;
                    int distX = localChunkCenterX - origin.getX();
                    int distZ = localChunkCenterZ - origin.getZ();
                    double distance = Math.sqrt((double)(distX * distX + distZ * distZ));
                    if (!(distance > (double)(buildRadius + 16))) {
                        int minY = ctx.chunkGenerator().getMinY();
                        int maxY = seaLevel + islandHeight;
                        BoundingBox box = new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
                        builder.addPiece(new BarrensPiece(origin, box, islandHeight, radius, buildRadius, noiseSeed, this.biomeHolder));
                    }
                }
            }
        })) : Optional.empty();
    }

    protected int getIslandHeight(GenerationContext ctx) {
        return ctx.random().nextInt(3, 5);
    }

    protected int getRadius(GenerationContext ctx) {
        return ctx.random().nextInt(48, 72);
    }

    @Override
    public @NotNull StructureType<?> type() {
        return BBStructureTypes.BLASTED_BARRENS_ISLAND.get();
    }
}