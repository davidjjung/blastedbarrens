package com.davigj.blasted_barrens.core.registry;

import com.davigj.blasted_barrens.core.BlastedBarrens;
import com.davigj.blasted_barrens.common.levelgen.structure.BarrensStructure;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BBStructureTypes {
    public static final DeferredRegister<StructureType<?>> STRUCTURES;

    static {
        STRUCTURES = DeferredRegister.create(Registries.STRUCTURE_TYPE, BlastedBarrens.MOD_ID);
    }

    public static final DeferredHolder<StructureType<?>, StructureType<BarrensStructure>> BLASTED_BARRENS_ISLAND =
            STRUCTURES.register("blasted_barrens_island", () -> () -> BarrensStructure.CODEC);
}