package com.davigj.blasted_barrens.core.other.tags;

import com.davigj.blasted_barrens.core.BlastedBarrens;
import com.teamabnormals.blueprint.core.util.TagUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class BBBiomeTags {
    public static final TagKey<Biome> HAS_SMEAR_CRATER = biomeTag("has_feature/has_smear_crater");
    public static final TagKey<Biome> HAS_PERIPHERY_CRATER = biomeTag("has_feature/has_periphery_crater");

    public static final TagKey<Biome> NO_DEFAULT_MONSTERS = forgeTag("no_default_monsters");

    private static TagKey<Biome> biomeTag(String tagName) {
        return TagUtil.biomeTag(BlastedBarrens.MOD_ID, tagName);
    }

    private static TagKey<Biome> forgeTag(String name) {
        return TagKey.create(Registries.BIOME, new ResourceLocation("forge", name));
    }
}
