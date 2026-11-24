package com.davigj.blasted_barrens.core;

import com.davigj.blasted_barrens.core.data.server.BBDatapackProvider;
import com.davigj.blasted_barrens.core.other.BBClientCompat;
import com.davigj.blasted_barrens.core.registry.*;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod(BlastedBarrens.MOD_ID)
public class BlastedBarrens {
    public static final String MOD_ID = "blasted_barrens";
    public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);
    public static final Logger LOGGER = LogUtils.getLogger();

    public BlastedBarrens(IEventBus bus, ModContainer container) {
        BBBlocks.BLOCKS.register(bus);
        BBBlocks.ITEMS.register(bus);
        BBParticleTypes.PARTICLE_TYPES.register(bus);
//        BBBlockEntityTypes.BLOCK_ENTITIES.register(bus);
        BBStructurePieceTypes.STRUCTURE_PIECE_TYPES.register(bus);
        BBStructureTypes.STRUCTURES.register(bus);
        BBFeatures.FEATURES.register(bus);

        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        bus.addListener(this::dataSetup);
        container.registerConfig(ModConfig.Type.COMMON, BBConfig.COMMON_SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        Set<Block> validBlocks = Sets.newHashSet(BlockEntityType.BRUSHABLE_BLOCK.validBlocks);
        validBlocks.addAll(Sets.newHashSet(BBBlocks.SUSPICIOUS_ASHEN_SAND.get()));
        BlockEntityType.BRUSHABLE_BLOCK.validBlocks = ImmutableSet.copyOf(validBlocks);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BBClientCompat.creativeTabPlacements();
        });
    }

    private void dataSetup(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();
        boolean server = event.includeServer();
        BBDatapackProvider datapack = new BBDatapackProvider(output, provider);
        generator.addProvider(server, datapack);
        provider = datapack.getRegistryProvider();
    }

    public static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}