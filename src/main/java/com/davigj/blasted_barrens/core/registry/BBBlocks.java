package com.davigj.blasted_barrens.core.registry;

import com.davigj.blasted_barrens.common.block.AshenSandBlock;
import com.davigj.blasted_barrens.common.block.ScorchedGrassBlock;
import com.davigj.blasted_barrens.common.block.SingedBushBlock;
import com.davigj.blasted_barrens.core.BlastedBarrens;
import com.teamabnormals.atmospheric.core.registry.AtmosphericSoundEvents;
import com.teamabnormals.blueprint.core.util.PropertyUtil;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import com.teamabnormals.blueprint.core.util.registry.ItemSubRegistryHelper;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static net.minecraft.world.item.CreativeModeTabs.BUILDING_BLOCKS;
import static net.minecraft.world.item.CreativeModeTabs.NATURAL_BLOCKS;
import static net.minecraft.world.item.crafting.Ingredient.of;

public class BBBlocks {
    public static final BlockSubRegistryHelper BLOCKS = BlastedBarrens.REGISTRY_HELPER.getBlockSubHelper();
    public static final ItemSubRegistryHelper ITEMS= BlastedBarrens.REGISTRY_HELPER.getItemSubHelper();

    public static final Supplier<Block> ASHEN_SAND = BLOCKS.createBlock("ashen_sand", () -> new AshenSandBlock(new ColorRGBA(5660253), BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_CONCRETE_POWDER)));
    public static final Supplier<Block> SUSPICIOUS_ASHEN_SAND = BLOCKS.createBlock("suspicious_ashen_sand", () -> new BrushableBlock((Block)ASHEN_SAND.get(), SoundEvents.BRUSH_SAND, SoundEvents.BRUSH_SAND_COMPLETED, BlockBehaviour.Properties.of().mapColor(MapColor.SAND).instrument(NoteBlockInstrument.SNARE).strength(0.25F).sound(SoundType.SUSPICIOUS_SAND).pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> SINGED_BUSH = BLOCKS.createBlock("singed_bush", () -> new SingedBushBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEAD_BUSH)));
    public static final DeferredBlock<Block> POTTED_SINGED_BUSH = BLOCKS.createBlockNoItem("potted_singed_bush", () -> new FlowerPotBlock((Block)SINGED_BUSH.get(), PropertyUtil.flowerPot(new FeatureFlag[0])));

    public static final Supplier<Block> SCORCHED_GRASS = BLOCKS.createBlock("scorched_grass", () -> new ScorchedGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));

    public static final Supplier<Block> ASHEN_SANDSTONE = BLOCKS.createBlock("ashen_sandstone", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE).mapColor(MapColor.COLOR_GRAY)));
    public static final Supplier<Block> ASHEN_SANDSTONE_STAIRS = BLOCKS.createBlock("ashen_sandstone_stairs", () -> new StairBlock(ASHEN_SANDSTONE.get().defaultBlockState(), Block.Properties.ofFullCopy(Blocks.SANDSTONE_STAIRS).mapColor(MapColor.COLOR_GRAY)));
    public static final Supplier<Block> ASHEN_SANDSTONE_SLAB = BLOCKS.createBlock("ashen_sandstone_slab", () -> new SlabBlock(Block.Properties.ofFullCopy(Blocks.SANDSTONE_SLAB).mapColor(MapColor.COLOR_GRAY)));
    public static final Supplier<Block> ASHEN_SANDSTONE_WALL = BLOCKS.createBlock("ashen_sandstone_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE_WALL).mapColor(MapColor.COLOR_GRAY)));

    public static final Supplier<Block> CHISELED_ASHEN_SANDSTONE = BLOCKS.createBlock("chiseled_ashen_sandstone", () -> new Block(BlockBehaviour.Properties.ofFullCopy(ASHEN_SANDSTONE.get())));

    public static final Supplier<Block> SMOOTH_ASHEN_SANDSTONE = BLOCKS.createBlock("smooth_ashen_sandstone", () -> new Block(BlockBehaviour.Properties.ofFullCopy(ASHEN_SANDSTONE.get())));
    public static final Supplier<Block> SMOOTH_ASHEN_SANDSTONE_STAIRS = BLOCKS.createBlock("smooth_ashen_sandstone_stairs", () -> new StairBlock(SMOOTH_ASHEN_SANDSTONE.get().defaultBlockState(), Block.Properties.ofFullCopy(Blocks.SANDSTONE_STAIRS).mapColor(MapColor.COLOR_GRAY)));
    public static final Supplier<Block> SMOOTH_ASHEN_SANDSTONE_SLAB = BLOCKS.createBlock("smooth_ashen_sandstone_slab", () -> new SlabBlock(Block.Properties.ofFullCopy(Blocks.SANDSTONE_SLAB).mapColor(MapColor.COLOR_GRAY)));

    public static final Supplier<Block> CUT_ASHEN_SANDSTONE = BLOCKS.createBlock("cut_ashen_sandstone", () -> new Block(BlockBehaviour.Properties.ofFullCopy(ASHEN_SANDSTONE.get())));
    public static final Supplier<Block> CUT_ASHEN_SANDSTONE_SLAB = BLOCKS.createBlock("cut_ashen_sandstone_slab", () -> new SlabBlock(Block.Properties.ofFullCopy(Blocks.SANDSTONE_SLAB).mapColor(MapColor.COLOR_GRAY)));

    public static void buildCreativeTabContents() {
        CreativeModeTabContentsPopulator.mod(BlastedBarrens.MOD_ID)
                .tab(NATURAL_BLOCKS).addItemsAfter(of(Blocks.RED_SANDSTONE), ASHEN_SAND, ASHEN_SANDSTONE, SUSPICIOUS_ASHEN_SAND)
                .tab(NATURAL_BLOCKS).addItemsAfter(of(Blocks.DEAD_BUSH), SINGED_BUSH, SCORCHED_GRASS)
                .tab(BUILDING_BLOCKS).addItemsAfter(of(Blocks.CUT_RED_SANDSTONE_SLAB), ASHEN_SANDSTONE, ASHEN_SANDSTONE_STAIRS, ASHEN_SANDSTONE_SLAB,
                        ASHEN_SANDSTONE_WALL, CHISELED_ASHEN_SANDSTONE, SMOOTH_ASHEN_SANDSTONE, SMOOTH_ASHEN_SANDSTONE_STAIRS,
                        SMOOTH_ASHEN_SANDSTONE_SLAB, CUT_ASHEN_SANDSTONE, CUT_ASHEN_SANDSTONE_SLAB);
    }
}