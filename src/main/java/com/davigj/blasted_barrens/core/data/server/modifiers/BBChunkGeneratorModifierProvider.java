package com.davigj.blasted_barrens.core.data.server.modifiers;

import com.davigj.blasted_barrens.core.BlastedBarrens;
import com.davigj.blasted_barrens.core.registry.BBBiomes;
import com.davigj.blasted_barrens.core.registry.BBBlocks;
import com.teamabnormals.blueprint.common.world.modification.chunk.ChunkGeneratorModifierProvider;
import com.teamabnormals.blueprint.common.world.modification.chunk.modifiers.SurfaceRuleModifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.levelgen.SurfaceRules;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.world.level.levelgen.SurfaceRules.*;

public class BBChunkGeneratorModifierProvider extends ChunkGeneratorModifierProvider {
    public BBChunkGeneratorModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(BlastedBarrens.MOD_ID, output, provider);
    }

    @Override
    protected void registerEntries(HolderLookup.Provider provider) {
        SurfaceRules.ConditionSource isBlastedBarrens = isBiome(BBBiomes.BLASTED_BARRENS);

        RuleSource ashenSand = state(BBBlocks.ASHEN_SAND.get().defaultBlockState());
        RuleSource ashenSandstone = state(BBBlocks.ASHEN_SANDSTONE.get().defaultBlockState());

        this.entry("blasted_barrens_surface_rule").selects("minecraft:overworld")
                .addModifier(new SurfaceRuleModifier(ifTrue(abovePreliminarySurface(),
                        ifTrue(isBlastedBarrens, sequence(ifTrue(ON_FLOOR, ifTrue(waterBlockCheck(-1, 0),
                                        sequence(ifTrue(ON_CEILING, ashenSandstone), ashenSand))),
                        ifTrue(waterStartCheck(-6, -1), sequence(ifTrue(UNDER_FLOOR,
                                sequence(ifTrue(ON_CEILING, ashenSandstone), ashenSand)),
                                ifTrue(VERY_DEEP_UNDER_FLOOR, ashenSandstone)))))), false));

    }
}
