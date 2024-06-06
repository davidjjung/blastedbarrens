package com.davigj.blasted_barrens.core;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class BBConfig {
    public static class Common {
//        public final ForgeConfigSpec.ConfigValue<Boolean> configExists;

        Common (ForgeConfigSpec.Builder builder) {
            builder.push("changes");
//            configExists = builder.comment("Does the template config exist").define("Config exists", true);
            builder.pop();
        }
    }

    static final ForgeConfigSpec COMMON_SPEC;
    public static final BBConfig.Common COMMON;


    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(BBConfig.Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }
}
