package com.davigj.blasted_barrens.core.registry;

import com.davigj.blasted_barrens.core.BlastedBarrens;
import com.teamabnormals.blueprint.core.util.registry.BlockEntitySubRegistryHelper;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;

import java.util.Set;
import java.util.function.Supplier;

public class BBBlockEntityTypes {
    public static final BlockEntitySubRegistryHelper BLOCK_ENTITIES = BlastedBarrens.REGISTRY_HELPER.getBlockEntitySubHelper();

//    public static final Supplier<BlockEntityType<BrushableBlockEntity>> SUSPICIOUS_ASHEN_SAND =
//            BLOCK_ENTITIES.createBlockEntity("suspicious_ashen_sand", BrushableBlockEntity::new, () -> Set.of(
//                    BBBlocks.SUSPICIOUS_ASHEN_SAND.get()
//            ));
}
