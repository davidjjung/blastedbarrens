package com.davigj.blasted_barrens.core;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class BBConfig {
    public static class Common {
//        public final ModConfigSpec.ConfigValue<Boolean> configExists;

        Common (ModConfigSpec.Builder builder) {
            builder.push("common");
//            configExists = builder.comment("Does the template config exist").define("Config exists", true);
            builder.pop();
        }
    }

    static final ModConfigSpec COMMON_SPEC;
    public static final BBConfig.Common COMMON;


    static {
        final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(BBConfig.Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }
}
