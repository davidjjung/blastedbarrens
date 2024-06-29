package com.davigj.blasted_barrens.core.registry;

import com.davigj.blasted_barrens.common.block.AshenSandBlock;
import com.davigj.blasted_barrens.common.block.ScorchedGrassBlock;
import com.davigj.blasted_barrens.common.block.SingedBushBlock;
import com.davigj.blasted_barrens.core.BlastedBarrens;
import com.google.common.collect.Maps;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static net.minecraft.world.item.CreativeModeTabs.BUILDING_BLOCKS;
import static net.minecraft.world.item.CreativeModeTabs.NATURAL_BLOCKS;
import static net.minecraft.world.item.crafting.Ingredient.of;

@Mod.EventBusSubscriber(modid = BlastedBarrens.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BBBlocks {
    public static final BlockSubRegistryHelper HELPER = BlastedBarrens.REGISTRY_HELPER.getBlockSubHelper();
    public static final RegistryObject<Block> ASHEN_SAND = HELPER.createBlock("ashen_sand", () -> new AshenSandBlock(5660253, BlockBehaviour.Properties.copy(Blocks.GRAY_CONCRETE_POWDER)));
    public static final RegistryObject<Block> SUSPICIOUS_ASHEN_SAND = HELPER.createBlock("suspicious_ashen_sand", () -> new BrushableBlock(ASHEN_SAND.get(), BlockBehaviour.Properties.copy(Blocks.GRAY_CONCRETE_POWDER).sound(SoundType.SUSPICIOUS_SAND).pushReaction(PushReaction.DESTROY), SoundEvents.BRUSH_SAND, SoundEvents.BRUSH_SAND_COMPLETED));
    public static final RegistryObject<Block> SINGED_BUSH = HELPER.createBlock("singed_bush", () -> new SingedBushBlock(BlockBehaviour.Properties.copy(Blocks.DEAD_BUSH)));
    public static final RegistryObject<Block> POTTED_SINGED_BUSH = HELPER.createBlockNoItem("potted_singed_bush", () -> flowerPot(SINGED_BUSH.get()));

    public static final RegistryObject<Block> SCORCHED_GRASS = HELPER.createBlock("scorched_grass", () -> new ScorchedGrassBlock(BlockBehaviour.Properties.copy(Blocks.GRASS)));

    public static final RegistryObject<Block> ASHEN_SANDSTONE = HELPER.createBlock("ashen_sandstone", () -> new Block(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).mapColor(MapColor.COLOR_GRAY)));
    public static final RegistryObject<Block> ASHEN_SANDSTONE_STAIRS = HELPER.createBlock("ashen_sandstone_stairs", () -> new StairBlock(() -> ASHEN_SANDSTONE.get().defaultBlockState(), Block.Properties.copy(Blocks.SANDSTONE_STAIRS).mapColor(MapColor.COLOR_GRAY)));
    public static final RegistryObject<Block> ASHEN_SANDSTONE_SLAB = HELPER.createBlock("ashen_sandstone_slab", () -> new SlabBlock(Block.Properties.copy(Blocks.SANDSTONE_SLAB).mapColor(MapColor.COLOR_GRAY)));
    public static final RegistryObject<Block> ASHEN_SANDSTONE_WALL = HELPER.createBlock("ashen_sandstone_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.SANDSTONE_WALL).mapColor(MapColor.COLOR_GRAY)));

    public static final RegistryObject<Block> CHISELED_ASHEN_SANDSTONE = HELPER.createBlock("chiseled_ashen_sandstone", () -> new Block(BlockBehaviour.Properties.copy(ASHEN_SANDSTONE.get())));

    public static final RegistryObject<Block> SMOOTH_ASHEN_SANDSTONE = HELPER.createBlock("smooth_ashen_sandstone", () -> new Block(BlockBehaviour.Properties.copy(ASHEN_SANDSTONE.get())));
    public static final RegistryObject<Block> SMOOTH_ASHEN_SANDSTONE_STAIRS = HELPER.createBlock("smooth_ashen_sandstone_stairs", () -> new StairBlock(() -> SMOOTH_ASHEN_SANDSTONE.get().defaultBlockState(), Block.Properties.copy(Blocks.SANDSTONE_STAIRS).mapColor(MapColor.COLOR_GRAY)));
    public static final RegistryObject<Block> SMOOTH_ASHEN_SANDSTONE_SLAB = HELPER.createBlock("smooth_ashen_sandstone_slab", () -> new SlabBlock(Block.Properties.copy(Blocks.SANDSTONE_SLAB).mapColor(MapColor.COLOR_GRAY)));

    public static final RegistryObject<Block> CUT_ASHEN_SANDSTONE = HELPER.createBlock("cut_ashen_sandstone", () -> new Block(BlockBehaviour.Properties.copy(ASHEN_SANDSTONE.get())));
    public static final RegistryObject<Block> CUT_ASHEN_SANDSTONE_SLAB = HELPER.createBlock("cut_ashen_sandstone_slab", () -> new SlabBlock(Block.Properties.copy(Blocks.SANDSTONE_SLAB).mapColor(MapColor.COLOR_GRAY)));

    public static void buildCreativeTabContents() {
        CreativeModeTabContentsPopulator.mod(BlastedBarrens.MOD_ID)
                .tab(NATURAL_BLOCKS).addItemsAfter(of(Blocks.RED_SANDSTONE), ASHEN_SAND, ASHEN_SANDSTONE, SUSPICIOUS_ASHEN_SAND)
                .tab(NATURAL_BLOCKS).addItemsAfter(of(Blocks.DEAD_BUSH), SINGED_BUSH, SCORCHED_GRASS)
                .tab(BUILDING_BLOCKS).addItemsAfter(of(Blocks.CUT_RED_SANDSTONE_SLAB), ASHEN_SANDSTONE, ASHEN_SANDSTONE_STAIRS, ASHEN_SANDSTONE_SLAB,
                        ASHEN_SANDSTONE_WALL, CHISELED_ASHEN_SANDSTONE, SMOOTH_ASHEN_SANDSTONE, SMOOTH_ASHEN_SANDSTONE_STAIRS,
                        SMOOTH_ASHEN_SANDSTONE_SLAB, CUT_ASHEN_SANDSTONE, CUT_ASHEN_SANDSTONE_SLAB)
        ;
    }

    private static FlowerPotBlock flowerPot(Block block, FeatureFlag... flags) {
        BlockBehaviour.Properties blockbehaviour$properties = BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY);
        if (flags.length > 0) {
            blockbehaviour$properties = blockbehaviour$properties.requiredFeatures(flags);
        }
        return new FlowerPotBlock(block, blockbehaviour$properties);
    }

    public static final Map<Supplier<Block>, Supplier<Block>> SAND_FALLABLES;

    static {
        SAND_FALLABLES = Util.make(new HashMap<Supplier<Block>, Supplier<Block>>(), (fallables) -> {
            fallables.put(BBBlocks.ASHEN_SANDSTONE, BBBlocks.ASHEN_SAND);
        });
    }
}