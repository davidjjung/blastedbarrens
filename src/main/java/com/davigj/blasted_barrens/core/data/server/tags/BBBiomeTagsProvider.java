package com.davigj.blasted_barrens.core.data.server.tags;

import com.davigj.blasted_barrens.core.BlastedBarrens;
import com.davigj.blasted_barrens.core.other.tags.BBBiomeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class BBBiomeTagsProvider extends BiomeTagsProvider {
    public BBBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        super(output, provider, BlastedBarrens.MOD_ID, helper);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(BBBiomeTags.HAS_SMEAR_CRATER).addTag(BiomeTags.IS_OVERWORLD);
        this.tag(BBBiomeTags.HAS_PERIPHERY_CRATER).addTag(BiomeTags.IS_OVERWORLD);
    }
}
