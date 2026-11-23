package com.davigj.blasted_barrens.core.other.compat;

import com.teamabnormals.upgrade_aquatic.core.UAConfig;

import static com.davigj.blasted_barrens.core.other.compat.ModConstants.UPGRADE_AQUATIC;

public class UACompat {
    public static final boolean renewableSand;

    static {
        renewableSand = UPGRADE_AQUATIC ? UAConfig.COMMON.renewableSand.get() : false;
    }
}
