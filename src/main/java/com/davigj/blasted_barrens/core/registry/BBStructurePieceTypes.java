package com.davigj.blasted_barrens.core.registry;

import com.davigj.blasted_barrens.core.BlastedBarrens;
import com.davigj.blasted_barrens.common.levelgen.structure.BarrensPiece;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BBStructurePieceTypes {
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES;

    static {
        STRUCTURE_PIECE_TYPES = DeferredRegister.create(Registries.STRUCTURE_PIECE, BlastedBarrens.MOD_ID);
    }

    public static final DeferredHolder<StructurePieceType, StructurePieceType> BLASTED_BARRENS_PIECE =
            STRUCTURE_PIECE_TYPES.register("blasted_barrens_piece", () -> BarrensPiece::new);
}