package com.davigj.blasted_barrens.core.registry;

import com.davigj.blasted_barrens.core.BlastedBarrens;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.Climate.Parameter;
import org.jetbrains.annotations.Nullable;

public final class BBOverworldBiomes {
    private static final Climate.Parameter DEEP_LAYER = Parameter.point(1.0F);
    private static final Climate.Parameter SURFACE_LAYER = Parameter.point(0.0F);
    private static final int MIN_SHARED_BIOMES = 8;
    private static volatile List<Pair<Climate.ParameterPoint, Holder<Biome>>> ourEntries = List.of();
    private static volatile Set<ResourceKey<Biome>> presetCompanions = Set.of();

    private BBOverworldBiomes() {
    }

    public static void remember(Climate.ParameterList<Holder<Biome>> preset) {
        List<Pair<Climate.ParameterPoint, Holder<Biome>>> mine = new ArrayList();
        Set<ResourceKey<Biome>> companions = new HashSet();

        for (Pair<Climate.ParameterPoint, Holder<Biome>> entry : preset.values()) {
            ResourceKey<Biome> key = (ResourceKey) ((Holder) entry.getSecond()).unwrapKey().orElse((Object) null);
            if (key != null) {
                if (isOurs(key)) {
                    mine.add(entry);
                } else {
                    companions.add(key);
                }
            }
        }

        ourEntries = List.copyOf(mine);
        presetCompanions = Set.copyOf(companions);
    }

    private static Climate.ParameterList<Holder<Biome>> restore(Climate.ParameterList<Holder<Biome>> list) {
        List<Pair<Climate.ParameterPoint, Holder<Biome>>> mine = ourEntries;
        if (mine.isEmpty()) {
            return list;
        } else {
            Host host = BBOverworldBiomes.Host.read(list);
            if (!host.isTheOverworld()) {
                return list;
            } else {
                List<Pair<Climate.ParameterPoint, Holder<Biome>>> missing = new ArrayList();

                for (Pair<Climate.ParameterPoint, Holder<Biome>> entry : mine) {
                    ResourceKey<Biome> key = (ResourceKey) ((Holder) entry.getSecond()).unwrapKey().orElse((Object) null);
                    if (key != null && host.reachesDepthOf((Climate.ParameterPoint) entry.getFirst())) {
                        Climate.ParameterPoint point = host.alignDepth((Climate.ParameterPoint) entry.getFirst());
                        if (!host.holds(point, key)) {
                            missing.add(Pair.of(point, (Holder) entry.getSecond()));
                        }
                    }
                }

                if (missing.isEmpty()) {
                    return list;
                } else {
                    return append(list.values(), missing);
                }
            }
        }
    }

    private static Climate.ParameterList<Holder<Biome>> append(List<Pair<Climate.ParameterPoint, Holder<Biome>>> base, List<Pair<Climate.ParameterPoint, Holder<Biome>>> extra) {
        return new Climate.ParameterList(ImmutableList.builder().addAll(base).addAll(extra).build());
    }

    private static boolean isOurs(ResourceKey<Biome> key) {
        return key.location().getNamespace().equals(BlastedBarrens.MOD_ID);
    }

    public record Merge(Climate.ParameterList<Holder<Biome>> source, Climate.ParameterList<Holder<Biome>> merged) {
        public static Merge of(@Nullable Merge cached, Climate.ParameterList<Holder<Biome>> list) {
            return cached == null || cached.source() != list && cached.merged() != list ? new Merge(list, BBOverworldBiomes.restore(list)) : cached;
        }
    }

    private record Host(Set<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> entries, int sharedBiomes, Climate.Parameter surfaceDepth, boolean keepsDeepLayer) {
        static Host read(Climate.ParameterList<Holder<Biome>> list) {
            Set<ResourceKey<Biome>> companions = BBOverworldBiomes.presetCompanions;
            Set<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> entries = new HashSet();
            Set<ResourceKey<Biome>> shared = new HashSet();
            Map<Climate.Parameter, Integer> surfaceBands = new HashMap();
            boolean keepsDeepLayer = false;

            for (Pair<Climate.ParameterPoint, Holder<Biome>> entry : list.values()) {
                ((Holder) entry.getSecond()).unwrapKey().ifPresent((key) -> {
                    entries.add(Pair.of((Climate.ParameterPoint) entry.getFirst(), (ResourceKey<Biome>) key));
                    if (companions.contains(key)) {
                        shared.add((ResourceKey<Biome>) key);
                    }

                });
                Climate.Parameter depth = ((Climate.ParameterPoint) entry.getFirst()).depth();
                if (depth.min() <= 0L && depth.max() >= 0L) {
                    surfaceBands.merge(depth, 1, Integer::sum);
                }

                if (depth.equals(BBOverworldBiomes.DEEP_LAYER)) {
                    keepsDeepLayer = true;
                }
            }

            return new Host(entries, shared.size(), commonest(surfaceBands), keepsDeepLayer);
        }

        private static Climate.Parameter commonest(Map<Climate.Parameter, Integer> bands) {
            Climate.Parameter widest = BBOverworldBiomes.SURFACE_LAYER;
            int seen = 0;

            for (Map.Entry<Climate.Parameter, Integer> band : bands.entrySet()) {
                if ((Integer) band.getValue() > seen) {
                    seen = (Integer) band.getValue();
                    widest = (Climate.Parameter) band.getKey();
                }
            }

            return widest;
        }

        boolean isTheOverworld() {
            return this.sharedBiomes >= MIN_SHARED_BIOMES;
        }

        boolean reachesDepthOf(Climate.ParameterPoint point) {
            return this.keepsDeepLayer || !point.depth().equals(BBOverworldBiomes.DEEP_LAYER);
        }

        Climate.ParameterPoint alignDepth(Climate.ParameterPoint point) {
            return !point.depth().equals(BBOverworldBiomes.SURFACE_LAYER) ? point : new Climate.ParameterPoint(point.temperature(), point.humidity(), point.continentalness(), point.erosion(), this.surfaceDepth, point.weirdness(), point.offset());
        }

        boolean holds(Climate.ParameterPoint point, ResourceKey<Biome> key) {
            return this.entries.contains(Pair.of(point, key));
        }
    }
}
