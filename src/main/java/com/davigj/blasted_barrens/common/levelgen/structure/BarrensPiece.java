package com.davigj.blasted_barrens.common.levelgen.structure;

import com.davigj.blasted_barrens.core.mixin.LevelChunkSectionAccessor;
import com.davigj.blasted_barrens.core.registry.BBBlocks;
import com.davigj.blasted_barrens.core.registry.BBStructurePieceTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import net.minecraft.world.level.chunk.PalettedContainer.Strategy;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;
import org.jetbrains.annotations.NotNull;

public class BarrensPiece extends StructurePiece {
    private final BlockPos origin;
    private final int islandHeight;
    private final int radius;
    private final int buildRadius;
    private final long seed;
    private final Holder<Biome> biomeHolder;

    public BarrensPiece(BlockPos origin, BoundingBox box, int islandHeight, int radius, int buildRadius, long seed, Holder<Biome> biomeHolder) {
        super(BBStructurePieceTypes.BLASTED_BARRENS_PIECE.get(), 0, box);
        this.origin = origin;
        this.islandHeight = islandHeight;
        this.radius = radius;
        this.buildRadius = buildRadius;
        this.seed = seed;
        this.biomeHolder = biomeHolder;
    }

    public BarrensPiece(StructurePieceSerializationContext ctx, CompoundTag tag) {
        super(BBStructurePieceTypes.BLASTED_BARRENS_PIECE.get(), tag);
        this.origin = new BlockPos(tag.getInt("X"), tag.getInt("Y"), tag.getInt("Z"));
        this.islandHeight = tag.getInt("IslandHeight");
        this.radius = tag.getInt("Radius");
        this.buildRadius = tag.getInt("BuildRadius");
        this.seed = tag.getLong("Seed");
        Optional<Registry<Biome>> registryOp = ctx.registryAccess().registry(Registries.BIOME);
        Holder<Biome> biomeHolder1;
        if (registryOp.isPresent()) {
            Registry<Biome> registry = registryOp.get();
            String biomeStr = tag.getString("Biome");
            ResourceLocation maybeLoc = ResourceLocation.parse(biomeStr);

            try {
                ResourceKey<Biome> key = ResourceKey.create(Registries.BIOME, maybeLoc);
                biomeHolder1 = registry.getHolder(key).orElse(null);
            } catch (Exception exception) {
                biomeHolder1 = null;
            }
        } else {
            biomeHolder1 = null;
        }
        this.biomeHolder = biomeHolder1;
    }

    private static String getBiome(String biomeStr) {
        String extracted = biomeStr.trim();
        if (extracted.startsWith("ResourceKey[") && extracted.contains(" ")) {
            int lastSpace = extracted.lastIndexOf(32);
            if (lastSpace != -1 && lastSpace + 1 < extracted.length()) {
                extracted = extracted.substring(lastSpace + 1);
                if (extracted.endsWith("]")) {
                    extracted = extracted.substring(0, extracted.length() - 1);
                }
            }
        }

        if (extracted.startsWith("[") && extracted.endsWith("]")) {
            extracted = extracted.substring(1, extracted.length() - 1);
        }

        if (extracted.contains(" / ")) {
            String[] parts = extracted.split(" / ");
            extracted = parts[parts.length - 1];
        }

        return extracted;
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tag) {
        tag.putInt("X", this.origin.getX());
        tag.putInt("Y", this.origin.getY());
        tag.putInt("Z", this.origin.getZ());
        tag.putInt("IslandHeight", this.islandHeight);
        tag.putInt("Radius", this.radius);
        tag.putInt("BuildRadius", this.buildRadius);
        tag.putLong("Seed", this.seed);
        if (this.biomeHolder != null) {
            this.biomeHolder.unwrapKey().ifPresent(key -> tag.putString("Biome", key.location().toString()));
        }

    }

    public void postProcess(@NotNull WorldGenLevel worldGenLevel, @NotNull StructureManager structureManager, @NotNull ChunkGenerator chunkGenerator, @NotNull RandomSource randomSource, @NotNull BoundingBox boundingBox, @NotNull ChunkPos chunkPos, @NotNull BlockPos blockPos) {
        this.generateIsland(worldGenLevel, chunkGenerator, randomSource, boundingBox, this.biomeHolder);
    }

    public long getSeed() {
        return this.seed;
    }

    public int getBuildRadius() {
        return this.buildRadius;
    }

    public int getRadius() {
        return this.radius;
    }

    public int getIslandHeight() {
        return this.islandHeight;
    }

    public BlockPos getOrigin() {
        return this.origin;
    }

    public void generateIsland(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox pieceBox, Holder<Biome> biomeHolder) {
        this.generateIsland(level, chunkGenerator, random, pieceBox, biomeHolder, null);
    }

    public void generateIsland(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox pieceBox, Holder<Biome> biomeHolder, Predicate<BlockPos> columnAllowed) {
        RandomSource islandRandom = RandomSource.create(this.getSeed());
        SimplexNoise noise = new SimplexNoise(islandRandom);
        int minX = Math.max(pieceBox.minX(), this.getOrigin().getX() - this.getBuildRadius());
        int maxX = Math.min(pieceBox.maxX(), this.getOrigin().getX() + this.getBuildRadius());
        int minZ = Math.max(pieceBox.minZ(), this.getOrigin().getZ() - this.getBuildRadius());
        int maxZ = Math.min(pieceBox.maxZ(), this.getOrigin().getZ() + this.getBuildRadius());
        List<BlockPos> blockPositions = new ArrayList<>();
        int minPlacedY = Integer.MAX_VALUE;
        int maxPlacedY = Integer.MIN_VALUE;
        double outerRadius = this.getRadius() + 17;
        double outerRadiusSq = outerRadius * outerRadius;
        BlockPos.MutableBlockPos columnCursor = new BlockPos.MutableBlockPos();

        for (int x = minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                int distX = x - this.getOrigin().getX();
                int distZ = z - this.getOrigin().getZ();
                double distSq = (double) distX * distX + (double) distZ * distZ;
                if (!(distSq > outerRadiusSq) && (columnAllowed == null || columnAllowed.test(columnCursor.set(x, 0, z)))) {
                    double baseNoise = noise.getValue((double) x * 0.025, (double) z * 0.025);
                    int seaFloorY = this.getSeaFloor(level, chunkGenerator, x, z);
                    int minY = Math.max(pieceBox.minY(), seaFloorY);
                    int maxY = Math.min(pieceBox.maxY(), chunkGenerator.getSeaLevel() + this.getIslandHeight());

                    for (int y = minY; y <= maxY; ++y) {
                        if (y >= -30) {
                            double threshold = (double) this.getThreshold(y, chunkGenerator.getSeaLevel(), this.getIslandHeight());
                            double octaveNoise = noise.getValue((double) x * 0.1, (double) y * 0.05, (double) z * 0.1);
                            double noisyRadius = (double) this.getRadius() + baseNoise * (double) 10.0F + octaveNoise * (double) 7.0F;
                            if (distSq <= noisyRadius * noisyRadius && baseNoise > threshold) {
                                BlockPos pos = new BlockPos(x, y, z);
                                boolean neighbor = false;

                                for (Direction direction : Direction.values()) {
                                    if (level.getBlockState(pos.relative(direction)).isSolid()) {
                                        neighbor = true;
                                        break;
                                    }
                                }

                                if (neighbor && !level.getBlockState(pos).is(Blocks.CHEST)) {
                                    level.setBlock(pos, this.getBaseState(), 2);
                                    blockPositions.add(pos);
                                    if (y < minPlacedY) {
                                        minPlacedY = y;
                                    }

                                    if (y > maxPlacedY) {
                                        maxPlacedY = y;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!blockPositions.isEmpty()) {
            this.setIslandBiome(level, pieceBox, biomeHolder, minPlacedY, maxPlacedY);
        }

        for (BlockPos pos : blockPositions) {
            this.decorateIsland(pos, random, islandRandom, chunkGenerator, level, biomeHolder);
        }
    }

    protected int getSeaFloor(WorldGenLevel level, ChunkGenerator chunkGenerator, int x, int z) {
        return level.getHeight(Types.OCEAN_FLOOR_WG, x, z);
    }

    private void setIslandBiome(WorldGenLevel level, BoundingBox pieceBox, Holder<Biome> biomeHolder, int minPlacedY, int maxPlacedY) {
        if (biomeHolder != null) {
            int minChunkX = pieceBox.minX() >> 4;
            int maxChunkX = pieceBox.maxX() >> 4;
            int minChunkZ = pieceBox.minZ() >> 4;
            int maxChunkZ = pieceBox.maxZ() >> 4;

            for(int chunkX = minChunkX; chunkX <= maxChunkX; ++chunkX) {
                for(int chunkZ = minChunkZ; chunkZ <= maxChunkZ; ++chunkZ) {
                    this.setIslandBiomeForChunk(level, chunkX, chunkZ, biomeHolder, minPlacedY, maxPlacedY);
                }
            }

        }
    }

    private void setIslandBiomeForChunk(WorldGenLevel level, int chunkX, int chunkZ, Holder<Biome> biomeHolder, int minPlacedY, int maxPlacedY) {
        ChunkAccess chunk = level.getChunk(chunkX, chunkZ);
        Registry<Biome> biomeRegistry = level.registryAccess().registryOrThrow(Registries.BIOME);
        double footprintRadius = (double)this.getRadius() + (double)17.0F;
        double footprintRadiusSq = footprintRadius * footprintRadius;
        int topY = maxPlacedY + 60;
        LevelChunkSection[] sections = chunk.getSections();
        boolean changed = false;

        for(int index = 0; index < sections.length; ++index) {
            LevelChunkSection var18 = sections[index];
            if (var18 instanceof LevelChunkSectionAccessor accessor) {
                int sectionMinY = SectionPos.sectionToBlockCoord(chunk.getSectionYFromSectionIndex(index));
                if (sectionMinY + 15 >= minPlacedY && sectionMinY <= topY) {
                    PalettedContainerRO<Holder<Biome>> oldBiomes = accessor.getBiomes();
                    PalettedContainer<Holder<Biome>> newBiomes = new PalettedContainer(biomeRegistry.asHolderIdMap(), biomeHolder, Strategy.SECTION_BIOMES);

                    for(int cx = 0; cx < 4; ++cx) {
                        double dx = (double)((chunkX << 4) + cx * 4 + 2 - this.getOrigin().getX());

                        for(int cz = 0; cz < 4; ++cz) {
                            double dz = (double)((chunkZ << 4) + cz * 4 + 2 - this.getOrigin().getZ());
                            boolean insideRadius = dx * dx + dz * dz <= footprintRadiusSq;

                            for(int cy = 0; cy < 4; ++cy) {
                                int cellMinY = sectionMinY + cy * 4;
                                boolean insideY = cellMinY + 3 >= minPlacedY && cellMinY <= topY;
                                newBiomes.set(cx, cy, cz, insideRadius && insideY ? biomeHolder : (Holder)oldBiomes.get(cx, cy, cz));
                            }
                        }
                    }

                    accessor.setBiomes(newBiomes);
                    changed = true;
                }
            }
        }

        if (changed) {
            chunk.setUnsaved(true);
        }
    }

    public void decorateIsland(BlockPos pos, RandomSource randomSource, RandomSource islandRandom, ChunkGenerator chunkGenerator, WorldGenLevel level, Holder<Biome> biomeHolder) {
        BlockState baseState = this.getBaseState();
        BlockState sand = BBBlocks.ASHEN_SAND.get().defaultBlockState();
        BlockState sandstone = BBBlocks.ASHEN_SANDSTONE.get().defaultBlockState();
        if (!level.getBlockState(pos).is(baseState.getBlock())) {
            return;
        }
        boolean exposedTop = !level.getBlockState(pos.above()).is(baseState.getBlock());
        boolean exposedCeiling = level.getBlockState(pos.above()).is(baseState.getBlock()) && !level.getBlockState(pos.below()).is(baseState.getBlock());
        if (exposedTop) {
            for (int depth = 0; depth < 3; ++depth) {
                BlockPos surfacePos = pos.below(depth);
                if (!level.getBlockState(surfacePos).is(baseState.getBlock())) {
                    break;
                }
                level.setBlock(surfacePos, sand, 2);
            }
            return;
        }
        if (exposedCeiling) {
            level.setBlock(pos, sandstone, 2);
        }
    }

    private static final float SURFACE_RELIEF = 2.5F;

    float getThreshold(int y, int seaLevel, int islandHeight) {
        float plateauBase = islandHeight - SURFACE_RELIEF;
        return (y - seaLevel - plateauBase) / SURFACE_RELIEF;
    }

    protected BlockState getBaseState() {
        return BBBlocks.ASHEN_SANDSTONE.get().defaultBlockState();
    }
}