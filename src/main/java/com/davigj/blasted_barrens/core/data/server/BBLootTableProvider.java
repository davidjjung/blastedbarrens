package com.davigj.blasted_barrens.core.data.server;

import com.davigj.blasted_barrens.core.other.BBLootTables;
import com.davigj.blasted_barrens.core.registry.BBBlocks;
import com.google.common.collect.ImmutableList;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Map;
import java.util.function.BiConsumer;

public class BBLootTableProvider extends LootTableProvider {

    public BBLootTableProvider(PackOutput output) {
        super(output, BuiltInLootTables.all(), ImmutableList.of(
                new LootTableProvider.SubProviderEntry(BBArchyLoot::new, LootContextParamSets.ARCHAEOLOGY)
        ));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext context) {
    }

    private static class BBArchyLoot implements LootTableSubProvider {

        public void generate(BiConsumer<ResourceLocation, Builder> consumer) {
            consumer.accept(BBLootTables.CRATER_ARCHY, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(Items.GUNPOWDER).setWeight(3))
                    .add(LootItem.lootTableItem(BBBlocks.SINGED_BUSH.get().asItem()))
                    .add(LootItem.lootTableItem(Items.LEATHER))
                    .add(LootItem.lootTableItem(Items.BONE))
                    .add(LootItem.lootTableItem(Items.STICK))));
            consumer.accept(BBLootTables.RARE_CRATER_ARCHY, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(Items.CREEPER_HEAD))
                    .add(LootItem.lootTableItem(Items.BONE_MEAL))
                    .add(LootItem.lootTableItem(Items.LEAD))
                    .add(LootItem.lootTableItem(Items.EMERALD))
            ));
        }
    }
}