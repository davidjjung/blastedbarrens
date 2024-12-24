package com.davigj.blasted_barrens.core.data.server.tags;

import com.davigj.blasted_barrens.core.BlastedBarrens;
import com.davigj.blasted_barrens.core.other.tags.BBBiomeTags;
import com.davigj.blasted_barrens.core.registry.BBBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.davigj.blasted_barrens.core.other.tags.BBBiomeTags.NO_DEFAULT_MONSTERS;

public class BBBiomeTagsProvider extends BiomeTagsProvider {
    public BBBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        super(output, provider, BlastedBarrens.MOD_ID, helper);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
//        this.tag(BBBiomeTags.HAS_SMEAR_CRATER).addTag(BiomeTags.IS_OVERWORLD);
        this.tag(BBBiomeTags.HAS_PERIPHERY_CRATER).addTag(BiomeTags.IS_OVERWORLD);
        this.tag(Tags.Biomes.IS_RARE).add(BBBiomes.BLASTED_BARRENS);
        this.tag(Tags.Biomes.IS_DRY_OVERWORLD).add(BBBiomes.BLASTED_BARRENS);
        this.tag(Tags.Biomes.IS_WASTELAND).add(BBBiomes.BLASTED_BARRENS);
        this.tag(NO_DEFAULT_MONSTERS).add(BBBiomes.BLASTED_BARRENS);
    }
}
