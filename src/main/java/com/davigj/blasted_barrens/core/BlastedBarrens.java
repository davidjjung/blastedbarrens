package com.davigj.blasted_barrens.core;

import com.davigj.blasted_barrens.core.data.server.BBDatapackBuiltInEntriesProvider;
import com.davigj.blasted_barrens.core.data.server.BBLootTableProvider;
import com.davigj.blasted_barrens.core.data.server.SlowNoiseModdedBiomeProvider;
import com.davigj.blasted_barrens.core.data.server.modifiers.BBChunkGeneratorModifierProvider;
import com.davigj.blasted_barrens.core.data.server.tags.BBBiomeTagsProvider;
import com.davigj.blasted_barrens.core.data.server.tags.BBBlockTagsProvider;
import com.davigj.blasted_barrens.core.other.BBClientCompat;
import com.davigj.blasted_barrens.core.registry.BBBlocks;
import com.davigj.blasted_barrens.core.registry.BBFeatures;
import com.davigj.blasted_barrens.core.registry.BBItems;
import com.davigj.blasted_barrens.core.registry.BBParticleTypes;
import com.teamabnormals.blueprint.core.util.BiomeUtil;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.concurrent.CompletableFuture;

@Mod(BlastedBarrens.MOD_ID)
public class BlastedBarrens {
    public static final String MOD_ID = "blasted_barrens";
    public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);

    public BlastedBarrens() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext context = ModLoadingContext.get();
        MinecraftForge.EVENT_BUS.register(this);

		REGISTRY_HELPER.register(bus);
        BBFeatures.FEATURES.register(bus);

        BiomeUtil.registerBiomeProvider(new ResourceLocation(BlastedBarrens.MOD_ID, "slow_noise"), SlowNoiseModdedBiomeProvider.CODEC);
        BBParticleTypes.PARTICLE_TYPES.register(bus);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            BBItems.buildCreativeTabContents();
            BBBlocks.buildCreativeTabContents();
        });

        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        bus.addListener(this::dataSetup);
        context.registerConfig(ModConfig.Type.COMMON, BBConfig.COMMON_SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {

        });
    }

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BBClientCompat.registerRenderLayers();
        });
    }

    private void dataSetup(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        boolean server = event.includeServer();
        BBDatapackBuiltInEntriesProvider datapackEntries = new BBDatapackBuiltInEntriesProvider(output, provider);
        generator.addProvider(server, datapackEntries);

        BBBlockTagsProvider blockTags = new BBBlockTagsProvider(output, provider, helper);
        generator.addProvider(server, blockTags);

        provider = datapackEntries.getRegistryProvider();

        generator.addProvider(server, new BBChunkGeneratorModifierProvider(output, provider));
        generator.addProvider(server, new BBBiomeTagsProvider(output, provider, helper));
        generator.addProvider(server, new BBLootTableProvider(output));
    }
}