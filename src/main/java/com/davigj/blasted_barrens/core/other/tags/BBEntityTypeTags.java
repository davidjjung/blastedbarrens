package com.davigj.blasted_barrens.core.other.tags;

import com.davigj.blasted_barrens.core.BlastedBarrens;
import com.teamabnormals.blueprint.core.util.TagUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class BBEntityTypeTags {
    public static final TagKey<EntityType<?>> RESIZABLE = entityTypeTag("resizable");
    public static final TagKey<EntityType<?>> LOW_VISION = entityTypeTag("low_vision");

    private static TagKey<EntityType<?>> entityTypeTag(String name) {
        return TagUtil.entityTypeTag(BlastedBarrens.MOD_ID, name);
    }
}
