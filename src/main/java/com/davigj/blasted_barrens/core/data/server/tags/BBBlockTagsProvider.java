package com.davigj.blasted_barrens.core.data.server.tags;

import com.davigj.blasted_barrens.core.BlastedBarrens;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.davigj.blasted_barrens.core.registry.BBBlocks.*;
import static net.minecraft.tags.BlockTags.*;

public class BBBlockTagsProvider extends BlockTagsProvider {
    public BBBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        super(output, provider, BlastedBarrens.MOD_ID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(Tags.Blocks.SAND).add(ASHEN_SAND.get());
        this.tag(BlockTags.SAND).add(ASHEN_SAND.get());
        this.tag(Tags.Blocks.SAND_COLORLESS).add(ASHEN_SAND.get());
        this.tag(MINEABLE_WITH_SHOVEL).add(ASHEN_SAND.get());
        this.tag(DEAD_BUSH_MAY_PLACE_ON).add(ASHEN_SAND.get());
        this.tag(ENDERMAN_HOLDABLE).add(ASHEN_SAND.get());
        this.tag(AZALEA_ROOT_REPLACEABLE).add(ASHEN_SAND.get());
        this.tag(AZALEA_GROWS_ON).add(ASHEN_SAND.get());

        this.tag(Tags.Blocks.SANDSTONE).add(ASHEN_SANDSTONE.get());
        this.tag(MINEABLE_WITH_PICKAXE).add(ASHEN_SANDSTONE.get());
        this.tag(SCULK_REPLACEABLE).add(ASHEN_SANDSTONE.get(), ASHEN_SAND.get());
        this.tag(SCULK_REPLACEABLE_WORLD_GEN).add(ASHEN_SANDSTONE.get());
        this.tag(OVERWORLD_CARVER_REPLACEABLES).add(ASHEN_SANDSTONE.get());
    }
}
